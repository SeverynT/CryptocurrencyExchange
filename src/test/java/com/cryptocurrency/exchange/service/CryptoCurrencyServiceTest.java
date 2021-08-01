package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.CurrenciesResponseDTO;
import com.cryptocurrency.exchange.dto.ExchangeRequestDTO;
import com.cryptocurrency.exchange.dto.ExchangeResponseDTO;
import com.cryptocurrency.exchange.errors.CryptocurrencyNotExistsException;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CryptoCurrencyServiceTest {

    private DataDownloaderService dataDownloaderService = new DataDownloaderService();
    private CryptoCurrencyService cryptoCurrencyService = new CryptoCurrencyService(dataDownloaderService);

    @Test
    @DisplayName("should throw CryptocurrencyNotExistsException when asset base name not exists")
    void shouldGetRatesForCryptocurrency_throwsCryptocurrencyNotExistsException_whenAssetBaseIncorrect() {
//        given
        String assetBase = "INCORRECT";

//        when
        var exception = catchThrowable(() -> cryptoCurrencyService.getRatesForCryptocurrency(assetBase, null));

//        then
        assertThat(exception)
                .isInstanceOf(CryptocurrencyNotExistsException.class)
                .hasMessageContaining(assetBase);
    }

    @Test
    @DisplayName("should throw CryptocurrencyNotExistsException when asset quote name not exists")
    void shouldGetRatesForCryptocurrency_throwsCryptocurrencyNotExistsException_whenAssetQuoteIncorrect() {
//        given
        String assetBase = "BTC";
        List<String> assetQuotes = List.of("INCORRECT");

//        when
        var exception = catchThrowable(() -> cryptoCurrencyService.getRatesForCryptocurrency(assetBase, assetQuotes));

//        then
        assertThat(exception)
                .isInstanceOf(CryptocurrencyNotExistsException.class)
                .hasMessageContaining(assetQuotes.get(0));
    }

    @Test
    @DisplayName("should get all rates for cryptocurrency without asset quotes")
    void shouldGetRatesForCryptoCurrencyWithoutAssetQuotes() {
//        given
        String assetBase = "BTC";

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
        String assetBase = "BTC";
        List<String> assetQuotes = List.of("LTC", "ETH");

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
                .to(List.of("ETH", "LTC"))
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
    @DisplayName("should throw MethodArgumentNotValidException when 'to' field is empty in request body")
    void shouldGetExchangePredictionsReturn400WhenToFieldIsEmpty() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from("BTC")
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
    void shouldGetExchangePredictionsReturn400WhenAmountFieldIsEmpty() {
//        given
        ExchangeRequestDTO exchangeRequestDTO = ExchangeRequestDTO.builder()
                .from("BTC")
                .to(List.of("ETH", "LTC"))
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
                .from("BTC")
                .to(List.of("ETH", "LTC"))
                .amount(new BigDecimal(100))
                .build();

//        when
        ExchangeResponseDTO exchangeResponseDTO = cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO);

//        then
        assertEquals(exchangeRequestDTO.getFrom(), exchangeResponseDTO.getFrom());
        assertTrue(exchangeResponseDTO.getTo().containsKey(exchangeRequestDTO.getTo().get(0)));
        assertTrue(exchangeResponseDTO.getTo().containsKey(exchangeRequestDTO.getTo().get(1)));
        assertEquals(exchangeRequestDTO.getAmount(), exchangeResponseDTO.getTo().get("ETH").getAmount());
    }
}
