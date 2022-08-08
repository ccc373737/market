package com.example.market.menum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionType {
    FIXED_PRICE,
    DUTCH_AUCTION,
    ENGLISH_AUCTION,
    EXCHANGE_AUCTION,
}
