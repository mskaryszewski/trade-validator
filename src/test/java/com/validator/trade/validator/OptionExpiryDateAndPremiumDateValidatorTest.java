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
import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class OptionExpiryDateAndPremiumDateValidatorTest {
	
	private final TradeValidator<Option> validator = new OptionExpiryDateAndPremiumDateValidator();
	private final LocalDate beginningOfYear2001 = LocalDate.of(2001, Month.JANUARY, 1);
	private final LocalDate beginningOfYear2002 = LocalDate.of(2002, Month.JANUARY, 1);
	private Option option;

	@Before
	public void init() {
		option = new Option();
	}
	
	@Test
	public void optionWithValidDatesTest() {
		option.setExpiryDate(beginningOfYear2001);
		option.setPremiumDate(beginningOfYear2001);
		option.setDeliveryDate(beginningOfYear2002);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
		assertThat(result.validationPassed(), is(true));
		
		Collection<ValidationError> validationErrors = result.getValidationErrors();
		assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void optionWithInvalidDatesTest() {
		option.setExpiryDate(beginningOfYear2001);
		option.setPremiumDate(beginningOfYear2001);
		option.setDeliveryDate(beginningOfYear2001);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.OPTION_INCORRECT_DATE)));
	}
	
	@Test
	public void optionWithNullPremiumDateTest() {
		option.setExpiryDate(beginningOfYear2001);
		option.setDeliveryDate(beginningOfYear2002);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.OPTION_MISSING_DATE)));
	}
}
