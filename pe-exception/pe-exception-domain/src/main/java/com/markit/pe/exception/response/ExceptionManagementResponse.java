/**
 * 
 */
package com.markit.pe.exception.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mahesh.agarwal
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ExceptionManagementResponse<T> {
	
	boolean requestPasssed;
	
	private String message;
	
	private T payload;
	
    private int errorCode;
		
	Class<? extends Exception> exceptionClass;
	
	
	public ExceptionManagementResponse(boolean requestPasssed) {
		this.requestPasssed = requestPasssed;
	}

	

}
