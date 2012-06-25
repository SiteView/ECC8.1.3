package com.siteview.ecc.treeview.controls;

public interface ISvdbControl {
	/**
	 * SVDB字段
	 */
	
	public String getSvdbField();

	
	public void setSvdbField(String s);

	
	/**
	 * SVDB数据类型
	 */
	
	public String getSvdbFieldDataType();
	
	public void setSvdbFieldDataType(String s);

	
	/**
	 * SVDB值
	 */
	public String getSvdbValue();

	
	public void setSvdbValue(String s);
	

	
	public String getHelptext();

	public void setHelptext(String s);
	

	
	public boolean getSvrun();
	
	
	public void setSvrun(boolean b);
	

}
