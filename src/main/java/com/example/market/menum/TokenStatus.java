package com.example.market.menum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenStatus {
    NORMAL,
    FIXED_PRICE,
    DUTCH_AUCTION,
    ENGLISH_AUCTION,
    EXCHANGE_AUCTION,
    EXCHANGED;
}
