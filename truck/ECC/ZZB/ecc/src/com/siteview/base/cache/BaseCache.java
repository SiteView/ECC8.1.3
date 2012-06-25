package com.siteview.base.cache;

import java.util.Date;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class BaseCache extends GeneralCacheAdministrator {
	private static final long serialVersionUID = -3059636372591730596L;
	// ����ʱ��(��λΪ��);
	private int refreshPeriod;
	// �ؼ���ǰ׺�ַ�;
	private String keyPrefix;

	public BaseCache(String keyPrefix, int refreshPeriod) {
		super();
		this.keyPrefix = keyPrefix;
		this.refreshPeriod = refreshPeriod;
	}

	// ��ӱ�����Ķ���;
	public void put(String key, Object value) {
		this.putInCache(this.keyPrefix + "_" + key, value);
	}

	// ɾ��������Ķ���;
	public void remove(String key) {
		this.flushEntry(this.keyPrefix + "_" + key);
	}

	// ɾ�����б�����Ķ���;
	public void removeAll(Date date) {
		this.flushAll(date);
	}

	public void removeAll() {
		this.flushAll();
	}

	// ��ȡ������Ķ���;
	public Object get(String key) throws Exception {
		try {
			return this.getFromCache(this.keyPrefix + "_" + key,
					this.refreshPeriod);
		} catch (NeedsRefreshException e) {
			this.cancelUpdate(this.keyPrefix + "_" + key);
			throw e;
		}

	}

}
