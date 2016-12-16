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
/**
 * @author aditya.gupta
 *
 */
@Entity
@Getter
@Setter
@Table(name="T_FLOATING_MARGIN")
public class FloatingSecurityMargin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2503844005797808634L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;

	@Column(name = "MARGIN_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected String marginId;
	
	@Column(name = "RESET_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date resetDate;
	
	@Column(name = "NEW_MARGIN", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal newMargin;

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((newMargin == null) ? 0 : newMargin.hashCode());
		result = prime * result + ((resetDate == null) ? 0 : resetDate.hashCode());
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
		FloatingSecurityMargin other = (FloatingSecurityMargin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (newMargin == null) {
			if (other.newMargin != null)
				return false;
		} else if (!newMargin.equals(other.newMargin))
			return false;
		if (resetDate == null) {
			if (other.resetDate != null)
				return false;
		} else if (!resetDate.equals(other.resetDate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(" "+DateUtility.buildDateFormat(resetDate));
        if(this.newMargin != null){
        	builder.append(" - "+newMargin.stripTrailingZeros().toPlainString());
        }	
		return builder.toString();
	}
	
	
}
