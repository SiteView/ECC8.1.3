/**
 * 
 */
package com.siteview.ecc.message;

import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author yuandong
 * ����ֵ����ҳ��
 */
public class ParamEdit extends GenericAutowireComposer {
	private Radio exist, notexist;
	private Textbox porcInfo;
	private Intbox porcLength;
	private Combobox  porcType;
	private Window parameditWin;
	private Groupbox paraGroupbox;

	public void onInit() throws Exception {
		if (((String) parameditWin.getAttribute("isExist")).equals("yes")) {
			paraGroupbox.setVisible(true);
			porcLength.setValue((Integer.valueOf((String) parameditWin
					.getAttribute("porcLength"))));
			porcType.setValue((String) parameditWin.getAttribute("porcType"));
			if(parameditWin.getAttribute("porcInfo")!=null)
			porcInfo.setValue((String) parameditWin.getAttribute("porcInfo"));
			exist.setChecked(true);
		} else {
			notexist.setChecked(true);
			paraGroupbox.setVisible(false);
		}
	}

	public void onApply() throws InterruptedException {
		
		Textbox returnValue = (Textbox) (parameditWin.getDesktop().getPage(
		"setDbPage").getFellow("setDbWin").getFellow("returnValue"));
		if (exist.isChecked()) {
			if(porcLength.getValue()==null||porcLength.getValue()<=0){
				Messagebox.show("�������Ȳ���ȷ��", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			else {
			
			returnValue.setValue("�������ͣ�" + porcType.getValue() + "��    �������ȣ�"
					+ porcLength.getValue() + "��    ����������" + porcInfo.getValue());

			Window win = (Window) (parameditWin.getDesktop().getPage(
					"setDbPage").getFellow("setDbWin"));
			win.setAttribute("isExist", exist.isChecked() ? "yes" : "no");
			win.setAttribute("porcLength", porcLength.getValue().toString());
			win.setAttribute("porcType", porcType.getValue());
			win.setAttribute("porcInfo", porcInfo.getValue());}
		}
		else
			returnValue.setValue("");//����ͨ�����ж��Ƿ��з���ֵ
		parameditWin.detach();
	}

}
