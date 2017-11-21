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

import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class OptionExcerciseStartDateValidatorTest {
	
	private final TradeValidator<Option> validator = new OptionExcerciseStartDateValidator();
	private final LocalDate beginningOfYear2001 = LocalDate.of(2001, Month.JANUARY, 1);
	private final LocalDate beginningOfYear2002 = LocalDate.of(2002, Month.JANUARY, 1);
	private final LocalDate beginningOfYear2003 = LocalDate.of(2003, Month.JANUARY, 1);
	private Option option;

	@Before
	public void init() {
		option = new Option();
	}
	
	@Test
	public void europeanOptionWithInvalidDatesTest() {
		option.setStyle("European");
		option.setTradeDate(beginningOfYear2001);
		option.setExcerciseStartDate(beginningOfYear2001);
		option.setExpiryDate(beginningOfYear2001);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void optionWithValidDatesTest() {
		option.setStyle("American");
		option.setTradeDate(beginningOfYear2001);
		option.setExcerciseStartDate(beginningOfYear2002);
		option.setExpiryDate(beginningOfYear2003);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void optionWithInValidDatesTest() {
		option.setStyle("American");
		option.setTradeDate(beginningOfYear2001);
		option.setExcerciseStartDate(beginningOfYear2001);
		option.setExpiryDate(beginningOfYear2003);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void optionWithNullExcersiseStartDateTest() {
		option.setStyle("American");
		option.setTradeDate(beginningOfYear2001);
		option.setExpiryDate(beginningOfYear2003);
		
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
}
