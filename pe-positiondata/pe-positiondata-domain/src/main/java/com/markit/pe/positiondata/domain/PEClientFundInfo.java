package com.markit.pe.positiondata.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_CLIENT_FUND_INFO")
public class PEClientFundInfo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CLIENT_FUND_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FUND_ID")  
	@Access(AccessType.FIELD)
	private PEFund fund;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CLIENT_ID")  
	@Access(AccessType.FIELD)
	private PEClient client;
	

}
