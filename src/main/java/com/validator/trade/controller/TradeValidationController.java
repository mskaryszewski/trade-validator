package com.validator.trade.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.service.ValidationService;

@RestController
public class TradeValidationController<T extends Trade> {

	@Autowired
	private ValidationService<T> tradeValidatorService;
	
	@PostMapping("/trade")
	public TradeValidationResult validateTrade(@RequestBody T trade) {
		return tradeValidatorService.validate(trade);
	}

	@PostMapping("/trades")
	public Collection<TradeValidationResult> validateTrades(@RequestBody Collection<T> trades) {
		return tradeValidatorService.validateMultiple(trades);
	}
}
