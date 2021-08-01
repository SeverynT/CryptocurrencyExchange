package com.cryptocurrency.exchange.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequestDTO {

    @NotEmpty(message = "Fill in the 'from' field in RequestBody")
    private String from;

    @NotEmpty(message = "Fill in the 'to' field in RequestBody")
    private List<String> to;

    @NotNull(message = "Fill in the 'amount' field in RequestBody")
    private BigDecimal amount;
}
