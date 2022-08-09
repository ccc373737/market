package com.free.market.entity;

import com.free.market.menum.AuctionEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document(collection = "auction_detail")
public class AuctionDetail {

    private String auctionId;

    private String operator;

    private String price;

    private AuctionEvent event;

    private Long eventTime;

}


