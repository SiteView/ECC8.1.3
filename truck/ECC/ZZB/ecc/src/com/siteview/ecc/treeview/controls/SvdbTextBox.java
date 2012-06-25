package com.siteview.ecc.treeview.controls;

import org.zkoss.zul.Textbox;

public class SvdbTextBox extends Textbox implements ISvdbControl
{
	
	public SvdbTextBox()
	{
	}
	
	/**
	 * SVDB�ֶ�
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
	 * SVDB��������
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
	 * SVDBֵ
	 */
	public String getSvdbValue()
	{
		return this.getValue();
	}
	
	public void setSvdbValue(String s)
	{
		this.setValue(s);
		
	}
	
	/**
	 * SVDB�����ı�
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
	 * ����
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
