package com.siteview.ecc.treeview.controls;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;

public class SvdbComboBox extends Combobox implements ISvdbControl
{
	public SvdbComboBox()
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
		if (this.getSelectedIndex() == -1)
		{
			if(this.getChildren().size() > 0){
				this.setSelectedIndex(0);
			}else{
				return "";
			}
		}
		return (String) this.getSelectedItem().getValue();
	}
	
	public void setSvdbValue(String s) 
	{
		if (s==null ||s.isEmpty())
		{
			if (this.getItemCount()>0)
			this.setSelectedIndex(0);
			return;
		}
		for (int j = 0; j < this.getItemCount(); j++)
		{
			if(this.getItemAtIndex(j).getValue()!=null)
			if (this.getItemAtIndex(j).getValue().equals(s))
			{
				
				this.setSelectedIndex(j);
				break;
			}
		}
		if (this.getSelectedIndex() == -1 && this.getItemCount() > 0)
		{
			if(s.equals("1")){
				this.setSelectedIndex(0);
			}else if(s.equals("2")){
				this.setSelectedIndex(1);
			}
		}
		
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
