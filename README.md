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
    "TEL",
    "ETH"
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
    "ETH": {
      "rate": 15.134183658169752,
      "amount": 121,
      "result": 1812.92386041215459169892,
      "fee": 1.21000000000000002518
    },
    "TEL": {
      "rate": 2182299.784337204,
      "amount": 121,
      "result": 261417691.16575366710504969143,
      "fee": 1.21000000000000002518
    }
  }
}
```
  </details>
  </details>

</details>
