package com.markit.pe.positiondata.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.markit.pe.portfoliodata.cache.CacheClient;
import com.markit.pe.positiondata.domain.SecurityUploadStatus;
import com.markit.pe.positiondata.domain.SecurityUploadStatus.SecurityLoadStatus;
import com.markit.pe.positiondata.repository.SecurityUploadStatusRepository;

@Component
public class CacheManagerImpl implements CacheManager{
	
	
	@Autowired
	private CacheClient<String, Object> inMemoryCacheClient;

	@Autowired
	private SecurityUploadStatusRepository securityUploadRepo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerImpl.class);
	@SuppressWarnings("unchecked")
	@Override
	@PostConstruct
	public void initializeCacheDuringBootup() {
		LOGGER.info("Build cache");
		final List<SecurityLoadStatus> list= new ArrayList<>();
		list.add(SecurityLoadStatus.IN_PROGRESS);
		list.add(SecurityLoadStatus.NEW);
		List<SecurityUploadStatus> securityUploadStatus=securityUploadRepo.findByLoadStatusIn(list);		
//		LOGGER.info("Populating cache from db");
		for (SecurityUploadStatus securityUploadStatus2 : securityUploadStatus) {
			
			if(inMemoryCacheClient.get(securityUploadStatus2.getPePortfolio().getId().toString()) != null){
				List<String> fileNames = (List<String>) inMemoryCacheClient.get(securityUploadStatus2.getPePortfolio().getId().toString());
				fileNames.add(securityUploadStatus2.getFileName());
			} else {
				List<String> fileNames= new ArrayList<>();
				fileNames.add(securityUploadStatus2.getFileName());
				inMemoryCacheClient.put(securityUploadStatus2.getPePortfolio().getId().toString(), fileNames);
			}			
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValue(final String key) {
    //    LOGGER.info("Looking in the cache for key id {}",key);
      if(inMemoryCacheClient.get(key)!= null)
    	  return (List<String>) inMemoryCacheClient.get(key);
      else{
   // 	  LOGGER.info("Nothing is present in the cache for the key id {}",key);
		return null;
	}}
	
	@Override
	@SuppressWarnings("unchecked")
	public void populateCache(Map<String, Object> map, Long key,String fileName) {
		final SecurityLoadStatus securityLoadStatus=(SecurityLoadStatus) map.get("uploadStatus");
		if(securityLoadStatus.equals(SecurityLoadStatus.NEW)||
		   		  securityLoadStatus.equals(SecurityLoadStatus.IN_PROGRESS)){
			if (inMemoryCacheClient.get(key.toString()) == null) {
			final List<String> fileNames=new ArrayList<>();
			fileNames.add(fileName);
			inMemoryCacheClient.put(key.toString(),fileNames);
		}
			else{
				List<String> fileNames = (List<String>) inMemoryCacheClient.get(key.toString());
				fileNames.add(fileName);
			}
		}
	}
	@Override
	@SuppressWarnings("unchecked")
	public void evictCache(Map<String, Object> map, Long key, String fileName) {
		final SecurityLoadStatus loadStatus = (SecurityLoadStatus) map.get("uploadStatus");
		if (loadStatus.equals(SecurityLoadStatus.LOAD_COMPLETED) || loadStatus.equals(SecurityLoadStatus.LOAD_FAILED)
				|| loadStatus.equals(SecurityLoadStatus.LOADED_WITH_EXCEPTIONS)) {
			LOGGER.info("Clearing the cache for the portfolio id {}", key);
			final Object listFiles = inMemoryCacheClient.get(key.toString());
			List<String> files = (List<String>) listFiles;
			Iterator<String> itr = files.iterator();
			LOGGER.info("Iterating the list of files");
			while (itr.hasNext()) {
				final String obj = (String) itr.next();
				if (obj.equals(fileName)) {
					LOGGER.info("Removing the file with the name {} from the cached", fileName);
					itr.remove();
				}
			}
			// inMemoryCacheClient.remove(portfolioId.toString());
		}
		if (loadStatus.equals(SecurityLoadStatus.IN_PROGRESS) || loadStatus.equals(SecurityLoadStatus.NEW)) {
			LOGGER.info("Status still in progress in db");
		}
	}
	
	
}
