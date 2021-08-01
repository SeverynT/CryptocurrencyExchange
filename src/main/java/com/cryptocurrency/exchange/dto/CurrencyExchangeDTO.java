package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyExchangeDTO {

    private BigDecimal rate;

    private BigDecimal amount;

    private BigDecimal result;

    private BigDecimal fee;
}
