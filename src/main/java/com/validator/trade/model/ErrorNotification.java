package com.validator.trade.model;

/**
 * Repository of error message content sent to consumer of validation service.
 * 
 * In fact trade validation application as a simple software to generate error notifications.
 * At this light it is reasonable to have a full control of content of all messages.
 * Thanks to this we see all kinds of error notifications that can be returned to the client.
 * 
 * Moreover this approach supports more robust automatic tests 
 * as we can refer to a single place of message definition and check correctness of message content.
 * 
 * @author Michal
 */
public enum ErrorNotification {
	
	VALUE_DATE_IS_MISSING("Value date is missing"),
	VALUE_DATE_BEFORE_TRADE_DATE("Value Date is before Trade Date"),
	VALUE_DATE_FALLS_ON_WEEKEND("Value Date falls on weekend"),
	VALUE_DATE_FALLS_ON_HOLIDAY("Value Date falls on holiday"),
	TRADE_DATE_IS_MISSING("Trade Date is missing"),
	
	COUNTERPARTY_IS_MISSING("CounterParty missing"),
	COUNTERPARTY_NOT_SUPPORTED("CounterParty not supported"),
	
	CURRENCY_PAIR_MISSING("Currency Pair cannot be null"),
	CURRENCY_PAIR_MUST_CONTAIN_6_CHARS("Currency Pair must contain 6 characters [A-Z]"),
	BASE_CURRENCY_NOT_VALID("Base Currency is not a valid ISO 4217 Code"),
	QUOTE_CURRENCY_NOT_VALID("Quote Currency is not a valid ISO 4217 Code"),

	SPOT_VALUE_DATE_INCORRECT("Difference between current date and value date is not equal 2"),
	
	FORWARD_VALUE_DATE_INCORRECT("Value Date must be later than current date + 2"),
	
	OPTION_STYLE_MISSING("Option Style Missing"),
	OPTION_STYLE_NOT_SUPPORTED("Option Style not supported"),
	OPTION_MISSING_DATE("Option cannot have empty expiryDate, premiumDate and deliveryDate"),
	OPTION_INCORRECT_DATE("ExpiryDate and premiumDate must be before deliveryDate"),
	AMERICAN_OPTION_MISSING_DATE("American Option cannot have empty excerciseStartDate, tradeDate or expiryDate"),
	AMERICAN_OPTION_INCORRECT_EXCERCISE_START_DAY("American Option excercise start day must be after trade date and before expiry day");
	
	private String content;
	
	private ErrorNotification(String content) { 
		this.content = content;	
	}
	public String getContent() {
		return this.content;
	}
}
