package com.siteview.ecc.general;

import java.util.ArrayList;

/**
 * 在使用虚拟视图并在树中点击时功能节点时传递该节点名称和子节点名称集合的实体类
 * @author MaKun
 *
 */
public class ShowInVirtualViewBean {
	private String nodeName;
	private ArrayList<String> sonNames;
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public ArrayList<String> getSonNames() {
		return sonNames;
	}
	public void setSonNames(ArrayList<String> sonNames) {
		this.sonNames = sonNames;
	}
}
