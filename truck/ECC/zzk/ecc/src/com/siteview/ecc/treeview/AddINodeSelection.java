package com.siteview.ecc.treeview;


import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


public class AddINodeSelection extends GenericForwardComposer
{	
	private Radio		justAddSelf;
	private Radio		AddWithAllSubMonitor;
	private Radio		AddWithConstruction;
	
	private Textbox     name;
	
	private Button		btnAdd;
	private Button		btnCancel;
	private Window      winAddINodeSelection;
	
	public AddINodeSelection()
	{	
	}
	
	public void onClick$btnAdd()
	{
		try 
		{
			name.setValue("");
			if( AddWithConstruction.isSelected() )
				name.setValue("3");			
			if( AddWithAllSubMonitor.isSelected() )
				name.setValue("2");
			if( justAddSelf.isSelected() )
				name.setValue("1");
			
			winAddINodeSelection.detach();		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onClick$btnCancel()
	{
		try 
		{
			name.setValue("");
			winAddINodeSelection.detach();		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
