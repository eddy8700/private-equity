/**
 * 
 */
package com.markit.pe.portfoliodata;

import java.math.BigDecimal;
import java.util.Date;

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
public class PEComparableInfo {
	
	private String valVersion;
	
	private long channelId;
	
	private long parentChannelId;
	
	private String calVersion;
	
	private String compSecId;
	
	private String issuer;
	
	private Date maturityDate;
	
	private Currency currency;
	
	private String rating;
	
	private String region;
	
	private String sector;
	
	private String classification;
	
	private Date evbFileDate;
	
	private BigDecimal midYTM;
	
	private BigDecimal coupon;
	
	private BigDecimal midPrice;
	
	private Integer isNotCalibrated;
	
	private long calibrationId;
	
	private long valuationId;
	
	private Integer sysGen;	
	
	
	

}
