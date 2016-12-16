package com.markit.pe.exception.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkInactivePEExceptionsRequest {
	
	private List<Long> exceptionIds;
	
	private String portfolioName;

	
	

}
