package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.AssetsListDTO;
import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.RateDTO;
import com.cryptocurrency.exchange.errors.ApiConnectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataDownloaderService {

    private static final String EXCHANGE_RATE_URL_BEGINNING = "https://rest.coinapi.io/v1/exchangerate/";
    private static final String ASSETS_LIST_URL_BEGINNING = "https://rest.coinapi.io/v1/assets/";
    private static final String KEY_NAME = "X-CoinAPI-Key";
    private static final String KEY_VALUE = "459538C2-FEAB-459C-A4BB-9CE3C7EA8C15";

    public ExchangeRateDataDTO getExchangeRateDataFromExternalApi(String assetBase) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JSONObject jsonObject = new JSONObject(getDataFromExternalApi(EXCHANGE_RATE_URL_BEGINNING + assetBase).string());

            ExchangeRateDataDTO exchangeRateDataDTO = ExchangeRateDataDTO.builder()
                    .asset_id_base(jsonObject.getString("asset_id_base"))
                    .rates(Arrays.asList(mapper.readValue(jsonObject.getJSONArray("rates").toString(), RateDTO[].class)))
                    .build();

            return exchangeRateDataDTO;
        } catch (IOException | JSONException exception) {
            throw new ApiConnectionException("Error during assets downloading");
        }
    }

    public AssetsListDTO getAssetsListFromExternalApi() {
        try {
            JSONArray jsonArray = new JSONArray(getDataFromExternalApi(ASSETS_LIST_URL_BEGINNING).string());
            List<String> asset_ids = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                asset_ids.add(jsonArray.getJSONObject(i).getString("asset_id"));
            }

            return AssetsListDTO.builder()
                    .asset_ids(asset_ids)
                    .build();

        } catch (IOException | JSONException exception) {
            throw new ApiConnectionException("Error during assets downloading");
        }
    }

    private ResponseBody getDataFromExternalApi(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header(KEY_NAME, KEY_VALUE)
                .url(url)
                .build();

        try {
            return client.newCall(request).execute().body();
        } catch (IOException exception) {
            throw new ApiConnectionException("Error during assets downloading");
        }
    }
}
