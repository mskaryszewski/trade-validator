package com.validator.trade.validator;

import org.junit.Test;

import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

public class ValueDateNotBeforeTradeDateValidatorTest {
	
	ValueDateNotBeforeTradeDateValidator validator = new ValueDateNotBeforeTradeDateValidator();

	@Test
	public void valueDateEqualToTradeDateTest() {
		Trade trade = new Spot();
		TradeValidationResult result = validator.validate(trade);
	}
	
	@Test
	public void valueDateBeforeTradeDateTest() {
		Trade trade = new Spot();
		validator.validate(trade);
	}
	
	@Test
	public void valueDateAfterTradeDateTest() {
		Trade trade = new Spot();
		validator.validate(trade);
	}
	
	@Test
	public void valueDateNull() {
		Trade trade = new Spot();
		validator.validate(trade);
	}
	
	@Test
	public void tradeDateNull() {
		Trade trade = new Spot();
		validator.validate(trade);
	}
}
