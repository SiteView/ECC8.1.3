package com.siteview.ecc.tuopu;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;

public class TuopListWindow extends Window {
	private static final long serialVersionUID = -1329117691177432923L;
	private static final Logger logger = Logger.getLogger(TuopListWindow.class);
	
	public Grid getTuopList(){
		return (Grid)BaseTools.getComponentById(this, "tuopList");
	}

	public Button getRefreshBtn(){
		return (Button)BaseTools.getComponentById(this, "btnRefresh");
	}
	public void onCreate(){
		Tree tree=(Tree)getDesktop().getPage("eccmain").getFellow("tree");
		EccTreeModel treeModel=(EccTreeModel)tree.getModel();
		View eccView=(View)treeModel.getView();
		EccTreeItem eccTreeItem = (EccTreeItem)tree.getSelectedItem().getValue();
		
		TuopulistModel tuopulistModel   = new SubTuolistModel(eccView, eccTreeItem);
		getTuopList().setModel(tuopulistModel);
		getTuopList().setRowRenderer(tuopulistModel);
		
		getRefreshBtn().addEventListener(Events.ON_CLICK, new refreshListener());
		init();
	}
	
	class refreshListener implements EventListener{
		@Override
		public void onEvent(Event arg0) throws Exception {
			refresh();
		}
		
	}
	private void refresh(){
		((SubTuolistModel)getTuopList().getModel()).refresh();		
		getTuopList().getPage().invalidate();
	}
	
	private void init(){
		Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setDelay(1*60*1000);
		timer.addEventListener(Events.ON_TIMER, new EventListener(){
			@Override
			public void onEvent(Event arg0) throws Exception {
				System.out.println("¿ªÊ¼Ë¢ÐÂÍØÆËÍ¼¡£¡£¡£¡£¡£");
				logger.info("refresh toplist!!!!!");
				refresh();
			}
		});
		this.appendChild(timer);
	}
}
