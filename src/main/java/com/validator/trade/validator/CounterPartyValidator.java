package com.validator.trade.validator;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.validator.trade.config.ConfigManager;
import com.validator.trade.config.Configuration;
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
public class CounterPartyValidator implements TradeValidator<Trade> {
	
	/**
	 * List of validCounterParties configured in application.properties
	 */
	private List<String> validCounterParties = Lists.newArrayList();
	
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
	
	/**
	 * checks if given trade has valid counterparty
	 */
	private boolean isValidCounterParty(Trade trade) {
		loadConfigIfMissing();
		return validCounterParties.contains(trade.getCustomer());
	}
	
	/**
	 * Retrieves valid counterparties from application.properties.
	 * List holds already trimmed data - no need to trim it anywhere else.
	 */
	private void loadConfigIfMissing() {
		if(validCounterParties.isEmpty()) {
			Configuration configuration = ConfigManager.getConfiguration();
			String counterparties = configuration.getCounterparties();
			if(!Strings.isNullOrEmpty(counterparties)) {
				validCounterParties = Splitter
						.on(",")
						.trimResults()
						.omitEmptyStrings()
						.splitToList(counterparties);
			}
		}
	}
}
