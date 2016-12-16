/**
 * 
 */
package com.markit.pe.valuationengine.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.valuationengine.response.ValuationEngineResponse;

/**
 * @author mahesh.agarwal
 *
 */
@RestController
@ControllerAdvice
public class ExceptionControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(Exception.class)
	public ValuationEngineResponse handleException(Exception ex){
		logger.error(ExceptionUtils.getStackTrace(ex));
		ValuationEngineResponse exEntity = new ValuationEngineResponse(false);
		exEntity.setMessage("Internal Server Error, please contact Administrator");
		exEntity.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		exEntity.setExceptionClass(ex.getClass());
		return exEntity;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(ValuationEngineException.class)
	public ValuationEngineResponse handlePositionDataException(ValuationEngineException ex){
		logger.error("Error occurred", ex.getMessage());
		logger.error(ExceptionUtils.getStackTrace(ex));		
		ValuationEngineResponse exEntity = new ValuationEngineResponse(false);
		exEntity.setMessage(ex.getCustomMessage());
		exEntity.setExceptionClass(ex.getClass());
		return exEntity;
	}
}
