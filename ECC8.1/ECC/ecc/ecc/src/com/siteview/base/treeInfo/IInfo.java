

package com.siteview.base.treeInfo;

import java.util.Date;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;

public interface IInfo
{
	
	
//se权限 ：	addsongroup=0,adddevice=0,  se_edit=0,
//组权限 ：		                                                                                                    粘贴(设备)                        刷新
//组权限 ：	addsongroup=0,adddevice=0,  editgroup=0,copydevice=0,   delgroup=0,    grouprefresh=0,
//设备权限 ：	addmonitor=0,               editdevice=0,testdevice=0,  deldevice=0,   devicerefresh=0,
//监测器权限 ：		                    editmonitor=0,              delmonitor=0,  monitorrefresh=0,
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
	 * 是否管理员 (管理员对整棵树所有节点，都拥有全部权限)
	 */	
	boolean isAdmin();
	
	/**
	 * 是否有权限编辑该节点
	 */	
	boolean canEdit();
	
	boolean canDisableOrEnableMonitor(View view);
	
	String	getLableofDisableOrEnable();
	
	/**
	 * 必须有编辑权限
	 */		
	boolean disableMonitor(Date start, Date end, View view)throws Exception;
	
	/**
	 * 必须有编辑权限
	 */		
	boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception;
	
	/**
	 * 必须有编辑权限
	 */		
	boolean enableMonitor(View view)throws Exception;
	
	/**
	 * 必须有编辑权限
	 */		
	boolean enableMonitor(String[] id, View view)throws Exception;
	
	
	
	
	/**
	 * 是否有权限 deleteNode()
	 */	
	boolean canDeleteNode();	
	/**
	 * 删除本节点及其所有子孙
	 */	
	boolean deleteNode(View view) throws Exception;
	
	/**
	 * 根据传入的id ，批量删除子孙
	 */	
	boolean deleteNode(String[] id,View view) throws Exception;
	
	
	/**
	 * 是否有权限 refresh()
	 */	
	boolean canRefresh();	
	/**
	 * 刷新本节点下的所有监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */
	String refresh()throws Exception;	
	
	/**
	 * 循环调用本方法以取得刷新的结果，循环间隔1-3秒，总时间不要超过60秒，其间如果有异常则退出循环
	 * <br/> 比如在 3 次调用后，已经取得全部结果，则在第 3 次调用时会抛出异常
	 * @param  queueName 消息队列的名称
	 * @return 返回的刷新数据在　RefreshData_1, RefreshData_2... 中, 其中 dstr 等监测信息
	 */		
	public RetMapInMap getRefreshedData(String queueName)throws Exception;

	/**
	 * 根据传入的id ，批量刷新监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */	
	String refresh(String[] id)throws Exception;	
}
