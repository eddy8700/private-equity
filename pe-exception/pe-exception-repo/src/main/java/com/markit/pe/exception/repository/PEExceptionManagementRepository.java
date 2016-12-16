package com.markit.pe.exception.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.exception.domain.PEException;

@Repository
@Transactional
public interface PEExceptionManagementRepository extends JpaRepository<PEException, Long> {
	

	public List<PEException> findByIdInAndPePortfolioPortfolioName(List<Long> exceptionIds, String portfolioName);
	
	@Modifying
	@Query("Update PEException em set em.isActive=false where em.id IN ?1")
	public void updateIsActiveToFalse(List<Long> exceptionIds);

	public List<PEException> findByPePortfolioPortfolioNameAndIsActiveTrue(String portfolioName);

	@Modifying
	@Query(nativeQuery=true,value="INSERT INTO T_PE_EXCEPTION(PORTFOLIO_ID,SEC_ID,FI_SEC_ID,EXCEPTION_MSG,IS_ACTIVE,PROCESS_NAME,PROCESS_STARTED_AT,PORTFOLIO_VALUATION_ID) VALUES"
			+ " (?1,?2,?3,?4,?5,?6,?7,?8)") //deliberate call to insert pportfolio id
	public void savePEException(final Long portfolioId,final String securityId,final Long fiSecId,final String exceptionMsg,
			final boolean isActive,final String processName,final Date processStartedAt,final Long portfolioValId);

	public List<PEException> findByPePortfolioIdAndPortfolioValuationId(Long portfolioId,
			Long portfolioValuationId);
	
	public List<PEException> findByPePortfolioId(Long portfolioId);

}
