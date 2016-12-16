/*package com.markit.pe.portfoliodata.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ValidationErrorSet<T> implements ValidationError<T> {

	private Collection<SingleValidationError<T>> errors = new ArrayList<SingleValidationError<T>>(
			0);

	public ValidationErrorSet() {
	}


	@SuppressWarnings("unchecked")
	public void add(final SingleValidationError<T>... validationError) {
		if (this.errors != null) {
			this.errors.addAll(Arrays.asList(validationError));
		}
	}

	public Set<String> errorCodes() {
		final Set<String> errorCodes = new HashSet<String>();
		if (this.errors != null) {
			for (final SingleValidationError<T> error : this.errors) {
				if (error.getErrorCode() != null) {
					errorCodes.add(error.getErrorCode());
				}
			}
		}
		return errorCodes;
	}

	public Collection<SingleValidationError<T>> getErrors() {
		return this.errors;
	}

	public boolean hasAnyError() {
		return !this.errorCodes().isEmpty();
	}

	public void setErrors(final Collection<SingleValidationError<T>> errors) {
		this.errors = errors;
	}


	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (SingleValidationError<T> singleValidationError : this.errors) {
			sb.append("[error code : " + singleValidationError.getErrorCode()
					+ ", message : " + singleValidationError.getErrorMsg()
					+ "]");
		}
		return sb.toString();
	}


	@Override
	public void setErrorCode(String code) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setErrorDescription(String errorDesc) {
		// TODO Auto-generated method stub
		
	}
}
*/