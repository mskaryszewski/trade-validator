package com.validator.trade.validator;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.TradeValidationResults;
import com.validator.trade.validator.registry.TradeValidatorsRegistry;

@Service
public class TradeValidatorService implements ValidatorService<Trade, TradeValidationResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorService.class);	
	
	@Autowired
	TradeValidatorsRegistry tradeValidatorsRegistry;
	
	@Override
	public TradeValidationResult validate(Trade trade) {
		Collection<Validator> validators = tradeValidatorsRegistry.getTradeValidatorsForATrade(trade);
		logger.debug("validators: {} for trade type {}", validators, trade.getType());
		return null;
	}

	@Override
	public TradeValidationResults validateMultiple(Collection<Trade> entities) {
		return null;
	}
}
