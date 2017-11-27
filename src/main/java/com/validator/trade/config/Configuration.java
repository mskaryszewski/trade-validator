package com.validator.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Useful structure holding entire business requirements configured in trade-validator.properties.
 * Entire configuration of all validators should be configured in that one place (trade-validator.properties),
 * 
 * @author Michal
 *
 */
@Component
@ConfigurationProperties("tradevalidation")
@PropertySource("classpath:/trade-validator.properties")
public class Configuration {
	
}
