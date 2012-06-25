package com.siteview.actions;

import java.util.HashMap;

import org.zkoss.zul.Image;

public class EccAction
{
	public final static int ACTION_FOR_POPUP=1;  
	public final static int ACTION_FOR_TREENODE=0;
	
	/*type�����������湦�ܲ˵��Ľڵ�type*/
	private String type;
	
	/*license��д��user.ini����Ȩ���룬��m_allview,m_UserAdmin��*/
	private String license;
	
	/*actionName����������*/
	private String actionName;
	
	/*������url*/
	private String url;

	/*���ڵ�ͼ����ʾ*/
	private String image=null;

	/*Ĭ�ϲ���ҪȨ��*/
	private boolean needAuth;
	
	int actionFor=-1;
	
	public EccAction(int actionFor)
	{
		this.actionFor=actionFor;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public boolean isNeedAuth() {
		return needAuth;
	}
	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
