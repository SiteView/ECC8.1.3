package com.siteview.ecc.start;

import java.io.Serializable;

import javax.servlet.ServletConfig;

/*
 * ���Ԥ��������Ⱥ�˳�������
 * ʵ�ֱ��ӿڣ��������õ�web.xml�е�uniqueStarterListenerNameList��starterListenersClass
 * */
public interface StarterListener extends Serializable{
	public void startInit(EccStarter starter);
	public void destroyed(EccStarter starter);
}
