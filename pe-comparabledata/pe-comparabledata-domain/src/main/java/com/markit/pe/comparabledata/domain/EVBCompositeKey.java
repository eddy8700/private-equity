package com.markit.pe.comparabledata.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EVBCompositeKey implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6645931388367838104L;



	@Column(name = "Date")
	@Temporal(TemporalType.TIMESTAMP)
	@Access(AccessType.FIELD)
	private Date date;
	
	

	@Column(name = "ISIN")
	@Access(AccessType.FIELD)
	private String isin;

	

}
