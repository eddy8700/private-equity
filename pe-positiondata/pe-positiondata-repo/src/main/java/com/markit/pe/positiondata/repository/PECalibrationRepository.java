/*package com.markit.pe.positiondata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.portfoliodata.PECalibrationInfo;

@Repository
public interface PECalibrationRepository extends JpaRepository<PECalibrationInfo, Long> {

	
	public PECalibrationInfo findTopByPePortfolioSecurityInfoIdOrderByCalVersionDesc(final Long channelId);
	
	public List<PECalibrationInfo> findByIsActiveAndChannelIdIn(final Integer isActive,final List<Long> channelIds);
}
*/