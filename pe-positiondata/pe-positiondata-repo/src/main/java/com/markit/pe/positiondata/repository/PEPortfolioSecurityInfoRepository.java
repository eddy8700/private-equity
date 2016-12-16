package com.markit.pe.positiondata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.markit.pe.positiondata.domain.PEPortfolioSecurityInfo;

@Repository
public interface PEPortfolioSecurityInfoRepository extends JpaRepository<PEPortfolioSecurityInfo, Long> {

	/*
	 * @Query(
	 * "Select pePortSecInfo from PEPortfolioSecurityInfo pePortSecInfo INNER JOIN pePortSecInfo.peSecurityDetails peSecDetails "
	 * +
	 * "where  peSecDetails IN (Select pSec from PESecurityDetails pSec INNER JOIN pSec.fund where (Select COUNT(*) from PESecurityDetails pa where "
	 * + " pSec.fund.id=pa.fund.id and pSec.securityId=pa.securityId)>1)")
	 * public List<PEPortfolioSecurityInfo>
	 * findLatestVersionSecurityDetails(final List<Long> channelIds);
	 */

	public List<PEPortfolioSecurityInfo> findByPePortfolioPortfolioName(String clientName);

	@Query("Select psecInfo from  PEPortfolioSecurityInfo psecInfo join fetch psecInfo.peSecurityDetails ps where psecInfo.id IN (Select MAX(pinfo.id) from PEPortfolioSecurityInfo pinfo join pinfo.pePortfolio pfolio  where  pfolio.id=?1 GROUP BY pinfo.parentChannelId)")
	public List<PEPortfolioSecurityInfo> findPortfolioSecurityInfoForLatestVersion(final Long portfolioId);
	
	@Modifying
	@Query("Update PEPortfolioSecurityInfo psecInfo set psecInfo.parentChannelId=?1 where psecInfo.id=?1")
	public Integer  updateParentChannelId(Long channelId);

	public PEPortfolioSecurityInfo findByPeSecurityDetailsFundIdAndPeSecurityDetailsSecurityIdAndPeSecurityDetailsSecurityVersion(
			Long id, String securityId, Integer securityVersion);

	/*@Query("Select psecInfo from  PEPortfolioSecurityInfo psecInfo join fetch psecInfo.peSecurityDetails ps where psecInfo.id IN (Select MAX(pinfo.id) from PEPortfolioSecurityInfo  GROUP BY pinfo.parentChannelId)")
	public PEPortfolioSecurityInfo findByPeSecurityDetailsId(Long id);*/
	


	
}
