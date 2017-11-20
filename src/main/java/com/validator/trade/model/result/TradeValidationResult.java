package com.validator.trade.model.result;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.ToString;

@ToString
public class TradeValidationResult {
	
	private final Collection<ValidationError> validationErrors = Lists.newArrayList();
	private TradeValidationStatus validationStatus;
	
	public static TradeValidationResult success() {
		return new TradeValidationResult(TradeValidationStatus.VALIDATION_OK);
	}
	
	public static TradeValidationResult failure(ValidationError error) {
		return new TradeValidationResult(TradeValidationStatus.VALIDATION_NOK, error);
	}
	
	public TradeValidationResult() {
	}
	
	private TradeValidationResult(TradeValidationStatus status) {
		this.validationStatus = status;
	}
	
	private TradeValidationResult(TradeValidationStatus status, ValidationError error) {
		this(status);
		this.addError(error);
	}
	
	public boolean validationFailed() {
		return !validationErrors.isEmpty();
	}
	
	public TradeValidationStatus getStatus() {
		return validationStatus;
	}
	
	public void setStatus(TradeValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
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
