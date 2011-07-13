package com.siteview.ecc.report.top10;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.top10.type.IComponent;
import com.siteview.ecc.util.Message;

public class DisplayComponent extends HtmlMacroComponent{
	private static final long serialVersionUID = 6678004561319768402L;

	public void onCreate() throws Exception {
		refresh();
	}
	
	public void refresh()throws Exception{
		try {
			this.getListbox().getItems().clear();
			String impl = (String)this.getDynamicProperty("impl");
			if (impl == null){
				impl = "com.siteview.ecc.report.top10.TopTenTestImpl";
			}
			Class<?> classz = Class.forName(impl);
			Object instance = classz.newInstance();
			if (instance instanceof TopTen){
				TopTen top10 = (TopTen)instance;
				this.getCaption().setLabel(top10.getCaption());
				if (this.getCaption().getLabel() == null) this.getCaption().setLabel("No title");
				Listhead head = getListhead();
				Listheader header = new Listheader();
				header.setLabel("ÐòºÅ");
				header.setParent(head);
				for (String title : top10.getTitles()){
					header = new Listheader();
					header.setLabel(title);
					header.setParent(head);
				}
				int num = 0;
				for (Map<String,IComponent> map : top10.getData()){
					List<Component> list = new LinkedList<Component>();
					num ++;
					list.add(new Label(String.valueOf(num)));
					for (String title : top10.getTitles()){
						IComponent comp = map.get(title);
						list.add(comp.getComponent());
					}
					setRow(this.getListbox(),"",list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message.showError(e.getMessage());
		}
	}
	public static Listitem setRow(Listbox list,Object value,List<Component> cols)
	{
		Listitem item = new Listitem();
		item.setValue(value);
		for (Object col : cols){
			Listcell cell = new Listcell();
			if (col instanceof Component){
				((Component)col).setParent(cell);
			}
			item.appendChild(cell);
		}
		list.appendChild(item);
		return item;
	}
	
	public Caption getCaption() {
		return (Caption) BaseTools.getComponentById(this, "caption");
	}

	public Listhead getListhead() {
		return (Listhead) BaseTools.getComponentById(this, "listhead");
	}
	public Listbox getListbox() {
		return (Listbox) BaseTools.getComponentById(this, "listbox");
	}

}