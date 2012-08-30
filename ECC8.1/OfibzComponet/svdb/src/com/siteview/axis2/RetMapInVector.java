package com.siteview.axis2;

public class RetMapInVector {
	private String estr;
	private boolean retbool;
	private KeyValueArray[] vmap;

	public RetMapInVector() {
		estr = null;
		retbool = false;
		vmap = null;
	}

	public RetMapInVector(boolean isok, String inestr, KeyValueArray[] invmap) {
		retbool = isok;
		estr = inestr;
		vmap = invmap;
	}

	public String getEstr() {
		return estr;
	}

	public boolean getRetbool() {
		return retbool;
	}

	public KeyValueArray[] getVmap() {
		return vmap;
	}

	public void setEstr(String inestr) {
		estr = inestr;
	}

	public void setRetbool(boolean isok) {
		retbool = isok;
	}

	public void setVmap(KeyValueArray[] invmap) {
		vmap = invmap;
	}

}
