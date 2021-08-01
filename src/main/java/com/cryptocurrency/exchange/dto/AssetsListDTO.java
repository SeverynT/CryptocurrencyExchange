package com.cryptocurrency.exchange.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetsListDTO {

    private List<String> asset_ids;
}
