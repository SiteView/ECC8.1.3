package com.siteview.cwmp;

import java.util.List;
import java.util.Map;

public interface IniFile {
	/**
	 * 调入数据
	 * @throws Exception
	 */
	public void load() throws Exception;
	/**
	 * 新建一个section，需要最后调用 saveChange()
	 */
	public void createSection(String section) throws Exception;
	/**
	 * 删除一个section，需要最后调用 saveChange()
	 */
	public void deleteSection(String section) throws Exception;
	/**
	 * 删除一个 key ，需要最后调用 saveChange()
	 */
	public void deleteKey(String section, String key) throws Exception;
	/**
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
	 */
	public void setKeyValue(String section, String key, String value) throws Exception;
	/**
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
	 */
	public void setKeyValue(String section, String key, int value) throws Exception;
	/**
	 * 保存修改
	 * <br/> 保存后请重新获取（类实例中的数据会被置 null）
	 */
	public void saveChange() throws Exception;
	
	public String getValue(String section, String key);
	
	public Map<String, String> getSectionData(String section);
	public List<String> getSectionList();
	public List<String> getKeyList(String section);
	public String getFileName();

}
