package com.markit.pe.exception.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.exception.api.ExceptionManagementService;
import com.markit.pe.exception.domain.PEException;
import com.markit.pe.exception.repository.PEExceptionManagementRepository;
import com.markit.pe.exception.request.MarkInactivePEExceptionsRequest;

@Component
public class ExceptionManagementServiceImpl implements ExceptionManagementService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionManagementServiceImpl.class);
	
	
	@Autowired
	private PEExceptionManagementRepository peExceptionManagementRepository;

	@Override
	public List<PEException> findExceptionsByPortfolioName(final String portfolioName) {
		LOGGER.info("find exceptions by portfolio name {}",portfolioName);
		if(portfolioName != null){
			final List<PEException> exceptionManagements=peExceptionManagementRepository.findByPePortfolioPortfolioNameAndIsActiveTrue(portfolioName);
			if(!CollectionUtils.isEmpty(exceptionManagements)){
				return exceptionManagements;
			}
	}
		return Collections.EMPTY_LIST;
	}
	@Override
	public void convertToInactiveExceptions(MarkInactivePEExceptionsRequest exceptionManagementDTO) {
	final List<Long> exceptionIds=exceptionManagementDTO.getExceptionIds();
	LOGGER.info("Converting inactive exceptions to active exceptions for portfolio name {} ",exceptionManagementDTO.getPortfolioName());
	if(!CollectionUtils.isEmpty(exceptionIds)){
		peExceptionManagementRepository.updateIsActiveToFalse(exceptionManagementDTO.getExceptionIds());
	}
	}
	@Override
	public void persistExceptions(PEException exceptionManagement) {
		LOGGER.info("Persisting the exception Management in the db");
		peExceptionManagementRepository.save(exceptionManagement);
		
	}
	@Override
	public boolean checkForPortfolioValuationException(Long portfolioId, Long portfolioValuationId) {
		LOGGER.info("Fetching the status for portfolioId {} and portfolioValuationId {}",portfolioId,portfolioValuationId);
		List<PEException> exceptions=peExceptionManagementRepository.findByPePortfolioIdAndPortfolioValuationId(portfolioId,portfolioValuationId);
		//List<PEException> exceptions=peExceptionManagementRepository.findByPePortfolioId(portfolioId);
		if(CollectionUtils.isEmpty(exceptions)){
			LOGGER.info("There is no exception for the portfolioValuationId {}",portfolioValuationId);
			return Boolean.FALSE;
		}
		else{
			LOGGER.info("There is some  exception for the portfolioValuationId {}",portfolioValuationId);
			return Boolean.TRUE;
		}
	}
	

}
