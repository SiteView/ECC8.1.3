package com.siteview.axis2;

public class RetMapInMap {
	private String estr = null;
	private boolean retbool = false;
	private AnyType2AnyTypeMapEntry[] fmap = null;

	public RetMapInMap() {
	}

	public RetMapInMap(boolean isok, String inestr,
			AnyType2AnyTypeMapEntry[] infmap) {
		retbool = isok;
		estr = inestr;
		fmap = infmap;
	}

	public String getEstr() {
		return estr;
	}

	public boolean getRetbool() {
		return retbool;
	}

	public AnyType2AnyTypeMapEntry[] getFmap() {
		return fmap;
	}

	public void setEstr(String inestr) {
		estr = inestr;
	}

	public void setRetbool(boolean isok) {
		retbool = isok;
	}

	public void setFmap(AnyType2AnyTypeMapEntry[] infmap) {
		fmap = infmap;
	}

}
