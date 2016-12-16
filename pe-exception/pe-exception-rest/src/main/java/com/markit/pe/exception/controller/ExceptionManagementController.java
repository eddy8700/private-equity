package com.markit.pe.exception.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.exception.api.ExceptionManagementService;
import com.markit.pe.exception.domain.PEException;
import com.markit.pe.exception.request.MarkInactivePEExceptionsRequest;
import com.markit.pe.exception.response.ExceptionManagementResponse;

@RestController
public class ExceptionManagementController {
	
	
	@Autowired
	private ExceptionManagementService exceptionManagementService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionManagementController.class);
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/fetchPEExceptions/{portfolioName}")
	public ExceptionManagementResponse<List<PEException>> fetchPEExceptions(@PathVariable final String portfolioName) {
		LOGGER.info("GET recieved to fetchPEExceptions() for portfolio name {}",portfolioName);
		final List<PEException> payload=exceptionManagementService.findExceptionsByPortfolioName(portfolioName);
		final ExceptionManagementResponse<List<PEException>> exceptionManagementResponse=new ExceptionManagementResponse<>(true);
		exceptionManagementResponse.setPayload(payload);
		return exceptionManagementResponse;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/markInactivePEExceptions" ,consumes="application/json")
	public ExceptionManagementResponse<String> markInactivePEExceptions(@RequestBody MarkInactivePEExceptionsRequest markInactivePEExceptionsRequest) {
		LOGGER.info("POST recieved to markInactivePEExceptions() with portfolioid {} ",markInactivePEExceptionsRequest.getPortfolioName());
		exceptionManagementService.convertToInactiveExceptions(markInactivePEExceptionsRequest);
		LOGGER.info("Rest request completed");		
		ExceptionManagementResponse<String> response = new ExceptionManagementResponse<String>(true);
		response.setPayload("Process has been completed");
		response.setMessage("Process has been completed");
		return response;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePEException" ,consumes="application/json")
	public ExceptionManagementResponse<String> savePEException(@RequestBody PEException peException) {
		LOGGER.info("POST recieved to savePEException() ",peException);
		exceptionManagementService.persistExceptions(peException);
		ExceptionManagementResponse<String> exceptionManagementResponse= new ExceptionManagementResponse<>(true);
		exceptionManagementResponse.setPayload("Process has been completed");
		exceptionManagementResponse.setMessage("Process has been completed");
		return exceptionManagementResponse;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/fetchPEExceptionStatus/{portfolioId}/{portfolioValuationId}")
	public boolean checkForPortfolioValuationException(@PathVariable final Long portfolioId,@PathVariable final Long portfolioValuationId) {
		LOGGER.info("GET recieved to fetchPEExceptionStatus() for portfolio id {}",portfolioId);
		final boolean isExceptionPresent=exceptionManagementService.checkForPortfolioValuationException(portfolioId,portfolioValuationId);
		return isExceptionPresent;
	}
}
