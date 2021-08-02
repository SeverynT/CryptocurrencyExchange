package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.AssetsListDTO;
import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.mapper.DataDownloaderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataDownloaderServiceTest {

    private static final String BTC_NAME = "BTC";
    private static final String INCORRECT_NAME = "INCORRECT";

    private DataDownloaderMapper dataDownloaderMapper = new DataDownloaderMapper();
    private DataDownloaderService dataDownloaderService = new DataDownloaderService(dataDownloaderMapper);

    @Test
    @DisplayName("should get exchange rate date from external API should return empty list when asset base is incorrect")
    void shouldGetExchangeRateDataFromExternalApiShouldReturnEmptyListWhenAssetBaseIsIncorrect() {
//        given
        String assetBase = INCORRECT_NAME;

//        when
        ExchangeRateDataDTO exchangeRateDataDTO = dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase);

//        then
        assertEquals(assetBase, exchangeRateDataDTO.getAsset_id_base());
        assertEquals(0, exchangeRateDataDTO.getRates().size());
    }

    @Test
    @DisplayName("should get exchange rate date from external API by asset base")
    void shouldGetExchangeRateDataFromExternalApiS() {
//        given
        String assetBase = BTC_NAME;

//        when
        ExchangeRateDataDTO exchangeRateDataDTO = dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase);

//        then
        assertEquals(assetBase, exchangeRateDataDTO.getAsset_id_base());
        assertNotEquals(0, exchangeRateDataDTO.getRates().size());
    }

    @Test
    @DisplayName("should get assets list from external api")
    void shouldGetAssetsListFromExternalApi() {
//        when
        AssetsListDTO assetsListDTO = dataDownloaderService.getAssetsListFromExternalApi();

//        then
        assertNotNull(assetsListDTO.getAsset_ids());
        assertNotEquals(0, assetsListDTO.getAsset_ids().size());
    }
}
