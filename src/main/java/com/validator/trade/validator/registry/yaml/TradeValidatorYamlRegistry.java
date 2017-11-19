package com.validator.trade.validator.registry.yaml;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeType;
import com.validator.trade.validator.TradeValidator;
import com.validator.trade.validator.registry.api.TradeValidationRegistry;
import com.validator.trade.validator.registry.api.TradeValidatorsRegistryBuilder;

/**
 * Registry which maps type of trade (Option, Spot, Forward) to a collection of Validators.
 * @author Michal
 *
 */
@Component
public class TradeValidatorYamlRegistry implements TradeValidationRegistry {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorYamlRegistry.class);	
	
	@Autowired
	private TradeValidatorsRegistryBuilder tradeValidatorsYamlRegistryBuilder;
	
	private Map<TradeType, Collection<TradeValidator>> tradeValidators = Maps.newHashMap();

	public Map<TradeType, Collection<TradeValidator>> getTradeValidators() {
		return tradeValidators;
	}
	
	/**
	 * Retrieves validators for a given type of trade.
	 * @param tradeType
	 * @return
	 */
	@Override
	public Collection<TradeValidator> getValidators(Trade trade) {
		return tradeValidators.get(trade.getType());
	}

	@Autowired
	public void registerTradeValidators() {
		tradeValidators = tradeValidatorsYamlRegistryBuilder.getTradeValidators();
		logger.info("All registered trade validators: {}", tradeValidators);
	}
}
