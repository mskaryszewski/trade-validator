package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;
import com.validator.trade.service.HolidayApiService;

public class ValueDateOnWorkingDayValidatorTest {
	
	private final ValueDateOnWorkingDayValidator validator = new ValueDateOnWorkingDayValidator();
	private HolidayApiService holidayApiService;

	private final Trade trade = new Spot();
	
	@Before
	public void init() {
		holidayApiService = mock(HolidayApiService.class);
		validator.setHolidayApiService(holidayApiService);
	}

	@Test
	public void valueDateFallsNotOnWeekend() {
		
		LocalDate valueDate = LocalDate.of(2017, Month.AUGUST, 1);
		
		when(holidayApiService.isHoliday(valueDate)).thenReturn(false);
		
		trade.setValueDate(valueDate);
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void valueDateNull() {
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING)));
	}
	
	@Test
	public void valueDateFallsOnWeekend() {
		
		LocalDate valueDate = LocalDate.of(2017, Month.NOVEMBER, 4);
		
		when(holidayApiService.isHoliday(valueDate)).thenReturn(false);
		
		trade.setValueDate(valueDate);
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_FALLS_ON_WEEKEND)));
	}
	
	@Test
	public void valueDateFallsOnWeekendAndHoliday() {
		
		LocalDate valueDate = LocalDate.of(2016, Month.MAY, 1);
		
		when(holidayApiService.isHoliday(valueDate)).thenReturn(true);
		
		trade.setValueDate(valueDate);
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(2));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_FALLS_ON_WEEKEND)));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_FALLS_ON_HOLIDAY)));
	}
}
