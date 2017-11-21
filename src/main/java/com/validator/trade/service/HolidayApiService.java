package com.validator.trade.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.validator.trade.model.holiday.HolidayResponseMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * REST API consumer which sends request to holidayapi.com
 * to retrieve information about holidays for a given day for a given location.
 * 
 * See {https://holidayapi.com}
 * 
 * This class at the time being is not guarded with proper status codes and exception handling.
 * @author Michal
 *
 */
@Service
@Getter
@Setter
public class HolidayApiService {
	
	/**
	 * Object which enables client-side HTTP access.
	 */
	@Autowired
    private RestTemplate restTemplate;
	
    /**
     * This key was generated for test purpose
     * holidayapi.com limits the number of REST calls to 500 calls per month and historical data only
     */
    private static final String KEY = "2574dc5b-e0f6-462f-ba19-f6899aa01341";
	
    /**
     * 
     * @param date for which we want to get the information if it is a holiday
     * @return
     */
	public boolean isHoliday(LocalDate date) {
		String url = generateURL(date);
		HolidayResponseMessage response = restTemplate.getForObject(url, HolidayResponseMessage.class);
		return !response.getHolidays().isEmpty();
	}
	
	  /**
     * Simplified logic, always return results for Polish calendar.
     */
	private String generateURL(LocalDate date) {
		return String.format("https://holidayapi.com/v1/holidays?key=%s&country=PL&year=%s&month=%s&day=%s",
				KEY,
				date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
	}
}
