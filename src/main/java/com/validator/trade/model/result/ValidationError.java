package com.validator.trade.model.result;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class ValidationError {
	
	private String errorMessage;
	
	public static ValidationError fromErrorMessage(String errorMessage) {
		return new ValidationError(errorMessage);
	}
	
	private ValidationError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	private ValidationError() {}
}
