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
import com.validator.trade.utils.ValidatorUtils;
import com.validator.trade.validator.Validator;

@Component
public class TradeValidatorsRegistryBuilder {
	
private static final Logger logger = LoggerFactory.getLogger(TradeValidatorsRegistryBuilder.class);	
	
	@Autowired
	private TradeValidatorsConfigReader tradeValidatorsConfigReader;
	
	private Map<String, Collection<Validator>> tradeValidators = Maps.newHashMap();
	
	/**
	 * Parses application.yml and creates single instances of trade validators.
	 * Trade validators are assigned to specific type of trades.
	 */
	@Autowired
	public void setTradeValidators() {
		
		Map<String, Validator> allValidators = instantiateAllValidators();
		assignValidatorsForAGivenProductType(allValidators);
		populateAllTradeValidators();
		 
		logger.info("Trade Validators Registry: {}", tradeValidators);
	}
	
	/**
	 * Create single instatnces of trade validators.
	 */
	private Map<String, Validator> instantiateAllValidators() {
		return tradeValidatorsConfigReader
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
		tradeValidators = tradeValidatorsConfigReader
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
			.collect(
					Collectors.toList());
	}
	
	/**
	 * Populates common validators to all validators specific for a given product type.
	 */
	private void populateAllTradeValidators() {
		tradeValidators
			.entrySet()
			.stream()
			.filter(e -> !ALL.equals(e.getKey()))
			.forEach(
					e -> e.getValue()
						  .addAll(
								  tradeValidators.get(ALL)));
	}
	
	private final static String ALL = "ALL";
}
