package com.validator.trade.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPayCcy() {
		return payCcy;
	}

	public void setPayCcy(String payCcy) {
		this.payCcy = payCcy;
	}

	public LocalDate getExcerciseStartDate() {
		return excerciseStartDate;
	}

	public void setExcerciseStartDate(LocalDate excerciseStartDate) {
		this.excerciseStartDate = excerciseStartDate;
	}

	public BigDecimal getPremium() {
		return premium;
	}

	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	public String getPremiumType() {
		return premiumType;
	}

	public void setPremiumType(String premiumType) {
		this.premiumType = premiumType;
	}

	public LocalDate getPremiumDate() {
		return premiumDate;
	}

	public void setPremiumDate(LocalDate premiumDate) {
		this.premiumDate = premiumDate;
	}
}
