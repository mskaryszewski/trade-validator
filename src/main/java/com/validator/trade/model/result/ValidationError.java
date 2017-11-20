package com.validator.trade.model.result;

import com.validator.trade.model.Trade;

import lombok.ToString;

@ToString
public class ValidationError {
	
	private final String errorMessage;
	private final Trade  trade;
	
	public ValidationError withErrorMessageAndTrade(String errorMessage, Trade trade) {
		return new ValidationError(errorMessage, trade);
	}
	
	private ValidationError(String errorMessage, Trade trade) {
		this.errorMessage = errorMessage;
		this.trade = trade;
	}
}
