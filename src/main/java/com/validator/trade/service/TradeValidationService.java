package com.validator.trade.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.validator.TradeValidator;
import com.validator.trade.validator.registry.api.TradeValidationRegistry;

@Service
public class TradeValidationService<T extends Trade> implements ValidationService<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidationService.class);	
	
	@Autowired
	TradeValidationRegistry<T> tradeValidatorYamlRegistry;
	
	public TradeValidationResult validate(T trade) {
		
		logger.debug("Trade validation started for trade {}", trade);
		TradeValidationResult tradeValidationResult = TradeValidationResult.forTrade(trade);
		Collection<TradeValidator<T>> validators = tradeValidatorYamlRegistry.getValidators(trade);
		
		validators.parallelStream()
				  .map(tradeValidator -> tradeValidator.validate(trade))
				  .filter(TradeValidationResult::validationFailed)
				  .collect(Collectors.toList())
				  .forEach(validationResult -> tradeValidationResult.addErrors(validationResult.getValidationErrors()));
		
		logger.debug("TradeValidationResult {} for trade {}", tradeValidationResult, trade);
		return tradeValidationResult;
	}

	public Collection<TradeValidationResult> validateMultiple(Collection<T> entities) {
		return entities
				.parallelStream()
                .map(this::validate)
                .collect(Collectors.toList());
	}
}
