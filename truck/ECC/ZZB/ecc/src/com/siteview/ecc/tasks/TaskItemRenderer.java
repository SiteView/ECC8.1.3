/**
 * 
 */
package com.siteview.ecc.tasks;

/**
 * @author yuandong
 *
 */
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.ecc.alert.AbstractWindow;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.TooltipPopup;

public class TaskItemRenderer extends ListModelList implements ListitemRenderer {

	EventListener checkEditListener = null;
	HashMap<String, String> m = new HashMap<String, String>();

	public TaskItemRenderer(List table) {

		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		Task t = (Task) arg1;
		item.setId(t.getName());
		Listcell l = new Listcell(t.getName());

		l.setImage("/main/images/tasksmall.gif");
		
		item.addEventListener("onClick", new ItemClickListener(t));

		Toolbarbutton b = new Toolbarbutton();
		Listcell l1 = new Listcell();
		b.addEventListener("onClick", new EditClickListener(item));// 点击时触发popup事件
		b.setParent(l1);
		b.setImage("/main/images/alert/edit.gif");
		
		l.setParent(item);
		Listcell l2 = new Listcell(t.getDescription());
		if (!t.getDescription().equals(""))
			if("1".equals(t.getType())||"2".equals(t.getType())){
			l2.setImage("/main/images/application_view_detail.gif");
			}
		l2.setParent(item);
		l1.setParent(item);

	}

	class EditClickListener implements org.zkoss.zk.ui.event.EventListener {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private Listitem l;

		public EditClickListener(Listitem li) {
			l = li;

		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			TaskPack tp = new TaskPack();
			if (tp.findByName((String) l.getId()).getType().equals("1")) {
				final Window win = (Window) Executions.createComponents(
						"/main/setting/editAbTask.zul", null, null);
				win.setTitle("编辑绝对时间任务计划");
				win.setAttribute("itemId", l.getId());
				win.setAttribute("flag", "edit");
				win.doModal();
			} else if (tp.findByName((String) l.getId()).getType().equals("2")) {
				final Window win = (Window) Executions.createComponents(
						"/main/setting/editPerTask.zul", null, null);
				win.setTitle("编辑时间段任务计划");
				win.setAttribute("itemId", l.getId());
				win.setAttribute("flag", "edit");
				win.doModal();
			} else  if (tp.findByName((String) l.getId()).getType().equals("3")){
				final Window win = (Window) Executions.createComponents(
						"/main/setting/editRelativeTask.zul", null, null);
				win.setTitle("编辑相对任务计划");
				win.setAttribute("itemId", l.getId());
				win.setAttribute("flag", "edit");
				win.doModal();
			}
		}
	}

	class ItemClickListener implements org.zkoss.zk.ui.event.EventListener {
		// 响应对item的单击，popup
		private Task t = new Task();

		public ItemClickListener(Task task) {

			t = task;

		}

		// Description=, sv_name=gggggggggggggg, Type=1, start1=0:0, start0=0:0,
		// Allow1=1, Allow2=1, start6=0:0, Allow3=0, Allow4=1, start4=0:0,
		// Allow5=1, start5=0:0, Allow6=1, start2=0:0, start3=0:0, Allow0=1}
		@Override
		public void onEvent(Event event) throws Exception {

			Popup p = new Popup();
			if (t.getType().equals("3")) {

				
				
			}else{
			Listbox lb = new Listbox();
			lb.setFixedLayout(true);

			Listitem li0 = new Listitem();
			Listitem li1 = new Listitem();
			Listitem li2 = new Listitem();
			Listitem li3 = new Listitem();
			Listitem li4 = new Listitem();
			Listitem li5 = new Listitem();
			Listitem li6 = new Listitem();
			Listhead lh = new Listhead();
			String[] s = new String[2];

			new Listcell("周日").setParent(li0);
			new Listcell((t.getAllow0().equals("1") ? "允许" : "禁止"))
					.setParent(li0);

			s = t.getStart0().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li0);
			else
				new Listcell(s[1]).setParent(li0);

			new Listcell("周一").setParent(li1);
			new Listcell((t.getAllow1().equals("1") ? "允许" : "禁止"))
					.setParent(li1);
			s = t.getStart1().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li1);
			else
				new Listcell(s[1]).setParent(li1);

			new Listcell("周二").setParent(li2);
			new Listcell((t.getAllow2().equals("1") ? "允许" : "禁止"))
					.setParent(li2);
			s = t.getStart2().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li2);
			else
				new Listcell(s[1]).setParent(li2);

			new Listcell("周三").setParent(li3);
			new Listcell((t.getAllow3().equals("1") ? "允许" : "禁止"))
					.setParent(li3);
			s = t.getStart3().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li3);
			else
				new Listcell(s[1]).setParent(li3);

			new Listcell("周四").setParent(li4);
			new Listcell((t.getAllow4().equals("1") ? "允许" : "禁止"))
					.setParent(li4);
			s = t.getStart4().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li4);
			else
				new Listcell(s[1]).setParent(li4);

			new Listcell("周五").setParent(li5);
			new Listcell((t.getAllow5().equals("1") ? "允许" : "禁止"))
					.setParent(li5);
			s = t.getStart5().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li5);
			else
				new Listcell(s[1]).setParent(li5);

			new Listcell("周六").setParent(li6);
			new Listcell((t.getAllow6().equals("1") ? "允许" : "禁止"))
					.setParent(li6);
			s = t.getStart6().split(" ");
			if (s.length == 1)
				new Listcell(s[0]).setParent(li6);
			else
				new Listcell(s[1]).setParent(li6);

			Listheader lh1 = new Listheader();
			lh1.setLabel("计划");
			lh1.setAlign("left");
			Listheader lh2 = new Listheader();
			lh2.setLabel("状态");
			lh2.setAlign("left");
			Listheader lh3 = new Listheader();
			lh3.setLabel("开始");
			lh3.setAlign("left");
			lh1.setParent(lh);
			lh2.setParent(lh);
			lh3.setParent(lh);
			if (t.getType().equals("2")) {
				Listheader lh4 = new Listheader();
				s = t.getEnd0().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li0);
				else
					new Listcell(s[1]).setParent(li0);
				s = t.getEnd1().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li1);
				else
					new Listcell(s[1]).setParent(li1);
				s = t.getEnd2().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li2);
				else
					new Listcell(s[1]).setParent(li2);
				s = t.getEnd3().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li3);
				else
					new Listcell(s[1]).setParent(li3);
				s = t.getEnd4().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li4);
				else
					new Listcell(s[1]).setParent(li4);
				s = t.getEnd5().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li5);
				else
					new Listcell(s[1]).setParent(li5);
				s = t.getEnd6().split(" ");
				if (s.length == 1)
					new Listcell(s[0]).setParent(li6);
				else
					new Listcell(s[1]).setParent(li6);

				lh4.setLabel("结束");
				lh4.setAlign("left");
				lh4.setParent(lh);

			}

			lh.setSizable(true);
			lh.setParent(lb);
			li0.setParent(lb);
			li1.setParent(lb);
			li2.setParent(lb);
			li3.setParent(lb);
			li4.setParent(lb);
			li5.setParent(lb);
			li6.setParent(lb);
			lb.setParent(p);
			p.setWidth("220px");
			p.setPage(event.getPage());
			p.setStyle("border:none;color:#FFFFFF;background-color:#717171");
			p.open((Listcell) (event.getTarget()).getFirstChild()
					.getNextSibling());
			}
		}	
	}
}
