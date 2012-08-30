package com.siteview.utils;

import java.io.OutputStream;

import org.apache.commons.io.output.NullOutputStream;

public class StreamFactory {

	public static OutputStream getNullOutputStream(){
		return new NullOutputStream();
	}
}
