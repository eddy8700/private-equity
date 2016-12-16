package com.markit.pe.portfoliodata.validator;

import java.util.List;

public interface Validator<T, VE extends ValidationError<T>> {
	/**
	 * returns null if error is not there
	 * 
	 * @param t
	 * @return
	 */
	List<SingleValidationError<T>> validate(T t);

	String identity();

	List<String> buildErrorMessage(List<SingleValidationError<T>> validationErrorSet);

	T buildForDefaultValues(T t);

}
