package com.validator.trade.validator;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

public interface TradeValidator<T extends Trade> {
	public TradeValidationResult validate(T trade);
}
