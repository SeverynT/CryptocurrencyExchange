package com.cryptocurrency.exchange.service;

import com.cryptocurrency.exchange.dto.ExchangeRateDataDTO;
import com.cryptocurrency.exchange.dto.ExchangeRatesDataDTO;
import com.cryptocurrency.exchange.errors.ApiConnectionException;
import com.cryptocurrency.exchange.errors.AssetQuoteException;
import com.cryptocurrency.exchange.mapper.DataDownloaderMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataDownloaderService {

    private static final String EXCHANGE_RATE_URL_BEGINNING = "https://rest.coinapi.io/v1/exchangerate/";
    private static final String ERROR_DOWNLOADING_MESSAGE = "Error during assets downloading";
    private static final String INCORRECT_ASSET_QUOTE_NAME_ERROR_MESSAGE_FROM_EXTERNAL_API = "You requested specific single item that we don't have at this moment.";

    /**
     * *  The key name and value we should get from an external file. Due to the demo version, the values are given explicitly.
     **/
    private static final String KEY_NAME = "X-CoinAPI-Key";
    private static final String KEY_VALUE = "1CC8F708-242A-4A85-A271-71EEFB76967B";

    private DataDownloaderMapper dataDownloaderMapper;

    public DataDownloaderService(final DataDownloaderMapper dataDownloaderMapper) {
        this.dataDownloaderMapper = dataDownloaderMapper;
    }

    public ExchangeRateDataDTO getExchangeRateDataFromExternalApi(String assetBase, String assetQuote) {
        try {

            JSONObject jsonObject = new JSONObject(getDataFromExternalApi(EXCHANGE_RATE_URL_BEGINNING + assetBase + "/" + assetQuote).string());

            if (jsonObject.toString().contains(INCORRECT_ASSET_QUOTE_NAME_ERROR_MESSAGE_FROM_EXTERNAL_API)) {
                throw new AssetQuoteException("You requested specific " + assetQuote + " cryptocurrency that we don't have at this moment.");
            }

            return dataDownloaderMapper.convertJsonObjectToExchangeRateDataDTO(jsonObject);
        } catch (IOException | JSONException exception) {
            throw new ApiConnectionException(ERROR_DOWNLOADING_MESSAGE);
        }
    }

    public ExchangeRatesDataDTO getExchangeRatesDataFromExternalApi(String assetBase) {
        try {

            JSONObject jsonObject = new JSONObject(getDataFromExternalApi(EXCHANGE_RATE_URL_BEGINNING + assetBase).string());

            return dataDownloaderMapper.convertJsonObjectToExchangeRatesDataDTO(jsonObject);
        } catch (IOException | JSONException exception) {
            throw new ApiConnectionException(ERROR_DOWNLOADING_MESSAGE);
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
            throw new ApiConnectionException(ERROR_DOWNLOADING_MESSAGE);
        }
    }
}
