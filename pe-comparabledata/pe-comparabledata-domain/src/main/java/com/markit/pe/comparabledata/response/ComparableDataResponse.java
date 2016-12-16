/**
 * 
 */
package com.markit.pe.comparabledata.response;

/**
 * @author mahesh.agarwal
 *
 */
public class ComparableDataResponse<T> {
	
	boolean requestPasssed;
	
	T payload;
	
	private int errorCode;
	
	private String message;
	
	Class<? extends Exception> exceptionClass;
	
	/**
	 * 
	 */
	public ComparableDataResponse() {
		// TODO Auto-generated constructor stub
	}	

	/**
	 * @param requestPasssed
	 */
	public ComparableDataResponse(boolean requestPasssed) {
		this.requestPasssed = requestPasssed;
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

	/**
	 * @return the requestPasssed
	 */
	public boolean isRequestPasssed() {
		return requestPasssed;
	}

}
