package com.markit.pe.positiondata.util;

import java.util.Map;

public interface RetryTemplate {
	
	public Map<String, Object> retryBulkUpload(Long portfolioId, String fileName);

}
