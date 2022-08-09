package com.free.market.entity;

import com.free.market.menum.TokenStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document(collection = "token")
public class Token {

    private String tokenAddress;

    private String tokenId;

    private String url;

    private String owner;

    private String description;

    private TokenStatus status;

    private Long createDate;

    private Long updateDate;

}
