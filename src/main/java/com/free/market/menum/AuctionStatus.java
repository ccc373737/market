package com.free.market.menum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionStatus {
    SELLING,
    FINISH,
    CANCEL;
}
