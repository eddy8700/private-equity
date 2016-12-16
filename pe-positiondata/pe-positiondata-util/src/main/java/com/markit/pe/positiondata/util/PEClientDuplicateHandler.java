package com.markit.pe.positiondata.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.repository.PEClientRepository;

@Component("clientDuplicateHandler")
public class PEClientDuplicateHandler implements DuplicateDomainHandler<PEClient>{
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PEClientDuplicateHandler.class);
	
	@Autowired
	private PEClientRepository peClientRepository;
	
	
	@Override
	public boolean processDuplicateDomain(final PEClient newPEClient) {
		LOGGER.info("Checking for the duplicatte client on basis for abbrivated name {}",newPEClient.getClientAbbrName());
		final String newAbbrivatedName=newPEClient.getClientAbbrName();
		final List<PEClient> peClientList=peClientRepository.findByClientAbbrName(newAbbrivatedName);
		LOGGER.info("Fetching the list size of {} client on basis of abbrivated name",peClientList.size(),newAbbrivatedName);
		if(CollectionUtils.isEmpty(peClientList)){
			LOGGER.info("No record found in the db for abbrivate name This may be fresh insert");
			return false;
		}
		else{
			LOGGER.info("Duplicate entry exists for the pe client with name {}",newAbbrivatedName);
			return true;
		}
	}

	@Override
	public void checkForDifferenceInObjects(PEClient oldDomain, PEClient newDomain) {
		// TODO Auto-generated method stub
		
	}

}
