package com.free.market.api;

import com.free.market.entity.Auction;
import com.free.market.entity.AuctionDetail;
import com.free.market.entity.Token;
import com.free.market.entity.dto.AuctionDTO;
import com.free.market.entity.dto.DetailDTO;
import com.free.market.menum.AuctionEvent;
import com.free.market.menum.AuctionStatus;
import com.free.market.menum.TokenStatus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/market")
@Slf4j
public class MarketController {

    @Resource
    MongoTemplate mongoTemplate;

    @PostMapping("/mint")
    public void mint(@RequestParam String tokenAddress,
                     @RequestParam String tokenId,
                     @RequestParam String owner,
                     @RequestParam String url,
                     @RequestParam String description) {
        if (mongoTemplate.findOne(queryByAddressAndToken(tokenAddress, tokenId), Token.class) != null) {
            log.error(tokenId, "token is alreay exist!");
            return;
        }

        Token token = new Token()
                .setTokenAddress(tokenAddress)
                .setTokenId(tokenId)
                .setUrl(url)
                .setOwner(owner)
                .setDescription(description)
                .setStatus(TokenStatus.NORMAL)
                .setCreateDate(System.currentTimeMillis())
                .setUpdateDate(System.currentTimeMillis());

        mongoTemplate.insert(token);
    }

    @PostMapping("/createAuction")
    @Transactional
    public void createAuction(@RequestBody Auction param) {
        Token token = mongoTemplate.findOne(queryByAddressAndToken(param.getTokenAddress(), param.getTokenId()), Token.class);
        if (token == null) {
            log.error(param.getTokenId(), "token is not existed!");
            return;
        }

        if (!TokenStatus.NORMAL.equals(token.getStatus())) {
            log.error(param.getTokenId(), "token is in auction!");
            return;
        }

        param.setStartTime(System.currentTimeMillis())
                .setStatus(AuctionStatus.SELLING);
        mongoTemplate.insert(param);

        AuctionDetail detail = new AuctionDetail()
                .setAuctionId(param.getId())
                .setEvent(AuctionEvent.START)
                .setOperator(param.getOwner())
                .setEventTime(System.currentTimeMillis());
        mongoTemplate.insert(detail);

        Update update = new Update().set("status", param.getType());
        mongoTemplate.updateFirst(queryByAddressAndToken(param.getTokenAddress(), param.getTokenId()), update, Token.class);
    }

    @PostMapping("/createDetail")
    @Transactional
    public void createDetail(@RequestBody DetailDTO param) {
        Query query = queryByAddressAndToken(param.getTokenAddress(), param.getTokenId());
        query.with(Sort.by("startTime").descending());

        Auction auction = mongoTemplate.findOne(query, Auction.class);

        if (auction == null || !auction.getStatus().equals(AuctionStatus.SELLING)) {
            log.error(param.getTokenId(), "token is not in auction!");
            return;
        }

        AuctionDetail detail = new AuctionDetail()
                .setAuctionId(auction.getId())
                .setOperator(param.getOperator())
                .setEvent(param.getEvent())
                .setEventTime(System.currentTimeMillis())
                .setPrice(param.getPrice());

        if (AuctionEvent.CANCEL.equals(param.getEvent())) {//cancel
            mongoTemplate.updateFirst(
                    queryByAddressAndToken(param.getTokenAddress(), param.getTokenId()),
                    new Update().set("status", TokenStatus.NORMAL),
                    Token.class);

            mongoTemplate.updateFirst(
                    new Query(Criteria.where("id").is(auction.getId())),
                    new Update().set("status", AuctionStatus.CANCEL),
                    Auction.class);
        } else if (AuctionEvent.FINISH.equals(param.getEvent())) {//finish
            if (TokenStatus.EXCHANGE_AUCTION.equals(auction.getType())) {//if is exchange auction, change exchanged token owner
                mongoTemplate.updateFirst(
                        queryByAddressAndToken(param.getTokenAddress(), param.getPrice()),
                        new Update().set("owner", auction.getOwner()),
                        Token.class);
            }

            mongoTemplate.updateFirst(//update token
                    queryByAddressAndToken(param.getTokenAddress(), param.getTokenId()),
                    new Update()
                            .set("owner", param.getOperator())
                            .set("status", TokenStatus.NORMAL)
                            .set("updateDate", System.currentTimeMillis()),
                    Token.class);

            mongoTemplate.updateFirst(
                    new Query(Criteria.where("id").is(auction.getId())),
                    new Update()
                            .set("buyer", param.getOperator())
                            .set("price", param.getPrice() )
                            .set("status", AuctionStatus.FINISH),
                    Auction.class);
        }

        mongoTemplate.insert(detail);
    }

    @GetMapping("/getMyToken")
    public List<Token> getMyToken(@RequestParam String tokenAddress,
                                  @RequestParam String owner) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("tokenAddress").is(tokenAddress),
                Criteria.where("owner").is(owner)));

        return mongoTemplate.find(query, Token.class);
    }

    @GetMapping("/getTokenList")
    public List<Token> getTokenList(@RequestParam String tokenAddress,
                                    @RequestParam(defaultValue = "1") Integer pageIndex,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Query query = new Query(Criteria.where("tokenAddress").is(tokenAddress))
                .with(PageRequest.of(pageIndex, pageSize, Sort.by("startTime").descending()));

        return mongoTemplate.find(query, Token.class);
    }


    @GetMapping("/getAuction")
    public List<AuctionDTO> getAuction(@RequestParam String tokenAddress,
                                       @RequestParam String tokenId) {
        Query query = queryByAddressAndToken(tokenAddress, tokenId).with(Sort.by("startTime").descending());
        List<AuctionDTO> list = mongoTemplate.find(query, AuctionDTO.class);

        list.forEach(item -> {
            List<AuctionDetail> details = mongoTemplate.find(
                    new Query(Criteria.where("id").is(item.getId())).with(Sort.by("eventTime").descending()),
                    AuctionDetail.class);
            item.setDetails(details);
        });

        return list;
    }

    private Query queryByAddressAndToken(String tokenAddress, String tokenId) {
        return new Query(new Criteria().andOperator(
                Criteria.where("tokenAddress").is(tokenAddress),
                Criteria.where("tokenId").is(tokenId)));
    }
}
