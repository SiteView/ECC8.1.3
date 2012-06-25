package com.siteview.ecc.controlpanel;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;

public class SetFilterDataMaxMin extends GenericForwardComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7929923769074861987L;
	private Spinner max;
	private Spinner min;
	
	private Window setcount;
	
	public void onCreate$setcount(Event event) throws Exception{
		Session session = Executions.getCurrent().getDesktop().getSession();
		Object imageMaxObj = session.getAttribute(FilterMaxMinConstant.ExitImageMax);
		Object imageMinObj = session.getAttribute(FilterMaxMinConstant.ExitImageMin);
		session.removeAttribute(FilterMaxMinConstant.ExitImageMax);
		session.removeAttribute(FilterMaxMinConstant.ExitImageMin);
		
		if(imageMaxObj != null && imageMinObj!=null){
			try{
				Double maxValue = Double.parseDouble(imageMaxObj.toString());
				Double minValue = Double.parseDouble(imageMinObj.toString());
				Integer maxInt = maxValue.intValue();
				Integer minInt = minValue.intValue();
				if(maxValue>maxInt){
					maxInt ++;//+1;//针对少数情况，小数点后mian的值会被省略 所以要 +1
				}
				max.setValue(maxInt);
				min.setValue(minInt);
			}catch(Exception e){
				e.printStackTrace();
				max.setValue(100);//默认的做法
				min.setValue(0);
			}

		}
//									<spinner id="max" style="margin-left:20px" step="1"
//		value="10" width="120px" constraint="min 0 max 1000000" />
		
	}
	
	public void onClick$okBtn(Event event) throws WrongValueException, Exception{

		Integer maxValue = max.getValue();
		Integer minValue = min.getValue();
		
		if(maxValue<=minValue){
			try{
				Messagebox.show("最大值不能     小于或等于     最小值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}catch(Exception e){}
		}
		
		Session session = Executions.getCurrent().getDesktop().getSession();
        session.setAttribute(FilterMaxMinConstant.FilterMax, maxValue);
        session.setAttribute(FilterMaxMinConstant.FilterMin, minValue);
		setcount.detach();
	}
}
