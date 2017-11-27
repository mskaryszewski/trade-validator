package com.validator.trade.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises error if excercise start day is not after trade date and before expiry day
 * @author Michal
 *
 */
@ToString
@Component
public class OptionExcerciseStartDateValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(option);
		if(!"American".equals(option.getStyle())) {
			return validationResult;
		}
		
		if(excerciseStartDateTradeDateOrExpiryDateIsNull(option)) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.AMERICAN_OPTION_MISSING_DATE));
		}
		
		if(validationResult.validationPassed() && !excerciseStartDateIsAfterTradeDateAndBeforeExpiryDate(option)) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.AMERICAN_OPTION_INCORRECT_EXCERCISE_START_DAY));
		}
		return validationResult;
	}
	
	private boolean excerciseStartDateTradeDateOrExpiryDateIsNull(Option option) {
		LocalDate excerciseStartDate = option.getExcerciseStartDate();
		LocalDate tradeDate          = option.getTradeDate();
		LocalDate expiryDate         = option.getExpiryDate();
		return     null == excerciseStartDate 
				|| null == tradeDate
				|| null == expiryDate;
	}
	
	private boolean excerciseStartDateIsAfterTradeDateAndBeforeExpiryDate(Option option) {
		LocalDate excerciseStartDate = option.getExcerciseStartDate();
		LocalDate tradeDate          = option.getTradeDate();
		LocalDate expiryDate         = option.getExpiryDate();
		
		return excerciseStartDate.isAfter(tradeDate) && excerciseStartDate.isBefore(expiryDate);
	}
}
