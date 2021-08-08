package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.ExchangeRatesDataDTO;
import com.cryptocurrency.exchange.errors.AssetQuoteException;
import com.cryptocurrency.exchange.mapper.DataDownloaderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DataDownloaderServiceTest {

    private static final String BTC_NAME = "BTC";
    private static final String ETH_NAME = "ETH";
    private static final String INCORRECT_NAME = "INCORRECT";

    private DataDownloaderMapper dataDownloaderMapper = new DataDownloaderMapper();
    private DataDownloaderService dataDownloaderService = new DataDownloaderService(dataDownloaderMapper);

    @Test
    @DisplayName("should get exchange rate date from external API should throws AssetQuoteException when asset quote name is incorrect")
    void shouldGetExchangeRateDataFromExternalApi_throwsAssetQuoteException_WhenAssetQuoteNameIsIncorrect() {
//        given
        String assetBase = BTC_NAME;
        String assetQuote = INCORRECT_NAME;

//        when
        var exception = catchThrowable(() -> dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase, assetQuote));

//        then
        assertThat(exception)
                .isInstanceOf(AssetQuoteException.class);
    }

    @Test
    @DisplayName("should get exchange rate date from external API by asset base and asset quote")
    void shouldGetExchangeRateDataFromExternalApi() {
//        given
        String assetBase = BTC_NAME;
        String assetQuote = ETH_NAME;

//        when
        ExchangeRateDataDTO exchangeRateDataDTO = dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase, assetQuote);

//        then
        assertEquals(assetBase, exchangeRateDataDTO.getAsset_id_base());
        assertEquals(assetQuote, exchangeRateDataDTO.getAsset_id_quote());
        assertNotEquals(new BigDecimal(0), exchangeRateDataDTO.getRate());
    }

    @Test
    @DisplayName("should get exchange rates date from external API should return empty list when asset base is incorrect")
    void shouldGetExchangeRateDataFromExternalApi_shouldReturnEmptyList_whenAssetBaseIsIncorrect() {
//        given
        String assetBase = INCORRECT_NAME;

//        when
        ExchangeRatesDataDTO exchangeRatesDataDTO = dataDownloaderService.getExchangeRatesDataFromExternalApi(assetBase);

//        then
        assertEquals(assetBase, exchangeRatesDataDTO.getAsset_id_base());
        assertEquals(0, exchangeRatesDataDTO.getRates().size());
    }

    @Test
    @DisplayName("should get exchange rates date from external API by asset base")
    void shouldGetExchangeRatesDataFromExternalApi() {
//        given
        String assetBase = BTC_NAME;

//        when
        ExchangeRatesDataDTO exchangeRatesDataDTO = dataDownloaderService.getExchangeRatesDataFromExternalApi(assetBase);

//        then
        assertEquals(assetBase, exchangeRatesDataDTO.getAsset_id_base());
        assertNotEquals(0, exchangeRatesDataDTO.getRates().size());
    }
}
