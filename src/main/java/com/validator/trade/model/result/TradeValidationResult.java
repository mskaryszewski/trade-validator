package com.validator.trade.model.result;

import java.util.Collection;

import com.google.common.collect.Lists;

public class TradeValidationResult {
	
	private Collection<ValidationError> validationErrors = Lists.newArrayList();
	public enum TradeValidationStatus { SUCCESS, FAILURE };
	
	public boolean validationFailed() {
		return !validationErrors.isEmpty();
	}
	
	public TradeValidationStatus getStatus() {
		return validationErrors.isEmpty() ? TradeValidationStatus.SUCCESS : TradeValidationStatus.FAILURE;
	}
	
	public void addError(ValidationError error) {
		validationErrors.add(error);
	}
	
	public void addErrors(Collection<ValidationError> errors) {
		validationErrors.addAll(errors);
	}
	
	public Collection<ValidationError> getValidationErrors() {
		return this.validationErrors;
	}
}
