/**
 * 
 */
package com.markit.pe.positiondata.exception;

/**
 * @author mahesh.agarwal
 *
 */
public class PositionDataException extends RuntimeException{
	
	private String customMessage;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7988285916632490225L;

	/**
	 * 
	 */
	public PositionDataException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public PositionDataException(String message) {
		super(message);
		this.customMessage = message;
	}
	
	/**
	 * @param message
	 */
	public PositionDataException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 */
	public PositionDataException(String message, Throwable cause) {
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
