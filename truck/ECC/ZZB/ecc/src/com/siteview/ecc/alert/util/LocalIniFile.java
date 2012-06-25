package com.siteview.ecc.alert.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.IniValidator;


public class LocalIniFile {
	private final static Logger logger = Logger.getLogger(LocalIniFile.class);
	private IniFile ini = null;
	private File file = null;
	
	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	private boolean remote = true;

	private static String REFIX = "WebEcc_";
	private static String SECTION_NAME = "SECTION_NAME";
	private static String KEY_NAME = "KEY_NAME";
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
		try{
			IniFileReader reader = new IniFileReader( ini, file );
			reader.read();
		}catch(Exception e){
			if (remote){
				com.siteview.base.data.IniFile inifile = new com.siteview.base.data.IniFile(REFIX + file.getName());
				inifile.load();
				this.init(inifile);
			}
		}
	}

	/**
	 * 新建一个section，需要最后调用 saveChange()
	 */
	public void createSection(String section) throws Exception
	{
		ini.addSection(section);
	}
	
	/**
	 * 删除一个section，需要最后调用 saveChange()
	 */
	public void deleteSection(String section) throws Exception
	{
		ini.removeSection(section);
	}
	
	/**
	 * 删除一个 key ，需要最后调用 saveChange()
	 */
	public void deleteKey(String section, String key) throws Exception
	{
		IniSection iniSection = ini.getSection(section);
		if (iniSection == null) return;
		iniSection.removeItem(key);
	}
	
	/**
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
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
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
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
	 * 把修改保存到服务器上
	 * <br/> 保存后请重新获取（类实例中的数据会被置 null）
	 */
	public synchronized void saveChange() throws Exception
	{
		 IniFileWriter writer = new IniFileWriter( ini, file );
		 writer.write();
  		 if (remote){
  			 com.siteview.base.data.IniFile inifile = new com.siteview.base.data.IniFile(REFIX + file.getName());
  			 inifile.createSection(SECTION_NAME);
  			 inifile.setKeyValue(SECTION_NAME, KEY_NAME, this.readFile(file.getName()));
  			 inifile.saveChange();
  		 }
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
		Map<String, String> map = new FastMap<String, String>();
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
		List<String> retlist = new FastList<String>();
		retlist.addAll(ini.getSectionNames());
		return retlist;
	}
	
	public List<String> getKeyList(String section)
	{
		List<String> retlist = new FastList<String>();
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
		logger.info("filename=" + file.getName());
		for ( String section : this.getSectionList()){
			logger.info("[" + section + "]");
			for ( String key : this.getKeyList(section)){
				logger.info(key);
				logger.info("=");
				logger.info(this.getValue(section, key));
			}
		}
	}
	
	private void init(com.siteview.base.data.IniFile inifile) throws Exception{
		this.file = new File(inifile.getFileName().replace(REFIX,""));
		this.file.delete();
		String context = inifile.getValue(SECTION_NAME, KEY_NAME);
		this.writeFile(this.file.getName(), context);
		this.load();
	}
	
	private void writeFile(String filename,String context) throws Exception{
		BufferedWriter bufferWriter = null;
		FileOutputStream fos=null;
		OutputStreamWriter osw=null;
		try{
	        fos = new FileOutputStream(filename);
	        osw = new OutputStreamWriter(fos, IniFileWriter.ENCODING );
	        bufferWriter = new BufferedWriter( osw );
	        bufferWriter.write( context );
		}finally{
	        try{bufferWriter.close();}catch(Exception e){}
	        try{osw.close();}catch(Exception e){}
	        try{fos.close();}catch(Exception e){}
		}
	}
	private String readFile(String filename) throws Exception{
		BufferedReader reader = null;
		InputStreamReader isr=null;
		FileInputStream fis=null;
		
		try{
			fis=new FileInputStream(new File(filename));
			isr=new InputStreamReader(fis, IniFileWriter.ENCODING );
			reader = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			 while( (line = reader.readLine()) != null ) {
				 sb.append(line);
				 sb.append("\n");
			 }
			 return sb.toString();
		}finally{
	        try{reader.close();}catch(Exception e){}
	        try{isr.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		LocalIniFile ini= new LocalIniFile("test.ini.ini");
		ini.load();
		ini.display();
		String section = "325429262248808141$2009-04-29 00:00:00$2009-04-30 00:00:00$中国任命";
		ini.createSection(section);
		ini.setKeyValue(section, "中国", 123);
		ini.setKeyValue(section, "key", 123);
		ini.saveChange();
		
		ini.load();
		ini.display();
	}
	
	public boolean exist(){
		return ini!=null;
	}

}
