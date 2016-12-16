package com.markit.pe.positiondata.retry.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.markit.pe.positiondata.api.PositionDataService;
import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;
import com.markit.pe.positiondata.util.RetryTemplate;

@Component
public class SecurityUploadRetryTemplate implements RetryTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUploadRetryTemplate.class);

	@Autowired
	private PositionDataService positionDataService;

	@Value("${security.bulk.upload.healthcheck.retry.count}")
	private int noOfRetries;

	@Value("${security.bulk.upload.healthcheck.retry.interval}")
	private long waitInterval;

	@Override
	public Map<String, Object> retryBulkUpload(Long portfolioId, final String fileName) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < noOfRetries; i++) {
			LOGGER.info("Retrying upload for the {} time", i);
			try {
				LOGGER.info("Waiting for {} seconds", waitInterval / 1000);
				Thread.sleep(waitInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			map = positionDataService.checkForLatestFileStatus(portfolioId, fileName);
			final SecurityLoadStatus securityLoadStatus = (SecurityLoadStatus) map.get("uploadStatus");
			LOGGER.info("Found the load status {} for the file {}", securityLoadStatus, fileName);
			if (securityLoadStatus.equals(SecurityLoadStatus.NEW)
					|| securityLoadStatus.equals(SecurityLoadStatus.IN_PROGRESS)) {
				continue;
			}
			if (securityLoadStatus.equals(SecurityLoadStatus.LOAD_COMPLETED)
					|| securityLoadStatus.equals(SecurityLoadStatus.LOAD_FAILED)
					|| securityLoadStatus.equals(SecurityLoadStatus.LOADED_WITH_EXCEPTIONS)) {
				break;
			}
		}
		return map;

	}
}
