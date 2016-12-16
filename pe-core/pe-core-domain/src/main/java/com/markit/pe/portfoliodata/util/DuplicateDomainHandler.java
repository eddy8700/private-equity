package com.markit.pe.portfoliodata.util;



public interface DuplicateDomainHandler<T> {

	public boolean processDuplicateDomain(T newDomain);
	
	public  void checkForDifferenceInObjects(T oldDomain,T newDomain);

	
}