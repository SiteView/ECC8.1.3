package com.siteview.ecc.treeview.windows;

import java.util.Map;
import java.util.TreeMap;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;

public class EntityTest extends GenericForwardComposer
{
	Window		wentitytest;
	Vbox		parentcontainers;
	Button		btnclose;
	Label		lbfinish;
	
	String		error_message;
	INode		node;
	View		view;
	EntityInfo	info;
	Timer		time;
	Timer		timstop;
	
	boolean		willExit	= false;
	
	public EntityTest()
	{
		
	}
	
	/**
	 * 
	 */
	public void onCreate$wentitytest()
	{
		node = (INode) wentitytest.getAttribute("inode");
		view = (View) wentitytest.getAttribute("view");
		//
		node = view.getNode(node.getSvId());
		String name = node.getName();
		wentitytest.setTitle("≤‚ ‘…Ë±∏£∫" + name);
		info = view.getEntityInfo(node);
		try
		{
			info.TestDevice(view);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		time = new Timer();
		time.setParent(wentitytest);
		time.setDelay(1000);
		time.setRepeats(true);
		time.setRunning(false);
		time.addEventListener("onTimer", new ontimer());
		time.start();
		
		timstop = new Timer();
		timstop.setParent(wentitytest);
		timstop.setDelay(6000);
		timstop.setRepeats(false);
		timstop.setRunning(false);
		timstop.addEventListener("onTimer", new ontimerstope());
	}
	
	private class ontimerstope implements org.zkoss.zk.ui.event.EventListener
	{
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			try
			{
				time.stop();
				time.detach();
				time.setRunning(false);
				wentitytest.removeChild(time);
				wentitytest.detach();
			} catch (Exception e)
			{
			}
		}
	}
	
	private class ontimer implements org.zkoss.zk.ui.event.EventListener
	{
		
		String	error_message	= null;
		
		public ontimer()
		{
			
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			TreeMap<String, String> testdata = null;
			if (!willExit)
			{
				try
				{
					
					Map<String, String> data = info.getTestDeviceData(view);
					
					// {
					// lbfinish.setValue("≤‚ ‘ ß∞‹!");
					// timstop.start();
					// return;
					// }
					if (data != null)
					{
						testdata = new TreeMap<String, String>();
						for (String key : data.keySet())
						{
							testdata.put(key, data.get(key));
						}
					}
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					lbfinish.setValue("≤‚ ‘ ß∞‹!");
					willExit = true;
					timstop.start();
				}
				if (testdata != null)
				{
					for (String key : testdata.keySet())
					{
						Label lb = new Label();
						String da = String.format("%S=%S", testdata.get(key), key);
						lb.setValue(da);
						parentcontainers.appendChild(lb);
					}
					lbfinish.setValue("≤‚ ‘ÕÍ≥…...!");
					willExit = true;
					timstop.start();
				}
				
			}
		}
	}
	
	/**
	 * 
	 */
	public void onClick$btnclose()
	{
		try
		{
			willExit = true;
			time.stop();
			time.detach();
			time.setRunning(false);
			wentitytest.removeChild(time);
			wentitytest.detach();
		} catch (Exception e)
		{
			
		}
	}
	
}
