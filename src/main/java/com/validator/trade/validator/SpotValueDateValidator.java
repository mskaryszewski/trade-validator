package com.validator.trade.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Spot;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;
import com.validator.trade.validator.timeprovider.ConstantDateProvider;
import com.validator.trade.validator.timeprovider.DateProvider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * For Spot trades value day must be always (not necessarily but let's assume always) equal to current_date + 2.
 * Validator raises error if it's not the case.
 * @author Michal
 *
 */
@ToString
@Setter
@Getter
public class SpotValueDateValidator implements TradeValidator<Spot> {
	
	DateProvider dateProvider = new ConstantDateProvider();
	
	@Override
	public TradeValidationResult validate(Spot spot) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(spot);

		LocalDate currentDate = dateProvider.getDate();
		LocalDate valueDate   = spot.getValueDate();
		
		if(null == valueDate) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING));
		}
		
		if(validationResult.validationPassed() && ChronoUnit.DAYS.between(currentDate, valueDate) != 2L) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.SPOT_VALUE_DATE_INCORRECT));
		}
		
		return validationResult;
	}
}
