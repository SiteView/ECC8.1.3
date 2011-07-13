package com.siteview.eccservice;

public class keyValue
{
	public String key;
	public String value;
	public keyValue()
	{
		key= null;
		value= null;
	}
	public keyValue(String inkey, String invalue)
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
