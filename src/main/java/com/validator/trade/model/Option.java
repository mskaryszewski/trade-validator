package com.validator.trade.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Option extends Trade {
	
	private String style;
	
	private String strategy;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate deliveryDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	
	private String payCcy;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate excerciseStartDate;
	
	private BigDecimal premium;
	
	private String premiumType;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate premiumDate;
	
	public Option() {
		type = TradeType.VanillaOption;
	}
}
