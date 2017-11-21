package com.validator.trade.model.result;

import com.validator.trade.model.ErrorNotification;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class ValidationError {
	
	private String errorMessage;
	
	public static ValidationError fromErrorMessage(ErrorNotification message) {
		return new ValidationError(message.getContent());
	}
	
	private ValidationError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	private ValidationError() {}
}
