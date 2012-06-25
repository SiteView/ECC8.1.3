package com.siteview.ecc.treeview.controls;

import org.zkoss.zul.Checkbox;

public class SvdbCheckBox extends Checkbox implements ISvdbControl
{
	public SvdbCheckBox()
	{
	}
	
	/**
	 * SVDB字段
	 */
	private String	svdbField;
	
	public String getSvdbField()
	{
		return svdbField;
	}
	
	public void setSvdbField(String s)
	{
		this.svdbField = s;
	}
	
	/**
	 * SVDB数据类型
	 */
	private String	svdbFieldDataType;
	
	public String getSvdbFieldDataType()
	{
		return svdbFieldDataType;
	}
	
	public void setSvdbFieldDataType(String s)
	{
		this.svdbFieldDataType = s;
		
	}
	
	/**
	 * SVDB值
	 */
	public String getSvdbValue()
	{
		return Boolean.toString(isChecked());
	}
	
	public void setSvdbValue(String s)
	{
		boolean sel = s.toLowerCase().equals("true") ? true : false;
		this.setChecked(sel);
		
	}
	
	/**
	 * SVDB帮助文本
	 */
	private String	helptext;
	
	public String getHelptext()
	{
		return helptext;
	}
	
	public void setHelptext(String s)
	{
		this.helptext = s;
	}
	
	/**
	 * 运行
	 */
	private boolean	svrun;
	
	public boolean getSvrun()
	{
		return svrun;
	}
	
	public void setSvrun(boolean b)
	{
		this.svrun = b;
	}
	
}
