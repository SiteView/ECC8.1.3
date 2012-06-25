/**
 * 
 */
package com.siteview.ecc.report.common;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYDataset;
import org.zkoss.zul.Panel;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;

/**
 *生成报表的所有接口
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-2
 */
 

public interface CreateReport {

	/**
	 * 获取报表数据
	 * 
	 * @param nodeids
	 *            监测器的NODEID
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Report> getReportData(String[] nodeids, String starttime, String endtime);

	public Map<String, Map<String, String>> getReportDataByNodeids(String nodeids, Date starttime, Date endtime);

	/**
	 * 获取报表数据
	 * 
	 * @param nodeids
	 *            监测器的NODEID
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Report> getReportData(String nodeid, String starttime, String endtime);

	public XYDataset buildDataset(Map<String, Map<Date, String>> imgdatas);

	/**
	 * 生成单个图表
	 * 
	 * @param title
	 * @param subtite
	 * @param valuelabel
	 * @param data
	 * @param step
	 * @param max
	 * @param startdate
	 * @param enddate
	 * @param min
	 * @param xlabel
	 * @param pngwidth
	 * @param pngheight
	 * @return
	 */

	public BufferedImage buildImageBuffer(String title, String subtite, String valuelabel, XYDataset data, double step, double max, Date startdate,
			Date enddate, double min, boolean xlabel, int pngwidth, int pngheight, String reporttype);

	/**
	 * 获得各图表数据
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @return
	 */
	public Map<Integer, Map<String, String>> getImagelist(Report simpleReport1);

	public Map<Integer, Map<String, String>> getImagelist(String nodeid, com.siteview.base.data.ReportDate simpleReport1);

	/**
	 * 生成监测器情况表
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createMonitorInfoGrid(List<Report> reports, int index, String title);

	public Panel createMonitorInfoGrid(String nodeids, ReportDate rd, int index, String title);

	/**
	 * 生成运行情况表
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createRuntableGrid(List<Report> reports, int index, String title);

	public Panel createRuntableGrid(String nodeids, ReportDate rd, int index, String title);

	/**
	 * 公用接口生成BufferedImage,其他生成报表的地方也会用到此接口
	 * 
	 * @param title
	 * @param subtite
	 * @param valuelabel
	 * @param data
	 * @param step
	 * @param max
	 * @param startdate
	 * @param enddate
	 * @param min
	 * @param xlabel
	 * @param pngwidth
	 * @param pngheight
	 *            comparetype对比方式,按天对比,按月对比,按周对比
	 * @return
	 */
	public BufferedImage getBufferedImage(String title, String subtite, String valuelabel, XYDataset data, double step, double max,
			Date startdate, Date enddate, double min, boolean xlabel, int pngwidth, int pngheight, String comparetype);

	/**
	 * 包装BufferedImage数据,使之适合ireport模板显示
	 * 
	 * @param bi
	 * @return
	 */
	public List createImageList(BufferedImage bi);

	/**
	 * 获得各图表数据
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @param enddate
	 *            增加结束时间
	 * @return
	 */
	public Map<Integer, HashMap<String, String>> getImagelist(Report simpleReport1, Date enddate);

	/**
	 * 根据KEY得到对应的资源
	 * 
	 * @param key
	 * @return
	 */
	public String getI18N(String key);
}
