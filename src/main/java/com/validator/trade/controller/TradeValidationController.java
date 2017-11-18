package com.validator.trade.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeValidationResult;
import com.validator.trade.validator.ValidatorService;

@RestController
public class TradeValidationController {

	@Autowired
	private ValidatorService<Trade, TradeValidationResult> tradeValidatorService;

	@GetMapping("/trade")
	public TradeValidationResult validateTradeGet() {
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
