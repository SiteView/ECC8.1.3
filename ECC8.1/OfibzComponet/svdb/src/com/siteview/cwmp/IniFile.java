package com.siteview.cwmp;

import java.util.List;
import java.util.Map;

public interface IniFile {
	/**
	 * ��������
	 * @throws Exception
	 */
	public void load() throws Exception;
	/**
	 * �½�һ��section����Ҫ������ saveChange()
	 */
	public void createSection(String section) throws Exception;
	/**
	 * ɾ��һ��section����Ҫ������ saveChange()
	 */
	public void deleteSection(String section) throws Exception;
	/**
	 * ɾ��һ�� key ����Ҫ������ saveChange()
	 */
	public void deleteKey(String section, String key) throws Exception;
	/**
	 * �޸�(���½�)һ�� key value����Ҫ������ saveChange()
	 */
	public void setKeyValue(String section, String key, String value) throws Exception;
	/**
	 * �޸�(���½�)һ�� key value����Ҫ������ saveChange()
	 */
	public void setKeyValue(String section, String key, int value) throws Exception;
	/**
	 * �����޸�
	 * <br/> ����������»�ȡ����ʵ���е����ݻᱻ�� null��
	 */
	public void saveChange() throws Exception;
	
	public String getValue(String section, String key);
	
	public Map<String, String> getSectionData(String section);
	public List<String> getSectionList();
	public List<String> getKeyList(String section);
	public String getFileName();

}
