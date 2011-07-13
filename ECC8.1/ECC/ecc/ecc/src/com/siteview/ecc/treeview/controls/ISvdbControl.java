package com.siteview.ecc.treeview.controls;

public interface ISvdbControl {
	/**
	 * SVDB�ֶ�
	 */
	
	public String getSvdbField();

	
	public void setSvdbField(String s);

	
	/**
	 * SVDB��������
	 */
	
	public String getSvdbFieldDataType();
	
	public void setSvdbFieldDataType(String s);

	
	/**
	 * SVDBֵ
	 */
	public String getSvdbValue();

	
	public void setSvdbValue(String s);
	

	
	public String getHelptext();

	public void setHelptext(String s);
	

	
	public boolean getSvrun();
	
	
	public void setSvrun(boolean b);
	

}
