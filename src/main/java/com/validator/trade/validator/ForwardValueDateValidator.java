package com.validator.trade.validator;

import com.validator.trade.model.Forward;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

@ToString
public class ForwardValueDateValidator implements TradeValidator<Forward> {

	@Override
	public TradeValidationResult validate(Forward forward) {
		return TradeValidationResult.forTrade(forward);
	}
}
