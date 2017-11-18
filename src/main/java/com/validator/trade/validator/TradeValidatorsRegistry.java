package com.validator.trade.validator;

import java.util.Collection;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("tradeValidation")
@Component
public class TradeValidatorsRegistry {
	
    private Map<String, Collection<String>> validatorsForTradeType;

	public Map<String, Collection<String>> getValidatorsForTradeType() {
		return validatorsForTradeType;
	}

	public void setValidatorsForTradeType(Map<String, Collection<String>> validatorsForTradeType) {
		this.validatorsForTradeType = validatorsForTradeType;
	}
	
	public Collection<String> getValidatorForTradeType(String tradeType) {
		return validatorsForTradeType.get(tradeType);
	}

	@Override
	public String toString() {
		return "TradeValidatorManager [validatorsForTradeType=" + validatorsForTradeType + "]";
	}
}
