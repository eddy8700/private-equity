package com.markit.pe.positiondata.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PESecurityDetails;


@Repository
public interface PESecurityDetailsRepository extends BasePESecurityRepository<PESecurityDetails> {


	@Query("SELECT MAX(peSec.securityVersion) from PESecurityDetails peSec INNER JOIN peSec.fund fun where peSec.securityId=?2 and fun.id=?1")
	Integer findByFundIdAndSecurityIdAndFetchMaxSecurityVersion(final Long id, final String securityId);

	/*List<PESecurityDetails> findByFundIdAndIssueDateAndCurrencyAndFrequencyAndCouponAndMaturityDateAndType(Long fundId,
			Date issueDate, Currency currency, String couponFrequency, BigDecimal couponRate, Date maturityDate,
			String type);*/
	
	
	List<PESecurityDetails> findByIssueDate(final Date issueDate);

	PESecurityDetails findByFundIdAndSecurityIdAndSecurityVersion(Long fundId, String securityId, Integer securityVersion);

	List<PESecurityDetails> findBySecurityId(String securityId);

	/*List<PESecurityDetails> findByFundIdAndIssueDateAndCurrencyAndFrequencyAndMarginAndMaturityDateAndType(Long id,
			Date issueDate, Currency currency, String frequency, BigDecimal margin, Date maturityDate, String type);*/


}
