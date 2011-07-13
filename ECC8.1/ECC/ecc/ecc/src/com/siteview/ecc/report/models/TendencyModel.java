package com.siteview.ecc.report.models;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.base.data.Report;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.ecc.report.beans.TendencyBean;
import com.siteview.ecc.report.beans.TendencyCheckDataBean;
import com.siteview.ecc.report.common.ChartUtil;

public class TendencyModel extends ListModelList implements ListitemRenderer {

	private Report report;
	
	public TendencyModel(Report report,String beanName){
		super();
		this.report=report;
		if(beanName.equals("TendencyCheckDataBean"))
			createTendencyCheckData();
		else
			createTendencyDate();
	}
	/**
	 * 运行情况报表数据
	 */
	public void createTendencyDate(){
		if(report==null)
			return;
		List<TendencyBean> beans = new ArrayList<TendencyBean>();
		TendencyBean bean = new TendencyBean();
		String newdate = report.getPropertyValue("latestStatus");
		if(newdate!=null){
			if(newdate.equals("ok")){
				bean.setNewDate("正常");
			}else if(newdate.equals("error")){
				bean.setNewDate("错误");
			}else
				bean.setNewDate("警告");
		}
		bean.setOk(report.getPropertyValue("okPercent"));
		bean.setName(report.getPropertyValue("MonitorName"));
		bean.setError(report.getPropertyValue("errorPercent"));
		bean.setWarn(report.getPropertyValue("warnPercent"));
		bean.setValue(report.getPropertyValue("errorCondition"));
		beans.add(bean);
		addAll(beans);
	}
	/**
	 * 监测数据统计表数据
	 */
	public void createTendencyCheckData(){
		if(report==null)
			return;
		List<TendencyCheckDataBean> beans = new ArrayList<TendencyCheckDataBean>();
		MonitorTemplate tmplate = ChartUtil.getView().getMonitorInfo(report.getM_node()).getMonitorTemplate();
		for(int i=0;i<report.getReturnSize();i++){
			TendencyCheckDataBean bean = new TendencyCheckDataBean();
			bean.setId(i);
			bean.setAverage(report.getReturnValue("average", i));
			bean.setLatest(report.getReturnValue("latest", i));
			bean.setMax(report.getReturnValue("max", i));
			bean.setMax_when(report.getReturnValue("when_max", i));
			bean.setMin(report.getReturnValue("min", i));
			bean.setName(report.getReturnValue("MonitorName", i));
			bean.setType(report.getReturnValue("ReturnName", i));
			String svdrawimage = report.getReturnValue("sv_drawimage", i);
			if (!tmplate.get_Return_Items().get(i).get("sv_type").equals("String"))
			{
				beans.add(bean);
			}
			
		}
		addAll(beans);
	}
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		Listitem item = arg0;
		if(arg1 instanceof TendencyBean){
			TendencyBean m = (TendencyBean) arg1;
	//		item.setId(m.getSection());
			String name = m.getName();
			Listcell c1 = new Listcell(name);
			c1.setTooltiptext(name);
			c1.setParent(item);
			String ok = m.getOk();
			Listcell c2 = new Listcell(ok);
			c2.setTooltiptext(ok);
			c2.setParent(item);
			
			String warn = m.getWarn();
			Listcell c3 = new Listcell(warn);
			c3.setTooltiptext(warn);
			c3.setParent(item);
			
			String error = m.getError();
			Listcell c4 = new Listcell(error);
			c4.setTooltiptext(error);
			c4.setParent(item);
			
			String newDate = m.getNewDate();
			Listcell c5 = new Listcell(newDate);
			c5.setTooltiptext(newDate);
			c5.setParent(item);
			
			String value = m.getValue();
			Listcell c6 = new Listcell(value);
			c6.setTooltiptext(value);
			c6.setParent(item);
		}else if(arg1 instanceof TendencyCheckDataBean){
			TendencyCheckDataBean m = (TendencyCheckDataBean) arg1;
			item.setValue(Integer.toString(m.getId()));
			
			String name = m.getName();
			Listcell c1 = new Listcell(name);
			c1.setTooltiptext(name);
			c1.setParent(item);
			
			String type = m.getType();
			Listcell c2 = new Listcell(type);
			c2.setTooltiptext(type);
			c2.setParent(item);
			
			String max = m.getMax();
			Listcell c3 = new Listcell(max);
			c3.setTooltiptext(max);
			c3.setParent(item);
			
			String min = m.getMin();
			Listcell c4 = new Listcell(min);
			c4.setTooltiptext(min);
			c4.setParent(item);
			
			String avg = m.getAverage();
			Listcell c5 = new Listcell(avg);
			c5.setTooltiptext(avg);
			c5.setParent(item);
			
			String latest = m.getLatest();
			Listcell c6 = new Listcell(latest);
			c6.setTooltiptext(latest);
			c6.setParent(item);
			
			String time = m.getMax_when();
			Listcell c7 = new Listcell(time);
			c7.setTooltiptext(time);
			c7.setParent(item);
		}
	}
}
