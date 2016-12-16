package com.markit.pe.exception.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.markit.pe.portfoliodata.AuditInfo;
import com.markit.pe.portfoliodata.converter.BooleanToIntegerConverter;
import com.markit.pe.positiondata.domain.PEPortfolio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name="T_PE_EXCEPTION")
public class PEException implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 106049037486149000L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "T_PE_EXCEPTION_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	@Column(name="PROCESS_NAME",nullable=false)
	@Access(AccessType.FIELD)
	private String processName;
	
	@Embedded
	@Access(AccessType.FIELD)
	private AuditInfo auditInfo;
	
	
	@Column(name="SEC_ID",nullable=true)
	@Access(AccessType.FIELD)
	private String securityId;

	@Transient
	private Long portfolioId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PORTFOLIO_ID")
	@Access(AccessType.FIELD)
	private PEPortfolio pePortfolio;
	
	@Column(name="FI_SEC_ID",nullable=true)
	@Access(AccessType.FIELD)
	private Long fiSecId;
	
	@Column(name="EXCEPTION_MSG",nullable=false)
	@Access(AccessType.FIELD)
	private String exceptionMessage;
	
	@Column(name="IS_ACTIVE",nullable=false)
	@Convert(converter=BooleanToIntegerConverter.class)
	@Access(AccessType.FIELD)
	private boolean isActive;
	
	@Column(name="FILE_NAME",nullable=true)
	@Access(AccessType.FIELD)
	private String fileName;
	
	
	@Column(name = "PROCESS_STARTED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date startTime;
	
	
	@Column(name = "PORTFOLIO_VALUATION_ID")
	@Access(AccessType.FIELD)
	private Long portfolioValuationId;
	
}
