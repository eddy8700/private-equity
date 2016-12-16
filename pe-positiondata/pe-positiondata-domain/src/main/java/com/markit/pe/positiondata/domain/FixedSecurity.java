package com.markit.pe.positiondata.domain;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.markit.pe.portfoliodata.constants.PEConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




/**
 * @author aditya.gupta
 *
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue(PEConstants.SECURITY_TYPE_FIXED)
public class FixedSecurity extends PESecurityDetails{
	
	@Column(name = "COUPON_RATE", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal coupon;


	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return PEConstants.SECURITY_TYPE_FIXED;
	}
	
}
