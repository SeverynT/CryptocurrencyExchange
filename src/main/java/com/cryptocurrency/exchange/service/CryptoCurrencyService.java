package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.*;
import com.cryptocurrency.exchange.errors.AssetQuoteException;
import com.cryptocurrency.exchange.errors.CryptocurrencyNotExistsException;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CryptoCurrencyService {

    private static final BigDecimal FEE = new BigDecimal(0.01);
    private static final int PRECISION_OF_NUMBERS = 20;

    private DataDownloaderService dataDownloaderService;

    public CryptoCurrencyService(final DataDownloaderService dataDownloaderService) {
        this.dataDownloaderService = dataDownloaderService;
    }

    public CurrenciesResponseDTO getRatesForCryptocurrency(String assetBase, List<String> assetQuotes) {
        List<String> asset_ids = dataDownloaderService.getAssetsListFromExternalApi().getAsset_ids();
        isCryptocurrency(asset_ids, assetBase);

        ExchangeRateDataDTO exchangeRateDataDTO = dataDownloaderService.getExchangeRateDataFromExternalApi(assetBase);

        if (assetQuotes != null && !assetQuotes.isEmpty()) {
            checkAssetBaseHasSameNameLikeAssetQuote(assetBase, assetQuotes);
            List<RateDTO> rateDTOS = new ArrayList<>();

            for (String assetQuote : assetQuotes) {
                isCryptocurrency(asset_ids, assetQuote);
                RateDTO rateDTO = exchangeRateDataDTO.getRates().stream()
                        .filter(rate -> rate.getAsset_id_quote().equals(assetQuote))
                        .findAny()
                        .orElseThrow(() -> new CryptocurrencyNotExistsException("Cryptocurrency with name " + assetQuote + " not exists"));

                rateDTOS.add(rateDTO);
            }

            exchangeRateDataDTO.setRates(rateDTOS);
        }

        return convertExchangeRateDataDTOtoCurrenciesResponseDTO(exchangeRateDataDTO);
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

    private void checkAssetBaseHasSameNameLikeAssetQuote(String assetBase, List<String> assetQuotes) {
        if (assetQuotes.stream().anyMatch(assetQuote -> assetQuote.equals(assetBase))) {
            throw new AssetQuoteException("Asset quote has the same name like asset base");
        }
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

        checkAssetBaseHasSameNameLikeAssetQuote(exchangeRequestDTO.getFrom(), exchangeRequestDTO.getTo());
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

    private CurrenciesResponseDTO convertExchangeRateDataDTOtoCurrenciesResponseDTO(ExchangeRateDataDTO exchangeRateDataDTO) {
        Map<String, BigDecimal> rate = new HashMap<>();

        for (RateDTO rateDTO : exchangeRateDataDTO.getRates()) {
            rate.put(rateDTO.getAsset_id_quote(), rateDTO.getRate());
        }

        return CurrenciesResponseDTO.builder()
                .source(exchangeRateDataDTO.getAsset_id_base())
                .rates(rate)
                .build();
    }

    private void isCryptocurrency(List<String> asset_ids, String cryptocurrency) {
        if (!asset_ids.contains(cryptocurrency)) {
            throw new CryptocurrencyNotExistsException("Cryptocurrency with name " + cryptocurrency + " not exists");
        }
    }
}
