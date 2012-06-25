package com.siteview.ecc.monitorbrower;

import java.util.List;

public interface IMonitorDao {
	/**
	 * �����û�����ɸѡ�����õ������
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<MonitorBean> queryMonitorInfo(MonitorFilter filter) throws Exception;
	/**
	 * ���ݼ����id�õ��������Ϣ
	 * @param monitorId
	 * @return
	 * @throws Exception
	 */
	public MonitorBean queryMonitorInfo(String monitorId) throws Exception;
}
