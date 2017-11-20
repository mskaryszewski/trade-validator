package com.validator.trade.validator.timeprovider;

import java.time.LocalDate;

/**
 * Loosely coupled solution to potentially inject different current_date.
 * Created for future use.
 * @author Michal
 *
 */
public interface DateProvider {
	LocalDate getDate();
}
