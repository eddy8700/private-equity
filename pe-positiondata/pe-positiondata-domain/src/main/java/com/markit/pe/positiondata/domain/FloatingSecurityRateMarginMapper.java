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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author aditya.gupta
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="T_FI_SEC_RATE_MARGIN")
public class FloatingSecurityRateMarginMapper {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	@Column(name="FI_SEC_ID",nullable=false)
	@Access(AccessType.FIELD)
	private Long fiSecId;
	
	
	@Column(name="MARGIN_ID",nullable=false)
	@Access(AccessType.FIELD)
	private String marginId;
	
	public FloatingSecurityRateMarginMapper(Long fiSecId2, String marginId2) {
		this.fiSecId=fiSecId2;
		this.marginId=marginId2;
	}

	
}
