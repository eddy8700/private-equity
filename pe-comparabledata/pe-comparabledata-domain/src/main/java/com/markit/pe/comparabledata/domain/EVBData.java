package com.markit.pe.comparabledata.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "T_EVB_COMPARABLES")
public class EVBData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EVB_MASTER_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	/*@EmbeddedId
    private EVBCompositeKey evbCompositeKey;*/
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@JsonProperty("evbFileDate")
	@Column(name = "Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name = "Service")
	@Access(AccessType.FIELD)
	private String service;

	@JsonProperty("compSecId")
	@Column(name = "ISIN")
	@Access(AccessType.FIELD)
	private String isin;
	
	@Column(name = "Cusip")
	@Access(AccessType.FIELD)
	private String cusip;

	@Column(name = "Ticker")
	@Access(AccessType.FIELD)
	private String ticker;
	
	@JsonProperty("issuer")
	@Column(name = "[Shortname of Issuer]")
	@Access(AccessType.FIELD)
	private String shortName;

	@JsonProperty("currency")
	@Column(name = "Ccy")
	@Access(AccessType.FIELD)
	private String currency;

	@JsonProperty("classification")
	@Column(name = "Classification")
	@Access(AccessType.FIELD)
	private String classification;
	
	@JsonProperty("region")
	@Column(name = "Region")
	@Access(AccessType.FIELD)
	private String region;

	@Column(name = "Tier")
	@Access(AccessType.FIELD)
	private String tier;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@JsonProperty("maturityDate")
	@Column(name = "Maturity")
	@Access(AccessType.FIELD)
	private Date maturityDate;
	
	/*@Column(name = "[Assumed Maturity]")
	@Access(AccessType.FIELD)
	private Date assumedMaturity;*/

	@JsonProperty("coupon")
	@Column(name = "Coupon", precision = 5, scale = 20)
	@Access(AccessType.FIELD)
	private BigDecimal coupon;

	@Column(name = "Frequency")
	@Access(AccessType.FIELD)
	private String frequency;
	
	@Column(name = "Type")
	@Access(AccessType.FIELD)
	private String type;
	
	@Column(name = "Defaulted")
	@Access(AccessType.FIELD)
	private String defaulted;
	
	@Column(name = "[Liquidity Score]")
	@Access(AccessType.FIELD)
	private Integer liquidityScore;
	
	@Column(name = "[Bid Price]")
	@Access(AccessType.FIELD)
	private BigDecimal bidPrice;
	
	@JsonProperty("midPrice")
	@Column(name = "[Mid Price]")
	@Access(AccessType.FIELD)
	private BigDecimal midPrice;
	
	@Column(name = "[Ask Price]")
	@Access(AccessType.FIELD)
	private BigDecimal askPrice;
	
	@Column(name = "[Bid Ask Price Spread]")
	@Access(AccessType.FIELD)
	private BigDecimal bidAskPriceSpread;
	
	@Column(name = "[Accrued Interest]")
	@Access(AccessType.FIELD)
	private BigDecimal accruedInterest;
	
	@Column(name = "[Dirty Bid Price]")
	@Access(AccessType.FIELD)
	private BigDecimal dirtyBidPrice;
	
	@Column(name = "[Dirty Mid Price]")
	@Access(AccessType.FIELD)
	private BigDecimal dirtyMidPrice;
	
	@Column(name = "[Dirty Ask Price]")
	@Access(AccessType.FIELD)
	private BigDecimal dirtyAskPrice;
	
	@Column(name = "[Bid YTM]")
	@Access(AccessType.FIELD)
	private BigDecimal bidYTM;
	
	@JsonProperty("midYTM")
	@Column(name = "[Mid YTM]")
	@Access(AccessType.FIELD)
	private BigDecimal midYTM;
	
	
	@JsonProperty("midModDuration")
	@Column(name = "[Mid Modified Duration]")
	@Access(AccessType.FIELD)
	private BigDecimal midModDuration;
	
	@Column(name = "[Ask YTM]")
	@Access(AccessType.FIELD)
	private BigDecimal askYTM;
	
	@Column(name = "[Spread vs Benchmark Bid]")
	@Access(AccessType.FIELD)
	private BigDecimal spreadVsBenchmarkBid;
	
	@Column(name = "[Mid ASW Spread]")
	@Access(AccessType.FIELD)
	private BigDecimal midASWSpread;
	
	@Column(name = "[Mid Z Spread]")
	@Access(AccessType.FIELD)
	private BigDecimal midZSpread;
	
	@JsonProperty("sector")
	@Column(name = "Sectorlevel5")
	@Access(AccessType.FIELD)
	private String sectorLevel5;
	
	
	
}
