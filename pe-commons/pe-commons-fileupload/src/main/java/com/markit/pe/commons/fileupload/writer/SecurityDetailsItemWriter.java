package com.markit.pe.commons.fileupload.writer;

import java.util.List;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.domain.PESecurityDetails;

@Component
public class SecurityDetailsItemWriter extends JdbcBatchItemWriter<PESecurityDetails> {
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void write(final List<? extends PESecurityDetails> items) throws Exception {
	}

}
