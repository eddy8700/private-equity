package com.markit.pe.positiondata.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DateUtility;
import com.markit.pe.portfoliodata.util.IDgeneratorUtil;
import com.markit.pe.positiondata.domain.FixedSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.value.objects.SecurityIdDTO;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class SecurityIdGeneratorUtil implements IDgeneratorUtil<SecurityIdDTO> {

	private static String FORMAT = "";

	private String clientName;

	private Integer noOfSecurities;

	private String securityName;

	private String currency;
	
	private String portfolioName;
	
	private String assetClass;

	private Integer couponRate;

	private String maturityDate;


	private synchronized Integer fetchSecurityCountForPortfolio(final PEPortfolio pePortfolio) {
		return pePortfolio.getSecurityCount();
	}
	
	private  String buildStringPattern(final String property){
		if (property.length() >= 5)
			return property.replaceAll("\\s+","").trim().substring(0, 5);
		if (property.length() < 5) {
			return property.replaceAll("\\s+","").trim();
		}
		return property;
		
	}

	@Override
	public String buildPattern(SecurityIdDTO domainObj) {
		final PEPortfolio pePortfolio=domainObj.getPePortfolio();
		final PESecurityDetails peSecurityDetails=domainObj.getPeSecurityDetails();
		this.clientName = buildStringPattern(pePortfolio.getPortfolioName());
		this.portfolioName=buildStringPattern(pePortfolio.getPortfolioName());
		this.noOfSecurities = fetchSecurityCountForPortfolio(pePortfolio);
		if (this.noOfSecurities == null)
			this.noOfSecurities = Integer.valueOf(1);
		this.securityName=buildStringPattern(peSecurityDetails.getSecurityName());
		this.currency = peSecurityDetails.getCurrency().toString().toUpperCase();
		this.assetClass = peSecurityDetails.getType();
		if(peSecurityDetails instanceof FixedSecurity){
			final FixedSecurity fixedSecurity =	(FixedSecurity) peSecurityDetails;
			this.couponRate = fixedSecurity.getCoupon().setScale(2, RoundingMode.DOWN).multiply(new BigDecimal(100))
					.intValue();
		}
		if(peSecurityDetails instanceof FloatingSecurity){
			final FloatingSecurity flaotingSecurity =	(FloatingSecurity) peSecurityDetails;
			this.couponRate = flaotingSecurity.getMargin().setScale(2, RoundingMode.DOWN).multiply(new BigDecimal(100))
					.intValue();//use margin rate as coupon rate
		}
			
		this.maturityDate = DateUtility.buildDateFormat(peSecurityDetails.getMaturityDate());
		FORMAT = String.join("_", this.clientName,this.portfolioName, this.noOfSecurities.toString(), this.securityName, this.currency,
				this.assetClass, this.couponRate.toString(), this.maturityDate.toString());
		return FORMAT;
		// TODO Auto-generated method stub
		
	}


}
