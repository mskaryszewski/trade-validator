package com.validator.trade.validator.registry.yaml;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeType;
import com.validator.trade.validator.TradeValidator;
import com.validator.trade.validator.registry.api.TradeValidatorsConfigReader;
import com.validator.trade.validator.registry.api.TradeValidatorsRegistryBuilder;

@Component
public class TradeValidatorsYamlRegistryBuilder<T extends Trade> implements TradeValidatorsRegistryBuilder<T>  {
	
	/**
	 * ConfigReader which provides a map of trade type to collection of trade validator names.
	 */
	@Autowired
	private TradeValidatorsConfigReader tradeValidatorsYamlConfigReader;
	
	/**
	 * All implementations of TradeValidator<Trade> interface
	 */
	@Autowired
    Collection<TradeValidator<T>> allTradeValidators;
	
	/**
	 * Final structure which holds a map of trade type and corresponding collection of TradeValidators.
	 * It is created by applying all implementations of TradeValidator<Trade> interface (allTradeValidators)
	 * on a parsed application.yml provided by tradeValidatorsYamlConfigReader.
	 * 
	 * If a particular validator name specified in application.yml does not exist, an error is thrown.
	 * If a particular type of trade specified in application.yml does not exist, an error is thrown.
	 * 
	 * In fact this error handling should be improved, if any configuration entry is invalid it means
	 * that Trade Validation Service does not work as expected and we cannot trust it's work.
	 * In such situation end user should be notified that service is unavailable and application.yml
	 * should be fixed. 
	 * 
	 */
	private Map<TradeType, Collection<TradeValidator<T>>> tradeValidatorsPerTradeType = Maps.newHashMap();

	/**
	 * We need to be sure that this logic is called after all dependencies are autowired,
	 * that's why we cannot implement it inside constructor.
	 */
	@PostConstruct
	private void setupTradeValidators() {
		
		Map<String, TradeValidator<T>> tradeValidatorNameToTradeValidator = allTradeValidators
				.stream()
				.collect(Collectors.toMap(
						e -> e.getClass().getSimpleName(),
						Function.identity()
						));
		
		// create a temporary structure holding type of trade name with assigned validator instances
		Map<String, Collection<TradeValidator<T>>> validatorNameToTradeValidators = tradeValidatorsYamlConfigReader
				.getValidatorsForTradeType()
				.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(),
						validators -> validators.getValue()                        // get each validator name
							.stream()
							.map(f -> tradeValidatorNameToTradeValidator.get(f))   // convert validator name to TradeValidator instance
							.collect(Collectors.toList())                          // collect all Validators to a list
						));

		// add trade validators from category 'ALL' to validators assigned to specific types of trades
		validatorNameToTradeValidators.entrySet()
				.stream()
				.filter(e -> !ALL.equals(e.getKey().toString()))
				.forEach(e -> e
						.getValue()
						.addAll(validatorNameToTradeValidators.get(ALL)));
		
		// remove trade validators for 'ALL' types of trades to keep the structure clean
		validatorNameToTradeValidators
				.keySet()
				.removeIf(validatorName -> ALL.equals(validatorName.toString()));
		
		// convert temporary structure to final one used by the applicaton with keys as Enums
		tradeValidatorsPerTradeType = validatorNameToTradeValidators
				.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> TradeType.valueOf(e.getKey()),
						Map.Entry::getValue));
	}
	
	/**
	 * Builds trade to collection of Validators Registry.
	 * Intentional RuntimeException is thrown if application.yml is configured incorrectly in the following situations:
	 * - unknown tradeType was introduced (example: typo by introducing 'FORARD' instead of 'FORWARD')
	 * - incorrect Validator class name was configured, example: CurValidator instead of CurrencyValidator
	 * 
	 * @return Configuration of trade validators for a given type of trade.
	 */
	@Override
	public Map<TradeType, Collection<TradeValidator<T>>> getTradeValidators() {
			return tradeValidatorsPerTradeType;
	}
	
	/**
	 * Constant String used to populate common validators to all validators specific for a given type of trade.
	 */
	private final static String ALL = "ALL";
}
