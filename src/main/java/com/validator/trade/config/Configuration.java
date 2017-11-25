package com.validator.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Useful structure holding entire business requirements configured in trade-validator.properties.
 * Entire configuration of all validators should be configured in that one place (trade-validator.properties),
 * returned by ConfigurationManager and used by validator implementation.
 * @author Michal
 *
 */
@Component
@ConfigurationProperties("tradevalidation")
@PropertySource("classpath:/trade-validator.properties")
public class Configuration {
	
	private String counterparties;

	public String getCounterparties() {
		return counterparties;
	}

	public void setCounterparties(String counterparties) {
		this.counterparties = counterparties;
	}

}
