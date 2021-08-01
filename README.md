# CRYPTOCURRENCY EXCHANGE

### REMARKS

* Project is using Java 11 and Maven. Please make sure you have them installed on your machine.
* Build project using mvn clean install
* Run application using mvn spring-boot:run
* Application should be available at URL http://localhost:8001
* Application is using Lombok. Please install Lombok plugin in your IDE.
* Application is using COIN-API from https://docs.coinapi.io/.
* You can generate new API KEY for COIN-API in https://docs.coinapi.io/.


# API Endpoints

<details>
  <summary style="font-size: 20px;">Currencies Endpoint</summary>

  <details style="padding-left: 30px;">
    <summary style="font-size: 20px;"><b>GET</b> currencies: <span style="color: rgb(188,164,113);">'http://localhost:8081/currencies/{assetBase}?filter={assetQuote}`</span></summary>

  <details style="padding-left: 30px;">
    <summary>Response body:</summary>

```json
{
  "source": "BTC",
  "rates": {
    "LTC": 284.136337246235,
    "DOG": 0.19716370404590686
  }
}
```
  </details>
  </details>

  <details style="padding-left: 30px;">
    <summary style="font-size: 20px;"><b>POST</b> currencies: <span style="color: rgb(188,164,113);">'http://localhost:8081/currencies/exchange`</span></summary>

  <details style="padding-left: 30px;">
    <summary>Request body:</summary>

```json
{
  "from": "BTC",
  "to": [
    "LTC",
    "DOG"
  ],
  "amount": 121
}
```
  </details>

  <details style="padding-left: 30px;">
    <summary>Response body:</summary>

```json
{
  "from": "BTC",
  "to": {
    "LTC": {
      "rate": 283.9520427430217,
      "amount": 121,
      "result": 0.42186701262230632270,
      "fee": 1.21000000000000002518
    },
    "DOG": {
      "rate": 0.1971447765543235,
      "amount": 121,
      "result": 607.62451886211506813326,
      "fee": 1.21000000000000002518
    }
  }
}
```
  </details>
  </details>

</details>
