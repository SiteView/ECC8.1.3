package com.siteview.ecc.monitorbrower;

import java.util.List;

public interface IMonitorDao {
	/**
	 * 根据用户传入筛选条件得到监测器
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public List<MonitorBean> queryMonitorInfo(MonitorFilter filter) throws Exception;
	/**
	 * 根据监测器id得到监测器信息
	 * @param monitorId
	 * @return
	 * @throws Exception
	 */
	public MonitorBean queryMonitorInfo(String monitorId) throws Exception;
}
