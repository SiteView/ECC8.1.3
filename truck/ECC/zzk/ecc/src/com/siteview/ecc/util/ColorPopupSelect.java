package com.siteview.ecc.util;

import java.awt.Color;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ColorPopupSelect extends Popup
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private Label				titleLbl			= new Label();
	private Listbox				listbox				= new Listbox();
	private boolean				displayTitle		= true;
	private Textbox              tb;
	private Toolbarbutton btn;
	public ColorPopupSelect()
	{
		super();
		super.setWidth("100px");
	}
	
	public void onCreate()
	{   
		Panel p=new Panel();
		p.setTitle("ÑÕÉ«Ñ¡Ôñ");
		p.setParent(this);
		Panelchildren pc=new Panelchildren();
		pc.setParent(p);
		pc.setStyle("margin:5px 5px 5px 5px;");
		getListbox();
		this.listbox.setStyle("border-style:none;");
		this.listbox.setParent(pc);
		
	}
	class onclick implements EventListener
	{
		Popup p;
		public onclick(Popup p)
		{
			this.p=p;
		}

		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			Listcell cell=(Listcell)arg0.getTarget();
			String cl=cell.getValue()+"";
			tb=(Textbox)p.getAttribute("ctx");
			btn=(Toolbarbutton)p.getAttribute("btn");
			btn.setStyle("color:Black;background:" +cl);
			tb.setValue(cl);
			this.p.close();
			
		}
		
	}
	private void getListbox()
	{
		this.listbox.getChildren().clear();
		Listhead tm = new Listhead();
		tm.setParent(this.listbox);
		Listheader ld1 = null;
		Listitem item = null;
		Listcell lc = null;
		ld1 = new Listheader();
		ld1.setParent(tm);
		ld1 = new Listheader();
		ld1.setParent(tm);
		ld1 = new Listheader();
		ld1.setParent(tm);
		ld1 = new Listheader();
		ld1.setParent(tm);
		for (int i = 0; i < 10; i++)
		{
			item = new Listitem();
			item.setParent(this.listbox);
			if (i == 0)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0,0,0)");
				lc.setValue("RGB(0,0,0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153,0,0)");
				lc.setValue("RGB(153,0,0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 204, 102)");
				lc.setValue("RGB(204, 204, 102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 153, 0)");
				lc.setValue("RGB(0, 153, 0)");
				lc.addEventListener("onClick", new onclick(this));
			}
			if (i == 1)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204,204,204)");
				lc.setValue("RGB(204,204,204)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153,0,51)");
				lc.setValue("RGB(153,0,51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 255, 102)");
				lc.setValue("RGB(204, 255, 102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 255, 102)");
				lc.setValue("RGB(0, 255, 102)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 2)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 255, 255)");
				lc.setValue("RGB(255, 255, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153,102,51)");
				lc.setValue("RGB(153,102,51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255,204,102)");
				lc.setValue("RGB(255,204,102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153, 255, 51)");
				lc.setValue("RGB(153, 255, 51)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 3)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 0, 0)");
				lc.setValue("RGB(255, 0, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 0, 0)");
				lc.setValue("RGB(204, 0, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 255, 51)");
				lc.setValue("RGB(255, 255, 51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153, 255, 204)");
				lc.setValue("RGB(153, 255, 204)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 4)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 255, 0)");
				lc.setValue("RGB(0, 255, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 51, 51)");
				lc.setValue("RGB(204, 51, 51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 255, 153)");
				lc.setValue("RGB(255, 255, 153)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153, 255, 204)");
				lc.setValue("RGB(153, 255, 204)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 5)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 0, 255)");
				lc.setValue("RGB(0, 0, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 102, 102)");
				lc.setValue("RGB(204, 102, 102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 51, 255)");
				lc.setValue("RGB(0, 51, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(153, 255, 153)");
				lc.setValue("RGB(153, 255, 153)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 6)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 255, 0)");
				lc.setValue("RGB(255, 255, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 0, 51)");
				lc.setValue("RGB(255, 0, 51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0, 204, 255)");
				lc.setValue("RGB(0, 204, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(102, 102, 51)");
				lc.setValue("RGB(102, 102, 51)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 7)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(0,255, 255)");
				lc.setValue("RGB(0,255, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 102, 51)");
				lc.setValue("RGB(255, 102, 51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(102, 51, 255)");
				lc.setValue("RGB(102, 51, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 204, 51)");
				lc.setValue("RGB(204, 204, 51)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 8)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255,153, 0)");
				lc.setValue("RGB(255,153, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 153, 102)");
				lc.setValue("RGB(255, 153, 102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(102, 102, 0)");
				lc.setValue("RGB(102, 102, 0)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 204, 153)");
				lc.setValue("RGB(204, 204, 153)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			if (i == 9)
			{
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255,0, 255)");
				lc.setValue("RGB(255,0, 255)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(255, 204, 102)");
				lc.setValue("RGB(255, 204, 102)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(102, 0, 51)");
				lc.setValue("RGB(102, 0, 51)");
				lc.addEventListener("onClick", new onclick(this));
				lc = new Listcell();
				lc.setParent(item);
				lc.setStyle("border-style:solid;border-width:1px;color:Black;background:RGB(204, 151, 255)");
				lc.setValue("RGB(204, 151, 255)");
				lc.addEventListener("onClick", new onclick(this));
			}
			
			
		}
		
	}
	
}
