package com.siteview.ecc.treeview;


import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


public class EditVirtualGroupName extends GenericForwardComposer
{	
	private Textbox     name;
	private Button		btnAddName;
	private Button		btnCancelAdd;
	private Window      winEditVirtualGroupName;
	
	public EditVirtualGroupName()
	{	
	}
	
	public void onClick$btnAddName()
	{
		try 
		{
			if("".equals(name.getValue().trim())){
				Messagebox.show("虚拟视图名称不能为空","提示",Messagebox.OK,Messagebox.INFORMATION);
				name.setFocus(true);
				return;
			}
			winEditVirtualGroupName.detach();		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onClick$btnCancelAdd()
	{
		try 
		{
			name.setValue("");
			winEditVirtualGroupName.detach();		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
