package com.siteview.ecc.general;

import java.util.ArrayList;

/**
 * ��ʹ��������ͼ�������е��ʱ���ܽڵ�ʱ���ݸýڵ����ƺ��ӽڵ����Ƽ��ϵ�ʵ����
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
