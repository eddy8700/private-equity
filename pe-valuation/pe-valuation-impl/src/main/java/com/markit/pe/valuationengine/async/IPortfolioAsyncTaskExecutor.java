package com.markit.pe.valuationengine.async;

import java.util.List;

import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.api.ValuationEngineService;
import com.markit.pe.valuationengine.request.PEPortfolioValuationRequest;

public interface IPortfolioAsyncTaskExecutor {

	void performPortfolioValuation(List<PESecurityInfoDTO> peSecurityInfoDTOs,
			PEPortfolioValuationRequest pePortfolioValuationRequest, ValuationEngineService valuationEngineService,PEPortfolio pePortfolio);

}
