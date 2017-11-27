package com.validator.trade.validator;

import org.springframework.stereotype.Component;

import com.google.common.base.Enums;
import com.validator.trade.model.ErrorNotification;
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
@Component
public class OptionStyleValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(option);
		if(null == option.getStyle()) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.OPTION_STYLE_MISSING));
		} else if (!Enums.getIfPresent(OptionStyle.class, option.getStyle().toUpperCase()).isPresent()) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.OPTION_STYLE_NOT_SUPPORTED));
	    }
		return validationResult;
	}
}
