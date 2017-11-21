package com.validator.trade.validator;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.context.ApplicationContext;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;
import com.validator.trade.service.HolidayApiService;
import com.validator.trade.utils.ApplicationContextProvider;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValueDateOnWorkingDayValidator implements TradeValidator<Trade> {
	
	private HolidayApiService holidayApiService;

	@Override
	public TradeValidationResult validate(Trade trade) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(trade);
		LocalDate valueDate = trade.getValueDate();
		
		if(null == valueDate) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING));
		}
		
		if(validationResult.validationPassed() && 
				(valueDate.getDayOfWeek() == DayOfWeek.SUNDAY || valueDate.getDayOfWeek() == DayOfWeek.SATURDAY)) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_FALLS_ON_WEEKEND));
		}
		
		if(null == holidayApiService) {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	    	this.holidayApiService = ctx.getBean(HolidayApiService.class);
		}
		
		if(null != valueDate) {
			boolean isHoliday = holidayApiService.isHoliday(valueDate);
			if(isHoliday) {
				validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_FALLS_ON_HOLIDAY));
			}
		}
		return validationResult;
	}
}
