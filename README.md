# trade-validator

This project is a RESTful web service implemented using [Spring Boot](http://projects.spring.io/spring-boot).
It's main purpose is to expose extensible Trade Validation service.

### Endpoints
`/trade` - validates a single trade using common trade validators defined for all trades as well as specified only for this type of trade. Returns trade with validation status and corresponding error messages  
`/trades` - validates chunk of trades and returns list of trades with validation status and corresponsing error messages  
`/shutdown` - graceful shutdown solution using [Spring Boot Actuator module](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready-endpoints)  
`/swagger-ui.html` - documentation of REST API exposed by the service

### Main assumptions, requirements and implemented solutions

The service exposes a REST interface consuming trades in JSON format and returning validation result to the client.

Service is flexible to extend the validation logic in terms of new business validation rules and supported new products.  
All Validators are Spring-managed beans and implement `TradeValidator` interface:

```java
public interface TradeValidator<T extends Trade> {
	TradeValidationResult validate(T trade);
}
```

Please note that after creating a `TradeValidator` implementation, you need to assign it to a specific type of trade. Without this step, newly created `TradeValidator` will not be used at all.

To link a particular `TradeValidator` with a specific type of trade, you need to add it to [application.yml](src/main/resources/application.yml) file.  

```yaml
tradeValidation:
    validatorsForTradeType:
        ALL:
            - ValueDateNotBeforeTradeDateValidator
            - ValueDateOnWorkingDayValidator
            - CounterPartyValidator
            - CurrencyValidator
        Spot:
            - SpotValueDateValidator
        Forward:
            - ForwardValueDateValidator
        VanillaOption:
            - OptionStyleValidator
            - OptionExcerciseStartDateValidator
            - OptionExpiryDateAndPremiumDateValidator
```
This configuration file is parsed by `TradeValidatorsYamlConfigReader` and stored in `TradeValidatorYamlRegistry`.

Thanks to this approach there is one global place which depicts all validators assigned to all types of trades.  

Since Java is strongly typed language, types of Trades are registered as enum TradeType

```java
public enum TradeType {
	Spot,
	Forward,
	VanillaOption;
}
```
In case of adding a new type of trade, don't forget to register it there.

### Trade Validators
There is not much to talk about `Validators` themselves. They just implement pretty basic functionality of trade validation like validation of Trade Date, Option Style, counterParty etc.

The only thing worth to be mentioned here `Validator` is `ValueDateOnWorkingDayValidator`. It consumes RESTful web service exposed by [holidayapi.com](https://holidayapi.com) by using Spring's `RestTemplate` shipped into `HolidayApiService`.

Thanks to this solution it is validated if trade's valuation date does not fall on weekend. This is the main bottleneck of the Trade Validation solution as it's dependent on external service. Unavailability of [holidayapi.com](https://holidayapi.com) or poor connection may degrade performance significantly. Thanks to introduction of cache using Spring's [@Cacheable](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html) solution, the impact of external service is mitigated.

## Ways how to run it
There are two basic alternatives
#### Option 1. Use the Maven plugin
```
mvn spring-boot:run
```
#### Option 2. Compile and Run
```
mvn clean package
java -jar target/trade-validator-0.0.1-SNAPSHOT.jar
```

## Client Apps
#### Option 1. Widely-used REST client, e.g. [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)

#### Option 2. Use curl
Examples of usage  
Validate single trade:

```
curl -H "Content-Type: application/json" -X POST -d @src\test\resources\spot-trade.json http://localhost:8080/trade
{"trade":{"type":"Spot","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-15","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date falls on holiday"},{"errorMessage":"Difference between current date and value date is not equal 2"}]}
```
Validate multiple trades:

```
curl -H "Content-Type: application/json" -X POST -d @src\test\resources\multiple-trades.json http://localhost:8080/trades
[{"trade":{"type":"Spot","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-15","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date falls on holiday"},{"errorMessage":"Difference between current date and value date is not equal 2"}]},{"trade":{"type":"Spot","customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"SELL","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-22","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Difference between current date and value date is not equal 2"}]},{"trade":{"type":"Forward","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","direction":"SELL","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-22","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date must be later than current date + 2"}]},{"trade":{"type":"Forward","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-21","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date falls on weekend"},{"errorMessage":"Value Date must be later than current date + 2"}]},{"trade":{"type":"Forward","customer":"PLUTO2","ccyPair":"EURUSD","type":"Forward","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-08","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is before Trade Date"},{"errorMessage":"Value Date must be later than current date + 2"}]},{"trade":{"type":"Forward","customer":"PLUT02","ccyPair":"EURUSD","type":"Forward","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-08","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is before Trade Date"},{"errorMessage":"CounterParty not supported"},{"errorMessage":"Value Date must be later than current date + 2"}]},{"trade":{"type":"Forward","customer":"PLUTO3","ccyPair":"EURUSD","type":"Forward","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-22","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"},"validationStatus":"failure","validationErrors":[{"errorMessage":"CounterParty not supported"},{"errorMessage":"Value Date must be later than current date + 2"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"EUROPEAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-19","payCcy":"USD","excerciseStartDate":null,"premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO2","ccyPair":"EURUSD","type":"VanillaOption","direction":"SELL","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"EUROPEAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-21","payCcy":"USD","excerciseStartDate":null,"premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"EUROPEAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-25","payCcy":"USD","excerciseStartDate":null,"premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"ExpiryDate and premiumDate must be before deliveryDate"},{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"AMERICAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-19","payCcy":"USD","excerciseStartDate":"2016-08-12","premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO2","ccyPair":"EURUSD","type":"VanillaOption","direction":"SELL","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"AMERICAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-21","payCcy":"USD","excerciseStartDate":"2016-08-12","premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"AMERICAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-25","payCcy":"USD","excerciseStartDate":"2016-08-12","premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"ExpiryDate and premiumDate must be before deliveryDate"},{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","direction":"BUY","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"AMERICAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-19","payCcy":"USD","excerciseStartDate":"2016-08-10","premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"Value Date is missing"}]},{"trade":{"type":"VanillaOption","customer":"PLUTO3","ccyPair":"EURUSD","type":"VanillaOption","direction":"SELL","tradeDate":"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":null,"legalEntity":"CS Zurich","trader":"Johann Baumfiddler","style":"AMERICAN","strategy":"CALL","deliveryDate":"2016-08-22","expiryDate":"2016-08-19","payCcy":"USD","excerciseStartDate":"2016-08-10","premium":0.20,"premiumType":"%USD","premiumDate":"2016-08-12"},"validationStatus":"failure","validationErrors":[{"errorMessage":"CounterParty not supported"},{"errorMessage":"Value Date is missing"}]}]
```
Graceful shutdown:

```
curl -X POST http://localhost:8080/shutdown
{"message":"Shutting down, bye..."}

```
#### Option 3. Run integration tests
Run [com.validator.trade.controller.TradeValidationControllerTest](src/test/java/com/validator/trade/controller/TradeValidationControllerTest.java) as JUnit Test.

## Motivation
Code Test as a one step in recruitment process.

## License
[MIT License](https://en.wikipedia.org/wiki/MIT_License)