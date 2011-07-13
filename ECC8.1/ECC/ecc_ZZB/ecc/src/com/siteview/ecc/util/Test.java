package com.siteview.ecc.util;

public class Test {
	public static void main(String args[]){
		
		
		
		String rightValues = "addsongroup=1,adddevice=1,se_edit=0,pastedevice=1,telebackup=1,adddevice=1,editgroup=1,addsongroup=1,pastedevice=1,delgroup=1,addmonitor=1,editdevice=1,testdevice=1,copydevice=1,pastemonitor=1,deldevice=1,devicerefresh=1,editmonitor=1,copymonitor=1,delmonitor=1,monitorrefresh=1";
		rightValues = rightValues.substring(0, 62);
		System.out.println(rightValues);
		rightValues = "addsongroup=1,adddevice=1,se_edit=0,pastedevice=1,adddevice=1,editgroup=1,addsongroup=1,pastedevice=1,delgroup=1,addmonitor=1,editdevice=1,testdevice=1,copydevice=1,pastemonitor=1,deldevice=1,devicerefresh=1,editmonitor=1,copymonitor=1,delmonitor=1,monitorrefresh=1";

		rightValues = rightValues.substring(50,112);
		System.out.println(rightValues);


	}
}
