package com.validator.trade.validator;

import java.time.LocalDate;

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
public class OptionExcerciseStartDateValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(option);
		if(!"American".equals(option.getStyle())) {
			return validationResult;
		}
		
		if(excerciseStartDateTradeDateOrExpiryDateIsNull(option)) {
			validationResult.addError(ValidationError.fromErrorMessage("American Option cannot have empty excerciseStartDate, tradeDate or expiryDate"));
		}
		
		if(validationResult.validationPassed() && !excerciseStartDateIsAfterTradeDateAndBeforeExpiryDate(option)) {
			validationResult.addError(ValidationError.fromErrorMessage("American Option excercise start day must be after trade date and before expiry day"));
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
