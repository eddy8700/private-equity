package com.markit.pe.valuationengine.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.markit.pe.positiondata.domain.FloatingSecurityMargin;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.domain.RedemptionSchedule;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.domain.PECustomizeCompRequest;
import com.markit.pe.valuationengine.request.PEValuationRequest;

public interface ValuationEngineService {

	void performValuation(PEValuationRequest peValuationRequest);

	Map<String, Object> getCustomizedOrLatestUsedCompInfos(PESecurityInfoDTO peSecurityInfoDTO);

	Map<String, Object> getSystemGeneratedCompInfos(PESecurityInfoDTO peSecurityInfoDTO);

	void customizeComparables(PECustomizeCompRequest customizeCompRequest);

	//void performValuation(PESecurityInfoDTO peSecurityInfoDTO, Date valDate);

	Map<String, Map> getValuationHistory(Long parentChannelId);

	Map<String, Object> getPortfolioValuationStatus(PEPortfolio portfolio);

	Long persistPortfolioValuationStatusIndb(PEPortfolio portfolio);

	void persistPortfolioValuationStatusInCache(PEPortfolio portfolio);

	PEPortfolio fetchPortfolioForClient(PEClient client);

	void performInitialCalibration(PESecurityInfoDTO peSecurityInfoDTO, Long portfolioValId);

	void performNextValuation(PESecurityInfoDTO peSecurityInfoDTO, Date valDate, Long portfolioValId);

	List<RedemptionSchedule> getRedemptionScheduleByFiSecId(final Long fiSecId);

	List<FloatingSecurityMargin> getFloatingSecurityMarginByFiSecId(final Long fiSecId);


	
	

}
