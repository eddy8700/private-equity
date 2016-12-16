package com.markit.pe.positiondata.util;

import java.util.List;
import java.util.Map;

public interface CacheManager {
	
	public void initializeCacheDuringBootup();

	List<String> getValue(String portfolioId);

	void populateCache(Map<String, Object> map, Long key, String fileName);

	void evictCache(Map<String, Object> map, Long key, String fileName);

}
