package com.markit.pe.valuationengine.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.markit.pe.positiondata.domain.PEPortfolio;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table( name="T_PORTFOLIO_VALUATION_UPLOAD_STATUS")
public class PortfolioValuationUploadStatus {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UPLOAD_ID", nullable = false)
	@Access(AccessType.FIELD)
	protected Long id;
	
	
	public enum PortfolioValuationStatus{
		 STARTED("Started"),IN_PROGRESS("In Progress"),DONE("Done"),DONE_WITH_EXCEPTIONS("Done with exceptions");
		  private String displayName;
		  
		  PortfolioValuationStatus(String displayName) {
		        this.displayName = displayName;
		    }
		  
		  
		  public String displayName() { return displayName; }
		 public static PortfolioValuationStatus convert(String str) {
		        for (PortfolioValuationStatus loadStatus : PortfolioValuationStatus.values()) {
		            if (loadStatus.displayName().equals(str)) {
		                return loadStatus;
		            }
		        }
		        return null;
		    }
	}
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PORTFOLIO_ID")
	@Access(AccessType.FIELD)
	private PEPortfolio pePortfolio;
	
	
	@Column(name="LOAD_STATUS")
	@Access(AccessType.FIELD)
	private PortfolioValuationStatus portfolioValuationStatus;


	
	
	
}
