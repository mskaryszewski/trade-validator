package com.validator.trade.validator.registry;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.TradeType;
import com.validator.trade.utils.ValidatorUtils;
import com.validator.trade.validator.Validator;

@Component
public class TradeValidatorsYamlRegistryBuilder implements TradeValidatorsRegistryBuilder  {
	
private static final Logger logger = LoggerFactory.getLogger(TradeValidatorsYamlRegistryBuilder.class);	
	
	@Autowired
	private TradeValidatorsConfigReader tradeValidatorsYamlConfigReader;
	
	private Map<String, Collection<Validator>> tradeValidators = Maps.newHashMap();
	
	/**
	 * @return Configuration of trade validators for a given type of trade.
	 */
	@Override
	public Map<TradeType, Collection<Validator>> getTradeValidators() {
		logger.debug("Trade validators: {}", tradeValidators);
		return tradeValidators
					.entrySet()
					.stream()
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
		Map<String, Validator> allValidators = instantiateAllTradeValidators();
		assignValidatorsForAGivenProductType(allValidators);
		populateAllTradeValidators();
	}
	
	/**
	 * Create single instatnces of trade validators.
	 */
	private Map<String, Validator> instantiateAllTradeValidators() {
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
	 * Does not handle common validators configured as 'ALL'
	 */
	private void assignValidatorsForAGivenProductType(Map<String, Validator> allValidators) {
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
	private Collection<Validator> getValidatorsForAProductType(Map<String, Validator> allValidators, Collection<String> productTypes) {
		return productTypes
			.stream()
			.map(e -> allValidators.get(e))
			.collect(Collectors.toList());
	}
	
	/**
	 * Populates common validators to all validators specific for a given product type.
	 */
	private void populateAllTradeValidators() {
		tradeValidators
			.entrySet()
			.stream()
			.filter(e -> !ALL.equals(e.getKey()))
			.forEach(e -> e.getValue().addAll(tradeValidators.get(ALL)));
	}
	
	private final static String ALL = "ALL";
}