package com.validator.trade.config;

import org.springframework.context.ApplicationContext;

import com.validator.trade.utils.ApplicationContextProvider;

public class ConfigManager {

	private static Configuration configuration;
	private ConfigManager() {};
	public static Configuration getConfiguration() {
		if (null == configuration) {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			configuration = ctx.getBean(Configuration.class);
		}
		return configuration;
	}
}





