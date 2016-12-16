package com.markit.pe.positiondata.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="T_FI_SEC_DETAILS_AUDIT")
public class PESecurityDetailsAudit implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2238873338760239422L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "AUDIT_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FI_SEC_ID")
	@Access(AccessType.FIELD)
	private PESecurityDetails peSecurityDetails;
	
	@Column(name = "PROPERTY_NAME")
	@Access(AccessType.FIELD)
	private String propertyName;
	
	@Column(name = "OLD_VALUE",length=2000)
	@Access(AccessType.FIELD)
	private String oldValue;
	
	@Column(name = "NEW_VALUE",length=2000)
	@Access(AccessType.FIELD)
	private String newValue;
	

	@Column(name = "CHANGED_BY")
	@Access(AccessType.FIELD)
	private String changedBy;
	
	@Column(name = "UPDATED_AT")
	@Access(AccessType.FIELD)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	/*public PESecurityDetails getPeSecurityDetails() {
		return peSecurityDetails;
	}*/

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PESecurityDetailsAudit [id=" + id + ", peSecurityDetails=" + peSecurityDetails + ", changedBy="
				+ changedBy + ", updatedAt=" + updatedAt + ", propertyName=" + propertyName + ", oldValue=" + oldValue
				+ ", newValue=" + newValue + "]";
	}
	
}
