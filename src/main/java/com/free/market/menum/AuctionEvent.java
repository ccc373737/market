package com.free.market.menum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionEvent {
    START,
    CANCEL,
    BID,
    WITHDRAW,
    FINISH;
}
