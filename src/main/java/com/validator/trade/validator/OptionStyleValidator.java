package com.validator.trade.validator;

import com.google.common.base.Enums;
import com.validator.trade.model.Option;
import com.validator.trade.model.OptionStyle;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which checks validity of OptionStyle
 * @author Michal
 *
 */
@ToString
public class OptionStyleValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(option);
		if(null == option.getStyle()) {
			validationResult.addError(ValidationError.fromErrorMessage("Option Style Missing"));
		} else if (!Enums.getIfPresent(OptionStyle.class, option.getStyle()).isPresent()) {
			validationResult.addError(ValidationError.fromErrorMessage("Option Style not supported"));
	    }
		return validationResult;
	}
}
