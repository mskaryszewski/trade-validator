package com.validator.trade.validator;

import java.time.LocalDate;

import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises and error if both expiryDate and premiumDate are not before deliveryDate
 * @author Michal
 *
 */
@ToString
public class OptionExpiryDateAndPremiumDateValidator implements TradeValidator<Option> {

	@Override
	public TradeValidationResult validate(Option option) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(option);
		
		if(excerciseStartDateTradeDateOrExpiryDateIsNull(option)) {
			validationResult.addError(ValidationError.fromErrorMessage("Option cannot have empty expiryDate, premiumDate and deliveryDate"));
		}
		
		if(validationResult.validationPassed() && !expiryDateAndPremiumDateBeforeDeliveryDate(option)) {
			validationResult.addError(ValidationError.fromErrorMessage("ExpiryDate and premiumDate must be before deliveryDate"));
		}
		return validationResult;
	}
	
	private boolean excerciseStartDateTradeDateOrExpiryDateIsNull(Option option) {
		LocalDate expiryDate    = option.getExpiryDate();
		LocalDate premiumDate   = option.getPremiumDate();
		LocalDate deliveryDate  = option.getDeliveryDate();
		return     null == expiryDate 
				|| null == premiumDate
				|| null == deliveryDate;
	}
	
	private boolean expiryDateAndPremiumDateBeforeDeliveryDate(Option option) {
		LocalDate expiryDate    = option.getExpiryDate();
		LocalDate premiumDate   = option.getPremiumDate();
		LocalDate deliveryDate  = option.getDeliveryDate();
		
		return expiryDate.isBefore(deliveryDate) && premiumDate.isBefore(deliveryDate);
	}
}
