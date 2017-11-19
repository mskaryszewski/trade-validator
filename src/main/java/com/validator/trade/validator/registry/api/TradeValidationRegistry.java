package com.validator.trade.validator.registry.api;

import java.util.Collection;

import com.validator.trade.model.Trade;
import com.validator.trade.validator.Validator;

public interface TradeValidationRegistry {
	
	Collection<Validator> getValidators(Trade trade);

}
