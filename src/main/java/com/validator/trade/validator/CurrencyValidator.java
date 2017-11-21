package com.validator.trade.validator;

import java.util.Currency;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises an error if currency is invalid ISO CODE
 * ISO 4217 defines always a-3character currency code.
 * ccyPair is always a 6-character string, it's safe to treat first three characters as first currency
 * and last three characters as second currency.
 * 
 * Correctness of currency is implemented by using Java's Currency getInstance(String) method.
 *  
 * @see java.util.Currency#getInstance(String)
 *   
 * @author Michal
 *
 */
@ToString
public class CurrencyValidator implements TradeValidator<Trade> {

	@Override
	public TradeValidationResult validate(Trade trade) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(trade);
		String currency = trade.getCcyPair();
		
		if(null == currency) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.CURRENCY_PAIR_NULL));
		}
		
		if(validationResult.validationPassed() && !currency.matches("[A-Z]{6}")) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.CURRENCY_PAIR_MUST_CONTAIN_6_CHARS));
		}
		
		if(validationResult.validationPassed()) {
			String baseCurrency  = currency.substring(0, 3);
			String quoteCurrency = currency.substring(3);
			
			try {
				Currency.getInstance(baseCurrency);
			} catch (IllegalArgumentException e) {
				validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.BASE_CURRENCY_NOT_VALID));
			}
			try {
				Currency.getInstance(quoteCurrency);
			} catch (IllegalArgumentException e) {
				validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.QUOTE_CURRENCY_NOT_VALID));
			}
		}
		return validationResult;
	}
}
