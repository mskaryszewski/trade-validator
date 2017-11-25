package com.validator.trade.validator.registry.yaml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.validator.trade.model.Trade;
import com.validator.trade.model.TradeType;
import com.validator.trade.validator.OptionExcerciseStartDateValidator;
import com.validator.trade.validator.OptionStyleValidator;
import com.validator.trade.validator.SpotValueDateValidator;
import com.validator.trade.validator.TradeValidator;
import com.validator.trade.validator.ValueDateNotBeforeTradeDateValidator;
import com.validator.trade.validator.ValueDateOnWorkingDayValidator;
import com.validator.trade.validator.registry.api.TradeValidatorsConfigReader;

public class TradeValidatorsYamlRegistryBuilderTest {
	
	@Mock
	TradeValidatorsConfigReader tradeValidatorsConfigReaderMock;
	
	@InjectMocks
	private final TradeValidatorsYamlRegistryBuilder builder = new TradeValidatorsYamlRegistryBuilder();
	
	
	@Before
    public void setUp() throws Exception {
		Map<String, Collection<String>> configurationParsedByConfigReaderMock = Maps.newHashMap();
		List<String> validatorsForAll           = Lists.newArrayList("ValueDateNotBeforeTradeDateValidator", "ValueDateOnWorkingDayValidator");
		List<String> validatorsForSpot          = Lists.newArrayList("SpotValueDateValidator");
		List<String> validatorsForVanillaOption = Lists.newArrayList("OptionStyleValidator", "OptionExcerciseStartDateValidator");
		configurationParsedByConfigReaderMock.put("ALL", validatorsForAll);
		configurationParsedByConfigReaderMock.put("Spot", validatorsForSpot);
		configurationParsedByConfigReaderMock.put("VanillaOption", validatorsForVanillaOption);
		
        MockitoAnnotations.initMocks(this);
        when(tradeValidatorsConfigReaderMock.getValidatorsForTradeType()).thenReturn(configurationParsedByConfigReaderMock);
        
 		builder.setTradeValidators();
    }
	
	@Test
	public void BuildTradeValidatorsRegistryTest() {
		Map<TradeType, Collection<TradeValidator<Trade>>> tradeValidators = builder.getTradeValidators();
		
		assertNotNull(tradeValidators);
		
		assertNotNull(tradeValidators.get(TradeType.Spot));
		assertThat(tradeValidators.get(TradeType.Spot), is(not(empty())));
		assertThat(tradeValidators.get(TradeType.Spot).size(), is(3));
		assertThat(tradeValidators.get(TradeType.Spot), hasItem(instanceOf(ValueDateNotBeforeTradeDateValidator.class)));
		assertThat(tradeValidators.get(TradeType.Spot), hasItem(instanceOf(ValueDateOnWorkingDayValidator.class)));
		assertThat(tradeValidators.get(TradeType.Spot), hasItem(instanceOf(SpotValueDateValidator.class)));
		
		assertNotNull(tradeValidators.get(TradeType.VanillaOption));
		assertThat(tradeValidators.get(TradeType.VanillaOption), is(not(empty())));
		assertThat(tradeValidators.get(TradeType.VanillaOption).size(), is(4));
		assertThat(tradeValidators.get(TradeType.VanillaOption), hasItem(instanceOf(ValueDateNotBeforeTradeDateValidator.class)));
		assertThat(tradeValidators.get(TradeType.VanillaOption), hasItem(instanceOf(ValueDateOnWorkingDayValidator.class)));
		assertThat(tradeValidators.get(TradeType.VanillaOption), hasItem(instanceOf(OptionStyleValidator.class)));
		assertThat(tradeValidators.get(TradeType.VanillaOption), hasItem(instanceOf(OptionExcerciseStartDateValidator.class)));
	}
}
