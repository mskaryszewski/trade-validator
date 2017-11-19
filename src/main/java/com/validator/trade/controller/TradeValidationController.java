package com.validator.trade.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.validator.ValidationService;

@RestController
public class TradeValidationController {

	@Autowired
	private ValidationService tradeValidatorService;
	
	@GetMapping("/trade")
	public TradeValidationResult validateTrade2() {
		return tradeValidatorService.validate(null);
	}

	@PostMapping("/trade")
	public TradeValidationResult validateTrade(@RequestBody Trade trade) {
		return tradeValidatorService.validate(trade);
	}

	@PostMapping("/trades")
	public Collection<TradeValidationResult> validateTrades(@RequestBody Collection<Trade> trades) {
		return tradeValidatorService.validateMultiple(trades);
	}
}
