package com.validator.trade.validator;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.validator.registry.api.TradeValidationRegistry;

@Service
public class TradeValidationService implements ValidationService {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidationService.class);	
	
	@Autowired
	TradeValidationRegistry tradeValidatorYamlRegistry;
	
	@Override
	public TradeValidationResult validate(Trade trade) {
		Collection<TradeValidator> validators = tradeValidatorYamlRegistry.getValidators(trade);
		logger.debug("validators: {} for trade type {}", validators, trade.getType());
		return null;
	}

	@Override
	public Collection<TradeValidationResult> validateMultiple(Collection<Trade> entities) {
		return null;
	}
}
