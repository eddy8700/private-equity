package com.markit.pe.positiondata.domain;

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
import javax.persistence.OneToOne;
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
@Table(name = "T_PORTFOLIO_SEC_INFO")
public class PEPortfolioSecurityInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CHANNEL_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="FI_SEC_ID")
	@Access(AccessType.FIELD)
	private PESecurityDetails peSecurityDetails;
	
	
	@Column(name = "PARENT_CHANNEL_ID")
	@Access(AccessType.FIELD)
	public Long parentChannelId;
	
	@Column(name = "PREVIOUS_CHANNEL_ID")
	@Access(AccessType.FIELD)
	public Long previousChannelId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PORTFOLIO_ID")
	@Access(AccessType.FIELD)
	private PEPortfolio pePortfolio;


	public PEPortfolioSecurityInfo(PESecurityDetails peSecurityDetails, PEPortfolio pePortfolio) {
		super();
		this.peSecurityDetails = peSecurityDetails;
		this.pePortfolio = pePortfolio;
	}




	@Override
	public String toString() {
		return "PEPortfolioSecurityInfo [id=" + id + ", peSecurityDetails=" + peSecurityDetails + ", parentChannelId="
				+ parentChannelId + ", previousChannelId=" + previousChannelId + ", pePortfolio=" + pePortfolio + "]";
	}
	
}
