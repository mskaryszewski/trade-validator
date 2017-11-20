package com.validator.trade.validator;

import com.validator.trade.model.Spot;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

@ToString
public class SpotValueDateValidator implements TradeValidator<Spot> {

	@Override
	public TradeValidationResult validate(Spot spot) {
		return TradeValidationResult.forTrade(spot);
	}
}
