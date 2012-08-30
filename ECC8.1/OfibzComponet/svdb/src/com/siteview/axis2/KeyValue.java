package com.siteview.axis2;

public class KeyValue
{
	private String key= null;
	private String value= null;
	public KeyValue()
	{
	}
	public KeyValue(String inkey, String invalue)
	{
		key= inkey;
		value= invalue;
	}
	public String getKey()
	{
		return key;
	}
	public String getValue()
	{
		return value;
	}
	public void setKey(String inkey)
	{
		key= inkey;
	}
	public void setValue(String invalue)
	{
		value= invalue;
	}
}
