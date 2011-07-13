package com.siteview.ecc.report.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkmax.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.base.data.QueryInfo;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.ecc.reportserver.DirectoryZip;
import com.siteview.ecc.simplereport.ImageDataSource;
import com.siteview.ecc.tuopu.MakeTuopuData;
import com.siteview.ecc.util.Toolkit;

public class ChartUtil {
	private final static Logger logger = Logger.getLogger(ChartUtil.class);

	public static final String REPORTTYPE_DAYREPORT = "daymodel";// 日报
	public static final String REPORTTYPE_MONTHREPORT = "monthmodel";// 月报
	public static final String REPORTTYPE_WEEKREPORT = "weekmodel";// 周报

	public static View getView()
	{
		return Toolkit.getToolkit().getSvdbView(
				Executions.getCurrent().getDesktop().getSession());
	}
	
	public static void makelistData(Listbox listb, ListModelList model,ListitemRenderer rend)
	{
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}
	
	public static List<INode> getAllMonitorNode(View view){
		Map<String, Map<String, String>> idMap = null;
		List<INode> nodeList = new ArrayList<INode>();
		QueryInfo info = new QueryInfo();
		info.needkey = "sv_monitortype";
		info.setNeedType_monitor();
		try {
			idMap = info.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(idMap==null) return nodeList;
		for(String key : idMap.keySet()){
			INode node = view.getNode(key);
			if(node==null) continue;
			nodeList.add(node);
		}
		return nodeList;
	}
	
	public static String getAllMonitorId(View view){
		Map<String, Map<String, String>> idMap = null;
		StringBuilder sb = new StringBuilder();
		QueryInfo info = new QueryInfo();
		info.needkey = "sv_monitortype";
		info.setNeedType_monitor();
		try {
			idMap = info.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(idMap==null) return "";
		int i=0;
		for(String key : idMap.keySet()){
			INode node = view.getNode(key);
			if(node==null) continue;
			i++;
			sb.append(key);
		}
		logger.info("all monitor id count is : " + i);
		return sb.toString();
	}
	public static Listitem findItem(Listbox box ,String itemLabel){
		List<?> children = box.getItems();
		Listitem returnItem = null;
		for(Object obj : children){
			if(obj instanceof Listitem) {
				returnItem = (Listitem)obj;
				if(itemLabel.equals(returnItem.getLabel())) break;
			}
		}
		return returnItem;
	}
	public static Listitem getFirstListitem(Listbox box)
	{
		List<?> children = box.getItems();
		Listitem returnItem = null;
		for(Object obj : children){
			if(obj instanceof Listitem) {
				returnItem = (Listitem)obj;
				break;
			}
		}
		return returnItem;
	}
	
	public static Listitem getLastListitem(Listbox box)
	{
		List<?> children = box.getItems();
		for(int i=children.size()-1;i>=0;i--)
		{
			Object obj = children.get(i);
			if(obj instanceof Listitem)
				return (Listitem)obj;
		}
		return null;
	}
	public static void clearComponent(Component component)
	{
		if(component!=null && component.getChildren()!=null){
			component.getChildren().clear();
		}
	}
	
	public static void addListhead(Listbox box,String... title){
		Listhead listHead = new Listhead();
		for(String str : title){
			Listheader header = new Listheader();
			header.setSort("auto");
			header.setWidth((100 / title.length) + "%");
			header.setLabel(str);
			listHead.appendChild(header);
		}
		listHead.setSizable(true);
		box.appendChild(listHead);
	}
	
	public static Listitem addRow(Listbox box ,Object obj, Object...cols)
	{
		Listitem item = new Listitem();
		item.setValue(obj);
		for(Object col : cols){
			Listcell cell = new Listcell();
			if(col instanceof String){
				cell.setLabel(col.toString());
				cell.setTooltiptext(col.toString());
				cell.setParent(item);
			}else if(col instanceof Component){
				Component c = (Component)col;
				cell.appendChild(c);
				cell.setParent(item);
			}
		}
		item.setParent(box);
		return item;
	}
	
	public static void clearListbox(Listbox box)
	{
		if(box.getItems()!=null && box.getItems().size()>0)
			box.getItems().clear();
	}
	
	public static AMedia saveAsHtml(String reportFilePath, String fileName, Map<?, ?> paramter,JRDataSource ds) throws Exception 
	{
		JRHtmlExporter exporter = new JRHtmlExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath,paramter, ds);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporter.exportReport();
		AMedia media = new AMedia(fileName+".html", "html", "text/html", oStream.toByteArray());
		
		return media;
	}

	public static AMedia saveAsPdf(String reportFilePath, String fileName, Map<?, ?> paramter,JRDataSource ds) throws Exception
	{
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath,
				paramter, ds);
		JRExporter exporter = new JRPdfExporter();
		if (paramter != null)
			exporter.setParameters(paramter);
		exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				arrayOutputStream);
		exporter.exportReport();
		arrayOutputStream.close();
		return new AMedia(fileName+".pdf", "pdf", "application/pdf",
				arrayOutputStream.toByteArray());
	}
	
	public static AMedia saveAsXls(String reportFilePath, String fileName,Map<?, ?> paramter,JRDataSource ds) throws Exception
	{
		com.siteview.ecc.report.xls.JRXlsExporter exporter = new com.siteview.ecc.report.xls.JRXlsExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				reportFilePath, paramter, ds);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.exportReport();
		byte[] bytes = oStream.toByteArray();
		return new AMedia(fileName+".xls", "xls", "application/vnd.ms-excel",
				bytes);
	}
