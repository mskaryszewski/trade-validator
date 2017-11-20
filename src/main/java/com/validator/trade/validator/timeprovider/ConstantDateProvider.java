package com.validator.trade.validator.timeprovider;

import java.time.LocalDate;
import java.time.Month;

/**
 * At the time being only implementation of DateProvider interface.
 * Used to implement the requirement of current_date=09.10.2016
 * 
 * @author Michal
 *
 */
public class ConstantDateProvider implements DateProvider {
	@Override
	public LocalDate getDate() {
		return LocalDate.of(2016, Month.OCTOBER, 9);
	}
}
