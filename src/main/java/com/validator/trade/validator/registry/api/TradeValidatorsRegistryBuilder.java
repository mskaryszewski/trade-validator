package com.validator.trade.validator.registry.api;

import java.util.Collection;
import java.util.Map;

import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeType;
import com.validator.trade.validator.TradeValidator;

public interface TradeValidatorsRegistryBuilder<T extends Trade> {
	
	public Map<TradeType, Collection<TradeValidator<T>>> getTradeValidators();
	
}
