package com.markit.pe.commons.fileupload.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.markit.pe.positiondata.domain.PESecurityDetails;

public class SecurityItemReader extends FlatFileItemReader<PESecurityDetails> {

	@Value("${file.path}")
	private String filePath;

	@Value("${line.to.skip:1}")
	private int linesToSkip;

	@Override
	public void setLineMapper(LineMapper<PESecurityDetails> lineMapper) {
		super.setLineMapper(lineMapper);
	}

	@Override
	public void setLinesToSkip(int linesToSkip) {
		super.setLinesToSkip(linesToSkip);
	}

	@Override
	public void setResource(Resource resource) {
		super.setResource(new ClassPathResource(filePath));
	}

	@Override
	protected PESecurityDetails doRead() throws Exception {

		return null;
	}
	
	
	

}
