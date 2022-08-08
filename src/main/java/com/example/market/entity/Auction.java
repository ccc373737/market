package com.example.market.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Auction {

    private String tokenId;
}

