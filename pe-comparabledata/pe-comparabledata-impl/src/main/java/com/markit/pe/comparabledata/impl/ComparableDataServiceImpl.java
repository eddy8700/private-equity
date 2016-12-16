/**
 * 
 */
package com.markit.pe.comparabledata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markit.pe.comparabledata.api.ComparableDataService;
import com.markit.pe.comparabledata.domain.EVBCompFilterRaw;
import com.markit.pe.comparabledata.domain.EVBCompSearchCriteria;
import com.markit.pe.comparabledata.domain.EVBData;
import com.markit.pe.comparabledata.domain.QEVBData;
import com.markit.pe.comparabledata.repository.EVBCompFilterRawRepository;
import com.markit.pe.comparabledata.repository.EVBCompRawDataRepository;
import com.markit.pe.comparabledata.request.EvbDataRefreshRequest;
import com.markit.pe.positiondata.value.objects.PESecurityInfoDTO;
import com.querydsl.core.types.dsl.BooleanExpression;


/**
 * @author mahesh.agarwal
 *
 */
@Service
@Transactional
public class ComparableDataServiceImpl implements ComparableDataService{
	
	private static final Logger logger = LoggerFactory.getLogger(ComparableDataServiceImpl.class);
	
	@Autowired
	EVBCompFilterRawRepository evbCompFilterRawRepository;
	
	@Autowired
	EVBCompRawDataRepository evbCompRawDataRepository;
	
	
	@Override
	public Map<String, Object> getSystemGenerated_proc(PESecurityInfoDTO peSecurityInfoDTO) {
		
		logger.info("In getSystemGenerated_proc()");
		
		logger.info("Searching in database...");
		final List<EVBData> results=evbCompRawDataRepository.fetchSystemComparables(peSecurityInfoDTO.getTransactionDate(), 
				//peSecurityInfoDTO.getIssueDate(), 
				peSecurityInfoDTO.getClassification(), 
				peSecurityInfoDTO.getCurrency().toString(), 
				peSecurityInfoDTO.getMaturityDate(), 
				peSecurityInfoDTO.getYtmTransient(), 
			peSecurityInfoDTO.getSector(), 
			//peSecurityInfoDTO.getPrincipalPaymentType(), 
			peSecurityInfoDTO.getAverageLife());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultsCount", results.size());
		map.put("content", results);
		
		//logger.info("Returing comparables : "+results.toString());
		logger.info("Returing comparables : "+results.size());
		logger.info("Returning getSystemGenerated_proc()");
		
		return Collections.unmodifiableMap(map);
	}
	
	@Override
	public Map<String, Object> getSystemGenerated(PESecurityInfoDTO peSecurityInfoDTO) {
		
		logger.info("In getSystemGenerated()");
		
		BooleanExpression criteria = EvbCompSystemGenPredicates.build(peSecurityInfoDTO);
		logger.info("Searching in database...");
		List<EVBData> results = (List<EVBData>) evbCompRawDataRepository.findAll(criteria);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultsCount", results.size());
		map.put("content", results);
		
		logger.info("Returing System Generated Comparables : "+results.size());
		logger.info("Returning getSystemGenerated()");
		
		return map;
	}
	
	@Override
	public Map<String, Object> getLatestCompsDetails(EvbDataRefreshRequest evbDataRefreshRequest) {
		logger.info("In getLatestCompsDetails()");
		
		List<String> compList = evbDataRefreshRequest.getCompList();
		Date asOfDate = evbDataRefreshRequest.getAsOfDate();
		
		BooleanExpression criteria = QEVBData.eVBData.isin.in(compList).
									and(QEVBData.eVBData.date.eq(asOfDate));
		
		List<EVBData> results = (List<EVBData>) evbCompRawDataRepository.findAll(criteria);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultsCount", results.size());
		map.put("content", results);
		
		logger.info("Returing comparables : "+results.size());
		logger.info("Returning getLatestCompsDetails()");		
		return map;
	}
	
