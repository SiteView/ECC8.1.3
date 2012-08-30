package com.siteview.cwmp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.IniValidator;

import com.siteview.cwmp.io.IniFileReader;
import com.siteview.cwmp.io.IniFileWriter;

/**
 * ���ص�Inifile���ʶ���
 * @author hailong.yi
 *
 */
public class LocalIniFile implements com.siteview.cwmp.IniFile{
	private IniFile ini = null;
	private File file = null;

	public LocalIniFile(String fileName)
	{
		 file = new File( fileName );
		 
		 IniValidator inivalidator = new IniValidator();
	     String validChars = "A-Za-z0-9_:!?%^&*()+\\-{}'#@~<>,.|$\\W";
    
		 String regex = "(^[" + validChars + "]" +
		                  "[" + validChars + " ]*" +   // notice that this includes a space character
		                  "[" + validChars + "]$)" +
		               "|(^[" + validChars + "]$)";
		 inivalidator.setSectionNameRegEx( Pattern.compile(regex) );
		 inivalidator.setItemNameRegEx( Pattern.compile(regex) );

		 ini = new BasicIniFile(inivalidator);
	}
	
	public void load() throws Exception
	{
		IniFileReader reader = new IniFileReader( ini, file );
		reader.read();
	}

	/**
	 * �½�һ��section����Ҫ������ saveChange()
	 */
	public void createSection(String section) throws Exception
	{
		ini.addSection(section);
	}
	
	/**
	 * ɾ��һ��section����Ҫ������ saveChange()
	 */
	public void deleteSection(String section) throws Exception
	{
		ini.removeSection(section);
	}
	
	/**
	 * ɾ��һ�� key ����Ҫ������ saveChange()
	 */
	public void deleteKey(String section, String key) throws Exception
	{
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) return;
		iniSection.removeItem(key);
	}
	
	/**
	 * �޸�(���½�)һ�� key value����Ҫ������ saveChange()
	 */
	public void setKeyValue(String section, String key, String value) throws Exception
	{
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) {
			ini.addSection(section).addItem(key).setValue(value);
			return;
		}
		IniItem item = iniSection.getItem(key);
		if (item == null) {
			iniSection.addItem(key).setValue(value);
			return;
		}
		item.setValue(value);
	}
	
	/**
	 * �޸�(���½�)һ�� key value����Ҫ������ saveChange()
	 */
	public void setKeyValue(String section, String key, int value) throws Exception
	{
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) {
			ini.addSection(section).addItem(key).setValue(value);
			return;
		}
		IniItem item = iniSection.getItem(key);
		if (item == null) {
			iniSection.addItem(key).setValue(value);
			return;
		}
		item.setValue(value);
	}
	
	/**
	 * ���޸ı��浽��������
	 * <br/> ����������»�ȡ����ʵ���е����ݻᱻ�� null��
	 */
	public void saveChange() throws Exception
	{
		 IniFileWriter writer = new IniFileWriter( ini, file );
		 writer.write();
	}
	
	public String getValue(String section, String key)
	{
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) {
			return null;
		}
		IniItem item = iniSection.getItem(key);
		if (item == null) {
			return null;
		}
		return item.getValue();
	}
	
	public Map<String, String> getSectionData(String section)
	{
		Map<String, String> map = new HashMap<String, String>();
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) {
			return map;
		}
		for (IniItem item : iniSection.getItems()){
			map.put(item.getName(), item.getValue());
		}
		return map;
	}
	
	public List<String> getSectionList()
	{
		List<String> retlist = new ArrayList<String>();
		retlist.addAll(ini.getSectionNames());
		return retlist;
	}
	
	public List<String> getKeyList(String section)
	{
		List<String> retlist = new ArrayList<String>();
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) {
			return retlist;
		}
		retlist.addAll(iniSection.getItemNames());
		return retlist;
	}
	
	public String getFileName()
	{
		return file == null ? null : file.getName();
	}
	
	public void display() throws Exception
	{
		System.out.println("filename=" + file.getName());
		for ( String section : this.getSectionList()){
			System.out.println("[" + section + "]");
			for ( String key : this.getKeyList(section)){
				System.out.print(key);
				System.out.print("=");
				System.out.println(this.getValue(section, key));
			}
		}
	}
	

}
