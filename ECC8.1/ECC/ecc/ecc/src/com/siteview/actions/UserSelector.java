package com.siteview.actions;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class UserSelector extends Listbox 
{
	public void setUserid(String curUserid)
	{
		for(Object item:super.getItems())
		{
			if(((Listitem)item).getValue().equals(curUserid))
				((Listitem)item).setSelected(true);
			else
				((Listitem)item).setSelected(false);
		}
		
	}
	public void setUserList(String[] userid,String userName[])
	{
		if(this.getChildren().isEmpty()==false){
			List<Listitem> r = new ArrayList<Listitem>();
			for (Object o : this.getChildren()) {
				try {
					r.add((Listitem) o);
				} catch (Exception e) {}
			}
			for (Listitem w : r) {
				this.removeChild(w) ;
			}
		}
		for(int i=0;i<userid.length;i++)
		{
			Listitem item=new Listitem(userName[i],userid[i]);
			item.setParent(this);
		}
	}
}
