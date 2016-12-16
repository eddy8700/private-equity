package com.markit.pe.portfoliodata.cache;

public interface CacheClient<K, V> {

	public V put(K key, V value, int expiryInSec);

	public V get(K key);

	public V put(K key, V value);

	public void clear();

	public V remove(K key);

	boolean containskey(String key);

	void put(String key);
}
