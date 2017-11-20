package com.validator.trade.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Main Model Abstract class which is a base class for all types of trades.
 * @author Michal
 *
 */

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
        )
@JsonSubTypes({
        @Type(value = Spot.class,    name = TradeType.SPOT),
        @Type(value = Forward.class, name = TradeType.FORWARD),
        @Type(value = Option.class,  name = TradeType.VANILLA_OPTION)
})
public abstract class Trade {
	
	private String customer;
	
	private String ccyPair;
	
	protected TradeType type;
	
	private String direction;
	
    @JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate tradeDate;
    
	private BigDecimal amount1;
	
	private BigDecimal amount2;
	
	private BigDecimal rate;
	
    @JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate valueDate;
    
	private String legalEntity;
	
	private String trader;
	
	public Trade() {
		
	}

	public Trade(TradeType type) {
		super();
		this.type = type;
	}
}
