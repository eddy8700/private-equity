/**
 * 
 */
package com.markit.pe.comparabledata.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.comparabledata.api.ComparableDataService;
import com.markit.pe.comparabledata.domain.EVBCompSearchCriteria;
import com.markit.pe.comparabledata.domain.EVBData;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.comparabledata.response.ComparableDataResponse;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;

/**
 * @author mahesh.agarwal
 *
 */
@RestController
@RefreshScope
public class ComparableDataController {
	
	private static final Logger logger = LoggerFactory.getLogger(ComparableDataController.class);
	
	@Autowired
	private ComparableDataService comparableDataService;
	
	@Value("${message}")
	private String message;
			
	@RequestMapping("/landing")
	//@RequestMapping(value="/landing", produces = "application/text")
	public String landingMessage(){
		return this.message;	
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/getEvbCompFilterMappings")
	public ComparableDataResponse<Map<String, Object>> getEvbCompFilterMappings() {
		logger.info("GET Request received to getEvbCompFilterMappings()");
		Map<String, Object> map = comparableDataService.getEvbCompFilterMappings();
		ComparableDataResponse<Map<String, Object>> comparableDataResponse = new ComparableDataResponse<Map<String, Object>>(true);
		comparableDataResponse.setPayload(map);
		return comparableDataResponse;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/searchEvbComp")
	public ComparableDataResponse<Map<String, Object>> searchEvbComp(@RequestBody EVBCompSearchCriteria evbCompSearchCriteria){
		logger.info("POST Request received to searchEvbComp() with "+evbCompSearchCriteria);
		Map<String, Object> map = comparableDataService.searchEvbData(evbCompSearchCriteria);
		ComparableDataResponse<Map<String, Object>> comparableDataResponse = new ComparableDataResponse<Map<String, Object>>(true);
		comparableDataResponse.setPayload(map);
		return comparableDataResponse;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/getSystemGenerated")
	public ComparableDataResponse<List<EVBData>> getSystemGenerated(@RequestBody PESecurityInfoDTO peSecurityInfoDTO){
		logger.info("POST Request received to getSystemGenerated() with "+peSecurityInfoDTO);
		Map<String, Object> map = comparableDataService.getSystemGenerated_proc(peSecurityInfoDTO);
		ComparableDataResponse<List<EVBData>> comparableDataResponse = new ComparableDataResponse<List<EVBData>>(true);
		comparableDataResponse.setPayload((List<EVBData>) map.get("content"));
		return comparableDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/getLatestCompsDetails")
	public ComparableDataResponse<List<EVBData>> getLatestCompsDetails(@RequestBody EvbDataRefreshRequest evbDataRefreshRequest){
		logger.info("POST Request received to getLatestCompsDetails() with "+evbDataRefreshRequest);
		Map<String, Object> map = comparableDataService.getLatestCompsDetails_proc(evbDataRefreshRequest);
		ComparableDataResponse<List<EVBData>> comparableDataResponse = new ComparableDataResponse<List<EVBData>>(true);
		comparableDataResponse.setPayload((List<EVBData>) map.get("content"));
		return comparableDataResponse;
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value="/getEvbCompFilterMappings_old")
	public Map<String, Object> getEvbCompFilterMappings_old() {
		logger.info("GET Request received to getEvbCompFilterMappings()");
		return comparableDataService.getEvbCompFilterMappings();
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/searchEvbComp_old")
	public Map<String, Object> searchEvbComp_old(@RequestBody EVBCompSearchCriteria evbCompSearchCriteria){
		logger.info("POST Request received to searchEvbComp() with "+evbCompSearchCriteria);
		return comparableDataService.searchEvbData(evbCompSearchCriteria);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/getSystemGenerated_old")
	public List<EVBData> getSystemGenerated_old(@RequestBody PESecurityInfoDTO peSecurityInfoDTO){
		logger.info("POST Request received to getSystemGenerated() with "+peSecurityInfoDTO);
		Map<String, Object> map = comparableDataService.getSystemGenerated_proc(peSecurityInfoDTO);		
		return (List<EVBData>) map.get("content");
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/getLatestCompsDetails_old")
	public List<EVBData> getLatestCompsDetails_old(@RequestBody EvbDataRefreshRequest evbDataRefreshRequest){
		logger.info("POST Request received to getLatestCompsDetails() with "+evbDataRefreshRequest);
		Map<String, Object> map = comparableDataService.getLatestCompsDetails_proc(evbDataRefreshRequest);		
		return (List<EVBData>) map.get("content");
	}
	*/
}