/**
 * 
 * @param reportFilePath
 * @param htmlPath
 * @param paramter
 * @param ds
 * @return
 */
	public static boolean saveAsHtml(String reportFilePath, String htmlPath,String htmlName,Map<?, ?> paramter,JRDataSource ds){
			try{
				File f = new File(htmlPath);
				if(!f.exists()) 
					f.mkdir();
				File tmpFile = new File(htmlPath+"report\\");
				if(!tmpFile.exists()) tmpFile.mkdir();
				String dest = htmlPath+"abc.zip";
				createHtmlFile(reportFilePath, tmpFile.getAbsolutePath()+File.separator+"monitor.html",paramter, ds);
				createZip(tmpFile.getAbsolutePath()+File.separator,dest);
				MakeTuopuData.delFolder(tmpFile.getAbsolutePath()+File.separator);
				File f2 = new File(dest);
				FileInputStream fis = new FileInputStream(f2);
				Filedownload.save(fis, "application/zip", htmlName+".zip");
			}catch(Exception e){
				return false;
			}
		return true;
	}
	public static void createHtmlFile(String reportFilePath, String htmlPath,Map<?, ?> paramter,JRDataSource ds){
		try {
			JasperPrint jasperPrint;
				jasperPrint = JasperFillManager.fillReport(reportFilePath,paramter, ds);
			JRExporter exporter = new JRHtmlExporter();
			if (paramter != null)	exporter.setParameters(paramter);
//			exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			 exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, htmlPath);
			 exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "GB2312");
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createZip(String strZipDirPath,String filename) {
		File f = new File(filename);
		if(f.exists()) f.delete();
		DirectoryZip zip = new DirectoryZip();
		try {
			zip.zip(strZipDirPath , filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Image getImage(String status) {
		if (status == null || status.equals("null")) 
			return new Image("/images/state_grey.gif");
		else if (status.equals("bad") || status.equals("error"))
			return new Image("/images/state_red.gif");
		else if (status.equals("ok"))
			return new Image("/images/state_green.gif");
		else if (status.equals("warning"))
			return new Image("/images/state_yellow.gif");
		else if(status.equals("disable"))
			return new Image("/images/state_stop.gif");
		return null;
	}
	
	public static void main(String[] args){
		createZip("d:\\abc","d:\\aaaaaaaaaaaaaaaaaaaa.zip");
	}
	
	public static boolean isShowReport(INode node,int index){
		MonitorTemplate tm =getView().getMonitorInfo(node).getMonitorTemplate();
		List<Map<String, String>> itms = tm.get_Return_Items();
		if(itms==null || index>itms.size()) return false;
		if(itms.get(index).get("sv_type").equals("String")) return false;
		return true;
	}
	
	/**
	 * 是不是将来时间
	 * @param date
	 * @return
	 */
	public static boolean isFutureTime(Date date){
		return new Date().before(date);
	}
	/**
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
	 * @throws IOException
	 */
	
	public static Image createBufferedImage(String title, String subtite,
			String valuelabel, XYDataset data, int step, double max,
			Date startdate, Date enddate, double min, boolean xlabel,
			int pngwidth, int pngheight, String comparetype) throws Exception {
		BufferedImage bi = ChartUtil.getBufferedImage(title, subtite, valuelabel, data, step, max, startdate, enddate, min, xlabel, pngwidth, pngheight, comparetype);
		return Toolkit.getToolkit().createImage(bi);
	}

	public static BufferedImage getBufferedImage(String title, String subtite,
			String valuelabel, XYDataset data, int step, double max,
			Date startdate, Date enddate, double min, boolean xlabel,
			int pngwidth, int pngheight, String comparetype) throws Exception {
		if (xlabel)
		{
			if (startdate == null) throw new Exception("开始时间未输入");
			if (enddate == null) throw new Exception("结束时间未输入");
			if (startdate.after(enddate))throw new Exception("开始时间后于结束时间");
		}
		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", valuelabel, data, true, true, false);
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title == null ? "" : title, new Font("黑体", Font.PLAIN, 12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体", Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(1, new Color(95, 165, 95));
		plot.getRenderer().setSeriesPaint(0, new Color(165, 95, 95));
		plot.getRenderer().setStroke(new BasicStroke(2.0f));
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		// 日期格式设置
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (xlabel)
		{
			axis.setDateFormatOverride(new SimpleDateFormat("MM/dd HH:mm"));
			axis.setLabelFont(new Font("黑体", Font.PLAIN, 10));
			axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			axis.setRange(startdate, enddate);
		//	if(enddate!=null) axis.setMaximumDate(enddate);
			//if(startdate!=null) axis.setMinimumDate(startdate);
			axis.setAutoTickUnitSelection(true);
			axis.setVerticalTickLabels(true);
			axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());// 設置標準的時間刻度單位
		} else
		{
			axis.setTickLabelsVisible(false);
		}
		// 设置轴线粗细
		axis.setAxisLineStroke(new BasicStroke(1.0f));
		axis.setAxisLinePaint(Color.BLACK);
		// 设置最大值和最小值
		ValueAxis valueaxis = plot.getRangeAxis();
		if (min == 0)
		{
			valueaxis.setRange(0, max*1.1);
			valueaxis.setLowerBound(0);
		} else
		{
			valueaxis.setRange(min*0.9, max*1.1);
			valueaxis.setLowerBound(min * 0.1);
		}
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		valueaxis.setTickLabelsVisible(true);
		valueaxis.setAutoTickUnitSelection(true);
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
	    XYItemRenderer localXYItemRenderer = plot.getRenderer();
	    if (localXYItemRenderer instanceof XYLineAndShapeRenderer)
	    {
	    	XYLineAndShapeRenderer rebderer = (XYLineAndShapeRenderer)localXYItemRenderer;
//	    	rebderer.setShapesVisible(true);
	    }
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = basechart.createBufferedImage(pngwidth, pngheight,
				BufferedImage.SCALE_FAST, info);
		return bi;
	}
	/**
	 * 时段对比报告专用，因为情况比较特殊
	 */
	public static Image createImage(String title, String subtite,
			String valuelabel, XYDataset data, double step, double max,
			Date startdate, Date enddate, double min, boolean xlabel,
			int pngwidth, int pngheight, String comparetype) throws IOException {
		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", valuelabel, data, true, true, false);
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title, new Font("黑体", Font.PLAIN, 12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体", Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
	
		//
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setStroke(new BasicStroke(2.0f));
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		// 日期格式设置
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (xlabel)
		{
			if(comparetype.equals(ChartUtil.REPORTTYPE_DAYREPORT))
			{
				DateTickUnit dt = new DateTickUnit(DateTickUnit.HOUR, 2);
				axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
				axis.setTickUnit(dt);
			}
			else if(comparetype.equals(ChartUtil.REPORTTYPE_WEEKREPORT))
			{
				DateTickUnit dt = new DateTickUnit(DateTickUnit.DAY,1);
				axis.setDateFormatOverride(new SimpleDateFormat("E"));
				axis.setTickUnit(dt);
			}
			else
			{
				DateTickUnit dt = new DateTickUnit(DateTickUnit.DAY,2);
				axis.setDateFormatOverride(new SimpleDateFormat("dd日"));
				axis.setTickUnit(dt);
			}
			axis.setLabelFont(new Font("宋体", Font.PLAIN, 10));
			axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			
			if(enddate!=null) axis.setMaximumDate(enddate);
			if(startdate!=null) axis.setMinimumDate(startdate);
			axis.setAutoTickUnitSelection(false);
			axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());// 設置標準的時間刻度單位
		} else
		{
			axis.setTickLabelsVisible(false);
		}
		// 设置轴线粗细
		axis.setAxisLineStroke(new BasicStroke(1.0f));
		axis.setAxisLinePaint(Color.BLACK);
		// 设置最大值和最小值
		ValueAxis valueaxis = plot.getRangeAxis();
		if (min == 0)
		{
			valueaxis.setRange(0, max);
		} else
		{
			valueaxis.setRange(min, max);
		}
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		valueaxis.setTickLabelsVisible(true);
		valueaxis.setAutoTickUnitSelection(true);
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = basechart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
		return Toolkit.getToolkit().createImage(bi);
	}
	
  public static Image create3DPieChart(String title,PieDataset data,int pngwidth,int pngheight){
    JFreeChart localJFreeChart = ChartFactory.createPieChart3D(title, data, true, true, false);
    PiePlot3D localPiePlot3D = (PiePlot3D)localJFreeChart.getPlot();
    localPiePlot3D.setStartAngle(290.0D);
    localPiePlot3D.setDirection(Rotation.CLOCKWISE);
    localPiePlot3D.setForegroundAlpha(0.5F);
    localPiePlot3D.setNoDataMessage("No data to display");
    localPiePlot3D.setSectionPaint(0,new Color(152,251,152));
    localPiePlot3D.setSectionPaint(1,new Color(238,221,130));
    localPiePlot3D.setSectionPaint(2,new Color(255, 62, 150));
    localPiePlot3D.setSectionPaint(3,new Color(139,0,0));
    localPiePlot3D.setSectionPaint(4,new Color(181,181,181));
    ChartRenderingInfo info = new ChartRenderingInfo();
	BufferedImage bi = localJFreeChart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
    return Toolkit.getToolkit().createImage(bi);
  }
}
