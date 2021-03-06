/**
 * 
 */
package com.markit.pe.valuationengine.exception;

/**
 * @author mahesh.agarwal
 *
 */
public class ValuationEngineException extends RuntimeException{
	
	private String customMessage;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7988285916632490225L;

	/**
	 * 
	 */
	public ValuationEngineException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public ValuationEngineException(String message) {
		super(message);
		this.customMessage = message;
	}
	
	/**
	 * @param message
	 */
	public ValuationEngineException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 */
	public ValuationEngineException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override 
	public String toString() 
	{ 
		return super.toString(); 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override 
	public String getMessage() 
	{ 
		return super.getMessage(); 
	}

	/**
	 * @return the customMessage
	 */
	public String getCustomMessage() {
		return customMessage;
	}

}
