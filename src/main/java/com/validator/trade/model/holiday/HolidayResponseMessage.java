package com.validator.trade.model.holiday;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of Response from https://holidayapi.com
 * See {https://holidayapi.com}
 * @author Michal
 *
 */
@Getter
@Setter
@ToString
public class HolidayResponseMessage {
	
	private int status;
	private List<Holiday> holidays;

}
