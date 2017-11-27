package com.validator.trade.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises an error for an unsupported CounterParty.
 * @author Michal
 *
 */
@ToString
@Component
public class CounterPartyValidator implements TradeValidator<Trade> {
	
	/**
	 * List of validCounterParties configured in trade-validator.properties
	 */
	private final List<String> validCounterParties;
	
	@Override
	public TradeValidationResult validate(Trade trade) {
		TradeValidationResult validationResult = TradeValidationResult.forTrade(trade);
		if(null == trade.getCustomer()) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_IS_MISSING));
	    } else {
			if(!isValidCounterParty(trade)) {
				validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.COUNTERPARTY_NOT_SUPPORTED));
			}
	    }
		return validationResult;
	}
	
	@Autowired
	public CounterPartyValidator(@Value("${tradevalidation.counterparties}") String counterparties) {
		if(!Strings.isNullOrEmpty(counterparties)) {
			validCounterParties = Splitter
					.on(",")
					.trimResults()
					.omitEmptyStrings()
					.splitToList(counterparties);
		} else {
			validCounterParties = Lists.newArrayList();
		}
	}
	
	/**
	 * checks if given trade has valid counterParty
	 */
	private boolean isValidCounterParty(Trade trade) {
		return validCounterParties.contains(trade.getCustomer());
	}
}
