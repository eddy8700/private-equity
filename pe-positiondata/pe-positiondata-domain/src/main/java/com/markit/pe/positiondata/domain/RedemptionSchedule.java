package com.markit.pe.positiondata.domain;

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

import com.markit.pe.portfoliodata.util.DateUtility;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="T_REDEMPTION_SCHEDULE")
public class RedemptionSchedule implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;

	@Column(name = "REDEMPTION_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected String redemptionId;
	
	
	@Column(name = "REPAYMENT_START_PERIOD")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date repaymentStartPeriod;
	
	@Column(name = "REPAYMENT_END_PERIOD")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date repaymentEndPeriod;
	
	@Column(name = "REPAYMENT_PERCENT", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal repaymentPercent;
	
	@Column(name = "REPAYMENT_AMOUNT", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal repaymentAmount;
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((repaymentAmount == null) ? 0 : repaymentAmount.hashCode());
		result = prime * result + ((repaymentEndPeriod == null) ? 0 : repaymentEndPeriod.hashCode());
		result = prime * result + ((repaymentPercent == null) ? 0 : repaymentPercent.hashCode());
		result = prime * result + ((repaymentStartPeriod == null) ? 0 : repaymentStartPeriod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedemptionSchedule other = (RedemptionSchedule) obj;
		if (repaymentAmount == null) {
			if (other.repaymentAmount != null)
				return false;
		} else if (!repaymentAmount.stripTrailingZeros().equals(other.repaymentAmount.stripTrailingZeros()))
			return false;
		if (repaymentEndPeriod == null) {
			if (other.repaymentEndPeriod != null)
				return false;
		} else if (repaymentEndPeriod.getTime() != other.repaymentEndPeriod.getTime())
			return false;
		if (repaymentPercent == null) {
			if (other.repaymentPercent != null)
				return false;
		} else if (!repaymentPercent.stripTrailingZeros().equals(other.repaymentPercent.stripTrailingZeros()))
			return false;
		if (repaymentStartPeriod == null) {
			if (other.repaymentStartPeriod != null)
				return false;
		} else if (repaymentStartPeriod.getTime() !=other.repaymentStartPeriod.getTime())
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(" "+DateUtility.buildDateFormat(repaymentStartPeriod)+" To "+DateUtility.buildDateFormat(repaymentEndPeriod));
        if(this.repaymentPercent != null){
        	builder.append(" - "+repaymentPercent.stripTrailingZeros().toPlainString());
        }	
        if(this.repaymentAmount != null){
        	builder.append(" - "+repaymentAmount.stripTrailingZeros().toPlainString());
        }
		return builder.toString();
	}
	
	
	
	
}
