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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "T_FI_CALIBRATION_INFO")
public class PECalibrationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860542856800569322L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FI_CALIBRATION_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	

	@Column(name = "CAL_VERSION")
	@Access(AccessType.FIELD)
	private String calVersion;
	
	
	@Column(name = "CHANNEL_ID")
	@Access(AccessType.FIELD)
	private long channelId;
	
	@Column(name = "CAL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date calDate;

	@Column(name = "TRANSACTION_PRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal transactionPrice;

	@Column(name = "DAY1_SPREAD", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal day1Spread;

	@Column(name = "DAY_CONVENTION")
	@Access(AccessType.FIELD)
	private String dayConvention;
	
	
	@Column(name = "IS_ACTIVE")
	@Access(AccessType.FIELD)
	private Integer isActive;
	
	public Long parentChannelId;

	

}
