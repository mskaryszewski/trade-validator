package com.validator.trade.validator.registry;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeType;
import com.validator.trade.validator.Validator;

/**
 * Registry which maps type of trade (Option, Spot, Forward) to a collection of Validators.
 * @author Michal
 *
 */
@Component
public class TradeValidatorsRegistry {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorsRegistry.class);	
	
	@Autowired
	private TradeValidatorsRegistryBuilder tradeValidatorsYamlRegistryBuilder;
	
	private Map<TradeType, Collection<Validator>> tradeValidators = Maps.newHashMap();

	public Map<TradeType, Collection<Validator>> getTradeValidators() {
		return tradeValidators;
	}
	
	/**
	 * Retrieves validators for a given type of trade.
	 * @param tradeType
	 * @return
	 */
	public Collection<Validator> getTradeValidatorsForATrade(Trade trade) {
		return tradeValidators.get(trade.getType());
	}

	/**
	 * Sets trade validator registry used to choose collection of validators for a given product type.
	 * Intentional RuntimeException is thrown if application.yml is configured incorrectly in the following situations:
	 * - unknown tradeType was introduced (example: typo by introducing 'FORARD' instead of 'FORWARD')
	 * - incorrect Validator class name was configured, example: CurValidator instead of CurrencyValidator
	 */
	@Autowired
	public void setTradeValidators() {
		tradeValidators = tradeValidatorsYamlRegistryBuilder.getTradeValidators();
		logger.info("Trade Validators Registry: {}", tradeValidators);
	}
}
