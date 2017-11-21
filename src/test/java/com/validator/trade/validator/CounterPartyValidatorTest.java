package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class CounterPartyValidatorTest {
	
	private final CounterPartyValidator validator = new CounterPartyValidator();
	private final Trade trade = new Spot();

	@Test
	public void supportedCustomerTest() {
		trade.setCustomer("PLUTO1");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void unsupportedCustomerTest() {
		trade.setCustomer("INCORRECT_CUSTOMER");
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_NOT_SUPPORTED)));
	}
	
	@Test
	public void customerIsNullTest() {
		TradeValidationResult result = validator.validate(trade);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
        assertThat(validationErrors, hasItem(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_IS_MISSING)));
	}
}
