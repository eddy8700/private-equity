package com.markit.pe.portfoliodata.validator;

public interface ValidationError<VE> {
	
	public void setErrorCode(final String code);
	
	public void setErrorDescription(final String errorDesc);
	

}
