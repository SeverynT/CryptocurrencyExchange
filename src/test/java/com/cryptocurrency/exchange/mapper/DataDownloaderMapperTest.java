package com.cryptocurrency.exchange.mapper;

import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.ExchangeRatesDataDTO;
import com.cryptocurrency.exchange.errors.AssetMapperException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataDownloaderMapperTest {

    private static final String INCORRECT_NAME = "INCORRECT";

    private static final String ASSET_ID_BASE = "asset_id_base";
    private static final String RATES = "rates";
    private static final String TIME = "time";
    private static final String ASSET_ID_QUOTE = "asset_id_quote";
    private static final String RATE = "rate";

    private static final String BTC_NAME = "BTC";
    private static final String ETH_NAME = "ETH";

    private static final BigDecimal BIG_DECIMAL = new BigDecimal(100);
    private static final String RANDOM_TIME = "08-11-2000T11:12";

    private DataDownloaderMapper dataDownloaderMapper = new DataDownloaderMapper();

    @Test
    @DisplayName("should throws AssetMapperException when JSONObject is incorrect")
    void shouldConvertJsonObjectToExchangeRateDataDTO_throwsAssetMapperException_whenJsonObjectIsIncorrect() throws JSONException {
//        given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(INCORRECT_NAME, INCORRECT_NAME);

//        when
        var exception = catchThrowable(() -> dataDownloaderMapper.convertJsonObjectToExchangeRateDataDTO(jsonObject));

//        then
        assertThat(exception)
                .isInstanceOf(AssetMapperException.class);
    }

    @Test
    @DisplayName("should convert JSONObject to ExchangeRateDataDTO")
    void shouldConvertJsonObjectToExchangeRateDataDTO() throws JSONException {
//        given
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ASSET_ID_BASE, BTC_NAME);
        jsonObject.put(ASSET_ID_QUOTE, ETH_NAME);
        jsonObject.put(RATE, BIG_DECIMAL);

//        when
        ExchangeRateDataDTO exchangeRateDataDTO = dataDownloaderMapper.convertJsonObjectToExchangeRateDataDTO(jsonObject);

//        then
        assertEquals(BTC_NAME, exchangeRateDataDTO.getAsset_id_base());
        assertEquals(ETH_NAME, exchangeRateDataDTO.getAsset_id_quote());
        assertEquals(BIG_DECIMAL, exchangeRateDataDTO.getRate());
    }

    @Test
    @DisplayName("should throws AssetMapperException when JSONObject is incorrect")
    void shouldConvertJsonObjectToExchangeRatesDataDTO_throwsAssetMapperException_whenJsonObjectIsIncorrect() throws JSONException {
//        given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(INCORRECT_NAME, INCORRECT_NAME);

//        when
        var exception = catchThrowable(() -> dataDownloaderMapper.convertJsonObjectToExchangeRatesDataDTO(jsonObject));

//        then
        assertThat(exception)
                .isInstanceOf(AssetMapperException.class);
    }

    @Test
    @DisplayName("should convert JSONObject to ExchangeRatesDataDTO")
    void shouldConvertJsonObjectToExchangeRatesDataDTO() throws JSONException {
//        given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ASSET_ID_BASE, BTC_NAME);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put(TIME, RANDOM_TIME);
        jsonObject2.put(ASSET_ID_QUOTE, ETH_NAME);
        jsonObject2.put(RATE, BIG_DECIMAL);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject2);

        jsonObject.put(RATES, jsonArray);

//        when
        ExchangeRatesDataDTO exchangeRatesDataDTO = dataDownloaderMapper.convertJsonObjectToExchangeRatesDataDTO(jsonObject);

//        then
        assertEquals(BTC_NAME, exchangeRatesDataDTO.getAsset_id_base());
        assertEquals(RANDOM_TIME, exchangeRatesDataDTO.getRates().get(0).getTime());
        assertEquals(ETH_NAME, exchangeRatesDataDTO.getRates().get(0).getAsset_id_quote());
        assertEquals(BIG_DECIMAL, exchangeRatesDataDTO.getRates().get(0).getRate());
    }
}
