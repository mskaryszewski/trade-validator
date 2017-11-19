package com.validator.trade.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import com.validator.trade.validator.Validator;

public class ValidatorUtils {
	
	/**
	 * Instantiates validator implementation based on a given string.
	 * @param className
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Validator convertStringToValidatorInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return (Validator) Class
				.forName(VALIDATOR_IMPLEMENTATION_LOCATION + className)
				.newInstance();
	}
	
	/**
	 * Instantiates validator implementation based on a given string.
	 * Throws RuntimeException instead of Checked Exceptions for the need of use in Streams.
	 * @param className
	 * @return
	 */
	public static Validator safeConvertStringToValidatorInstance(String className) {
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
	public static Collection<Validator> safeConvertCollectionOfStringToCollectionsOfValidatorInstances(Collection<String> classNames) {
		return classNames.stream()
				.map(ValidatorUtils::safeConvertStringToValidatorInstance)
				.collect(Collectors.toList());
	}
	
	/**
	 * As a convention all Validator implementations must be packaged inside com.validator.trade.validator. 
	 */
	private final static String VALIDATOR_IMPLEMENTATION_LOCATION = "com.validator.trade.validator.";
}
