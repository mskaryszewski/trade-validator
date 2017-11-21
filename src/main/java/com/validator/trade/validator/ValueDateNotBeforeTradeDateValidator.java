package com.validator.trade.validator;

import java.time.LocalDate;

import com.validator.trade.model.ErrorNotification;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

import lombok.ToString;

/**
 * Validator which raises an error when value date is before trade date
 * @author Michal
 *
 */
@ToString
public class ValueDateNotBeforeTradeDateValidator implements TradeValidator<Trade> {

	public TradeValidationResult validate(Trade trade) {
		
		TradeValidationResult validationResult = TradeValidationResult.forTrade(trade);
		
		LocalDate tradeDate = trade.getTradeDate();
		LocalDate valueDate = trade.getValueDate();
		
		if(null == tradeDate) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.TRADE_DATE_IS_MISSING));
		}
		
		if(null == valueDate) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_IS_MISSING));
		}
		
		if(validationResult.validationPassed() && valueDate.isBefore(tradeDate)) {
			validationResult.addError(ValidationError.fromErrorMessage(ErrorNotification.VALUE_DATE_BEFORE_TRADE_DATE));
		}
		
		return validationResult;
	}
}
