package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.CurrenciesResponseDTO;
import com.cryptocurrency.exchange.dto.ExchangeRequestDTO;
import com.cryptocurrency.exchange.dto.ExchangeResponseDTO;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import com.cryptocurrency.exchange.mapper.DataDownloaderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class CryptoCurrencyServiceTest {

    private static final String BTC_NAME = "BTC";
    private static final String ETH_NAME = "ETH";
    private static final String LTC_NAME = "LTC";

    private DataDownloaderMapper dataDownloaderMapper = new DataDownloaderMapper();
    private DataDownloaderService dataDownloaderService = new DataDownloaderService(dataDownloaderMapper);
    private CryptoCurrencyService cryptoCurrencyService = new CryptoCurrencyService(dataDownloaderService);

    @Test
    @DisplayName("should get all rates for cryptocurrency without asset quotes")
    void shouldGetRatesForCryptoCurrencyWithoutAssetQuotes() {
//        given
        String assetBase = BTC_NAME;

//        when
        CurrenciesResponseDTO currenciesResponseDTO = cryptoCurrencyService.getRatesForCryptocurrency(assetBase, null);

//        then
        assertEquals(assetBase, currenciesResponseDTO.getSource());
        assertNotNull(currenciesResponseDTO.getRates());
        assertNotEquals(0, currenciesResponseDTO.getRates().size());
    }

    @Test
    @DisplayName("should get all rates for cryptocurrency with asset quotes")
    void shouldGetRatesForCryptoCurrencyWithAssetQuotes() {
//        given
        String assetBase = BTC_NAME;
        List<String> assetQuotes = List.of(LTC_NAME, ETH_NAME);

//        when
        CurrenciesResponseDTO currenciesResponseDTO = cryptoCurrencyService.getRatesForCryptocurrency(assetBase, assetQuotes);

//        then
        assertEquals(assetBase, currenciesResponseDTO.getSource());
        assertNotNull(currenciesResponseDTO.getRates());
        assertNotEquals(0, currenciesResponseDTO.getRates().size());
        assertNotNull(currenciesResponseDTO.getRates().get(assetQuotes.get(0)));
        assertNotNull(currenciesResponseDTO.getRates().get(assetQuotes.get(1)));
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when 'from' field is empty in request body")
    void shouldGetExchangePredictionsReturn400WhenFromFieldIsEmpty() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from(null)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(new BigDecimal(100))
                .build();

//        when
        var exception = catchThrowable(() -> cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO));

//        then
        assertThat(exception)
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessageContaining("from");
    }

    @Test
    @DisplayName("should throw InvalidRequestBodyException when 'to' field is empty in request body")
    void shouldGetExchangePredictions_throwsInvalidRequestBodyException_WhenToFieldIsEmpty() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(null)
                .amount(new BigDecimal(100))
                .build();

//        when
        var exception = catchThrowable(() -> cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO));

//        then
        assertThat(exception)
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessageContaining("to");
    }

    @Test
    @DisplayName("should throw MethodArgumentNotValidException when 'amount' field is empty in request body")
    void shouldGetExchangePredictions_throwsMethodArgumentNotValidExceptionWhenAmountFieldIsEmpty() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(null)
                .build();

//        when
        var exception = catchThrowable(() -> cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO));

//        then
        assertThat(exception)
                .isInstanceOf(InvalidRequestBodyException.class)
                .hasMessageContaining("amount");
    }

    @Test
    @DisplayName("should get exchange predictions")
    void shouldGetExchangePredictions() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(new BigDecimal(100))
                .build();

//        when
        ExchangeResponseDTO exchangeResponseDTO = cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO);

//        then
        assertEquals(exchangeRequestDTO.getFrom(), exchangeResponseDTO.getFrom());
        assertTrue(exchangeResponseDTO.getTo().containsKey(exchangeRequestDTO.getTo().get(0)));
        assertTrue(exchangeResponseDTO.getTo().containsKey(exchangeRequestDTO.getTo().get(1)));
        assertEquals(exchangeRequestDTO.getAmount(), exchangeResponseDTO.getTo().get(ETH_NAME).getAmount());
    }
}
