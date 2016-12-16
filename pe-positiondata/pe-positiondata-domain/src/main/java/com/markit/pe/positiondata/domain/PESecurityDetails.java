package com.markit.pe.positiondata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.markit.pe.portfoliodata.Currency;


/**
 * @author aditya.gupta
 *
 */

@Entity
@Table(name="T_FI_SEC_DETAILS",uniqueConstraints = { @UniqueConstraint(columnNames = { "FUND_ID" }), @UniqueConstraint(columnNames = { "SEC_ID" }),@UniqueConstraint(columnNames = { "SEC_VERSION" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FixedSecurity.class,name="Fixed"),@JsonSubTypes.Type(value = FloatingSecurity.class,name="Floating")})
@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)
public abstract class PESecurityDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FI_SEC_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long fiSecId;
	

	@Column(name = "SEC_NAME")
	@Access(AccessType.FIELD)
	private String securityName;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "FUND_ID")
	@Access(AccessType.FIELD)
	private PEFund fund;
	
	@Column(name = "SEC_ID")
	@Access(AccessType.FIELD)
	private String securityId;
	
	@Column(name = "SEC_VERSION")
	@Access(AccessType.FIELD)
	private Integer securityVersion;

	@Column(name = "ISSUE_DATE")
	@Temporal(TemporalType.DATE)
	@Access(AccessType.FIELD)
	private Date issueDate;
	
	/*@Column(name = "TYPE")
	@Access(AccessType.FIELD)
	private String type;*/

	@Column(name = "CURRENCY")
	@Access(AccessType.FIELD)
	private Currency currency;
	
	@Column(name = "NEXT_PAYMENT_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date nextPaymentDate;

	@Column(name = "PAR_VALUE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal startingPrincipal;
	
	
	@Transient
	private List<PESecurityDetailsAudit> audits=new LinkedList<>(); 

	@Column(name = "COUPON_FREQUENCY")
	@Access(AccessType.FIELD)
	private String frequency;
	


	@Column(name = "MATURITY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date maturityDate;

	@Column(name = "CLASSIFICATION")
	@Access(AccessType.FIELD)
	private String classification;

	/*@Column(name = "TYPE")
	@Access(AccessType.FIELD)
	private String type;*/

	@Column(name = "TRANSACTION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date transactionDate;
	
	@Column(name = "DAY_COUNT_CONV")
	@Access(AccessType.FIELD)
	private String dayCountConvention;
	
	@Column(name = "BUSINESS_DAY_CONV")
	@Access(AccessType.FIELD)
	private String businessDayConvention;
	
	@JsonProperty("sectorLevel5")
	@Column(name = "SECTOR")
	@Access(AccessType.FIELD)
	private String sector;
	
	@Column(name = "UNITPRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal unitPrice;
	
	
	@Column(name = "TRANSACTION_PRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal transactionPrice;

	@Column(name = "YIELD_TO_CALL", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal yieldToCall;

	@Column(name = "FLOOR", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal floor;

	@Column(name = "CAP", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal cap;

	@Column(name = "CALL_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date callDate;	

	@Column(name = "DEBT_TYPE")
	@Access(AccessType.FIELD)
	private String debtType;

	@Column(name = "SEC_DESCRIPTION")
	@Access(AccessType.FIELD)
	private String securityDescription;

	@Column(name = "FIID")
	@Access(AccessType.FIELD)
	private String fiid;

	@Column(name = "SPREAD")
	@Access(AccessType.FIELD)
	private BigDecimal spread;

	@Column(name = "CALL_TYPE")
	@Access(AccessType.FIELD)
	private String callType;

	@Column(name = "RATING")
	@Access(AccessType.FIELD)
	private String rating;

	@Column(name = "CALL_PRICE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal callPrice;
	
	@Column(name = "PRINCIPAL_PAYMENT_TYPE")
	@Access(AccessType.FIELD)
	public String principalPaymentType;
	

	@Transient
	private List<RedemptionSchedule> redemptionSchedules=new ArrayList<>();
	


	public List<RedemptionSchedule> getRedemptionSchedules() {
		return redemptionSchedules;
	}


	public void setRedemptionSchedules(List<RedemptionSchedule> redemptionSchedules) {
		this.redemptionSchedules = redemptionSchedules;
	}




	/**
	 * @return the fiSecId
	 */
	public Long getFiSecId() {
		return fiSecId;
	}


	/**
	 * @param fiSecId the fiSecId to set
	 */
	public void setFiSecId(Long fiSecId) {
		this.fiSecId = fiSecId;
	}


	@JsonIgnore
	public PEFund getFund() {
		return fund;
	}


	public void setFund(PEFund fund) {
		this.fund = fund;
	}


	public String getSecurityId() {
		return securityId;
	}


	public void setSecurityId(String securityId) {
		
		this.securityId = securityId;
	}

	public Integer getSecurityVersion() {
		return securityVersion;
	}

	public void setSecurityVersion(Integer securityVersion) {
		this.securityVersion = securityVersion;
	}

	/**
	 * @return the businessDayConvention
	 */
	public String getBusinessDayConvention() {
		return businessDayConvention;
	}


	/**
	 * @param businessDayConvention the businessDayConvention to set
	 */
	public void setBusinessDayConvention(String businessDayConvention) {
		this.businessDayConvention = businessDayConvention;
	}


	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}



	public String getDayCountConvention() {
		return dayCountConvention;
	}


	public void setDayCountConvention(String dayCountConvention) {
		this.dayCountConvention = dayCountConvention;
	}


	public Currency getCurrency() {
		return currency;
	}


	public void setCurrency(Currency currency) {
		this.currency = currency;
	}


	public Date getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(Date nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public BigDecimal getStartingPrincipal() {
		return startingPrincipal;
	}

	public void setStartingPrincipal(BigDecimal startingPrincipal) {
		this.startingPrincipal = startingPrincipal;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}


	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}




	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	/*public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}*/


	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getYieldToCall() {
		return yieldToCall;
	}

	public void setYieldToCall(BigDecimal yieldToCall) {
		this.yieldToCall = yieldToCall;
	}

	public BigDecimal getFloor() {
		return floor;
	}

	public void setFloor(BigDecimal floor) {
		this.floor = floor;
	}

	public BigDecimal getCap() {
		return cap;
	}

	public void setCap(BigDecimal cap) {
		this.cap = cap;
	}

	public Date getCallDate() {
		return callDate;
	}

	public void setCallDate(Date callDate) {
		this.callDate = callDate;
	}

	
	

	public String getDebtType() {
		return debtType;
	}



	public String getPrincipalPaymentType() {
		return principalPaymentType;
	}


	public void setPrincipalPaymentType(String principalPaymentType) {
		this.principalPaymentType = principalPaymentType;
	}


	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}

	public String getSecurityDescription() {
		return securityDescription;
	}

	public void setSecurityDescription(String securityDescription) {
		this.securityDescription = securityDescription;
	}

	public String getFiid() {
		return fiid;
	}

	public void setFiid(String fiid) {
		this.fiid = fiid;
	}

	public BigDecimal getSpread() {
		return spread;
	}

	public void setSpread(BigDecimal spread) {
		this.spread = spread;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public BigDecimal getCallPrice() {
		return callPrice;
	}

	public void setCallPrice(BigDecimal callPrice) {
		this.callPrice = callPrice;
	}


	public List<PESecurityDetailsAudit> getAudits() {
		return audits;
	}


	public void setAudits(List<PESecurityDetailsAudit> audits) {
		this.audits = audits;
	}


	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public abstract String  getType();


	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}


	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}


	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PESecurityDetails [id=" + fiSecId + ", securityName=" + securityName + ", fund=" + fund + ", securityId="
				+ securityId + ", securityVersion=" + securityVersion + ", issueDate=" + issueDate + ", currency="
				+ currency + ", nextPaymentDate=" + nextPaymentDate + ", startingPrincipal=" + startingPrincipal
				+ ", audits=" + audits + ", frequency=" + frequency + ", maturityDate="
				+ maturityDate + ", classification=" + classification + ", transactionDate="
				+ transactionDate + ", dayCountConvention=" + dayCountConvention + ", sector=" + sector
				+ ", unitPrice=" + unitPrice + ", transactionPrice=" + transactionPrice + ", yieldToCall=" + yieldToCall
				+ ", floor=" + floor + ", cap=" + cap + ", callDate=" + callDate + ", principalPaymentType="
				+ principalPaymentType + ", debtType=" + debtType + ", securityDescription=" + securityDescription
				+ ", fiid=" + fiid + ", spread=" + spread + ", callType=" + callType + ", rating=" + rating
				+ ", callPrice=" + callPrice + "]";
	}
	
}
