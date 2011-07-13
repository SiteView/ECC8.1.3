/**
 * 
 */
package com.siteview.ecc.report.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.ReportManager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.report.Constand;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

/**
 *�������������ʵ���� �κ�����������ɲ��մ������ɱ���
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-2
 */
 

public class CreateReportImpl implements CreateReport {
	private final static Logger logger = Logger.getLogger(CreateReportImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.siteview.ecc.report.common.CreateReport#buildDataset(java.util.HashMap
	 * )
	 */
	@Override
	public XYDataset buildDataset(Map<String, Map<Date, String>> imgdatas) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		TimeSeries timeseries = null;
		Map<Date, String> imgdata = null;
		for (String name : imgdatas.keySet()) {
			if (name == null)
				continue;
			timeseries = new TimeSeries(name, org.jfree.data.time.Second.class);
			imgdata = imgdatas.get(name);
			for (Date date1 : imgdata.keySet()) {
				int ss = date1.getSeconds();
				int mm = date1.getMinutes();
				int hh = date1.getHours();
				int d = date1.getDate();
				int m = date1.getMonth() + 1;
				int y = date1.getYear() + 1900;

				org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m, y);
				String value = imgdata.get(date1);
				if (value.trim().startsWith("(status)")) {
					timeseries.add(ttime, null);
				} else {
					if (value.isEmpty()) {
						timeseries.add(ttime, null);
					} else {
							timeseries.add(ttime, Double.parseDouble(value));
					}
				}
			}
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.siteview.ecc.report.common.CreateReport#getReportData(java.lang.String
	 * [], com.siteview.base.manage.View, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Report> getReportData(String[] nodeids, String starttime, String endtime) {

		List<Report> rl = new ArrayList<Report>();
		View w = this.getReportView();
		try {
			if (starttime != null && endtime != null) {
				for (String nodeid : nodeids) {
					nodeid = nodeid.trim();
					if (nodeid == null || nodeid.equals("") || nodeid.equals(",")) {
					} else {
						INode n = w.getNode(nodeid);
						if (n != null) {
							Report simpleReport = ReportManager.getReport(n, new Date(starttime), new Date(endtime));
							rl.add(simpleReport);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			show(this.getClass().getName() + "��ʼ���������ݷ����쳣:" + ex.getMessage());
		}
		return rl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.siteview.ecc.report.common.CreateReport#getReportData(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
	@Override
	public List<Report> getReportData(String nodeid, String starttime, String endtime) {

		List<Report> rl = new ArrayList<Report>();
		View w = this.getReportView();
		try {
			if (starttime != null && endtime != null) {

				nodeid = nodeid.trim();
				if (nodeid == null || nodeid.equals("") || nodeid.equals(",")) {
				} else {
					INode n = w.getNode(nodeid);
					if (n != null) {
						Report simpleReport = ReportManager.getReport(n, new Date(starttime), new Date(endtime));
						rl.add(simpleReport);
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			show(this.getClass().getName() + "��ʼ���������ݷ����쳣:" + ex.getMessage());
		}
		return rl;
	}

	/**
	 * ��ø�ͼ������
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @param enddate
	 *            ���ӽ���ʱ��
	 * @return
	 */
	public Map<Integer, Map<String, String>> getImagelist(Report simpleReport1) {
		Map<Integer, Map<String, String>> listimage = new LinkedHashMap<Integer, Map<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(); i++) {
			HashMap<String, String> keyvalue = new HashMap<String, String>();
			String max = simpleReport1.getReturnValue("max", i);
			String min = simpleReport1.getReturnValue("min", i);
			String average = simpleReport1.getReturnValue("average", i);
			keyvalue.put("subtitle", "���ֵ" + max + "ƽ��ֵ" + average + "��Сֵ" + min);
			keyvalue.put("title", simpleReport1.getReturnValue("ReturnName", i));
			keyvalue.put("maxvalue", max);
			keyvalue.put("minvalue", min);
			/*
			 * if (enddate != null) keyvalue.put("maxdate",
			 * enddate.toLocaleString());
			 */
			listimage.put(i, keyvalue);
		}
		return listimage;
	}

	private String time=(String)Executions.getCurrent().getDesktop().getSession().getAttribute("STATETIMES");
	private MonitorEdit monitorEdit;
	public Map<Integer, Map<String, String>> getImagelist(String nodeid, com.siteview.base.data.ReportDate simpleReport1) {
		Map<Integer, Map<String, String>> listimage = new LinkedHashMap<Integer, Map<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(nodeid); i++) {
			HashMap<String, String> keyvalue = new HashMap<String, String>();
			String max = simpleReport1.getReturnValue(nodeid, "max", i);
			String min = simpleReport1.getReturnValue(nodeid, "min", i);
			String average = simpleReport1.getReturnValue(nodeid, "average", i);
			Map<String, String> detail = simpleReport1.getM_fmap().get("(Return_"+i+ ")" + nodeid);
			int fr = 0;
			if(detail.containsKey("detail")){
				String rdata = detail.get("detail");
				String[] s=rdata.split(",");
				if(s.length>2){
					String date1=s[0];
					String[] date11=date1.split("-");
					int year1=Integer.parseInt(date11[0].trim());
					int mouth1=Integer.parseInt(date11[1].trim());
					String[] day22=date11[2].split(":");
//					System.out.println(day22);
					String[] day222=day22[0].split(" ");
					int day1=Integer.parseInt(day222[0].trim());
					int hour1=Integer.parseInt(day222[1].trim());
					int min1=Integer.parseInt(day22[1].trim());
					String date2=s[1];
					String[] date21=date2.split("-");
					int year2=Integer.parseInt(date21[0].trim());
					int mouth2=Integer.parseInt(date21[1].trim());
					String[] day211=date21[2].split(":");
					String[] day2111=day211[0].split(" ");
					int day2=Integer.parseInt(day2111[0].trim());
					int hour2=Integer.parseInt(day2111[1].trim());
					int min2=Integer.parseInt(day211[1].trim());
//					System.out.println("year1 :" +year1+"year2 :" +year2+"mouth1 :" +mouth1+"mouth2 :" +mouth2+"day1 :" +day1+"day2 :" +day2+"hour1 :" +hour1+"hour2 :" +hour2+"min1 :" +min1+"min2 :" +min2);
					fr = (year1 - year2) * 365 * 24 * 60 + (mouth1 - mouth2)
							* 30 * 24 * 60 + (day1 - day2)  * 24 * 60
							+ (hour1 - hour2) * 60
							+ (min1 - min2);
				}
			}
			keyvalue.put("subtitle", "���ֵ" + max + "ƽ��ֵ" + average + "��Сֵ" + min +"\r\n"+"���Ƶ��:"+fr+"����  " +" ���ʱ��:"+time);
			String MonitorName=simpleReport1.getReturnValue(nodeid, "MonitorName", i);
			keyvalue.put("title", MonitorName+"["+simpleReport1.getReturnValue(nodeid, "ReturnName", i)+"]");
			keyvalue.put("ytitle",simpleReport1.getReturnValue(nodeid, "ReturnName", i));
			keyvalue.put("maxvalue", max);
			keyvalue.put("minvalue", min);
			listimage.put(i, keyvalue);
		}
//		Executions.getCurrent().getDesktop().getSession().removeAttribute("STATETIMES");
		return listimage;
	}

	private int Integer(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * ��ø�ͼ������
	 * 
	 * @param listimage
	 * @param simpleReport1
	 * @param enddate
	 *            ���ӽ���ʱ��
	 * @return
	 */
	public LinkedHashMap<Integer, HashMap<String, String>> getImagelist(Report simpleReport1, Date enddate) {
		LinkedHashMap<Integer, HashMap<String, String>> listimage = new LinkedHashMap<Integer, HashMap<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(); i++) {
			HashMap<String, String> keyvalue = new HashMap<String, String>();
			String max = simpleReport1.getReturnValue("max", i);
			String min = simpleReport1.getReturnValue("min", i);
			String average = simpleReport1.getReturnValue("average", i);
			keyvalue.put("subtitle", "���ֵ" + max + "ƽ��ֵ" + average + "��Сֵ" + min);
			keyvalue.put("title", simpleReport1.getReturnValue("ReturnName", i));
			keyvalue.put("maxvalue", max);
			keyvalue.put("minvalue", min);
			if (enddate != null)
				keyvalue.put("maxdate", Toolkit.getToolkit().formatDate(enddate));

			listimage.put(i, keyvalue);
		}
		return listimage;
	}
	public BufferedImage buildImageBuffer(String title, String subtite, String valuelabel, XYDataset data, double step, double max, Date startdate,
			Date enddate, double min, boolean xlabel, int pngwidth, int pngheight, String reporttype) {
		BufferedImage bi = this.getBufferedImage(title, subtite, valuelabel, data, step, max, startdate, enddate, min, xlabel,
				pngwidth, pngheight, reporttype);
		return bi;
	}

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
			Date startdate, Date enddate, double min, boolean xlabel, int pngwidth, int pngheight, String comparetype) {

		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", valuelabel, data, true, true, false);

		// ���ñ����ӱ���
		TextTitle texttitle = new TextTitle(title, new Font("����", Font.PLAIN, 18));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		try {
			TextTitle subtexttitle = new TextTitle(subtite, new Font("����", Font.PLAIN, 12));
			subtexttitle.setPaint(new Color(30, 91, 153));
			basechart.addSubtitle(subtexttitle);
		} catch (Exception e) {
		}
		TextTitle subtexttitle1 = new TextTitle("", new Font("����", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
		//
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setForegroundAlpha(1.0f);// �������͸����
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// �����������ɫ
		plot.setDomainGridlinePaint(Color.BLACK);// �����������ɫ
		plot.setDomainGridlinesVisible(true);// ��ʾ���������
		plot.setRangeGridlinesVisible(true);// ��ʾ���������
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// ��������ͼ��xy��ľ���
		// ������ɫ
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setSeriesPaint(1, Color.red);
		plot.getRenderer().setSeriesPaint(2, Color.yellow);
		plot.getRenderer().setSeriesPaint(3, Color.blue);
		plot.getRenderer().setSeriesPaint(4, Color.green);
		// plot.getRenderer().setBaseStroke(new BasicStroke(2));
		// �����ڱ߿�
		// plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// ������߿�
		basechart.setBorderVisible(true);
		// ���ڸ�ʽ����
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setVerticalTickLabels(true);// ���Ի������� falseΪ����
		// ͼ����ʾ���Ҳ�
		changeNotePosition(basechart);
		if (xlabel) {
			axis.setAutoTickUnitSelection(true);// ��Ҫ�Զ����ÿ̶�
			DateTickUnit dt = null;
			if (comparetype == null || comparetype.equals("")) {// �O�Ø˜ʵĕr�g�̶Ȇ�λ
				axis.setAutoTickUnitSelection(true);// �Զ����ÿ̶�
				axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());// �O�Ø˜ʵĕr�g�̶Ȇ�λ
			} else if (comparetype.equals(Constand.reporttype_dayreport)) {// ����Ա�
				// ���Ϊ��Сʱ
				dt = new DateTickUnit(DateTickUnit.HOUR, 6);
				axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
			} else if (comparetype.equals(Constand.reporttype_weekreport)) {// ���ܶԱ�
				// ���Ϊһ��,��������ʾ
				dt = new DateTickUnit(DateTickUnit.DAY, 1);
				axis.setDateFormatOverride(new SimpleDateFormat("E")); // ������
			} else if (comparetype.equals(Constand.reporttype_monthreport)) {// ���¶Ա�
				// ���Ϊһ��
				dt = new DateTickUnit(DateTickUnit.DAY, 1);
				axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
			} else if (comparetype.equals("fishview")) {// ������ͼ��
				// ���ΪһСʱ
				try {
					long seconds = (enddate.getTime() - startdate.getTime()) / 1000;
					long date = seconds / (24 * 60 * 60); // ��������
					long hour = (seconds - date * 24 * 60 * 60) / (60 * 60);// ����Сʱ��
					axis.setVerticalTickLabels(false);// ���Ի������� falseΪ����
					if (hour < 13 && date < 1) {
						dt = new DateTickUnit(DateTickUnit.HOUR, 1);
					} else if (hour >= 13 && hour < 24 && date < 1) {
						dt = new DateTickUnit(DateTickUnit.HOUR, 3);
					} else if (date >= 1 && date < 2) {
						dt = new DateTickUnit(DateTickUnit.HOUR, 4);
					} else if (date >= 2 && date < 7) {
						dt = new DateTickUnit(DateTickUnit.HOUR, 24);
					} else if (date >= 7) {
						axis.setVerticalTickLabels(true);// ���Ի������� falseΪ����
						dt = new DateTickUnit(DateTickUnit.HOUR, 24 * 3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				axis.setDateFormatOverride(new SimpleDateFormat("MM-dd HH:mm"));
			}
//			axis.setTickUnit(dt);

			axis.setLabelFont(new Font("����", Font.PLAIN, 10));
			axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);

			// axis.setMinorTickCount(10);
			if (enddate != null) {
				axis.setMaximumDate(enddate);
			}
			if (startdate != null) {
				axis.setMinimumDate(startdate);
			}

		} else {
			axis.setTickLabelsVisible(false);
		}
		// �������ߴ�ϸ
		axis.setAxisLineStroke(new BasicStroke(2.0f));
		axis.setAxisLinePaint(Color.BLACK);
		// ���ڻ���
		// axis.setAutoTickUnitSelection(true);
		// axis.setTickUnit(new DateTickUnit(1, 2, new
		// SimpleDateFormat("MM/dd")));
		// xis.setVerticalTickLabels(true);
		// �������ֵ����Сֵ
		ValueAxis valueaxis = plot.getRangeAxis();
		if (min == 0) {
			valueaxis.setRange(0, max);
		} else {
			valueaxis.setRange(min, max);
		}
		valueaxis.setAutoRange(false);
		// �������ߴ�ϸ
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// ���ÿ̶�
		if (max == 1 || step == 10) {// Y��
			valueaxis.setAutoTickUnitSelection(false);
			NumberTickUnit nt = new NumberTickUnit(step);
			((NumberAxis) valueaxis).setTickUnit(nt);
		}

		// ֵ������
		valueaxis.setLabelFont(new Font("����", Font.PLAIN, 12));
		// ������ʾ
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		BufferedImage bi = basechart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
		return bi;
	}

	private void changeNotePosition(JFreeChart chart) {
		// ����Legend��λ�� ͼ����λ��
		LegendTitle legendtitle = chart.getLegend();
		BlockContainer blockcontainer = new BlockContainer(new BorderArrangement());
		BlockContainer blockcontainer1 = legendtitle.getItemContainer();
		blockcontainer1.setPadding(1D, 1D, 1D, 1D);
		blockcontainer.add(blockcontainer1);
		blockcontainer1.setWidth(20);
		legendtitle.setWrapper(blockcontainer);
		legendtitle.setPosition(RectangleEdge.TOP);
		legendtitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		legendtitle.setVerticalAlignment(VerticalAlignment.CENTER);
		legendtitle.setMargin(0, 0, 0, 0);
		legendtitle.setWidth(20);
		// ʵ����ͼ��
		/*
		 * LegendTitle legendtitle = new LegendTitle(chart.getPlot());
		 * //����ͼƬ������ɫ legendtitle.setBackgroundPaint(Color.CYAN); //ʵ����ͼ����
		 * BlockContainer blockcontainer = new BlockContainer(new
		 * BorderArrangement()); //ͼ���ı߽��� blockcontainer.setBorder(1.0D, 1.0D,
		 * 1.0D, 1.0D); //�Զ���ͼ�����⣨��ǩ�� LabelBlock labelblock = new
		 * LabelBlock("test�˵�:", new Font("����", 1, 12)); //�Զ���߽����
		 * labelblock.setPadding(5D, 5D, 5D, 5D); //�Զ���ͼ����ǩ��λ�ò�ʹ֮װ���Զ���ͼ����
		 * blockcontainer.add(labelblock, RectangleEdge.TOP); //�Զ���ͼ�����⣨��ǩ��
		 * LabelBlock labelblock1 = new LabelBlock("by thomas 2008.12.19");
		 * //�Զ���߽���� labelblock1.setPadding(8D, 20D, 2D, 5D);
		 * //�Զ���ͼ����ǩ��λ�ò�ʹ֮װ���Զ���ͼ���� blockcontainer.add(labelblock1,
		 * RectangleEdge.BOTTOM); //���ԭͼ����ʵ����Ŀ�� BlockContainer blockcontainer1 =
		 * legendtitle.getItemContainer(); //�Զ���߽����
		 * blockcontainer1.setPadding(2D, 10D, 5D, 2D); //�Զ���ͼ����ǩ��λ�ò�ʹ֮װ���Զ���ͼ����
		 * blockcontainer.add(blockcontainer1); //����ͼ���߽�
		 * legendtitle.setWrapper(blockcontainer); //����ͼ����ͼƬ�е�λ��(��������)
		 * legendtitle.setPosition(RectangleEdge.RIGHT); //����ͼ����ͼƬ�е�λ��(������)
		 * legendtitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		 * 
		 * //ʹͼ��������Ч chart.addSubtitle(legendtitle); //���������е���Ҫ��ʽ���ö���Ч //
		 * plot.setRenderer(renderer);
		 */
	}

	/**
	 * �������������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createRuntableGrid(List<Report> reports, int index, String title) {
		Panel panel = new Panel();
		panel.setTitle(title);
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setParent(children);
		box.setWidth("100%");
		box.setRows(4);
		Listhead head = new Listhead();
		head.setSizable(true);
		head.setParent(box);
		// ���� ����(%) Σ��(%) ����(%) ��ֵ ����״̬
		Listheader column1 = new Listheader("����");
		column1.setSort("auto");
		 column1.setWidth("30%");
		column1.setParent(head);
		Listheader column2 = new Listheader("����(%)");
		column2.setSort("auto");
		column2.setParent(head);
		 column2.setWidth("10%");
		Listheader column3 = new Listheader("Σ��(%)");
		column3.setSort("auto");
		column3.setParent(head);
		 column3.setWidth("10%");
		Listheader column4 = new Listheader("����(%)");
		column4.setSort("auto");
		column4.setParent(head);
		 column4.setWidth("10%");
		Listheader column5 = new Listheader("��ֵ");
		column5.setSort("auto");
		column5.setParent(head);
		Listheader column6 = new Listheader("����״̬");
		column6.setSort("auto");
		// column6.setParent(head);
		for (Report report : reports) {
			int a = report.getReturnSize();
			for (int x = 0; x < a; x++) {
				Listitem item = new Listitem();
				item.setParent(box);
				Listcell cell1 = new Listcell();
				cell1.setParent(item);
				Listcell cell2 = new Listcell();
				cell2.setParent(item);
				Listcell cell3 = new Listcell();
				cell3.setParent(item);
				Listcell cell4 = new Listcell();
				cell4.setParent(item);
				Listcell cell5 = new Listcell();
				cell5.setParent(item);
				Listcell cell6 = new Listcell();
				cell6.setParent(item);

				new Label(report.getPropertyValue("MonitorName")).setParent(cell1);
				// report.display();

				new Label(report.getPropertyValue("okPercent")).setParent(cell2);
				new Label(report.getPropertyValue("warnPercent")).setParent(cell3);
				new Label(report.getPropertyValue("errorPercent")).setParent(cell4);
				new Label(report.getPropertyValue("errorCondition")).setParent(cell5);
				new Label(report.getPropertyValue("����״̬")).setParent(cell6);
			}

		}
		return panel;
	}

	/**
	 * �������������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createRuntableGrid(String nodeids, ReportDate rd, int index, String title) {
		Panel panel = new Panel();
		panel.setTitle(title);
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setParent(children);
		box.setWidth("100%");
		box.setMold("paging");
		box.setPageSize(4);
		// box.setRows(4);
		Listhead head = new Listhead();
		head.setSizable(true);
		head.setParent(box);
		// ���� ����(%) Σ��(%) ����(%) ��ֵ ����״̬
		Listheader column1 = new Listheader("����");
		 column1.setWidth("30%");
		 column1.setSort("auto");
		column1.setParent(head);
		Listheader column2 = new Listheader("����(%)");
		column2.setSort("auto");
		column2.setWidth("10%");
		column2.setParent(head);
		Listheader column3 = new Listheader("Σ��(%)");
		column3.setSort("auto");
		column3.setWidth("10%");
		column3.setParent(head);
		Listheader column4 = new Listheader("����(%)");
		column4.setSort("auto");
		column4.setWidth("10%");
		column4.setParent(head);
		Listheader column5 = new Listheader("��ֵ");
		column5.setSort("auto");
		column5.setParent(head);
		Listheader column6 = new Listheader("����״̬");
		column6.setSort("auto");
		// column6.setParent(head);
		String[] nodeid = nodeids.split(",");
		for (String id : nodeid) {
			//int a = rd.getReturnSize(id);
			//for (int x = 0; x < a; x++) {
				Listitem item = new Listitem();
				item.setParent(box);
				Listcell cell1 = new Listcell();
				cell1.setParent(item);
				Listcell cell2 = new Listcell();
				cell2.setParent(item);
				Listcell cell3 = new Listcell();
				cell3.setParent(item);
				Listcell cell4 = new Listcell();
				cell4.setParent(item);
				Listcell cell5 = new Listcell();
				cell5.setParent(item);
				Listcell cell6 = new Listcell();
				cell6.setParent(item);

				cell1.setLabel(rd.getPropertyValue(id, "MonitorName"));
				// report.display();

				cell2.setLabel(rd.getPropertyValue(id, "okPercent"));
				cell3.setLabel(rd.getPropertyValue(id, "warnPercent"));
				cell4.setLabel(rd.getPropertyValue(id, "errorPercent"));
				cell5.setLabel(rd.getPropertyValue(id, "errorCondition"));
				cell6.setLabel(rd.getPropertyValue(id, "����״̬"));
			//}

		}
		return panel;
	}

	/**
	 * ���ɼ���������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createMonitorInfoGrid(List<Report> reports, int index, String title) {
		Panel panel = new Panel();
		panel.setTitle(title);
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setParent(children);
		box.setWidth("100%");
		box.setRows(4);
		// ���� ����ֵ���� ���ֵ ��Сֵ ƽ��ֵ ���һ��
		Listhead head = new Listhead();
		head.setParent(box);
		head.setSizable(true);
		Listheader column0 = new Listheader("����");
		column0.setSort("auto");
		column0.setWidth("150px");
		column0.setParent(head);
		Listheader column1 = new Listheader("����ֵ����");
		column1.setSort("auto");
		column1.setWidth("100px");
		column1.setParent(head);
		Listheader column2 = new Listheader("���ֵ");
		column2.setSort("auto");
		column2.setWidth("100px");
		column2.setParent(head);
		Listheader column4 = new Listheader("��Сֵ");
		column4.setSort("auto");
		column4.setWidth("100px");
		column4.setParent(head);
		Listheader column3 = new Listheader("ƽ��ֵ");
		column3.setSort("auto");
		column3.setWidth("100px");
		column3.setParent(head);
		Listheader column5 = new Listheader("���һ��");
		column5.setSort("auto");
		column5.setWidth("100px");
		column5.setParent(head);

		for (Report report : reports) {
			int xx = report.getReturnSize();
			for (int d = 0; d < xx; d++) {
				Listitem item = new Listitem();
				item.setParent(box);
				Listcell cell0 = new Listcell();
				cell0.setParent(item);
				Listcell cell1 = new Listcell();
				cell1.setParent(item);
				Listcell cell2 = new Listcell();
				cell2.setParent(item);
				Listcell cell3 = new Listcell();
				cell3.setParent(item);
				Listcell cell4 = new Listcell();
				cell4.setParent(item);
				Listcell cell5 = new Listcell();
				cell5.setParent(item);
				new Label(report.getReturnValue("MonitorName", d)).setParent(cell0);
				new Label(report.getReturnValue("ReturnName", d)).setParent(cell1);
				new Label(report.getReturnValue("max", d)).setParent(cell2);
				new Label(report.getReturnValue("min", d)).setParent(cell3);
				new Label(report.getReturnValue("average", d)).setParent(cell4);
				new Label(report.getReturnValue("latest", d)).setParent(cell5);
			}
		}
		return panel;
	}

	/**
	 * ��װBufferedImage����,ʹ֮�ʺ�ireportģ����ʾ
	 * 
	 * @param bi
	 * @return
	 */
	public List createImageList(BufferedImage bi) {
		List imageList = new ArrayList();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = null;
		try {
			imOut = ImageIO.createImageOutputStream(bs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(bi, "GIF", imOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream istream = new ByteArrayInputStream(bs.toByteArray());
		imageList.add(istream);
		return imageList;
	}

	/**
	 * ���ɼ���������
	 * 
	 * @param reports
	 * @param index
	 * @param title
	 * @return
	 */
	public Panel createMonitorInfoGrid(String nodeids, ReportDate rd, int index, String title) {
		Panel panel = new Panel();
		panel.setTitle(title);
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setParent(children);
		box.setWidth("100%");
		// box.setRows(4);
		box.setMold("paging");
		box.setPageSize(4);
		// ���� ����ֵ���� ���ֵ ��Сֵ ƽ��ֵ ���һ��
		Listhead head = new Listhead();
		head.setParent(box);
		head.setSizable(true);
		Listheader column0 = new Listheader("����");
		column0.setSort("auto");
		column0.setWidth("150px");
		column0.setParent(head);
		Listheader column1 = new Listheader("����ֵ����");
		column1.setSort("auto");
		column1.setWidth("100px");
		column1.setParent(head);
		Listheader column2 = new Listheader("���ֵ");
		column2.setSort("auto");
		column2.setWidth("100px");
		column2.setParent(head);
		Listheader column4 = new Listheader("��Сֵ");
		column4.setSort("auto");
		column4.setWidth("100px");
		column4.setParent(head);
		Listheader column3 = new Listheader("ƽ��ֵ");
		column3.setSort("auto");
		column3.setWidth("100px");
		column3.setParent(head);
		Listheader column5 = new Listheader("���һ��");
		column5.setSort("auto");
		column5.setWidth("100px");
		column5.setParent(head);
		String[] nodeid = nodeids.split(",");
		for (String id : nodeid) {
			int xx = rd.getReturnSize(id);
			for (int d = 0; d < xx; d++) {
				Listitem item = new Listitem();
				item.setParent(box);
				Listcell cell0 = new Listcell();
				cell0.setParent(item);
				Listcell cell1 = new Listcell();
				cell1.setParent(item);
				Listcell cell2 = new Listcell();
				cell2.setParent(item);
				Listcell cell3 = new Listcell();
				cell3.setParent(item);
				Listcell cell4 = new Listcell();
				cell4.setParent(item);
				Listcell cell5 = new Listcell();
				cell5.setParent(item);
				cell0.setLabel(rd.getReturnValue(id, "MonitorName", d));
				cell1.setLabel(rd.getReturnValue(id, "ReturnName", d));
				cell2.setLabel(rd.getReturnValue(id, "max", d));
				cell3.setLabel(rd.getReturnValue(id, "min", d));
				cell4.setLabel(rd.getReturnValue(id, "average", d));
				cell5.setLabel(rd.getReturnValue(id, "latest", d));
			}
		}
		return panel;
	}

	public Map<String, Map<String, String>> getReportDataByNodeids(String nodeids, Date starttime, Date endtime) {
		ReportDate rd = new ReportDate(starttime, endtime);
		try {
			Map<String, Map<String, String>> fmap = rd.getReportDate(nodeids);
			return fmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡView
	 */
	public View getReportView() {
		View w = null;
		try {
			w = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		} catch (Exception e) {
			show("��ȡView���ݷ����쳣:" + e.getMessage());
		}
		return w;

	}

	private void show(Object m) {
		logger.info(m);
	}

	@Override
	public String getI18N(String key) {
		try {
			return UnivData.getResource(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
