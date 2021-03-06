package com.validator.trade.validator.registry.yaml;

import java.util.Collection;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.validator.trade.validator.registry.api.TradeValidatorsConfigReader;

/**
 * Object which parses application.yml config file and builds
 * a map of trade validators for a specific type of trade.
 * @author Michal
 *
 */
@ConfigurationProperties("tradeValidation")
@Component
public class TradeValidatorsYamlConfigReader implements TradeValidatorsConfigReader {
	
    private Map<String, Collection<String>> validatorsForTradeType;

    @Override
	public Map<String, Collection<String>> getValidatorsForTradeType() {
		return validatorsForTradeType;
	}

	public void setValidatorsForTradeType(Map<String, Collection<String>> validatorsForTradeType) {
		this.validatorsForTradeType = validatorsForTradeType;
	}
	
	@Override
	public String toString() {
		return "TradeValidatorManager [validatorsForTradeType=" + validatorsForTradeType + "]";
	}
}
