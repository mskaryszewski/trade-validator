package com.validator.trade.model.result;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.google.common.collect.Lists;
import com.validator.trade.model.Trade;

import lombok.ToString;

@ToString
public class TradeValidationResult {
	
	/**
	 * Validation Result is always applicable for a given trade
	 * that's why it is an class member
	 */
	private Trade trade;
	
	/**
	 * Validation Result Status
	 */
	private TradeValidationStatus validationStatus;
	
	/**
	 * All validation errors for a given trade
	 */
	private final Collection<ValidationError> validationErrors = Lists.newArrayList();
	
	public static TradeValidationResult forTrade(Trade trade) {
		return new TradeValidationResult(trade);
	}
	
	private TradeValidationResult(Trade trade) {
		this.trade = trade;
	}
	
	/**
	 * Constructor required for JSON parser
	 */
	private TradeValidationResult() {
	}
	
	public boolean validationFailed() {
		return !validationPassed();
	}
	
	public boolean validationPassed() {
		return validationErrors.isEmpty();
	}
	
	@JsonGetter
	/**
	 * Validation status is calculated always dynamically based on number of errors.
	 * @return
	 */
	public TradeValidationStatus getValidationStatus() {
		return validationPassed() ? TradeValidationStatus.success : TradeValidationStatus.failure;
	}
	
	public void setValidationStatus(TradeValidationStatus validationStatus) {
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

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}
}
