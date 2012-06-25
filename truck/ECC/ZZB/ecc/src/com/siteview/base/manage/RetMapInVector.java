package com.siteview.base.manage;

import java.util.List;
import java.util.Map;

public class RetMapInVector {
	private String estr;
	private boolean retbool;
	private List<Map<String, String>> vmap;

	public RetMapInVector() {
		estr = null;
		retbool = false;
		vmap = null;
	}

	public RetMapInVector(boolean isok, String inestr,
			List<Map<String, String>> invmap) {
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

	public List<Map<String, String>> getVmap() {
		return vmap;
	}

	public void setEstr(String inestr) {
		estr = inestr;
	}

	public void setRetbool(boolean isok) {
		retbool = isok;
	}

	public void setVmap(List<Map<String, String>> invmap) {
		vmap = invmap;
	}

}
