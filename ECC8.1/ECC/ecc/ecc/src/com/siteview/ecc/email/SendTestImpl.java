/**
 * 
 */
package com.siteview.ecc.email;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;

/**
 * @author
 */
public class SendTestImpl extends GenericForwardComposer{
	private Map<String, Map<String, String>>	m_fmap;

	private Textbox 									mailTo;
	private Textbox 									content;
    private Window 										testMailWin;
    private Label 										messageLabel;
	private Button 										testMailButton;
	
    public boolean emailTest(String strMailServer, String strMailTo, String strMailFrom, String strUser, 
            String strPassword, String strBackupServer, String strContent) throws Exception{     
    	/*Helper.XfireCreateKeyValue("dowhat","EmailTest"),
        Helper.XfireCreateKeyValue("mailServer",strMailServer),
        Helper.XfireCreateKeyValue("mailTo",strMailTo),
        Helper.XfireCreateKeyValue("mailFrom",strMailFrom),
        Helper.XfireCreateKeyValue("user",strUser),
        Helper.XfireCreateKeyValue("password",strPassword),
        Helper.XfireCreateKeyValue("subject",strSubject),
        Helper.XfireCreateKeyValue("content",strContent)*/
    		HashMap<String, String> ndata = new HashMap<String, String>();
    		ndata.put("dowhat","EmailTest");
    		ndata.put("mailServer",strMailServer);
    		ndata.put("mailTo",strMailTo);
    		ndata.put("mailFrom",strMailFrom);
    		ndata.put("user",strUser);
    		ndata.put("password",strPassword);
    		ndata.put("subject",strBackupServer);
    		ndata.put("content",strContent);
    		RetMapInMap retm= ManageSvapi.GetUnivData(ndata);

    		if(!retm.getRetbool())
    			throw new Exception("Failed to load :" + retm.getEstr());
    		m_fmap = retm.getFmap();
    		if(!m_fmap.containsKey("return") )
    			return false;
    		Map<String, String> data= m_fmap.get("return");
    		if(data==null)
    			return false;
    		if(!data.containsKey("value"))
    			return false;
    		return retm.getRetbool();

    			
      }
    public void onApply(Event event)throws Exception{
    	try {
    		messageLabel.setValue(null);
	    	Session mySession=this.session;
	    	String mailServer=(String) testMailWin.getAttribute("mailServer");
	    	String mailFrom=(String)testMailWin.getAttribute("mailFrom");
	    	String backupServer=(String) testMailWin.getAttribute("backupServer");
	    	String user=(String) testMailWin.getAttribute("user");
	    	String password=(String) testMailWin.getAttribute("password"); 
	    	String strmailTo=mailTo.getValue();
	    	Pattern regex = Pattern.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+(,\\w+(\\.\\w+)*@\\w+(\\.\\w+)+)*$");
			Matcher matcher = regex.matcher(strmailTo);
			
    		if(("").equals(mailTo.getValue().trim())){
    			Messagebox.show("Email地址不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
    			return;
	    	}else if(!matcher.matches()){
	    		Messagebox.show(strmailTo+" 非法的Email地址！", "提示", Messagebox.OK, Messagebox.INFORMATION);
	    		return;
	    	}
			this.emailTest(mailServer,mailTo.getValue(),mailFrom,user,password,backupServer,content.getValue());
			messageLabel.setValue(strmailTo+" 发送成功");	messageLabel.setStyle("color:green");		
		}catch (Exception e) {
			// TODO Auto-generated catch block
			messageLabel.setValue(this.mailTo.getValue().trim()+" 发送失败");messageLabel.setStyle("color:red");
			e.printStackTrace();
		}
    }

  }
    
    
    
