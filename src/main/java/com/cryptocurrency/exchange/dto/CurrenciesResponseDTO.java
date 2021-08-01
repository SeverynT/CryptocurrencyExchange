package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrenciesResponseDTO {

    private String source;

    private Map<String, BigDecimal> rates;
}
