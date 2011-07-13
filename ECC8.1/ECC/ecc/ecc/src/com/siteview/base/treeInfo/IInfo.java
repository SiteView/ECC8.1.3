

package com.siteview.base.treeInfo;

import java.util.Date;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;

public interface IInfo
{
	
	
//seȨ�� ��	addsongroup=0,adddevice=0,  se_edit=0,
//��Ȩ�� ��		                                                                                                    ճ��(�豸)                        ˢ��
//��Ȩ�� ��	addsongroup=0,adddevice=0,  editgroup=0,copydevice=0,   delgroup=0,    grouprefresh=0,
//�豸Ȩ�� ��	addmonitor=0,               editdevice=0,testdevice=0,  deldevice=0,   devicerefresh=0,
//�����Ȩ�� ��		                    editmonitor=0,              delmonitor=0,  monitorrefresh=0,
//
	
	int get_sub_entity_sum(Session session);
	
	int get_sub_monitor_sum(Session session);
	
	int get_sub_monitor_disable_sum(Session session);
	
	int get_sub_monitor_error_sum(Session session);
	
	int get_sub_monitor_warning_sum(Session session);
	
	int get_sub_monitor_ok_sum(Session session);

	int get_sub_entity_sum(View view);
	
	int get_sub_monitor_sum(View view);
	
	int get_sub_monitor_disable_sum(View view);
	
	int get_sub_monitor_error_sum(View view);
	
	int get_sub_monitor_warning_sum(View view);
	
	int get_sub_monitor_ok_sum(View view);

	/**
	 * �Ƿ����Ա (����Ա�����������нڵ㣬��ӵ��ȫ��Ȩ��)
	 */	
	boolean isAdmin();
	
	/**
	 * �Ƿ���Ȩ�ޱ༭�ýڵ�
	 */	
	boolean canEdit();
	
	boolean canDisableOrEnableMonitor(View view);
	
	String	getLableofDisableOrEnable();
	
	/**
	 * �����б༭Ȩ��
	 */		
	boolean disableMonitor(Date start, Date end, View view)throws Exception;
	
	/**
	 * �����б༭Ȩ��
	 */		
	boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception;
	
	/**
	 * �����б༭Ȩ��
	 */		
	boolean enableMonitor(View view)throws Exception;
	
	/**
	 * �����б༭Ȩ��
	 */		
	boolean enableMonitor(String[] id, View view)throws Exception;
	
	
	
	
	/**
	 * �Ƿ���Ȩ�� deleteNode()
	 */	
	boolean canDeleteNode();	
	/**
	 * ɾ�����ڵ㼰����������
	 */	
	boolean deleteNode(View view) throws Exception;
	
	/**
	 * ���ݴ����id ������ɾ������
	 */	
	boolean deleteNode(String[] id,View view) throws Exception;
	
	
	/**
	 * �Ƿ���Ȩ�� refresh()
	 */	
	boolean canRefresh();	
	/**
	 * ˢ�±��ڵ��µ����м����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */
	String refresh()throws Exception;	
	
	/**
	 * ѭ�����ñ�������ȡ��ˢ�µĽ����ѭ�����1-3�룬��ʱ�䲻Ҫ����60�룬���������쳣���˳�ѭ��
	 * <br/> ������ 3 �ε��ú��Ѿ�ȡ��ȫ����������ڵ� 3 �ε���ʱ���׳��쳣
	 * @param  queueName ��Ϣ���е�����
	 * @return ���ص�ˢ�������ڡ�RefreshData_1, RefreshData_2... ��, ���� dstr �ȼ����Ϣ
	 */		
	public RetMapInMap getRefreshedData(String queueName)throws Exception;

	/**
	 * ���ݴ����id ������ˢ�¼����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */	
	String refresh(String[] id)throws Exception;	
}
