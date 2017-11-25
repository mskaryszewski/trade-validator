package com.validator.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
