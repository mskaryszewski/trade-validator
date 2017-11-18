package com.validator.trade.validator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeValidationResult;
import com.validator.trade.model.TradeValidationResults;

@Service
public class TradeValidatorService implements ValidatorService<Trade, TradeValidationResult> {
	
	@Autowired
	TradeValidatorsRegistry tradeValidatorsRegistry;
	
	@Override
	public TradeValidationResult validate(Trade trade) {
		//Collection<String> validators = tradeValidationConfiguration.getValidatorsForTradeType(trade.getTradeType());
		Collection<String> validators = tradeValidatorsRegistry.getValidatorForTradeType("SPOT");
		return null;
	}

	@Override
	public TradeValidationResults validateMultiple(Collection<Trade> entities) {
		return null;
	}
}
