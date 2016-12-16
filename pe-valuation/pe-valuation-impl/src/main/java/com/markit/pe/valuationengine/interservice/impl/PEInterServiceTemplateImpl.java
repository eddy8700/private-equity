package com.markit.pe.valuationengine.interservice.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.markit.pe.comparabledata.domain.EVBData;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.comparabledata.response.ComparableDataResponse;
import com.markit.pe.portfoliodata.PEPositionInfo;
import com.markit.pe.positiondata.domain.PEClient;
import com.markit.pe.positiondata.response.PositionDataResponse;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.markit.pe.valuationengine.interservice.api.PEInterServiceTemplate;
import com.markit.pe.valuationengine.util.ApacheBeanUtil;


/**
 * @author Aditya Gupta
 *
 */

@Component
public class PEInterServiceTemplateImpl implements PEInterServiceTemplate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PEInterServiceTemplateImpl.class);

	@Value("${position.url}")
	private String positionInfoUrl;
	
	@Value("${latest.comparabledetails.url}")
	private String latestCompDetailsUrl;
	
	@Value("${system.generated.url}")
	private String systemGeneratedUrl;
	
	@Value("${exception.status.url}")
	private String exceptionStatusUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@SuppressWarnings({"unused","unchecked"})
	public List<PEPositionInfo> performInterServiceCallForPositionInfo(final PEClient peClient) {
		Map<String, Object> response = new HashMap<String, Object>();
		PositionDataResponse<Map<String, Object>> apiResponse = new PositionDataResponse<Map<String, Object>>();
		ParameterizedTypeReference<PositionDataResponse<Map<String, Object>>> typeRef = new ParameterizedTypeReference<PositionDataResponse<Map<String, Object>>>() {
		};
		ResponseEntity<PositionDataResponse<Map<String, Object>>> responseEntity = restTemplate
				.exchange(positionInfoUrl, HttpMethod.POST, new HttpEntity<>(peClient), typeRef);
		LOGGER.info("Got the response for the performInterServiceCallForPositionInfo");
		apiResponse=responseEntity.getBody();
		response=apiResponse.getPayload();
		List<LinkedHashMap<String, Object>> content = (List<LinkedHashMap<String, Object>>) response.get("content");
		List<PEPositionInfo> positions = new ArrayList<PEPositionInfo>();
		ApacheBeanUtil.registerConverters();
		for(int i=0;i<content.size();i++){
			PEPositionInfo info= new PEPositionInfo();
			try {
				//BeanUtilsBean.getInstance().getConvertUtils().register(true, true, 0);
				LOGGER.info("Populating the map into position info object");
				BeanUtilsBean.getInstance().populate(info, content.get(i));
				positions.add(info);
		
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		final HttpStatus x=responseEntity.getStatusCode();
		LOGGER.info("Returning the List<PositionInfo> with the size {}",positions.size());
		return positions;
	}
	
	
	@Override
	public List<EVBData> performInterServiceCallForLatestCompsDetails(EvbDataRefreshRequest evbDataRefreshRequest){
		List<EVBData> response = new ArrayList<EVBData>();
		ComparableDataResponse<List<EVBData>> apiResponse = new ComparableDataResponse<List<EVBData>>();
		LOGGER.info("Getting latest comparables market data for Request : "+evbDataRefreshRequest.toString());
		ParameterizedTypeReference<ComparableDataResponse<List<EVBData>>> typeRef = new ParameterizedTypeReference<ComparableDataResponse<List<EVBData>>>(){};
		try {
			ResponseEntity<ComparableDataResponse<List<EVBData>>> responseEntity = restTemplate.exchange(latestCompDetailsUrl, HttpMethod.POST, new HttpEntity<>(evbDataRefreshRequest), typeRef);
			apiResponse = responseEntity.getBody();
			response = apiResponse.getPayload();
		} catch (Exception e){
			LOGGER.error(e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}		
		return response;
	}
	@Override
	public List<EVBData> performInterServiceCallForSystemGeneratedComps(PESecurityInfoDTO peSecurityInfoDTO){
		List<EVBData> response = new ArrayList<EVBData>();
		ComparableDataResponse<List<EVBData>> apiResponse = new ComparableDataResponse<List<EVBData>>();
		LOGGER.info("Getting system generated comparables market data for Request : "+peSecurityInfoDTO.toString());
		ParameterizedTypeReference<ComparableDataResponse<List<EVBData>>> typeRef = new ParameterizedTypeReference<ComparableDataResponse<List<EVBData>>>(){};
		try {
			ResponseEntity<ComparableDataResponse<List<EVBData>>> responseEntity = restTemplate.exchange(systemGeneratedUrl, HttpMethod.POST, new HttpEntity<>(peSecurityInfoDTO), typeRef);
			apiResponse = responseEntity.getBody();
			response = apiResponse.getPayload();
		} catch (Exception e){
			LOGGER.error(e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}		
		return response;
	}


	@Override
	public boolean performInterServiceCallForCheckPortfolioValuationException(final Long portfolioId,
	    final Long portfolioValuationStatusId) {
		LOGGER.info("Performing the interservice call for checkForPortfolioValuationException");
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("portfolioId", portfolioId);
		params.put("portfolioValuationId", portfolioValuationStatusId);
		final ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(exceptionStatusUrl, Boolean.class,
				params);
		boolean isExceptionPresent = responseEntity.getBody();
		return isExceptionPresent;
	}

}
