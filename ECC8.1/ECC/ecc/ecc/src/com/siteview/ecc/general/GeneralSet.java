/**
 * 
 */
package com.siteview.ecc.general;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Div;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserEdit;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong基本设置，读写ini文件
 * 
 */
public class GeneralSet extends GenericAutowireComposer {
	
	private Checkbox 						ipCheckbox;
	private Textbox 						ip;
	private Button 							applyButton;
	private Button 							recoverButton;
	private Div 							setTuopuId;
	
	
	public void onInit() throws Exception {
		try{
			IniFile ini = new IniFile("general.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			String IPAddressValue = ini.getValue("IPCheck", "IPAddress");
			ip.setValue(IPAddressValue);
			String isCheckValue = ini.getValue("IPCheck", "isCheck");
			if ("1".equals(isCheckValue)){
				ipCheckbox.setChecked(true);
			}else{
				ipCheckbox.setChecked(false);
			}
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop().getSession());
			if(view.isAdmin()){
				setTuopuId.setVisible(true);
			}else{
				UserEdit userEdit = new UserEdit(view.getUserIni());
				if(userEdit.getModuleRight(UserRightId.TopoView)){//拓扑视图
					setTuopuId.setVisible(true);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onApply(Event event)throws Exception {
		try{
			String ipValue = ip.getValue().trim();
			String ipCheckboxValue = "0";
			if(ipCheckbox.isChecked()){
				ipCheckboxValue = "1";
			}
			
			if("".equals(ipValue)){
				Messagebox.show("IP地址为空!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			Pattern regex = Pattern.compile("^([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}(,([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})*$");
			Matcher matcher = regex.matcher(ipValue);
			if (!matcher.matches()) {
				Messagebox.show("IP地址格式不正确，多个IP地址请用\",\"隔开", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			IniFile ini = new IniFile("general.ini");
			try{
				ini.load();
			}catch(Exception e){}
			Map<String, Map<String, String>> map = ini.getFmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("IPCheck")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("IPCheck");
			}
			
			ini.setKeyValue("IPCheck", "IPAddress", ipValue);
			ini.setKeyValue("IPCheck", "isCheck", ipCheckboxValue);
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + "在" + OpObjectId.general_set.name + "中进行了"+OpTypeId.edit.name+""+OpObjectId.general_set.name+"操作";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.general_set);
			applyButton.setDisabled(true);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onRecover() throws Exception{
		onInit();
	}
}
