package com.validator.trade.model.result;

import java.util.UUID;

public class ValidationError {
	
	private final String errorMessage;
	private final UUID tradeId;
	
	public ValidationError(String errorMessage, UUID tradeId) {
		this.errorMessage = errorMessage;
		this.tradeId = tradeId;
	}

	@Override
	public String toString() {
		return "ValidationError [errorMessage=" + errorMessage + ", tradeId=" + tradeId + "]";
	}
}
