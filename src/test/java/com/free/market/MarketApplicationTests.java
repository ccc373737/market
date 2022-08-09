package com.free.market;

import com.free.market.api.MarketController;
import com.free.market.entity.Auction;
import com.free.market.entity.Token;
import com.free.market.menum.AuctionStatus;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
class MarketApplicationTests {

    @Resource
    MarketController marketController;

    @Resource
    MongoTemplate mongoTemplate;


    @Test
    public void insertAuction() {
        marketController.createAuction(new Auction()
                .setTokenId("3")
                .setEndTime(System.currentTimeMillis())
                .setStatus(AuctionStatus.SELLING));
    }

    @Test
    public void query() {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("tokenAddress").is("11"),
                Criteria.where("tokenId").is("0"));

        Token one = mongoTemplate.findOne(new Query(criteria), Token.class);

        System.out.println(one);
    }

}
