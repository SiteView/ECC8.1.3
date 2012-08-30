package com.siteview.axis2;

public class KeyValueArray {
	private KeyValue[] values = null;

	public KeyValueArray(){
	}
	public KeyValueArray(KeyValue[] values){
		this.values = values;
	}
	public KeyValue[] getValues() {
		return values;
	}

	public void setValues(KeyValue[] values) {
		this.values = values;
	}
}
