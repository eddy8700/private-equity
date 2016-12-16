/**
 * 
 */
package com.markit.pe.positiondata.domain;

import java.io.Serializable;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "T_PORTFOLIO_INFO")
public class PEPortfolio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173264789223328127L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PORTFOLIO_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CLIENT_FUND_ID")
	@Access(AccessType.FIELD)
	private PEClientFundInfo peClientFund;

	public PEClientFundInfo getPeClientFund() {
		return peClientFund;
	}

	public void setPeClientFund(PEClientFundInfo peClientFund) {
		this.peClientFund = peClientFund;
	}

	@Column(name = "PORTFOLIO_NAME")
	@Access(AccessType.FIELD)
	private String portfolioName;

	@Column(name = "SECURITY_COUNT")
	@Access(AccessType.FIELD)
	private Integer securityCount;

	

	public PEPortfolio( String portfolioName) {
		super();
		this.portfolioName = portfolioName;
	}

	
}
