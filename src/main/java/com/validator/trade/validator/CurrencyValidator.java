package com.validator.trade.validator;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

@ToString
public class CurrencyValidator implements TradeValidator {

	@Override
	public TradeValidationResult validate(Trade trade) {
		return TradeValidationResult.success();
	}
}
