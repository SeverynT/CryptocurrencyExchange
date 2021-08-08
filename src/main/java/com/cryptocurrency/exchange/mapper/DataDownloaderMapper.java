package com.cryptocurrency.exchange.mapper;

import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.ExchangeRatesDataDTO;
import com.cryptocurrency.exchange.dto.RateDTO;
import com.cryptocurrency.exchange.errors.AssetMapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataDownloaderMapper {

    private static final String ERROR_CONVERTING_MESSAGE = "Error during assets converting";

    public ExchangeRateDataDTO convertJsonObjectToExchangeRateDataDTO(JSONObject jsonObject) {
        try {
            return ExchangeRateDataDTO.builder()
                    .asset_id_base(jsonObject.getString("asset_id_base"))
                    .asset_id_quote(jsonObject.getString("asset_id_quote"))
                    .rate(new BigDecimal(jsonObject.getString("rate")))
                    .build();
        } catch (JSONException exception) {
            throw new AssetMapperException(ERROR_CONVERTING_MESSAGE);
        }
    }

    public ExchangeRatesDataDTO convertJsonObjectToExchangeRatesDataDTO(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return ExchangeRatesDataDTO.builder()
                    .asset_id_base(jsonObject.getString("asset_id_base"))
                    .rates(Arrays.asList(mapper.readValue(jsonObject.getJSONArray("rates").toString(), RateDTO[].class)))
                    .build();
        } catch (JSONException | JsonProcessingException exception) {
            throw new AssetMapperException(ERROR_CONVERTING_MESSAGE);
        }
    }
}
