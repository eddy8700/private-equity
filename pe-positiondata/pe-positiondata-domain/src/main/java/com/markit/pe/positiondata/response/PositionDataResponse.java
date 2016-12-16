/**
 * 
 */
package com.markit.pe.positiondata.response;

import java.util.List;

/**
 * @author mahesh.agarwal
 *
 */
public class PositionDataResponse<T> {
	
	boolean requestPasssed;
	
	private List<String> message;
	
	private T payload;
	
    private int errorCode;
		
	Class<? extends Exception> exceptionClass;
	
	
	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setExceptionClass(Class<? extends Exception> exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the exceptionClass
	 */
	public Class<? extends Exception> getExceptionClass() {
		return exceptionClass;
	}
	/**
	 * @param status
	 */
	public PositionDataResponse(boolean requestPasssed) {
		this.requestPasssed = requestPasssed;
	}

	public PositionDataResponse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the requestPasssed
	 */
	public boolean isRequestPasssed() {
		return requestPasssed;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}


}
