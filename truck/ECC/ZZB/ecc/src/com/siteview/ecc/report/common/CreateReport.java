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
 *���ɱ�������нӿ�
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-2
 */
 

public interface CreateReport {

	/**
	 * ��ȡ��������
	 * 
	 * @param nodeids
	 *            �������NODEID
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Report> getReportData(String[] nodeids, String starttime, String endtime);

	public Map<String, Map<String, String>> getReportDataByNodeids(String nodeids, Date starttime, Date endtime);

	/**
	 * ��ȡ��������
	 * 
	 * @param nodeids
	 *            �������NODEID
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Report> getReportData(String nodeid, String starttime, String endtime);

	public XYDataset buildDataset(Map<String, Map<Date, String>> imgdatas);

	/**
	 * ���ɵ���ͼ��
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
	 * ��ø�ͼ������
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @return
	 */
	public Map<Integer, Map<String, String>> getImagelist(Report simpleReport1);

	public Map<Integer, Map<String, String>> getImagelist(String nodeid, com.siteview.base.data.ReportDate simpleReport1);

	/**
	 * ���ɼ���������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createMonitorInfoGrid(List<Report> reports, int index, String title);

	public Panel createMonitorInfoGrid(String nodeids, ReportDate rd, int index, String title);

	/**
	 * �������������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createRuntableGrid(List<Report> reports, int index, String title);

	public Panel createRuntableGrid(String nodeids, ReportDate rd, int index, String title);

	/**
	 * ���ýӿ�����BufferedImage,�������ɱ���ĵط�Ҳ���õ��˽ӿ�
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
	 *            comparetype�Աȷ�ʽ,����Ա�,���¶Ա�,���ܶԱ�
	 * @return
	 */
	public BufferedImage getBufferedImage(String title, String subtite, String valuelabel, XYDataset data, double step, double max,
			Date startdate, Date enddate, double min, boolean xlabel, int pngwidth, int pngheight, String comparetype);

	/**
	 * ��װBufferedImage����,ʹ֮�ʺ�ireportģ����ʾ
	 * 
	 * @param bi
	 * @return
	 */
	public List createImageList(BufferedImage bi);

	/**
	 * ��ø�ͼ������
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @param enddate
	 *            ���ӽ���ʱ��
	 * @return
	 */
	public Map<Integer, HashMap<String, String>> getImagelist(Report simpleReport1, Date enddate);

	/**
	 * ����KEY�õ���Ӧ����Դ
	 * 
	 * @param key
	 * @return
	 */
	public String getI18N(String key);
}
