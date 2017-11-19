package com.validator.trade.validator.registry.yaml;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.TradeType;
import com.validator.trade.utils.ValidatorUtils;
import com.validator.trade.validator.TradeValidator;
import com.validator.trade.validator.registry.api.TradeValidatorsConfigReader;
import com.validator.trade.validator.registry.api.TradeValidatorsRegistryBuilder;

@Component
public class TradeValidatorsYamlRegistryBuilder implements TradeValidatorsRegistryBuilder  {
	
	@Autowired
	private TradeValidatorsConfigReader tradeValidatorsYamlConfigReader;
	
	private Map<String, Collection<TradeValidator>> tradeValidators = Maps.newHashMap();
	
	/**
	 * Builds trade to collection of Validators Registry.
	 * Intentional RuntimeException is thrown if application.yml is configured incorrectly in the following situations:
	 * - unknown tradeType was introduced (example: typo by introducing 'FORARD' instead of 'FORWARD')
	 * - incorrect Validator class name was configured, example: CurValidator instead of CurrencyValidator
	 * 
	 * @return Configuration of trade validators for a given type of trade.
	 */
	@Override
	public Map<TradeType, Collection<TradeValidator>> getTradeValidators() {
		return tradeValidators
					.entrySet()
					.stream()
					.filter(e -> !ALL.equals(e.getKey()))
					.collect(Collectors.toMap(
							e -> TradeType.valueOf(e.getKey()),
							Map.Entry::getValue
				        ));
	}
	
	/**
	 * Parses application.yml and creates single instances of trade validators.
	 * Trade validators are assigned to specific type of trades.
	 * Can throws RuntimeException when a user introduced incorrect configuration - i.e. incorrect Validator class name.
	 */
	@Autowired
	public void setTradeValidators() {
		Map<String, TradeValidator> allValidators = instantiateAllTradeValidators();
		assignValidatorsForAGivenProductType(allValidators);
		populateAllTradeValidators();
	}
	
	/**
	 * Create single instatnces of trade validators.
	 */
	private Map<String, TradeValidator> instantiateAllTradeValidators() {
		return tradeValidatorsYamlConfigReader
			.getValidatorsForTradeType()
			.values()
			.stream()
			.flatMap(Collection::stream)
			.collect(Collectors.toSet())
			.stream()
			.collect(Collectors.toMap(
					Function.identity(),
					ValidatorUtils::safeConvertStringToValidatorInstance
					));
	}
	
	/**
	 * Trade validators are assigned to a specific type of trades.
	 * @param allValidators Assures that one validator will not be instantiated multiple times. 
	 */
	private void assignValidatorsForAGivenProductType(Map<String, TradeValidator> allValidators) {
		tradeValidators = tradeValidatorsYamlConfigReader
			.getValidatorsForTradeType()
			.entrySet()
			.stream()
			.collect(Collectors.toMap(
					Map.Entry::getKey,
					e -> getValidatorsForAProductType(allValidators, e.getValue())
					));
	}

	/**
	 * @param productTypes
	 * @return Validator instances for a given type of product.
	 */
	private Collection<TradeValidator> getValidatorsForAProductType(Map<String, TradeValidator> allValidators, Collection<String> productTypes) {
		return productTypes
			.stream()
			.map(e -> allValidators.get(e))
			.collect(Collectors.toList());
	}
	
	/**
	 * Populates common validators to all validators specific for a given product type.
	 * The sequence of validators does not matter (which validators should be used firstly - common ones or tradeType-specific?)
	 * because always all validators are used.
	 */
	private void populateAllTradeValidators() {
		tradeValidators
			.entrySet()
			.stream()
			.filter(e -> !ALL.equals(e.getKey()))
			.forEach(e -> e.getValue().addAll(tradeValidators.get(ALL)));
	}
	
	/**
	 * Constant String used to populate common validators to all validators specific for a given type of trade.
	 */
	private final static String ALL = "ALL";
}
