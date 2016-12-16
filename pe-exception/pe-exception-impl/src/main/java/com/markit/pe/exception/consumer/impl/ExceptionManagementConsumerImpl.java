package com.markit.pe.exception.consumer.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.markit.pe.exception.consumer.api.ExceptionManagementConsumer;
import com.markit.pe.exception.domain.PEException;
import com.markit.pe.exception.repository.PEExceptionManagementRepository;



@Component
public class ExceptionManagementConsumerImpl implements ExceptionManagementConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionManagementConsumerImpl.class);
	
	@Autowired
	private PEExceptionManagementRepository peExceptionManagementRepository;
	
	

	@Override
	@ServiceActivator(inputChannel="peExceptionTopic")
	public void consumeExceptionMessage(final Message<PEException> exceptionManagement) {
		final PEException exceptionObj=exceptionManagement.getPayload();
		LOGGER.info("PE Exception received : "+exceptionObj);
		LOGGER.info("Persisting the exception in the db");
		final Long portfolioId=exceptionObj.getPortfolioId();
		final Long portfolioValId=exceptionObj.getPortfolioValuationId();
		final String securityId=exceptionObj.getSecurityId();
		final Long fiSecId=exceptionObj.getFiSecId();
		final String exceptionMsg=exceptionObj.getExceptionMessage();
		final boolean isActive=exceptionObj.isActive();
		final String processName=exceptionObj.getProcessName();
		final Date processStartedAt=exceptionObj.getStartTime();
		peExceptionManagementRepository.savePEException(portfolioId, securityId, fiSecId, exceptionMsg, isActive, processName, processStartedAt,portfolioValId);
		//LOGGER.info("Peristed the exception management with the id {}",peristedExceptionManagement.getId());
	}

}
