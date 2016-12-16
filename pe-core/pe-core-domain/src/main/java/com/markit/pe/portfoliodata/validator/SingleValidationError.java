package com.markit.pe.portfoliodata.validator;

import org.springframework.stereotype.Component;

@Component
public class SingleValidationError<T> implements ValidationError<T> {

	private String errorCode;

	private  String errorMsg;

	public SingleValidationError() {
		this(null, null, null);
	}

	public SingleValidationError(final String errorCode, final String errorMsg,
			final T type) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}



	public String toString() {
		return "[error code : " + getErrorCode() + ", message : "
				+ getErrorMsg() + "]";
	}

	@Override
	public void setErrorCode(String code) {
		this.errorCode=code;
	}

	@Override
	public void setErrorDescription(String errorDesc) {
     this.errorMsg=errorDesc;
	}

}