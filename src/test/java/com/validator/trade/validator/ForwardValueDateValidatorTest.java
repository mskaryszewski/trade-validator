package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.validator.trade.model.Forward;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class ForwardValueDateValidatorTest {
	
	private final ForwardValueDateValidator validator = new ForwardValueDateValidator();
	private Forward forwardTrade;
	
	@Before
	public void init() {
		forwardTrade = new Forward();
	}

	@Test
	public void valueDateAfterCurrentDayPlusMoreThanTwoTest() {
		forwardTrade.setValueDate(LocalDate.of(2016, Month.OCTOBER, 20));

		TradeValidationResult result = validator.validate(forwardTrade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void valueDateAfterCurrentDayPlusTwoTest() {
		forwardTrade.setValueDate(LocalDate.of(2016, Month.OCTOBER, 11));

		TradeValidationResult result = validator.validate(forwardTrade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void valueDateIsNullTest() {
		forwardTrade.setValueDate(LocalDate.of(2000, Month.JANUARY, 1));

		TradeValidationResult result = validator.validate(forwardTrade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
}
