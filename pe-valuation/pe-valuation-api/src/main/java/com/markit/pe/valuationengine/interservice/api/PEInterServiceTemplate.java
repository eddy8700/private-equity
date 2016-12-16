package com.markit.pe.valuationengine.interservice.api;

import java.util.List;

import com.markit.pe.comparabledata.domain.EVBData;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
/**
 * @author Aditya Gupta
 *
 */
public interface PEInterServiceTemplate {
	
	public List<PEPositionInfo> performInterServiceCallForPositionInfo(final PEClient peClient);

	public List<EVBData> performInterServiceCallForLatestCompsDetails(EvbDataRefreshRequest evbDataRefreshRequest);

	public List<EVBData> performInterServiceCallForSystemGeneratedComps(PESecurityInfoDTO peSecurityInfoDTO);
	
	public boolean performInterServiceCallForCheckPortfolioValuationException(final Long portfolioId,final Long PortfolioValuationStatusId);

}
