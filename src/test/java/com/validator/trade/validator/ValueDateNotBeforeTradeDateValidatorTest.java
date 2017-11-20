package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Test;

import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class ValueDateNotBeforeTradeDateValidatorTest {
	
	private final TradeValidator<Trade> validator = new ValueDateNotBeforeTradeDateValidator();
	
	private LocalDate beginningOfYear2000 = LocalDate.of(2000, Month.JANUARY, 1);
	private LocalDate beginningOfYear2010 = LocalDate.of(2010, Month.JANUARY, 1);
	
	private final Trade trade = new Spot();

	@Test
	public void valueDateEqualToTradeDateTest() {
		trade.setTradeDate(beginningOfYear2000);
		trade.setValueDate(beginningOfYear2000);
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void valueDateAfterTradeDateTest() {
		
		trade.setValueDate(beginningOfYear2010);
		trade.setTradeDate(beginningOfYear2000);
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void valueDateBeforeTradeDateTest() {
		trade.setValueDate(beginningOfYear2000);
		trade.setTradeDate(beginningOfYear2010);
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void valueDateNull() {
		trade.setTradeDate(beginningOfYear2000);
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void tradeDateNull() {
		trade.setValueDate(beginningOfYear2000);
		
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
}
