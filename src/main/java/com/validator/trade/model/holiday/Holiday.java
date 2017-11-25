package com.validator.trade.model.holiday;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of Holiday object returned by https://holidayapi.com
 * See {https://holidayapi.com}
 * 
 * Actually this class can define no  as our only concern is to check if a holiday for a given day exists or not.
 * In the end class members are not used and only decrease code coverage.
 * But for the sake of completeness and for potential future use, let's keep these instance variables. 
 * 
 * @author Michal
 *
 */
@ToString
@Getter
@Setter
public class Holiday {
	
	private String name;
	private LocalDate date;
	private LocalDate observed;
	
	@JsonProperty("public")
	private boolean isPublic;

}
