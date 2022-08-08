package com.example.market.entity;

import com.example.market.menum.TokenStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Token {

    private String tokenAddress;

    private String tokenId;

    private String url;

    private String owner;

    private TokenStatus status;

    private Long createDate;

}
