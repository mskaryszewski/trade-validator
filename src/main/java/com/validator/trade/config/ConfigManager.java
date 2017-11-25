package com.validator.trade.config;

import org.springframework.context.ApplicationContext;

import com.validator.trade.utils.ApplicationContextProvider;

/**
 * Returns Configuration object holding entire confguration defined in application.properties
 * @author Michal
 *
 */
public class ConfigManager {

	/**
	 * We can treat it just as a util class, should not expose public constructor
	 */
	private ConfigManager() {};
	
	/**
	 * Configuration loaded based on application.properties
	 */
	private static Configuration configuration;
	
	/**
	 * 
	 * @return application context used to retrieve configuration specified in application.context
	 */
	public static Configuration getConfiguration() {
		if (null == configuration) {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			configuration = ctx.getBean(Configuration.class);
		}
		return configuration;
	}
}





