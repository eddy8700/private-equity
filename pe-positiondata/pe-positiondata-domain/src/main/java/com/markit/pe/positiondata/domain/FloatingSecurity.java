package com.markit.pe.positiondata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.markit.pe.portfoliodata.constants.PEConstants;

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
@DiscriminatorValue(PEConstants.SECURITY_TYPE_FLOAT)
public class FloatingSecurity extends PESecurityDetails {
	
	@Column(name = "MARGIN", precision = 18, scale = 36)
	@Access(AccessType.FIELD)
	private BigDecimal margin;
	
	@Column(name = "COUPON_BENCHMARK")
	@Access(AccessType.FIELD)
	public String couponBenchmark;
	
	
	@Column(name = "COUPON_RESET_FREQUENCY")
	@Access(AccessType.FIELD)
	private String couponResetFrequency;
	
	@Transient
	private List<FloatingSecurityMargin> resetMargins=new ArrayList<>();
	

	@Override
	public String getType() {
		return PEConstants.SECURITY_TYPE_FLOAT;
	}
	
}
