package com.validator.trade.controller;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

import org.junit.Before;
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
import com.validator.trade.model.TradeType;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.TradeValidationResults;

/**
 * Client Test Class which consumes exposed RESTful web services to validate trades.
 * 
 * The purpose of this test class is just a verification 
 * - if connection to REST server can be established
 * - if all required endpoints are correct
 * - if we can send some data and receive expected objects in return
 * - load and send JSON trades definition stored in files
 * - print returned data on console as objects and as JSON files
 * 
 * The main focus is not to validate specific object attribute values but 
 * verification if we can establish connection and if objects are properly returned.
 * 
 * @author Michal
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradeValidatorApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeValidationControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeValidationControllerTest.class);	

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	/**
	 * Dummy Trade used in tests
	 */
	private Trade dummySpotTrade;
	
	/**
	 * Jackson InputStream to Object mapper
	 */
	private final ObjectMapper objectMapper  = new ObjectMapper().registerModule(new JavaTimeModule());
	
	/**
	 * URL to validate a single trade
	 */
	private final String SINGLE_TRADE_URL   = "http://localhost:%d/trade";
	
	/**
	 * URL to validate multiple trades
	 */
	private final String BULK_OF_TRADES_URL = "http://localhost:%d/trades";

	@Before
	public void init() {
		dummySpotTrade = new Spot();
		dummySpotTrade.setType(TradeType.Spot);
		dummySpotTrade.setValueDate(LocalDate.of(2010, Month.JANUARY, 1));
	}
	
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
		dummySpotTrade.setCcyPair("ASDASD");
		dummySpotTrade.setTrader("TRADER");
		dummySpotTrade.setType(TradeType.Spot);
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
