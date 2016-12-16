package com.markit.pe.positiondata.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.NoRepositoryBean;

import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.positiondata.domain.PEPortfolioSecurityInfo;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;

@NoRepositoryBean
public interface PEValuationRepository  {

	Map<Long, Map<String, Object>> findLastestCalibrationAndValuationInfo(List<Long> channelIds);

	void buildCalibrationAndValuationMapper(PEPortfolioSecurityInfo portfolioSecurityInfo,
			Map<Long, Map<String, Object>> map, PEPositionInfo positionInfo);
	
//	public List<PEValuationInfo> findByChannelId(Long channelId);

	String getActiveCalVersion(long parentChannelId);

	Map<Long, String> getActiveCalVersions(List<Long> parentChannelIds);

	List<PEPositionInfo> getPortfolioPositions(String portfolioName);

	List<PESecurityInfoDTO> getSecurityDetails(String portfolioName);
	
	List<PEPositionInfo> findLatestSecurityWithoutValuationAndCalibration(String portfolioName);
	/*PEValuationInfo findTopByPeCalibrationInfoIdOrderByValVersionDesc(final Long calibrationId);*/

}

