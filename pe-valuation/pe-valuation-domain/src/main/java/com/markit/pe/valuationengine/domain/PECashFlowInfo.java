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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name="T_FI_CASHFLOW")
public class PECashFlowInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2827130688878129857L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FI_CASHFLOW_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	@Column(name = "CASHFLOW_VERSION")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private String cashFlowVersion;
	
	@Column(name = "COMP_TYPE")
	@Access(AccessType.FIELD)
	@JsonIgnore
	private String compType;
	
	@Column(name = "ACCRUAL_START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date accrualStartDate;
	
	@Column(name = "VAL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	@JsonIgnore
	private Date valDate;	
	
	@Column(name = "PAYMENT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date paymentDate;
	
	@Column(name = "STARTING_PRINCIPAL", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal startingPrincipal;
	
	@Column(name = "[PERIODIC INTEREST]", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal periodicInterest;
	
	@Column(name = "INTEREST_DUE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal interestDue;
	
	@Column(name = "PRINCIPAL_PAYMENT", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal principalPayment;
	
	@Column(name = "ACCRUED_INTEREST", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal accruedInterest = new BigDecimal(0);
	
	@Column(name = "DAYS_UNTIL_CF", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private Integer daysUntilCf;
	
	@Column(name = "YEARS", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal years;
	
	@Column(name = "DISCOUNT_FACTOR", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal discountFactor;
	
	@Column(name = "PV_OF_CF", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal presentValueOfCf;
	
	
//	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
//	@JoinColumn(name = "FI_VALUATION_ID")
//	@Access(AccessType.FIELD)
//	private PEValuationInfo peValuationInfo;

	@JsonIgnore
	private long valuationId;
	
	@JsonIgnore
	@Transient
	private BigDecimal endingPrincipal;
	
	
}
