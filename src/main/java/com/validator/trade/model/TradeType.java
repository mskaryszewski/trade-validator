package com.validator.trade.model;

/**
 * Type of trade. Drawback of keeping this information as Enum instead of a plain String is that
 * introduction of new type of Trade is a bit more complicated - not only application.yml file needs to be configured
 * appropriately, but we also need to register it here.
 * 
 * Solution to store type of trade as Enum supports OOP. Design decision was to use benefit of Enum
 * at the expense of a slight additional effort of registering a new product type by adding it to TradeType Enum.
 * @author Michal
 *
 */
public enum TradeType {
	Spot,
	Forward,
	VanillaOption;
	
	/**
	 * These Strings are required for Trade model because Jackson forces us not to use Enums but Strings
	 * to configure polymorphism.
	 */
	public static final String SPOT           = "Spot";
	public static final String FORWARD        = "Forward";
	public static final String VANILLA_OPTION = "VanillaOption";
}
