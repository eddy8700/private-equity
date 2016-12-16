package com.markit.pe.positiondata.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.markit.pe.portfoliodata.Currency;
import com.markit.pe.positiondata.domain.FixedSecurity;

@Repository
public interface FixedSecurityRepository extends BasePESecurityRepository<FixedSecurity> {

	
	List<FixedSecurity> findByFundIdAndIssueDateAndCurrencyAndFrequencyAndCouponAndMaturityDate(Long fundId,
			Date issueDate, Currency currency, String couponFrequency, BigDecimal couponRate, Date maturityDate);

	List<FixedSecurity> findBySecurityNameAndIssueDateAndCurrency(String securityName, Date issueDate,
			Currency currency);
	
	List<FixedSecurity> findBySecurityNameAndCurrency(String securityName,
			Currency currency);
	
}
