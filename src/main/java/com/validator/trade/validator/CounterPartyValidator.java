package com.validator.trade.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;

import lombok.ToString;

/**
 * Validator which raises an error for an unsupported counterparty.
 * @author Michal
 *
 */
@ToString
public class CounterPartyValidator implements TradeValidator<Trade> {

	private Collection<String> supportedCustomers = Lists.newArrayList();

	@Value("${validator.customer.validCustomers}")
	public String validCustomersFromString(String customerList) {
		supportedCustomers = new ArrayList<>(Arrays.asList(customerList.split(",")));
		return supportedCustomers.toString();
	}

	@Override
	public TradeValidationResult validate(Trade trade) {
		System.out.println(supportedCustomers);
		return TradeValidationResult.forTrade(trade);
	}
}
