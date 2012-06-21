package com.siteview.ecc.report.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.siteview.base.data.Report;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;

public class TendencyDataReportModel{
	private final static Logger logger = Logger.getLogger(TendencyDataReportModel.class);
	private static final long serialVersionUID = -7218141986436220002L;
	private INode node;
	private Report report;

	public INode getNode() {
		return node;
	}

	public void setNode(INode node) {
		this.node = node;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	private String flag;// 显示的状态

	public TendencyDataReportModel(Report report, String flag) {
		this.report = report;
		this.flag = flag;
	}

	public TendencyDataReportModel(INode node, Report report, String flag) {
		this(report, flag);
		this.node = node;
	}

	public List<String> generateListheadData() {
		List<String> headers = new ArrayList<String>();
		MonitorTemplate tmplate = ChartUtil.getView().getMonitorInfo(node)
				.getMonitorTemplate();
		List<Map<String, String>> rtItem = tmplate.get_Return_Items();
		for (Map<String, String> itemKey : rtItem) {
//			if (!itemKey.get("sv_type").equals("String")) {
				headers.add(itemKey.get("sv_label"));
//			}
		}
		return headers;
	}

	public Listbox generateListbox() {
		Listbox box = new Listbox();
		box.setFixedLayout(true);
		box.setVflex(true);
		box.setStyle("border:none;overflow-y:auto;overflow-x:auto");
		box.setMold("paging");
		box.setPageSize(15);
		View v =  ChartUtil.getView();
		MonitorInfo info =v.getMonitorInfo(node);
		MonitorTemplate tmplate = info.getMonitorTemplate();
		List<Map<String, String>> rtItem = tmplate.get_Return_Items();
		Listhead head = new Listhead();
		head.setSizable(true);
		addListheader(head, "时间", "100px");
		for (Map<String, String> itemKey : rtItem) {
//			if (!itemKey.get("sv_type").equals("String")) {
				addListheader(head, itemKey.get("sv_label"), "100px");
//			}
		}
		box.appendChild(head);
		Map<Date, DstrItem> res = report.getDstr();
		for (Date date : res.keySet()) {
			if (res.get(date).status.equals(this.flag)
					|| this.flag.equals("all")) {
				Listitem item = new Listitem();
				addListcell(item, Toolkit.getToolkit().formatDate(date));
				for(String key : generateListheadData()){
					addListcell(item, getValue(res.get(date).value, key));
					
				}
				box.appendChild(item);
			}
		}
		return box;
	}

	//	
	
	public InputStream writeDataToXls() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		WritableWorkbook book = Workbook.createWorkbook(baos);
		WritableSheet sheet = book.createSheet("Sheet_1", 0);
		List<String> head = generateListheadData();
		jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(	wfc);
		jxl.write.WritableFont wfc1 = new jxl.write.WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.GREEN);
		jxl.write.WritableCellFormat wcfFC1 = new jxl.write.WritableCellFormat(wfc1);
		jxl.write.WritableFont wfc2 = new jxl.write.WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);
		jxl.write.WritableCellFormat wcfFC2 = new jxl.write.WritableCellFormat(wfc2);
		
		sheet.setColumnView(0, 25);
		sheet.addCell(new Label(0, 0, "时间", wcfFC));
		for (int i = 0; i < head.size(); i++) {
			sheet.setColumnView(i, 25);
			Label label = new Label(i+1, 0, head.get(i), wcfFC);
			sheet.addCell(label);
		}
		Map<Date, DstrItem> res = report.getDstr();
		int row=1;
		for(Date date : res.keySet()){
			if ((row+1) % 2 == 0) {
				Label timelabel = new Label(0, row, Toolkit.getToolkit().formatDate(date),wcfFC2);
				sheet.addCell(timelabel);
			}else{
				Label label = new Label(0, row, Toolkit.getToolkit().formatDate(date),wcfFC1);
				sheet.addCell(label);
			
			}
			for(int i=0;i<head.size();i++){
				if ((row+1) % 2 == 0) {
						Label label = new Label(i+1, row, getValue(res.get(date).value,head.get(i)),wcfFC2);
						sheet.addCell(label);
				}else{
						Label label = new Label(i+1, row, getValue(res.get(date).value,head.get(i)),wcfFC1);
						sheet.addCell(label);
				}
			}
			row++;
		}
		book.write();
		book.close();
		baos.close();
		byte[] data = baos.toByteArray();
		return new ByteArrayInputStream(baos.toByteArray());
	}


	private void addListheader(Listhead head, String headerTitle, String width) {
		head.setSizable(true);
		Listheader header = new Listheader(headerTitle);
		header.setWidth(width);
		header.setSort("auto");
		header.setAlign("center");
//		header.setWidth("20%");
		head.appendChild(header);
	}

	private void addListcell(Listitem item, String cellLabel) {
		Listcell cell = new Listcell(cellLabel);
		cell.setTooltiptext(cellLabel);
		item.appendChild(cell);
	}

	private String trimLastQuota(String src) {
		if (src.endsWith("."))
			return src.substring(0, src.lastIndexOf("."));
		return src;
	}

	public String getValue(String destStr, String key) {
		if (destStr.contains(key + "=")) {
			int index = destStr.indexOf(key) + key.length();
			int endIndex = destStr.indexOf(",", index);
			if (endIndex > 0)
				return destStr.substring(index + 1, endIndex);
			else
				return trimLastQuota(destStr.substring(index + 1));
		}
		return "";
	}

	public static void main(String[] args) {
		String str = "CPU综合使用率(%)=11,CPU详细使用率(%)=CPU0:11.";
		String dest = "CPU详细使用率(%)";
		if (str.contains(dest + "=")) {
			int index = str.indexOf(dest) + dest.length();
			int endIndex = str.indexOf(",", index);
			if (endIndex > 0)
				logger.info(str.substring(index + 1, endIndex));
			else
				logger.info(str.substring(index + 1));
		}
	}
}
