package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.*;
import com.cryptocurrency.exchange.errors.AssetBaseException;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CryptoCurrencyService {

    private static final BigDecimal FEE = new BigDecimal(0.01);
    private static final int PRECISION_OF_NUMBERS = 20;

    private DataDownloaderService dataDownloaderService;

    public CryptoCurrencyService(final DataDownloaderService dataDownloaderService) {
        this.dataDownloaderService = dataDownloaderService;
    }

    public CurrenciesResponseDTO getRatesForCryptocurrency(String assetBase, List<String> assetQuotes) {
        if (assetQuotes != null && !assetQuotes.isEmpty()) {

            List<ExchangeRateDataDTO> exchangeRateDataDTOS = assetQuotes.stream()
                    .map(assetQuote -> dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase, assetQuote))
                    .collect(Collectors.toList());

            return convertExchangeRateDataDTOStoCurrenciesResponseDTO(exchangeRateDataDTOS);
        } else {
            return convertExchangeRatesDataDTOtoCurrenciesResponseDTO(dataDownloaderService.getExchangeRatesDataFromExternalApi(assetBase));
        }
    }

    public ExchangeResponseDTO getExchangePredictions(ExchangeRequestDTO exchangeRequestDTO) {
        checkExchangeRequestDTOisCorrect(exchangeRequestDTO);

        CurrenciesResponseDTO currenciesResponseDTO = getRatesForCryptocurrency(exchangeRequestDTO.getFrom(), exchangeRequestDTO.getTo());
        BigDecimal amount = exchangeRequestDTO.getAmount();

        Map<String, CurrencyExchangeDTO> cryptocurrencyDTOmap = new HashMap<>();

        Iterator<Map.Entry<String, BigDecimal>> iterator = currenciesResponseDTO.getRates().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BigDecimal> rate = iterator.next();

            CurrencyExchangeDTO currencyExchangeDTO = CurrencyExchangeDTO.builder()
                    .rate(rate.getValue())
                    .amount(amount)
                    .result(calculateResultOfExchange(rate.getValue(), amount))
                    .fee(calculateFeeOfExchange(amount))
                    .build();

            cryptocurrencyDTOmap.put(rate.getKey(), currencyExchangeDTO);
        }

        return ExchangeResponseDTO.builder()
                .from(exchangeRequestDTO.getFrom())
                .to(cryptocurrencyDTOmap)
                .build();
    }

    private void checkExchangeRequestDTOisCorrect(ExchangeRequestDTO exchangeRequestDTO) {
        if (exchangeRequestDTO.getFrom() == null || exchangeRequestDTO.getFrom().isEmpty()) {
            throw new InvalidRequestBodyException("Fill in the 'from' field in ExchangeRequestDTO");
        }

        if (exchangeRequestDTO.getTo() == null || exchangeRequestDTO.getTo().isEmpty()) {
            throw new InvalidRequestBodyException("Fill in the 'to' field in ExchangeRequestDTO");
        }

        if (exchangeRequestDTO.getAmount() == null) {
            throw new InvalidRequestBodyException("Fill in the 'amount' field in ExchangeRequestDTO");
        }
    }

    private BigDecimal calculateResultOfExchange(BigDecimal rate, BigDecimal amount) {
        BigDecimal fee = calculateFeeOfExchange(amount);
//        result = (amount - fee) * rate;
        BigDecimal result = (amount.subtract(fee)).multiply(rate).setScale(PRECISION_OF_NUMBERS, RoundingMode.DOWN);
        return result;
    }

    private BigDecimal calculateFeeOfExchange(BigDecimal amount) {
//        fee = FEE * amount
        return FEE.multiply(amount).setScale(PRECISION_OF_NUMBERS, RoundingMode.DOWN);
    }

    private CurrenciesResponseDTO convertExchangeRateDataDTOStoCurrenciesResponseDTO(List<ExchangeRateDataDTO> exchangeRateDataDTOS) {
        Map<String, BigDecimal> rate = new HashMap<>();

        for (ExchangeRateDataDTO exchangeRateDataDTO : exchangeRateDataDTOS) {
            rate.put(exchangeRateDataDTO.getAsset_id_quote(), exchangeRateDataDTO.getRate());
        }

        return CurrenciesResponseDTO.builder()
                .source(exchangeRateDataDTOS.stream().findFirst().orElseThrow(
                        () -> new AssetBaseException("ExchangeRateDataDTO doesn't have asset_id_base"))
                        .getAsset_id_base())
                .rates(rate)
                .build();
    }

    private CurrenciesResponseDTO convertExchangeRatesDataDTOtoCurrenciesResponseDTO(ExchangeRatesDataDTO exchangeRatesDataDTO) {
        Map<String, BigDecimal> rate = new HashMap<>();

        for (RateDTO rateDTO : exchangeRatesDataDTO.getRates()) {
            rate.put(rateDTO.getAsset_id_quote(), rateDTO.getRate());
        }

        return CurrenciesResponseDTO.builder()
                .source(exchangeRatesDataDTO.getAsset_id_base())
                .rates(rate)
                .build();
    }

}
