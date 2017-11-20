package com.validator.trade.validator;

import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

@ToString
public class OptionStyleValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		return TradeValidationResult.forTrade(option);
	}
}
