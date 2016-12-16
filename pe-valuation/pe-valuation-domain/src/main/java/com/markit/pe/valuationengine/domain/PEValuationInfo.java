package com.markit.pe.valuationengine.domain;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="T_FI_VALUATION_INFO")
public class PEValuationInfo implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6171277284333854719L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FI_VALUATION_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;

	@Column(name = "VAL_VERSION")
	@Access(AccessType.FIELD)
	private String valVersion;
	
	@Column(name = "COMP_TYPE")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private String  compType;
	
	@Column(name = "CASHFLOW_VERSION")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private Integer cashflowVersion;	
	
	@Column(name = "VALUATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date valDate;
	
	@Column(name = "BENCHMARK_YTM", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal  benchmarkYtm;
	
	@Column(name = "DAY_1_SPREAD", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal day1Spread;
	
	@Column(name = "YTM", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal ytm;	
	
	@Column(name = "CALCULATED_DIRTY", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal calculatedDirty;
	
	@Column(name = "ACCRUED_INTEREST", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal accruedInterest;
	
	@Column(name = "CLEAN_VALUE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal cleanValue;	

	@Column(name = "DIRTY_PRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal dirtyPrice;

	@Column(name = "ACCRUED_INTEREST_RATIO", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal accruedInterestRatio;
	
	@Column(name = "CLEAN_PRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal cleanPrice;
	
	/*@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name = "FI_CALIBRATION_ID")
	@Access(AccessType.FIELD)
	private PECalibrationInfo peCalibrationInfo;*/
	
	@Column(name = "FI_CALIBRATION_ID")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private long calibrationId;
	
	@Column(name = "CHANNEL_ID")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private long channelId;
	
	/*@JsonIgnore
	@Transient
	private double startingPrincipalOfNextPaidCF;
	
	@JsonIgnore
	@Transient
	private double startingPrincipalOfNextFutureCF;
	
	@JsonIgnore
	@Transient
	private double sumPvOfAllCFs;*/
	

	
}
