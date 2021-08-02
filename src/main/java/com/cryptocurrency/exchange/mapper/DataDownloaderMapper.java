package com.cryptocurrency.exchange.mapper;

import com.cryptocurrency.exchange.dto.AssetsListDTO;
import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.RateDTO;
import com.cryptocurrency.exchange.errors.AssetMapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataDownloaderMapper {

    private static final String ERROR_CONVERTING_MESSAGE = "Error during assets converting";

    public ExchangeRateDataDTO convertJsonObjectToExchangeRateDataDTO(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ExchangeRateDataDTO exchangeRateDataDTO = ExchangeRateDataDTO.builder()
                    .asset_id_base(jsonObject.getString("asset_id_base"))
                    .rates(Arrays.asList(mapper.readValue(jsonObject.getJSONArray("rates").toString(), RateDTO[].class)))
                    .build();

            return exchangeRateDataDTO;
        } catch (JSONException | JsonProcessingException exception) {
            throw new AssetMapperException(ERROR_CONVERTING_MESSAGE);
        }
    }

    public AssetsListDTO convertJsonArrayToAssetsListDTO(JSONArray jsonArray) {
        List<String> asset_ids = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                asset_ids.add(jsonArray.getJSONObject(i).getString("asset_id"));
            }

            return AssetsListDTO.builder()
                    .asset_ids(asset_ids)
                    .build();
        } catch (JSONException exception) {
            {
                throw new AssetMapperException(ERROR_CONVERTING_MESSAGE);
            }
        }
    }
}
