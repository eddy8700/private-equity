/**
 * 
 */
package com.markit.pe.positiondata.value.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.markit.pe.portfoliodata.Currency;
import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.RedemptionSchedule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
@ToString
public class PESecurityInfoDTO {
	
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
	
	private String activeCalVersion;	
	
	private BigDecimal ytmTransient;
	
	private List<RedemptionSchedule> redemptionSchedules = new ArrayList<RedemptionSchedule>();
	
	private List<FloatingSecurityMargin> floatingSecurityMargins= new ArrayList<>();
	
	private List<Date> redemptionDateList = new ArrayList<Date>();
	
	private List<Double> redemptionAmountList = new ArrayList<Double>();	
	
	private String couponResetFrequency;
	
	private  String couponBenchmark;
	
	private BigDecimal margin;
	
	private List<String> holidayCodes;
	
	private BigDecimal averageLife;	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	
}
