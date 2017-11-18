package com.validator.trade.validator;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.TradeType;
import com.validator.trade.utils.CollectionUtils;
import com.validator.trade.utils.ValidatorUtils;

/**
 * Registry which maps type of trade (Option, Spot, Forward) to a collection of Validators.
 * @author Michal
 *
 */
@Component
public class TradeValidatorsRegistry {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorsRegistry.class);	
	
	@Autowired
	private TradeValidatorsConfigReader tradeValidatorsConfigReader;
	
	private Map<TradeType, Collection<Validator>> tradeValidators = Maps.newHashMap();

	public Map<TradeType, Collection<Validator>> getTradeValidators() {
		return tradeValidators;
	}
	
	/**
	 * Retrieves validators for a given trade type.
	 * @param tradeType
	 * @return
	 */
	public Collection<Validator> getTradeValidatorsForATrade(TradeType tradeType) {
		return CollectionUtils.concat(
				tradeValidators.get(TradeType.ALL),
				tradeValidators.get(tradeType));
	}

	/**
	 * Sets trade validator registry used to choose collection of validators for a given product type.
	 * Intentional RuntimeException is thrown if application.yml is configured incorrectly in the following situations:
	 * - unknown tradeType was introduced (example: typo by introducing 'FORARD' instead of 'FORWARD')
	 * - incorrect Validator class name was configured, example: CurValidator instead of CurrencyValidator
	 */
	@Autowired
	public void setTradeValidators() {
		tradeValidators = tradeValidatorsConfigReader
					.getValidatorsForTradeType()
					.entrySet()
					.stream()
					.collect(Collectors.toMap(
							e -> TradeType.valueOf(e.getKey()),
							e -> ValidatorUtils.saveConvertCollectionOfStringToCollectionsOfValidatorInstances(e.getValue())
				        ));
		logger.info("Trade Validators Registry: {}", tradeValidators);
	}
}
