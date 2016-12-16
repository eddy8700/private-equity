package com.markit.pe.commons.fileupload.mapper;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.domain.PESecurityDetails;


@Component
public class SecurityItemRowMapper extends  DefaultLineMapper<PESecurityDetails> {

	
	
	@Override
	public void setLineTokenizer(LineTokenizer tokenizer) {
		super.setLineTokenizer(new SecurityDelimitedLineTokenizer());
	}
		
}
