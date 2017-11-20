package com.validator.trade.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.validator.trade.model.Option;
import com.validator.trade.model.result.TradeValidationResult;
import com.validator.trade.model.result.ValidationError;

public class OptionStyleValidatorTest {
	
	private final TradeValidator<Option> validator = new OptionStyleValidator();
	private final Option option = new Option();

	@Test
	public void supportedOptionStyleTest() {
		option.setStyle("American");
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationPassed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(empty()));
	}
	
	@Test
	public void optionStyleNullTest() {
		TradeValidationResult result = validator.validate(option);
		
		assertNotNull(result);
        assertThat(result.validationFailed(), is(true));
        
        Collection<ValidationError> validationErrors = result.getValidationErrors();
        assertThat(validationErrors, is(not(empty())));
        assertThat(validationErrors.size(), is(1));
	}
}
