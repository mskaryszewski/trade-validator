package com.validator.trade.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import com.validator.trade.model.Trade;
import com.validator.trade.validator.TradeValidator;

public class ValidatorUtils {
	
	/**
	 * Instantiates validator implementation based on a given string.
	 * @param className
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static TradeValidator<Trade> convertStringToValidatorInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (TradeValidator) Class
				.forName(VALIDATOR_IMPLEMENTATION_LOCATION + className)
				.newInstance();
	}
	
	/**
	 * Instantiates validator implementation based on a given string.
	 * Throws RuntimeException instead of Checked Exceptions for the need of use in Streams.
	 * @param className
	 * @return
	 */
	public static TradeValidator<Trade> safeConvertStringToValidatorInstance(String className) {
		try {
			return convertStringToValidatorInstance(className);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Class Name [%s] was not converted to class.", className), e);
		}
	}
	
	/**
	 * Instantiates multiple validator implementations based on a given collection of strings.
	 * RuntimeException is thrown in case of InstantiationException, IllegalAccessException or ClassNotFoundException.
	 * @param classNames
	 * @return
	 */
	public static Collection<TradeValidator<Trade>> safeConvertCollectionOfStringToCollectionsOfValidatorInstances(Collection<String> classNames) {
		return classNames.stream()
				.map(ValidatorUtils::safeConvertStringToValidatorInstance)
				.collect(Collectors.toList());
	}
	
	/**
	 * As a convention all Validator implementations must be placed inside com.validator.trade.validator package.
	 */
	private final static String VALIDATOR_IMPLEMENTATION_LOCATION = "com.validator.trade.validator.";
}
