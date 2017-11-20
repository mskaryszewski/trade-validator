package com.validator.trade.validator;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

@ToString
public class ValueDateNotOnNonWorkingDayForCurrencValidator implements TradeValidator<Trade> {

	@Override
	public TradeValidationResult validate(Trade trade) {
		return TradeValidationResult.forTrade(trade);
	}
}
