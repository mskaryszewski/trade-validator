package com.validator.trade.validator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.validator.trade.TradeValidatorApp;
import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeValidationResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradeValidatorApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpotTradeValidatorTest {

	@Autowired
	private ValidatorService<Trade, TradeValidationResult> tradeValidator;
	
	@Test
	public void returnsSuccessWhileValidatingASingleTrade() {
		tradeValidator.validate(new Trade("SPOT"));
	}
}
