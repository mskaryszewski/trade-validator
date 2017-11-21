package com.validator.trade.validator;

import java.time.LocalDate;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Forward;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;
import com.validator.trade.validator.timeprovider.ConstantDateProvider;
import com.validator.trade.validator.timeprovider.DateProvider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * For Forwards trades value day must be always > current_date + 2.
 * Validator raises error if it's not the case.
 * @author Michal
 *
 */
@ToString
@Getter
@Setter
public class ForwardValueDateValidator implements TradeValidator<Forward> {
	
	DateProvider dateProvider = new ConstantDateProvider();
	
	@Override
	public TradeValidationResult validate(Forward forward) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(forward);

		LocalDate currentDate = dateProvider.getDate();
		LocalDate valueDate   = forward.getValueDate();
		
		if(null == valueDate) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING));
		}
		
		if(validationResult.validationPassed() && !valueDate.isAfter(currentDate.plusDays(2))) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.FORWARD_VALUE_DATE_INCORRECT));
		}
		
		return validationResult;
	}
}
