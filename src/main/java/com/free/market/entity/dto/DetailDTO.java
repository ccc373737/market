package com.free.market.entity.dto;

import com.free.market.menum.AuctionEvent;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ccc
 * @description
 * @date 2022/8/9 2:42 PM
 */

@Data
@Accessors(chain = true)
public class DetailDTO {

    private String tokenAddress;

    private String tokenId;

    private String operator;

    private String price;

    private AuctionEvent event;

}
