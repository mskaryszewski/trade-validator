package com.validator.trade.validator;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.validator.trade.model.TradeType;
import com.validator.trade.utils.CollectionUtils;

/**
 * Object which reads application.yml
 * Holds a list of configured trade validators assigned to a specific product type.
 * @author Michal
 *
 */
@ConfigurationProperties("tradeValidation")
@Component
public class TradeValidatorsConfigReader {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidatorsConfigReader.class);	
	
    private Map<String, Collection<String>> validatorsForTradeType;

	public Map<String, Collection<String>> getValidatorsForTradeType() {
		return validatorsForTradeType;
	}

	public void setValidatorsForTradeType(Map<String, Collection<String>> validatorsForTradeType) {
		this.validatorsForTradeType = validatorsForTradeType;
	}
	
	/**
	 * Retrieves validators generic for all trade types and specific for a particular type of trade
	 * @param tradeType
	 * @return
	 */
	public Collection<String> getValidatorForTradeType(TradeType tradeType) {
		logger.debug("getValidatorForTradeType");
		return CollectionUtils.concat(
				validatorsForTradeType.get(ALL),
				validatorsForTradeType.get(tradeType.toString()));
	}

	@Override
	public String toString() {
		return "TradeValidatorManager [validatorsForTradeType=" + validatorsForTradeType + "]";
	}
	
	private final String ALL = "ALL";
}
