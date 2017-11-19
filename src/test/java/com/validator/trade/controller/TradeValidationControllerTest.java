package com.validator.trade.controller;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.InputStream;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.validator.trade.TradeValidatorApp;
import com.validator.trade.model.Spot;
import com.validator.trade.model.Trade;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.TradeValidationResults;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradeValidatorApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeValidationControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidationControllerTest.class);	

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private final Trade dummySpotTrade = new Spot();
	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void returnsSuccessWhileValidatingASingleTrade() throws Exception {
		final String SINGLE_TRADE_URL = String.format("http://localhost:%d/trade", this.port);
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(SINGLE_TRADE_URL, dummySpotTrade, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void returnsSuccessWhileValidatingBulkOfTrades() throws Exception {
		final String SINGLE_TRADE_URL = String.format("http://localhost:%d/trades", this.port);
		Collection<Trade> trades = Lists.newArrayList(dummySpotTrade, dummySpotTrade, dummySpotTrade);
		
		ResponseEntity<TradeValidationResults> response = this.testRestTemplate.postForEntity(SINGLE_TRADE_URL, trades, TradeValidationResults.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void validateASingeTradeFromAFile() throws Exception {
		final String SINGLE_TRADE_URL = String.format("http://localhost:%d/trade", this.port);
		
		InputStream is = new ClassPathResource("single-trade.json").getInputStream();
		Trade trade = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(is, Trade.class);
		
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(SINGLE_TRADE_URL, trade, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void validateMultipleTradesFromAFile() throws Exception {
		final String SINGLE_TRADE_URL = String.format("http://localhost:%d/trades", this.port);
		
		InputStream is = new ClassPathResource("multiple-trades.json").getInputStream();
		Collection<Trade> trades = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(is, Collection.class);
		
		ResponseEntity<TradeValidationResults> response = this.testRestTemplate.postForEntity(SINGLE_TRADE_URL, trades, TradeValidationResults.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response.getgetStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	
	
	private void printResultOnConsoleAsObjectAndJson(TradeValidationResult validationResult) throws JsonProcessingException {
		logger.info("Result Object {}", validationResult);
		logger.info("Result as JSON format {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(validationResult));
	}
	
	private void printResultOnConsoleAsObjectAndJson(TradeValidationResults validationResults) throws JsonProcessingException {
		validationResults.forEach(result -> {
			try {
				printResultOnConsoleAsObjectAndJson(result);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}
}
