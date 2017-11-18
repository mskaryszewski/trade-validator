package com.validator.trade.model;

public class Trade {
	
	private String tradeType;

	public String getTradeType() {
		return tradeType;
	}
	
	public Trade() {
		
	}

	public Trade(String tradeType) {
		super();
		this.tradeType = tradeType;
	}

	@Override
	public String toString() {
		return "Trade [tradeType=" + tradeType + "]";
	}
}
