# trade-validator

This project is a RESTful web service implemented using [Spring Boot](http://projects.spring.io/spring-boot).
It's main purpose it to expose extensible Trade Validation service.

### Endpoints
`/trade` - validates a single trade using trade validators used for all trades as well as specified only for this trade flavor. It returns trade with validation status and corresponsing error messages  
`/trades` - validates chunk of trades and returns list of trades with validation status and corresponsing error messages  
`/shutdown` - graceful shutdown solution using [Spring Boot Actuator module](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready-endpoints)  

### Main assumptions, requirements and implemented solutions

The service exposes a REST interface consuming trades in JSON format and returning validation result to the client.

Service is flexible to extend the validation logic in terms of new business validation rules and supported new products.  
All Validators need to implement `TradeValidator` interface:
```java
public interface TradeValidator<T extends Trade> {
	TradeValidationResult validate(T trade);
}
```

Flexibility and extensibility is implemented by introducing [application.yml](src/main/resources/application.yml) configuration file when we define different types of trades together with their Validators.
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
To add a new type of trade and/or trade validator you simply need to add it to [application.yml](src/main/resources/application.yml) file.
This configuration file is parsed by `TradeValidatorsYamlConfigReader` and stored in `TradeValidatorYamlRegistry`.

Additionally each validator per convention needs to be placed in `com.validator.trade.validator` package as only then they are instantiated by `TradeValidatorsYamlRegistryBuilder` using Java [reflection](https://docs.oracle.com/javase/tutorial/reflect/member/ctorInstance.html).

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

The only worth to be mentioned here Validator is `ValueDateOnWorkingDayValidator`. It consumes RESTful web service exposed by [holidayapi.com](https://holidayapi.com) by using Spring's `RestTemplate` shipped into `HolidayApiService`.

Thanks to this solution it is validated if trade's valuation date does not fall on weekend. This is the main bottleneck of the Trade Validation solution as it's dependent on external service. Unavailability of [holidayapi.com](https://holidayapi.com) or poor connection may degrade performance significantly.

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
```
postman
curl
integration tests
```

## Motivation
Code Test as a one step in recruitment process.

## License
[MIT License](https://en.wikipedia.org/wiki/MIT_License)