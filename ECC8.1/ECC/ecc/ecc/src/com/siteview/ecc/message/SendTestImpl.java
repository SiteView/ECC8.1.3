/**
 * 
 */
package com.siteview.ecc.message;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong ���ŷ��Ͳ���
 */
public class SendTestImpl extends GenericForwardComposer
{
	
	private Map<String, Map<String, String>>	m_fmap;
	private Textbox										phoneNum;		
	private Label										messageLabel;
	private Window										testMessageWin;
	
	public boolean smsTest(String strPhoneNumber, boolean bByWebSms) throws Exception
	{
		/**/
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SmsTest");
		ndata.put("phoneNumber", strPhoneNumber);
		ndata.put("ByWebSms", bByWebSms ? "true" : "false");
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		if (!retm.getRetbool())
			throw new Exception("Failed to load :" + retm.getEstr());
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		return retm.getRetbool();
		
	}
	
	public boolean smsTest(String strPhoneNumber, String strParameter, String strDllName) throws Exception
	{
		/*
		 * Helper.XfireCreateKeyValue("dowhat","SmsTestByDll"), Helper.XfireCreateKeyValue("phoneNumber",SmsTestByDll), Helper.XfireCreateKeyValue("parameter",strParameter),
		 * Helper.XfireCreateKeyValue("dllName",strDllName)
		 */
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SmsTestByDll");
		ndata.put("phoneNumber", strPhoneNumber);
		ndata.put("parameter", strParameter);
		ndata.put("dllName", strDllName);
		RetMapInMap retm = ManageSvapi.GetUnivData(ndata);
		if (!retm.getRetbool())
			throw new Exception("Failed to load :" + retm.getEstr());
		m_fmap = retm.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		return retm.getRetbool();
		
	}
	
	public void onSmsTest(Event event)throws Exception
	{
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String mobileValue = phoneNum.getValue().toString();			
		if ("".endsWith(mobileValue.trim())) {
			try{
				Messagebox.show("�ֻ����벻��Ϊ�գ�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			phoneNum.setValue(null);
			phoneNum.setFocus(true);
			return;
		}
		long mobileLong = 0;
		try{
			mobileLong = Long.parseLong(mobileValue);
			if(mobileLong > Long.parseLong("19999999999") ||mobileLong < Long.parseLong("10000000000")){//11λ	
				throw new Exception("");
			}
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show("�ֻ����벻��ȷ��", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			phoneNum.setFocus(true);
			return;
		}
		
		// ɾ���ļ�
		IniFile inif = new IniFile("smstestresult.ini");
		try
		{
			inif.load();
		}catch(Exception e){}
		try{
			inif.deleteSection("websms");
			inif.saveChange();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		IniFile inif2 = new IniFile("smstestresult.ini");
		try
		{
			inif2.load();
		}catch(Exception e){}
		try{	
			inif2.deleteSection("comsms");
			inif2.saveChange();
		} catch (Exception e)
		{
		}
		String temp1 = "";
		try
		{
			if ("web".equals(testMessageWin.getAttribute("flag")))
			{
				if (smsTest(mobileValue, true))
				{
					Thread.sleep(10000);//�ȴ� 10����
					temp1 = "";
					try
					{
						IniFile file = new IniFile("smstestresult.ini");
						file.load();
						temp1 = file.getSectionData("websms").get("result");
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					if (!"".equals(temp1) )
					{
						String[] templength = temp1.split("%");
						if (templength.length > 0)
						{
							if (templength[0].contains("ʧ��"))
							{
								messageLabel.setValue(templength[1]);
								messageLabel.setStyle("color:red");
								try
								{
									Messagebox.show(templength[1], "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
								} catch (Exception e)
								{
								}
							} else
							{
								messageLabel.setValue(templength[1]);//��ȡ�ļ��е���ʾ��Ϣ
								messageLabel.setStyle("color:green");
								String minfo = loginname + "��" + OpObjectId.sms_set.name + "�н����� web��ʽ���Ͷ���Ϣ ����";
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.confirm, OpObjectId.sms_set);
							}
						} else
						{
							messageLabel.setValue("���ŷ���ʧ��");
							messageLabel.setStyle("color:red");
						}
					}else
					{
						messageLabel.setValue("���ŷ���ʧ��");
						messageLabel.setStyle("color:red");
					}
					
				} else
				{
					messageLabel.setValue("���ŷ���ʧ��");
					messageLabel.setStyle("color:red");
				}
			} else if ("serial".equals(testMessageWin.getAttribute("flag")))
			{
				if (smsTest(mobileValue, false))
				{
					Thread.sleep(10000);//�ȴ� 10��
					temp1 = "";
					try
					{
					IniFile file = new IniFile("smstestresult.ini");
					file.load();
					temp1 = file.getSectionData("websms").get("result");
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					if (!"".equals(temp1))
					{
						String[] templength = temp1.split("%");
						if (templength.length > 0)
						{
							if ("ʧ��".equals(templength[0]))
							{
								messageLabel.setValue(templength[1]);
								messageLabel.setStyle("color:red");
								try
								{
									Messagebox.show(templength[1], "��ʾ", Messagebox.OK, Messagebox.EXCLAMATION);
								} catch (Exception e)
								{
								}
							} else
							{
								messageLabel.setValue(templength[1]);
								messageLabel.setStyle("color:green");
								String minfo = loginname + "��" + OpObjectId.sms_set.name + "�н����˴��ڷ�ʽ���Ͷ���Ϣ ����";
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.confirm, OpObjectId.sms_set);
							}
						} else
						{
							messageLabel.setValue("���ŷ���ʧ��");
							messageLabel.setStyle("color:red");
						}
					}else
					{
						messageLabel.setValue("���ŷ���ʧ��");
						messageLabel.setStyle("color:red");	
					}
					
				} else
				{
					messageLabel.setValue("���ŷ���ʧ��");
					messageLabel.setStyle("color:red");
				}
			} else if ("dll".equals(testMessageWin.getAttribute("flag")))
			{
				String phoneNumStr  = "" + phoneNum.getValue();
				String parameterStr = (String)testMessageWin.getAttribute("parameter");
				String dllNameStr   = (String) testMessageWin.getAttribute("dllName");
				boolean flag = false;
				try{
					flag = smsTest(phoneNumStr, parameterStr, dllNameStr);
				}catch(Exception e){
					e.printStackTrace();
				}
				if (flag)
				{
					messageLabel.setValue("���ŷ��ͳɹ�");
					messageLabel.setStyle("color:green");
					
					String minfo = loginname + " " + "��" + OpObjectId.sms_set.name + "�н����˶�̬�ⷽʽ���Ͷ���Ϣ ����";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.confirm, OpObjectId.sms_set);
				} else
				{
					messageLabel.setValue("���ŷ���ʧ��");
					messageLabel.setStyle("color:red");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
