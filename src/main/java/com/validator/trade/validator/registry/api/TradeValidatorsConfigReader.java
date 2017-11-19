package com.validator.trade.validator.registry.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface to load trade validation configuration.
 * @author Michal
 *
 */
public interface TradeValidatorsConfigReader {
	
	public Map<String, Collection<String>> getValidatorsForTradeType();
	
}
