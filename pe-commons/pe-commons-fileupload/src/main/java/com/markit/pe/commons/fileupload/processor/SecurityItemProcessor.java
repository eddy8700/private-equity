package com.markit.pe.commons.fileupload.processor;

import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.domain.PESecurityDetails;

@Component
public class SecurityItemProcessor implements org.springframework.batch.item.ItemProcessor<PESecurityDetails, PESecurityDetails>  {

	@Override
	public PESecurityDetails process(PESecurityDetails peSecurityDetails) throws Exception {
		return null;
	}

}
