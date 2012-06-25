package com.siteview.ecc.treeview.windows;

import java.util.HashMap;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;

public class AddStepDetail extends GenericForwardComposer
{
	Window wStepDetail;
	Radio rdUrl,rdLinks,rdForms,rdFrames,rdRefreshs;
	Textbox tbUrl,tbPostDataParameter,tbContent,tbErrorContent,tbUser,tbPwd;
	Combobox cbLinks,cbForms,cbFrames,cbRefreshs;
	Button btnok,btncancel;
	
	String monitorid;
	Boolean isedit;
//	HashMap<String, String> sectionData;
	String url;
	String nIndex="0";
	private String	INI_FILE	= "_UrlStepInfo.ini";
	UrlTransBean bean=null;
	UrlTransStepData data=new UrlTransStepData();
	public AddStepDetail()
	{
		
	}
	
	public void onCheck$rdUrl()
	{
		setrds();
		rdUrl.setChecked(true);
	}
	public void onCheck$rdLinks()
	{
		setrds();
		rdLinks.setChecked(true);
	}
	public void onCheck$rdForms()
	{
		setrds();
		rdForms.setChecked(true);
	}
//	public void onClick$rdFrames()
//	{
//		setrds();
//		rdFrames.setChecked(true);
//	}
//	public void onClick$rdRefreshs()
//	{
//		setrds();
//		rdRefreshs.setChecked(true);
//	}
	private void setrds()
	{
		rdUrl.setChecked(false);
		rdLinks.setChecked(false);
		rdForms.setChecked(false);
//		rdFrames.setChecked(false);
//		rdRefreshs.setChecked(false);
	}
	public void onCreate$wStepDetail()
	{
		
		
		monitorid=(String)wStepDetail.getAttribute("monitorid");
		isedit=(Boolean)wStepDetail.getAttribute("isedit");
		nIndex=(String)wStepDetail.getAttribute("nIndex");
		neddpostParemeter=new HashMap<Integer, String>();
		if(isedit)
		{
		 setrds();
	
		try
		{
			bean = data.EditUrlStep(monitorid, nIndex);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String Steptype=bean.getSteptype();
		String stepname=bean.getStepname();
		String StepPostData=bean.getPostData();
		tbPostDataParameter.setValue(StepPostData);
		String StepContent=bean.getStepContent();
		tbContent.setValue(StepContent);
		String StepErrorContent=bean.getStepErrorContent();
		tbErrorContent.setValue(StepErrorContent);
		String StepUserName=bean.getStepUserName();
		tbUser.setValue(StepUserName);
		String StepUserPwd=bean.getStepUserPwd();
		tbPwd.setValue(StepUserPwd);
		String links=bean.getLinks();
		String forms=bean.getForms();
		buildLinks(cbLinks,links);
		buildLinks(cbForms, forms);
		 if (Steptype.equals("url"))
		 {
			 rdUrl.setChecked(true); 
			 tbUrl.setValue(stepname);
		 }
		 if (Steptype.equals("link"))
		 {
			 rdLinks.setChecked(true); 
			 setcbvalue(cbLinks,stepname);
		 }
		 if (Steptype.equals("form"))
		 {
			 rdForms.setChecked(true); 
			 setcbvalue(cbForms,stepname);
		 }
//		 if (Steptype.equals("frame"))
//		 {
//			 rdFrames.setChecked(true); 
//			 setcbvalue(cbFrames,stepname);
//		 }
//		 if (Steptype.equals("refresh"))
//		 {
//			 rdRefreshs.setChecked(true); 
//			 setcbvalue(cbRefreshs,stepname);
//		 }
		}
		else
		{
			url=(String)wStepDetail.getAttribute("url");
			tbUrl.setValue(url);
			
			try
			{
			bean=data.AddUrlStep(monitorid, nIndex);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void setcbvalue(Combobox cb,String s)
	{
		if (s==null ||s.isEmpty())
		{
			if (cb.getItemCount()>0)
				cb.setSelectedIndex(0);
			return;
		}
		for (int j = 0; j < cb.getItemCount(); j++)
		{
			if(cb.getItemAtIndex(j).getValue()!=null)
			if (cb.getItemAtIndex(j).getValue().equals(s))
			{
				
				cb.setSelectedIndex(j);
				break;
			}
		}
		if (cb.getSelectedIndex() == -1 && cb.getItemCount() > 0)
		{
			cb.setSelectedIndex(0);
		}
	}
	public void onClick$btnok()
	{
		try
		{
			
		
			String name="";
			String type="";
			if (rdUrl.isChecked())
			{
				name=tbUrl.getValue();
				if(name=="")
				{
				Messagebox.show("URL不能为空" , "提示", Messagebox.OK , Messagebox.EXCLAMATION);	
				return;	
				}
				type="url";
			}
			if(rdLinks.isChecked())
			{
				name=cbLinks.getValue();
				if(name=="")
				{
				Messagebox.show("请选择links" , "提示", Messagebox.OK , Messagebox.EXCLAMATION);	
				return;	
				}
				type="link";
			}
			if(rdForms.isChecked())
			{
				name=cbForms.getValue();
				if(name=="")
				{
				Messagebox.show("请选择Forms" , "提示", Messagebox.OK , Messagebox.EXCLAMATION);	
				return;	
				}
				type="form";
			}
//			if(rdFrames.isChecked())
//			{
//				name=cbFrames.getValue();
//				type="frame";
//			}
//			if(rdRefreshs.isChecked())
//			{
//				name=cbRefreshs.getValue();
//				type="refresh";
//			}
			String postdata=tbPostDataParameter.getValue();
			String StepContent=tbContent.getValue();
			String StepErrorContent=tbErrorContent.getValue();
		    String StepUserName=tbUser.getValue();
		    String StepUserPwd=tbPwd.getValue();
		    
		    bean.setStepname(name);
	    	bean.setSteptype(type);
	    	bean.setStepContent(StepContent);
	    	bean.setStepErrorContent(StepErrorContent);
	    	bean.setPostData(postdata);
	    	bean.setStepUserName(StepUserName);
	    	bean.setStepUserPwd(StepUserPwd);
	    	data.SaveUrlStep(monitorid, bean);
			try
			{
				wStepDetail.detach();
			}catch(Exception e)
			{}
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	private void buildLinks(Combobox cb,String links)
	{
		if (links==null||links.equals(""))
		{
			return;
		}
		cb.getItems().clear();
		for (String s: links.split("$"))
		{
			if(s==null ||s.equals(""))
			{
				continue;
			}
			Listitem item=new Listitem(s);
			cb.getItems().add(item);
		}
	}
	
    private HashMap<Integer, String> neddpostParemeter = null;
    private void buildPostData(String postData)
    {
    	if (postData==null||postData.equals("") )
		{
			return;
		}
    	int i=0;
    	for (String s: postData.split("$"))
		{
			if(s==null ||s.equals(""))
			{
				continue;
			}
			neddpostParemeter.put(i, s);
			i++;
		}
    }
    public void onChanging$cbForms()
    {
    	if(cbForms.getSelectedIndex()>-1)
    	{
    		int sel=cbForms.getSelectedIndex();
    		tbPostDataParameter.setValue(neddpostParemeter.get(sel));
    	}
    }

	public void onClick$btncancel()
	{
		try
		{
			wStepDetail.detach();
		}catch(Exception e)
		{}
	}
}
