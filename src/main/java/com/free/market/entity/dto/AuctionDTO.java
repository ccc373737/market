package com.free.market.entity.dto;

import com.free.market.entity.AuctionDetail;
import com.free.market.menum.AuctionStatus;
import com.free.market.menum.TokenStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AuctionDTO {

    private String id;

    private String owner;

    private TokenStatus type;

    private AuctionStatus status;

    private Long startTime;

    private Long endTime;

    private String buyer;

    private String price;

    private String param;

    private List<AuctionDetail> details;
}


