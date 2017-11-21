package com.validator.trade.validator;

import java.util.Collection;

import com.google.common.base.Enums;
import com.google.common.collect.Lists;
import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Trade;
import com.validator.trade.model.ValidCounterParty;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises an error for an unsupported CounterParty.
 * @author Michal
 *
 */
@ToString
public class CounterPartyValidator implements TradeValidator<Trade> {

	private Collection<String> supportedCustomers = Lists.newArrayList();

	@Override
	public TradeValidationResult validate(Trade trade) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(trade);
		if(null == trade.getCustomer()) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_IS_MISSING));
		} else if (!Enums.getIfPresent(ValidCounterParty.class, trade.getCustomer()).isPresent()) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_NOT_SUPPORTED));
	    }
		return validationResult;
	}
}
