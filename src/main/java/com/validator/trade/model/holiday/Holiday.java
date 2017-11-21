package com.validator.trade.model.holiday;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of Holiday object returned by https://holidayapi.com
 * See {https://holidayapi.com}
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
