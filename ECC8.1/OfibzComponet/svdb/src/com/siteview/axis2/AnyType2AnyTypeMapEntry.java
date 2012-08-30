package com.siteview.axis2;


public class AnyType2AnyTypeMapEntry {
	private String key = null;
	private KeyValue[] value = null;
	public AnyType2AnyTypeMapEntry()
	{
		
	}
	public AnyType2AnyTypeMapEntry(String inkey, KeyValue[] invalue)
	{
		key= inkey;
		value= invalue;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public KeyValue[] getValue() {
		return value;
	}
	public void setValue(KeyValue[] value) {
		this.value = value;
	}
}
