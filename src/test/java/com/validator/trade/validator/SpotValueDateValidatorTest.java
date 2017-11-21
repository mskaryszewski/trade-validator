package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Spot;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class SpotValueDateValidatorTest {
	
	private final SpotValueDateValidator validator = new SpotValueDateValidator();
	private Spot spotTrade;
	
	@Before
	public void init() {
		spotTrade = new Spot();
	}

	@Test
	public void valueDateEqualsCurrentDatePlusTwoTest() {
		spotTrade.setValueDate(LocalDate.of(2016, Month.OCTOBER, 11));

		TradeValidationResult result = validator.validate(spotTrade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
        
	}
	
	@Test
	public void valueDateIsNotEqualToCurrentDatePlusTwoTest() {
		spotTrade.setValueDate(LocalDate.of(2000, Month.JANUARY, 1));

		TradeValidationResult result = validator.validate(spotTrade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.SPOT_VALUE_DATE_INCORRECT)));
        
	}
	
	@Test
	public void valueDateIsNullTest() {
		TradeValidationResult result = validator.validate(spotTrade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING)));
	}
}
