package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class CurrencyValidatorTest {
	
	private final TradeValidator<Trade> validator = new CurrencyValidator();
	private Trade trade;

	@Before
	public void init() {
		trade = new Spot();
	}
	
	@Test
	public void validCurrencyPairTest() {
		trade.setCcyPair("EURUSD");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void baseCurrencyIncorrectTest() {
		trade.setCcyPair("AAAUSD");

		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void quoteCurrencyIncorrectTest() {
		trade.setCcyPair("EURAAA");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void bothCurrenciesIncorrectTest() {
		trade.setCcyPair("AAAAAA");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(2));
	}
	
	@Test
	public void currencyPairShorterThanLengthOf1CurrencyCodeTest() {
		trade.setCcyPair("E");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void currencyPairMissingTest() {
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
	
	@Test
	public void currencyPairEmptyTest() {
		trade.setCcyPair("");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
}
