package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRatesDataDTO {

    private String asset_id_base;

    private List<RateDTO> rates;

}
