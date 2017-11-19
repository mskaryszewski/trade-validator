package com.validator.trade.validator.registry.api;

import java.util.Collection;
import java.util.Map;

import com.validator.trade.model.TradeType;
import com.validator.trade.validator.Validator;

public interface TradeValidatorsRegistryBuilder {
	
	public Map<TradeType, Collection<Validator>> getTradeValidators();
	
}
