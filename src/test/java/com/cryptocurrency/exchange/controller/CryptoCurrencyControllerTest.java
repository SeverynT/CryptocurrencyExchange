package com.cryptocurrency.exchange.controller;

import com.cryptocurrency.exchange.dto.CurrenciesResponseDTO;
import com.cryptocurrency.exchange.dto.ExchangeRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CryptoCurrencyControllerTest {

    private static final String BTC_NAME = "BTC";
    private static final String ETH_NAME = "ETH";
    private static final String LTC_NAME = "LTC";

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("should get all rates for cryptocurrency without filter by http GET /currencies endpoint")
    void httpGet_returnRatesForCryptoCurrencyWithoutFilter() {
//        given
        String assetBase = BTC_NAME;

//        when
        CurrenciesResponseDTO result = testRestTemplate.getForObject("http://localhost:" + port + "/currencies/" + assetBase, CurrenciesResponseDTO.class);

//        then
        assertEquals(assetBase, result.getSource());
        assertNotNull(result.getRates());
        assertNotEquals(0, result.getRates().size());
    }

    @Test
    @DisplayName("should get all rates for cryptocurrency with filter by http GET /currencies endpoint")
    void httpGet_returnRatesForCryptoCurrencyWithFilter() {
//        given
        String assetBase = BTC_NAME;
        String filter = ETH_NAME;

//        when
        CurrenciesResponseDTO result = testRestTemplate.getForObject("http://localhost:" + port + "/currencies/" + assetBase + "?filter=" + filter,
                CurrenciesResponseDTO.class);

//        then
        assertEquals(assetBase, result.getSource());
        assertNotNull(result.getRates());
        assertEquals(1, result.getRates().size());
        assertTrue(result.getRates().containsKey(filter));
    }

    @Test
    @DisplayName("should get exchange prediction should return BAD REQUEST http status when 'from' field in request body is empty")
    void getExchangePredictionsShouldReturn400NotValidExceptionWhenFromIsEmpty() throws Exception {
//        given
        ExchangeRequestDTO requestBody = ExchangeRequestDTO.builder()
                .from(null)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(new BigDecimal(100))
                .build();

//        when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/currencies/exchange")
                .content(asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

//        then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should get exchange prediction should return BAD REQUEST http status when 'to' field in request body is empty")
    void getExchangePredictionsShouldReturn400WhenToIsEmpty() throws Exception {
//        given
        ExchangeRequestDTO requestBody = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(null)
                .amount(new BigDecimal(100))
                .build();

//        when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/currencies/exchange")
                .content(asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

//        then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should get exchange prediction should return BAD REQUEST http status when 'from' field in request body is empty")
    void getExchangePredictionsShouldReturn400WhenAmountIsEmpty() throws Exception {
//        given
        ExchangeRequestDTO requestBody = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(null)
                .build();

//        when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/currencies/exchange")
                .content(asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

//        then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should get exchange prediction")
    void getExchangePredictions() throws Exception {
//        given
        ExchangeRequestDTO requestBody = ExchangeRequestDTO.builder()
                .from(BTC_NAME)
                .to(List.of(ETH_NAME, LTC_NAME))
                .amount(new BigDecimal(100))
                .build();

//        when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/currencies/exchange")
                .content(asJsonString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

//        then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
