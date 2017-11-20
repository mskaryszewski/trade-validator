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

/**
 * Client Test Class.
 * Consumes exposed RESTful web services.
 * The purpose of this test class is just a verification 
 * - if connection to REST server can be established
 * - if all required endpoints are correct
 * - if we can send some data and receive expected objects in return
 * - load and send JSON trades definition stored in files
 * - print returned data on console as objects and as JSON files
 * 
 * The main focus is not to validate specific object attribute values but if objects are returned at all.
 * 
 * @author Michal
 *
 */
public class TradeValidationControllerTest2 {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidationControllerTest2.class);	

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	/**
	 * Dummy Trade used in tests
	 */
	private final Trade dummySpotTrade = new Spot();
	
	/**
	 * Jackson InputStream to Object mapper
	 */
	private final ObjectMapper objectMapper  = new ObjectMapper().registerModule(new JavaTimeModule());
	
	/**
	 * URL to validate a single trade
	 */
	private final String SINGLE_TRADE_URL   = "http://localhost:%d/trade";
	
	/**
	 * URL to validate a multiple trades
	 */
	private final String BULK_OF_TRADES_URL = "http://localhost:%d/trades";

	@Test
	public void returnsSuccessWhileValidatingASingleTrade() throws Exception {
		final String URL = String.format(SINGLE_TRADE_URL, this.port);
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(URL, dummySpotTrade, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void returnsSuccessWhileValidatingBulkOfTrades() throws Exception {
		final String URL = String.format(BULK_OF_TRADES_URL, this.port);
		Collection<Trade> trades = Lists.newArrayList(dummySpotTrade, dummySpotTrade, dummySpotTrade);
		
		ResponseEntity<TradeValidationResults> response = this.testRestTemplate.postForEntity(URL, trades, TradeValidationResults.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void validateSpotTradeFromFile() throws Exception {
		final String URL = String.format(SINGLE_TRADE_URL, this.port);
		
		InputStream is = new ClassPathResource("spot-trade.json").getInputStream();
		Trade tradeFromJsonFile = objectMapper.readValue(is, Trade.class);
		
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(URL, tradeFromJsonFile, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void validateForwardTradeFromFile() throws Exception {
		final String URL = String.format(SINGLE_TRADE_URL, this.port);
		
		InputStream is = new ClassPathResource("forward-trade.json").getInputStream();
		Trade tradeFromJsonFile = objectMapper.readValue(is, Trade.class);
		
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(URL, tradeFromJsonFile, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	@Test
	public void validateOptionTradeFromFile() throws Exception {
		final String URL = String.format(SINGLE_TRADE_URL, this.port);
		
		InputStream is = new ClassPathResource("vanillaoption-trade.json").getInputStream();
		Trade tradeFromJsonFile = objectMapper.readValue(is, Trade.class);
		
		ResponseEntity<TradeValidationResult> response = this.testRestTemplate.postForEntity(URL, tradeFromJsonFile, TradeValidationResult.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void validateMultipleTradesFromFile() throws Exception {
		final String URL = String.format(BULK_OF_TRADES_URL, this.port);
		
		InputStream is = new ClassPathResource("multiple-trades.json").getInputStream();
		Collection<Trade> tradesFromJsonFile = objectMapper.readValue(is, Collection.class);
		
		ResponseEntity<TradeValidationResults> response = this.testRestTemplate.postForEntity(URL, tradesFromJsonFile, TradeValidationResults.class);
		printResultOnConsoleAsObjectAndJson(response.getBody());
		then(response).hasNoNullFieldsOrProperties();
	}
	
	private void printResultOnConsoleAsObjectAndJson(TradeValidationResult validationResult) throws JsonProcessingException {
		logger.info("Result as Object: {}", validationResult);
		logger.info("Result as JSON: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validationResult));
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
