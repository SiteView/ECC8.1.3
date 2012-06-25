package com.siteview.base.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCacheMap<K, V> implements Map<K, V> {

	private Cache cache;

	public EhCacheMap(String cahceName) {
		CacheManager manager = CacheManager.create();
		if (manager.getCache(cahceName) == null) {
			manager.addCache(cahceName);
		}
		this.cache = manager.getCache(cahceName);
	}
	
	@Override
	public synchronized  void clear() {
		this.cache.removeAll();
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return this.cache.isKeyInCache(key);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return this.cache.isValueInCache(value);
	}

	@Override
	public synchronized Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> entries = new HashSet<java.util.Map.Entry<K, V>>();
		List cacheKeys = this.cache.getKeys();
		for (Object key : cacheKeys) {
			entries.add(new CacheEntry(cache.get(key)));
		}
		return entries;
	}

	@Override
	public synchronized V get(Object key) {
		V entry = null;
		Element element = this.cache.get(key);
		if (element != null) {
			entry = (V) element.getObjectValue();
		}
		return entry;
	}

	@Override
	public synchronized boolean isEmpty() {
		 return size() == 0;

	}

	@Override
	public synchronized Set<K> keySet() {
		Set<K> keys = new HashSet<K>();
		List<K> cacheKeys = this.cache.getKeys();
		for (K cacheKey : cacheKeys) {
			keys.add(cacheKey);
		}
		return keys;
	}

	@Override
	public synchronized  V put(K key, V value) {
        this.cache.put(new Element(key, value));
        return value;
	}

	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> m) {
		Set<K> keys = (Set<K>) m.keySet();
		for (K key : keys) {
			put(key, m.get(key));
		}
	}

	@Override
	public synchronized  V remove(Object key) {
		V entry = get(key);
		this.cache.remove(key);
		return entry;
	}

	@Override
	public synchronized int size() {
		return this.cache.getSize();

	}

	@Override
	public synchronized Collection<V> values() {
		Collection<V> entries = new ArrayList<V>();
		List cacheKeys = this.cache.getKeys();
		for (Object cacheKey : cacheKeys) {
			entries.add((V) this.cache.get(cacheKey).getObjectValue());
		}
		return entries;
	}

}

class CacheEntry<K, V> implements Map.Entry<K, V>{
	private K k = null;
	private V v = null;
	public CacheEntry(Element element){
		this.k = (K) element.getObjectKey();
		this.v = (V) element.getObjectValue();
	}
	@Override
	public K getKey() {
		return k;
	}

	@Override
	public V getValue() {
		return v;
	}

	@Override
	public V setValue(V value) {
		v = value;
		return v;
	}
	
}
