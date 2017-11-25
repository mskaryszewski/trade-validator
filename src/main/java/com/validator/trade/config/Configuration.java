package com.validator.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Useful structure holding entire business requirements configured in application.properties.
 * Entire configuration of all validators should be configured in that one place (application.properties),
 * returned by ConfigurationManager and used by validator implementation.
 * @author Michal
 *
 */
@Component
@ConfigurationProperties("tradevalidation")
public class Configuration {
	
	private String counterparties;

	public String getCounterparties() {
		return counterparties;
	}

	public void setCounterparties(String counterparties) {
		this.counterparties = counterparties;
	}

}
