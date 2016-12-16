package com.markit.pe.portfoliodata;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -267940718659130720L;
	
	
	@Column(name = "INSERTED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date insertedAt;
	
	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date updatedAt;

	@Column(name = "UPDATED_BY", updatable = false)
	private String updatedBy;
	
	@Column(name = "INSERTED_BY", updatable = false)
	private String insertedBy;



}
