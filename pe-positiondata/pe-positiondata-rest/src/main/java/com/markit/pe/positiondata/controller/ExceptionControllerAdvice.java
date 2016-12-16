/**
 * 
 */
package com.markit.pe.positiondata.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.positiondata.exception.PositionDataException;
import com.markit.pe.positiondata.response.PositionDataResponse;

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
	public PositionDataResponse handleException(Exception ex){
		logger.error("Error occurred", ex.getMessage());
		logger.error(ExceptionUtils.getStackTrace(ex));
		PositionDataResponse positionDataResponse= new PositionDataResponse<>(false);
		List<String> message = new ArrayList<>();
		message.add("Internal Server Error, please contact Administrator");
		positionDataResponse.setMessage(message);
		positionDataResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		positionDataResponse.setExceptionClass(ex.getClass());
		return positionDataResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ExceptionHandler(PositionDataException.class)
	public PositionDataResponse handlePositionDataException(PositionDataException ex){		
		logger.error("Error occurred", ex.getMessage());
		logger.error(ExceptionUtils.getStackTrace(ex));
		PositionDataResponse positionDataResponse= new PositionDataResponse<>(false);
		List<String> message = new ArrayList<>();
		message.add(ex.getCustomMessage());
		positionDataResponse.setMessage(message);
		positionDataResponse.setExceptionClass(ex.getClass());
		return positionDataResponse;
	}

	/*@ExceptionHandler(Exception.class)
	public ResponseEntity<PositionDataExceptionResponse> handleException(Exception ex){
		PositionDataExceptionResponse exEntity = new PositionDataExceptionResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error, please contact Administrator");
		return new ResponseEntity<PositionDataExceptionResponse>(exEntity, HttpStatus.OK);
	}*/
	
	/*@ExceptionHandler(PositionDataException.class)
	public ResponseEntity<PositionDataExceptionResponse> handlePositionDataException(PositionDataException ex){
		PositionDataExceptionResponse exEntity = new PositionDataExceptionResponse(ex.getCustomMessage());
		return new ResponseEntity<PositionDataExceptionResponse>(exEntity, HttpStatus.OK);
	}*/	
	
	/*@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
	@ExceptionHandler(Exception.class)
	public void handleException(){
		
	}*/
}
