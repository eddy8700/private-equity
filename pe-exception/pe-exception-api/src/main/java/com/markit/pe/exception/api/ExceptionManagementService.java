package com.markit.pe.exception.api;

import java.util.List;

import com.markit.pe.exception.domain.PEException;
import com.markit.pe.exception.request.MarkInactivePEExceptionsRequest;

public interface ExceptionManagementService {

	public List<PEException> findExceptionsByPortfolioName(final String portfolioId);

	public void convertToInactiveExceptions(final MarkInactivePEExceptionsRequest exceptionManagementDTO);

	public void persistExceptions(PEException exceptionManagement);

	public boolean  checkForPortfolioValuationException(final Long portfolioId, final Long portfolioValuationId);

}
