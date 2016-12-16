/**
 * 
 */
package com.markit.pe.valuationengine.response;

/**
 * @author mahesh.agarwal
 *
 */
public class ValuationEngineResponse<T> {
	
	boolean requestPasssed;
	
	private String message;
	
	T payload;
	
	private int errorCode;
	
	Class<? extends Exception> exceptionClass;
	
	/**
	 * 
	 */
	public ValuationEngineResponse() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param requestPasssed
	 */
	public ValuationEngineResponse(boolean requestPasssed) {
		this.requestPasssed = requestPasssed;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the requestPasssed
	 */
	public boolean isRequestPasssed() {
		return requestPasssed;
	}

	/**
	 * @return the payload
	 */
	public T getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the exceptionClass
	 */
	public Class<? extends Exception> getExceptionClass() {
		return exceptionClass;
	}

	/**
	 * @param exceptionClass the exceptionClass to set
	 */
	public void setExceptionClass(Class<? extends Exception> exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	/**
	 * @param requestPasssed the requestPasssed to set
	 */
	public void setRequestPasssed(boolean requestPasssed) {
		this.requestPasssed = requestPasssed;
	}

}
