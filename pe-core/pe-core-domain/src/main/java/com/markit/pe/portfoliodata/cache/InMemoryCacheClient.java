package com.markit.pe.portfoliodata.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


public class InMemoryCacheClient<K,V> implements CacheClient<String, Object>{
	
	private final Map<String, Object> map = Collections.synchronizedMap(new HashMap<String, Object>());

	
	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Object get(final String key) {
		return this.map.get(key);
	}

	@Override
	public Object put(final String key, final Object value) {
		return this.map.put(key, value);
	}

	@Override
	public Object put(final String key, final Object value, final int expiryInSec) {
		return this.map.put(key, value);
	}

	@Override
	public Object remove(final String key) {
		return this.map.remove(key);
	}
    
	@Override
	public boolean containskey(final String key){
		return this.map.containsKey(key);
	}

	@Override
	public void put(String key) {
		this.map.put(key, null);
		
	}

}
