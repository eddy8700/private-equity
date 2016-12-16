package com.markit.pe.valuationengine.api;

import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;

public interface PortfolioValuationDao {

	public Long persistPortfolioStatus(final PEPortfolio portfolio,final PortfolioValuationStatus portfolioValuationStatus);

	public void updatePortfolioStatus(Long portfolioValuationStatusId, PortfolioValuationStatus doneWithExceptions);
	
}
