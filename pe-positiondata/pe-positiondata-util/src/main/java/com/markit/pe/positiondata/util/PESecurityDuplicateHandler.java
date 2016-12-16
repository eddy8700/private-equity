package com.markit.pe.positiondata.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.util.DuplicateDomainHandler;
import com.markit.pe.positiondata.domain.FixedSecurity;
import com.markit.pe.positiondata.domain.FloatingSecurity;
import com.markit.pe.positiondata.domain.PESecurityDetails;
import com.markit.pe.positiondata.repository.FixedSecurityRepository;
import com.markit.pe.positiondata.repository.FloatingSecurityRepository;

@Component("peDuplicateHandler")
public  class PESecurityDuplicateHandler implements DuplicateDomainHandler<PESecurityDetails> {


	@Autowired
	private FixedSecurityRepository fixedSecurityRepository;
	
	@Autowired
	private FloatingSecurityRepository floatingSecurityRepository;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PESecurityDuplicateHandler.class);

	@Override
	public boolean processDuplicateDomain(final PESecurityDetails newDomain) {
		LOGGER.debug("Check for the pe security duplicate data");
		 List<? extends PESecurityDetails> list= new ArrayList<>();
		if(newDomain instanceof FixedSecurity){
			FixedSecurity fixedSecurity=(FixedSecurity) newDomain;
			list=fixedSecurityRepository.findBySecurityNameAndIssueDateAndCurrency(fixedSecurity.getSecurityName(),fixedSecurity.getIssueDate(),fixedSecurity.getCurrency());
			//list=fixedSecurityRepository.findBySecurityNameAndCurrency(fixedSecurity.getSecurityName(), fixedSecurity.getCurrency());
			LOGGER.debug("List size for PESecurity Details is ",list.size());
		}
		if(newDomain instanceof FloatingSecurity){
			FloatingSecurity floatingSecurity=(FloatingSecurity) newDomain;
			list=floatingSecurityRepository.findBySecurityNameAndIssueDateAndCurrency(floatingSecurity.getSecurityName(),floatingSecurity.getIssueDate(),floatingSecurity.getCurrency());
			LOGGER.debug("List size for PESecurity Details is ",list.size());
			
		}
		return list.isEmpty()?false:true;
		
	}

	@Override
	public void checkForDifferenceInObjects(PESecurityDetails oldDomain, PESecurityDetails newDomain) {
		
	}

}
