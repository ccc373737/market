package com.example.market.api;

import com.example.market.entity.Token;
import com.example.market.menum.TokenStatus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/market")
@Slf4j
public class MarketController {

    @Resource
    private MongoTemplate mongoTemplate;

    private static final String TOKEN_COLL = "token";

    @PostMapping("/mint")
    public void mint(@RequestParam String tokenAddress,
                     @RequestParam String tokenId,
                     @RequestParam String owner,
                     @RequestParam String url) {

        Token token = new Token()
                .setTokenAddress(tokenAddress)
                .setTokenId(tokenId)
                .setUrl(url)
                .setOwner(owner)
                .setStatus(TokenStatus.NORMAL)
                .setCreateDate(System.currentTimeMillis());

        Token info = mongoTemplate.insert(token, TOKEN_COLL);
        log.info("存储的token信息为：{}", info);
    }

    @GetMapping("/getList")
    public List<Token> TokenList() {
        List<Token> list = mongoTemplate.findAll(Token.class, TOKEN_COLL);
        return list;
    }
}
