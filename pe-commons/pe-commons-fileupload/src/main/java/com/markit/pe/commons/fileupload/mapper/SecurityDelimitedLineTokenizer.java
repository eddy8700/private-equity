package com.markit.pe.commons.fileupload.mapper;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityDelimitedLineTokenizer extends DelimitedLineTokenizer {
	
	
	@Value("${delimiter.type:}")
	private String delimiterType;

}
