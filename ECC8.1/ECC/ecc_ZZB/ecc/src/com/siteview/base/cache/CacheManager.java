package com.siteview.base.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

  
public class CacheManager {   
       
    private BaseCache cache;   
    private final static Map<String,CacheManager> instances = new ConcurrentHashMap<String,CacheManager>();
       
    public CacheManager(String prefix) {   
        //这个根据配置文件来，初始BaseCache而已;   
    	cache = new BaseCache(prefix,60);        
    }   
       
    public static CacheManager getInstance(String prefix){  
    	CacheManager instance = instances.get(prefix);
        if (instance == null){   
            instance = new CacheManager(prefix);   
        	instances.put(prefix, instance);
        }   
        return instance;   
    }   
    
    private List<String> keys = new CopyOnWriteArrayList<String>();
  
    public List<String> getKeys() {
		return keys;
	}

	public void put(String key ,Object object) {   
    	cache.put(key,object);
    	if (keys.contains(key) == false){
    		keys.add(key);
    	}
    }   
  
    public void remove(String key) {   
    	cache.remove(key);  
    	keys.remove(key);
    }   
  
    public Object get(String key) throws Exception {   
         return cache.get(key);   
    }   
  
    public void removeAll() {   
    	cache.removeAll();
    	keys.clear();
    }   
  
}  
