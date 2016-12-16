/**
 * 
 */
package com.markit.pe.portfoliodata;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PEPositionInfo {
	
	private long fiSecId;
	
	private long portfolioId;
	
	private long channelId;
	
	private long parentChannelId;
	
	private String securityName;
	
	private String securityId;
	
	private Integer securityVersion;
	
	private Date issueDate;
	
	private Currency currency;
	
	private Date nextPaymentDate;
	
	private BigDecimal startingPrincipal;
	
	private String frequency;
	
	private BigDecimal coupon;
	
	private Date maturityDate;
	
	private String classification;
	
	private String type;
	
	private String dayCountConvention;
	
	private String businessDayConvention;
	
	@JsonProperty("sectorLevel5")
	private String sector;	
	
	private BigDecimal unitPrice;
	
	private String securityDescription;
	
	private String fiid;
	
	private String rating;
	
	private String debtType;
	
	private BigDecimal spread;
	
	private BigDecimal callPrice;
	
	private BigDecimal yieldToCall;
	
	private Date callDate;
	
	private String callType;
	
	private Date transactionDate;
	
	private BigDecimal transactionPrice;
	
	private BigDecimal cap;
	
	private BigDecimal floor;
	
	private String principalPaymentType;
	
	private BigDecimal margin;
	
	
	//Valuation specific ones
	
	private BigDecimal ytmTransient;

	private Date calDate;
	
	private Date valDate;
	
	private String calVersion;
	
	private BigDecimal  day1Spread;
	
	private BigDecimal  accruedInterestRatio;
	
	private BigDecimal accruedInterest;
	
	private BigDecimal  cleanValue;
	
	private BigDecimal  cleanPrice;
	
	private BigDecimal calculatedDirty;
	
	private BigDecimal dirtyPrice;
	
	private BigDecimal ytm;
	
	private String compType;
	
	private BigDecimal benchmarkYtm;
	
	private String valVersion;
	
    private String couponResetFrequency;
	
	private  String couponBenchmark;
	
	public void setSectorLevel5(String sector) {
		//this setter is only for BeanUtilBean.populate method do not use it for other purposes //TODO
		//use setSector instead
		this.sector = sector;
	}

}
