package com.validator.trade.service;

import java.util.Collection;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

/**
 * @author Michal
 *
 * @param <ENTITY> Entity to be validated: Trade, Product, Currency.
 * At the time being used only to validate Trades.
 */
public interface ValidationService {
	
	TradeValidationResult validate(Trade entity);
	Collection<TradeValidationResult> validateMultiple(Collection<Trade> entities);
	
}
