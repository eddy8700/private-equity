/**
 * 
 */
package com.markit.pe.comparabledata.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
public class EVBCompSearchCriteria {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	//@JsonProperty("date")
	private Date date;
	
	//@JsonProperty("ISIN")
	private String isin;
	
	//@JsonProperty("SHORTNAME")
	private String shortName;
	
	//@JsonProperty("TIER")
	private String tier;
	
	//@JsonProperty("CCY")
	private String currency;
	
	//@JsonProperty("CLASSIFICATION")
	private String classification;
	
	//@JsonProperty("COUPON")
	private BigDecimal coupon;
	
	//@JsonProperty("REGION")
	private String region;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	//@JsonProperty("MATURITYDATE")
	private Date maturityDate;
	
	//@JsonProperty("FREQUENCY")
	private String frequency;
	
	//@JsonProperty("TYPE")
	private String type;
	
	//@JsonProperty("SECTORLEVEL5")
	private String sectorLevel5;
	
	//@JsonProperty("sortByField")
	private String sortByField;
	
	//@JsonProperty("sortByOrder")
	private String sortByOrder;	
	
	private Integer pageSize;	
	
	private Integer pageNum;
	
	private Integer maturityMonthRange;
	
	private BigDecimal minYTM;
	
	private BigDecimal maxYTM;
	
	private BigDecimal minModDuration;
	
	private BigDecimal maxModDuration;
	


}
