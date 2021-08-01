package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateDTO {

    private String time;

    private String asset_id_quote;

    private BigDecimal rate;
}
