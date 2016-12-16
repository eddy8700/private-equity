package com.markit.pe.valuationengine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.domain.PEPortfolio;
import com.markit.pe.valuationengine.api.PortfolioValuationDao;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;
import com.markit.pe.valuationengine.repository.PEPortfolioValuationUploadStatusRepository;

@Component
public class PortfolioValuationDaoImpl implements PortfolioValuationDao {

	
	@Autowired
	private PEPortfolioValuationUploadStatusRepository pePortfolioValuationUploadStatusRepository;
	
	
	private static final Logger logger = LoggerFactory.getLogger(PortfolioValuationDaoImpl.class);
	@Override
	public Long persistPortfolioStatus(final PEPortfolio portfolio,final PortfolioValuationStatus portfolioValuationStatus) {
			logger.info("First insert");
			final PortfolioValuationUploadStatus portfolioValuationUploadStatus= new PortfolioValuationUploadStatus();
			portfolioValuationUploadStatus.setPePortfolio(portfolio);
			portfolioValuationUploadStatus.setPortfolioValuationStatus(portfolioValuationStatus);
			logger.info("Inserting portfolioValuationUploadStatus with {} status",portfolioValuationStatus);
		final PortfolioValuationUploadStatus persistedPortfolioValuationUploadStatus= pePortfolioValuationUploadStatusRepository.save(portfolioValuationUploadStatus);
		return persistedPortfolioValuationUploadStatus.getId();
		
	}
	@Override
	public void updatePortfolioStatus(final Long portfolioValUploadStatus, PortfolioValuationStatus portfolioValuationStatus) {
		logger.info("Update call");
		final PortfolioValuationUploadStatus persistedPortfolioValuationUploadStatus=pePortfolioValuationUploadStatusRepository.findOne(portfolioValUploadStatus);
		persistedPortfolioValuationUploadStatus.setPortfolioValuationStatus(portfolioValuationStatus);
		pePortfolioValuationUploadStatusRepository.save(persistedPortfolioValuationUploadStatus);
	}
	

}
