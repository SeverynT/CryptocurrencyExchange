package com.cryptocurrency.exchange.controller;

import com.cryptocurrency.exchange.dto.CurrenciesResponseDTO;
import com.cryptocurrency.exchange.dto.ExchangeRequestDTO;
import com.cryptocurrency.exchange.dto.ExchangeResponseDTO;
import com.cryptocurrency.exchange.service.CryptoCurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CryptoCurrencyController {

    private CryptoCurrencyService cryptoCurrencyService;

    public CryptoCurrencyController(final CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    @GetMapping("/{assetBase}")
    public ResponseEntity<CurrenciesResponseDTO> getRatesForCryptoCurrency(@PathVariable String assetBase, @RequestParam(required = false) List<String> filter) {
        return ResponseEntity.ok(cryptoCurrencyService.getRatesForCryptocurrency(assetBase, filter));
    }

    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResponseDTO> getExchangePredictions(@Valid @RequestBody ExchangeRequestDTO exchangeRequestDTO) {
        return ResponseEntity.ok(cryptoCurrencyService.getExchangePredictions(exchangeRequestDTO));
    }
}
