package com.markit.pe.positiondata.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * @author aditya.gupta
 *
 */
@Entity
@Getter
@Setter
@Table(name="T_FI_SEC_REDEMPTION")
public class SecurityRedemption {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	@Column(name="FI_SEC_ID",nullable=false)
	@Access(AccessType.FIELD)
	private Long fiSecId;
	
	
	@Column(name="REDEMPTION_ID",nullable=false)
	@Access(AccessType.FIELD)
	private String redemptionId;
	


	public SecurityRedemption(Long fiSecId, String redemptionId) {
		super();
		this.fiSecId = fiSecId;
		this.redemptionId = redemptionId;
	}



	
	
}
