package com.markit.pe.valuationengine.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.cache.CacheClient;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus;
import com.markit.pe.valuationengine.domain.PortfolioValuationUploadStatus.PortfolioValuationStatus;
import com.markit.pe.valuationengine.repository.PEPortfolioValuationUploadStatusRepository;

@Component
public class CacheManagerImpl implements CacheManager{
	
	private static final Logger logger = LoggerFactory.getLogger(CacheManagerImpl.class);
	
	@Autowired
	private PEPortfolioValuationUploadStatusRepository pePortfolioValuationUploadStatusRepository; 
	
	@Autowired
	private CacheClient<String, Object> inMemoryCacheClient;

	@Override
	@PostConstruct
	public void initializeCacheDuringBootup() {
		logger.info("Initializing cache during boot up time");
		final List<PortfolioValuationUploadStatus> portfolioValuationUploadStatus=pePortfolioValuationUploadStatusRepository.findByPortfolioValuationStatus(PortfolioValuationStatus.IN_PROGRESS);
	    logger.info("Initializing the cahce with the portfolio id ");
	    if(portfolioValuationUploadStatus.size()!=0 && !portfolioValuationUploadStatus.isEmpty()){
	    	logger.info("Size of the portfolioValuationUploadStatus is {}",portfolioValuationUploadStatus.size());
	    	for (PortfolioValuationUploadStatus portfolioValuationUploadStatus2 : portfolioValuationUploadStatus) {
				final String portfolioId=portfolioValuationUploadStatus2.getPePortfolio().getId().toString();
				inMemoryCacheClient.put(portfolioId);
			}
	    }
	}

}
