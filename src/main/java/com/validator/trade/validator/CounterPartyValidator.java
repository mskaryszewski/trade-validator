package com.validator.trade.validator;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

public class CounterPartyValidator implements TradeValidator {

	@Override
	public TradeValidationResult validate(Trade trade) {
		return null;
	}
}
