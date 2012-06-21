package com.siteview.actions;

import java.util.HashMap;

import org.zkoss.zul.Image;

public class EccAction
{
	public final static int ACTION_FOR_POPUP=1;  
	public final static int ACTION_FOR_TREENODE=0;
	
	/*type是整体树上面功能菜单的节点type*/
	private String type;
	
	/*license是写入user.ini的授权编码，如m_allview,m_UserAdmin等*/
	private String license;
	
	/*actionName是中文名称*/
	private String actionName;
	
	/*触发的url*/
	private String url;

	/*树节点图标显示*/
	private String image=null;

	/*默认不需要权限*/
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
