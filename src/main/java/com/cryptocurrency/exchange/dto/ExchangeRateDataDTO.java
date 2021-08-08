package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDataDTO {

    private String asset_id_base;

    private String asset_id_quote;

    private BigDecimal rate;

}