	@Override
	public Map<String, Object> getLatestCompsDetails_proc(EvbDataRefreshRequest evbDataRefreshRequest) {
		logger.info("In getLatestCompsDetails_proc()");
		
		String commaSepISINs = StringUtils.join(evbDataRefreshRequest.getCompList(), ",");
		logger.info("Searching in database...");
		final List<EVBData> results=evbCompRawDataRepository.getLatestCompsDetails(
				commaSepISINs, 
				evbDataRefreshRequest.getAsOfDate());
				
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultsCount", results.size());
		map.put("content", results);
		
		logger.info("Returing Latest Comparables : "+results.size());
		logger.info("Returning getLatestCompsDetails_proc()");		
		return map;
	}

	
	@Override
	public Map<String, Object> searchEvbData(EVBCompSearchCriteria evbCompSearchCriteria) {
		logger.info("In searchEvbData()");
		BooleanExpression criteria = EvbCompSearchPredicates.build(evbCompSearchCriteria);
		logger.info("Searching in database...");
		Page<EVBData> page = evbCompRawDataRepository.findAll(criteria, createPageRequest(evbCompSearchCriteria));
		
		logger.info("Total Results : "+page.getTotalElements());
		if(page.getTotalElements()>0){
			logger.info("Total pages: "+page.getTotalPages());
			int pageNum = page.getNumber()+1;
			logger.info("Page number : "+pageNum);
			logger.info("Records : "+page.getNumberOfElements());
		}				
		List<EVBData> results = page.getContent();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultsCount", page.getTotalElements());
		map.put("content", results);
		logger.info("Returning searchEvbData()");
		return map;
	}
	
	private Pageable createPageRequest(EVBCompSearchCriteria evbCompSearchCriteria) {
	    return new PageRequest(evbCompSearchCriteria.getPageNum(), evbCompSearchCriteria.getPageSize());
	}
	
	private List<EVBCompFilterRaw> getAllEVBCompFilterRaw() {
		List<EVBCompFilterRaw> evbCompFilterRaws = (List<EVBCompFilterRaw>) evbCompFilterRawRepository.findAll();
		
		return evbCompFilterRaws;
	}
	
	@Override
	public Map<String, Object> getEvbCompFilterMappings(){
		
		logger.info("In getEvbCompFilterMappings()");
		
		Map<String, Object> mappings = new HashMap<String, Object>();
		
		logger.info("Fetching EVB Comp Filter Raw from database...");
		
		List<EVBCompFilterRaw> evbCompFilterRaws = getAllEVBCompFilterRaw();
		Map<Long, EVBCompFilterRaw> evbCompFilterRawMap = new HashMap<Long, EVBCompFilterRaw>();
		for(EVBCompFilterRaw entity : evbCompFilterRaws){
			evbCompFilterRawMap.put(entity.getId(), entity);
		}
		for(EVBCompFilterRaw entity : evbCompFilterRawMap.values()){
			if(null==entity.getParent()){
				if(mappings.containsKey(entity.getType())){
					//List<Map<String, Object>> valueList = (List<Map<String, Object>>) mappings.get(entity.getType());
					boolean hasChild = false;
					Map<String, Object> internalMap =  new HashMap<String, Object>();
					internalMap.put("name", entity.getValue());
					for(EVBCompFilterRaw entity1 : evbCompFilterRawMap.values()){
						if(null != entity1.getParent() && Integer.parseInt(entity1.getParent())==entity.getId()){
							hasChild = true;
							if(internalMap.containsKey(entity1.getType())){
								List<String> childList = (List<String>) internalMap.get(entity1.getType());
								childList.add(entity1.getValue());
							} else {
								List<String> childList = new ArrayList<String>();
								childList.add(entity1.getValue());
								internalMap.put(entity1.getType(),childList);
							}					
						}
					}
					
					if(hasChild){
						List<Map<String, Object>> valueList = (List<Map<String, Object>>) mappings.get(entity.getType());
						valueList.add(internalMap);
						mappings.put(entity.getType(), valueList);
					} else {
						List<String> valueList = (List<String>) mappings.get(entity.getType());
						valueList.add(entity.getValue());
						mappings.put(entity.getType(), valueList);
					}					
					
				} else {
					//List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
					boolean hasChild = false;
					Map<String, Object> internalMap =  new HashMap<String, Object>();
					internalMap.put("name", entity.getValue());
					for(EVBCompFilterRaw entity1 : evbCompFilterRawMap.values()){
						if(null != entity1.getParent() && Integer.parseInt(entity1.getParent())==entity.getId()){
							hasChild = true;
							if(internalMap.containsKey(entity1.getType())){
								List<String> childList = (List<String>) internalMap.get(entity1.getType());
								childList.add(entity1.getValue());
							} else {
								List<String> childList = new ArrayList<String>();
								childList.add(entity1.getValue());
								internalMap.put(entity1.getType(),childList);
							}					
						}
					}
					if(hasChild){
						List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
						valueList.add(internalMap);
						mappings.put(entity.getType(), valueList);
					} else {
						List<String> valueList = new ArrayList<String>();
						valueList.add(entity.getValue());
						mappings.put(entity.getType(), valueList);
					}									
				}
			}						
		}
		logger.info("Returning getEvbCompFilterMappings()");
		logger.debug("With : "+mappings);
		return mappings;
	}

	
}
