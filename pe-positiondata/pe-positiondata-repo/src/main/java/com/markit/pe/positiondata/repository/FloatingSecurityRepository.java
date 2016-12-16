package com.markit.pe.positiondata.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.markit.pe.portfoliodata.Currency;
import com.markit.pe.positiondata.domain.FloatingSecurity;

@Repository
public interface FloatingSecurityRepository  extends BasePESecurityRepository<FloatingSecurity>{
	
	List<FloatingSecurity> findByFundIdAndIssueDateAndCurrencyAndFrequencyAndMarginAndMaturityDate(Long id,
			Date issueDate, Currency currency, String frequency, BigDecimal margin, Date maturityDate);

	List<FloatingSecurity> findBySecurityNameAndIssueDateAndCurrency(String securityName, Date issueDate,
			Currency currency);

}
