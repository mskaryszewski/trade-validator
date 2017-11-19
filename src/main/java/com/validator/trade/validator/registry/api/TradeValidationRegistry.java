package com.validator.trade.validator.registry.api;

import java.util.Collection;

import com.validator.trade.model.Trade;
import com.validator.trade.validator.TradeValidator;

public interface TradeValidationRegistry {
	
	Collection<TradeValidator> getValidators(Trade trade);

}
