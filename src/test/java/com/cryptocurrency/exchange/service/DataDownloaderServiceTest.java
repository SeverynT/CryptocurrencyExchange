package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.AssetsListDTO;
import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class DataDownloaderServiceTest {

    private DataDownloaderService dataDownloaderService = new DataDownloaderService();

    @Test
    @DisplayName("should get exchange rate date from external API should return empty list when asset base is incorrect")
    void shouldGetExchangeRateDataFromExternalApiShouldReturnEmptyListWhenAssetBaseIsIncorrect() {
//        given
        String assetBase = "INCORRECT";

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
        String assetBase = "BTC";

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