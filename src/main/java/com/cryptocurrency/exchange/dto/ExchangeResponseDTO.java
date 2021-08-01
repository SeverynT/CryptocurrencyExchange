package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeResponseDTO {

    private String from;

    private Map<String, CurrencyExchangeDTO> to;
}
