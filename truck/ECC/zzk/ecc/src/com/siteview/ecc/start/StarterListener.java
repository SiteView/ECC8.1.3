package com.siteview.ecc.start;

import java.io.Serializable;

import javax.servlet.ServletConfig;

/*
 * 解决预加载类的先后顺序的问题
 * 实现本接口，并且配置到web.xml中的uniqueStarterListenerNameList和starterListenersClass
 * */
public interface StarterListener extends Serializable{
	public void startInit(EccStarter starter);
	public void destroyed(EccStarter starter);
}
