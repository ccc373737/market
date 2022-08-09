package com.free.market.entity;

import com.free.market.menum.AuctionStatus;
import com.free.market.menum.TokenStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Accessors(chain = true)
@Document(collection = "auction")
public class Auction {

    @MongoId
    private String id;

    private String tokenAddress;

    private String tokenId;

    private String owner;

    private TokenStatus type;

    private AuctionStatus status;

    private Long startTime;

    private Long endTime;

    private String buyer;

    private String price;

    private String param;
}


