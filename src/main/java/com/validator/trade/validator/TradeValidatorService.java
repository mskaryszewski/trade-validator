package com.validator.trade.validator;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.TradeValidationResults;

@Service
public class TradeValidatorService implements ValidatorService<Trade, TradeValidationResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorService.class);	
	
	@Autowired
	TradeValidatorsRegistry TradeValidatorsregistry;
	
	@Override
	public TradeValidationResult validate(Trade trade) {
		Collection<Validator> validators = TradeValidatorsregistry.getTradeValidatorsForATrade(trade.getType());
		logger.debug("validators: {}", validators);
		return null;
	}

	@Override
	public TradeValidationResults validateMultiple(Collection<Trade> entities) {
		return null;
	}
}
