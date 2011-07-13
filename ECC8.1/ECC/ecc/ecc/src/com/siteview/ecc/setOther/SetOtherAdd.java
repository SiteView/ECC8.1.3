package com.siteview.ecc.setOther;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.siteview.ecc.message.MessageConstant;
import com.siteview.ecc.setOther.bean.SetOtherBean;

public class SetOtherAdd extends GenericAutowireComposer {
	
	private static final long serialVersionUID = -1774501338303860063L;
	private Include eccBody;
	private static String syspassword = "/main/system/syspassword.zul";
	private Textbox sysName;
	private Textbox syspass;
	private Textbox resyspass;
	private SetOtherBean setOtherBean;
	boolean b = false;

	public void setSyspasswordvalue(SetOtherBean setOtherBean) {
		this.setOtherBean = setOtherBean;
	}

	public SetOtherBean getSyspasswordvalue() {
		return setOtherBean;
	}

	public Textbox getSysName() {
		return sysName;
	}

	public void setSysName(Textbox sysName) {
		this.sysName = sysName;
	}

	public Textbox getSyspass() {
		return syspass;
	}

	public void setSyspass(Textbox syspass) {
		this.syspass = syspass;
	}

	public Textbox getResyspass() {
		return resyspass;
	}

	public void setResyspass(Textbox resyspass) {
		this.resyspass = resyspass;
	}

	public void onSave(Event event) throws Exception {
		String sysNameValue = sysName.getValue().trim();
		String syspassValue = syspass.getValue().trim();
		Boolean bool = bool(sysNameValue);
		
					
	}

	private void editSave(String sysNameValue,String syspassValue,String resyspassValue) throws Exception{
//		BaseData retdata = null;
//		getSyspass().setValue(
//				this.getSyspasswordvalue().get);
//		getSysName().setValue(
//				this.getSyspasswordvalue().getSysName());
//		getResyspass().setValue(
//				this.getSyspasswordvalue().getSyspass());
//		if (sysNameValue.trim().equals(
//				this.getSyspasswordvalue().getSysName())
//				&& syspassValue.trim().equals(
//						this.getSyspasswordvalue().getSyspass())
//				&& resyspassValue.trim().equals(
//						this.getSyspasswordvalue().getSyspass())) {
//			Messagebox.show(Labels.getLabel("NotUpdate"), Labels.getLabel("point"), Messagebox.OK,
//					Messagebox.INFORMATION);
//		} else {
//			if (b) {
//				if (sysNameValue.trim() == null
//						|| "".equals(sysNameValue.trim())) {
//					Messagebox.show(Labels.getLabel(".InputServerPasswordName"), Labels.getLabel("point"),
//							Messagebox.OK, Messagebox.INFORMATION);
//				} else if (syspassValue.trim() == null
//						|| ""
//								.equals(syspassValue.trim().equals(
//										""))) {
//					Messagebox.show(Labels.getLabel(".ServerPassword"), Labels.getLabel("point"),
//							Messagebox.OK, Messagebox.INFORMATION);
//				} else {
//					retdata = CommunictionFactory
//							.getEccService()
//							.call(
//									"api_preferences",
//									"set_prefs",
//									new SvAtom("pwdManagement"),
//									new SvAtom(this
//											.getSyspasswordvalue()
//											.getId()),
//									new SvTuple(
//											new BaseData[] {
//													new SvAtom(
//															"pwdSet"),
//													new SvAtom(
//															this.getSyspasswordvalue().getId()),
//													new SvString(
//															sysNameValue),
//													new SvString(
//															syspassValue) }));
//				}
//				
//				Session session = this.session;
//				session.removeAttribute(MessageConstant.MessageEditSection);
//				session.setAttribute(MessageConstant.MessageEditSection, this.getSyspasswordvalue().getId());
//				
//				onReFresh();
//			}
//			b = true;
//		}
//	
	}

	public void onCreate(Event event) throws Exception {
//		if (b || syspasswordvalue != null) {
//			onSave(event);
//		}
	}

	public void onReFresh() {
		try {
			eccBody = (Include) (this.desktop.getPage("eccmain")
					.getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(syspassword);
		} catch (Exception e) {

		}
	}

	boolean bool(String sysNameValue) throws Exception {
		List<SetOtherBean> result = QuerySysPsssword();
		boolean bool = false;
		if (result.size() != 0) {
			for (SetOtherBean setotherbean : result) {
				SetOtherBean bean = (SetOtherBean) setotherbean;
				if (bean.getUrlName().equals(sysNameValue)) {
					bool = false;
					break;
				} else {
					bool = true;
				}
			}
		} else {
			bool = true;
		}
		return bool;
	}

	private List<SetOtherBean> QuerySysPsssword() throws Exception {
//		BaseData retdata = CommunictionFactory.getEccService().call(
//				"api_preferences", "get_all", new SvAtom("pwdManagement"));
		List<SetOtherBean> setotherbeanlist = new ArrayList<SetOtherBean>();
//		String id = null;
//		String name = null;
//		String password = null;
//		String other = null;
//		if (retdata instanceof SvTuple) {
//			BaseData[] retdatavalue = ((SvTuple) retdata).getValue();
//			if (retdatavalue[1] instanceof SvList) {
//				BaseData[] retdatavalueSvList = ((SvList) retdatavalue[1])
//						.getValue();
//				for (int j = 0; j < retdatavalueSvList.length; j++) {
//					if (retdatavalueSvList[j] instanceof SvTuple) {
//						BaseData[] retdatavalueSvListvalue = ((SvTuple) retdatavalueSvList[j])
//								.getValue();
//						if (retdatavalueSvListvalue[0] instanceof SvAtom) {
//							id = ((SvAtom) retdatavalueSvListvalue[0])
//									.getValue() == null ? ""
//									: ((SvAtom) retdatavalueSvListvalue[0])
//											.getValue();
//						}
//						if (retdatavalueSvListvalue[1] instanceof SvTuple) {
//							BaseData[] rettdatavalue = ((SvTuple) retdatavalueSvListvalue[1])
//									.getValue();
//							if (rettdatavalue[0] instanceof SvAtom) {
//								other = ((SvAtom) rettdatavalue[0]).getValue();
//							}
//							if (rettdatavalue[1] instanceof SvAtom) {
//								id = ((SvAtom) rettdatavalue[1]).getValue();
//							}
//							if (rettdatavalue[2] instanceof SvString) {
//								name = ((SvString) rettdatavalue[2]).getValue() == null ? ""
//										: ((SvString) rettdatavalue[2])
//												.getValue();
//							}
//							if (rettdatavalue[3] instanceof SvString) {
//								password = ((SvString) rettdatavalue[3])
//										.getValue() == null ? ""
//										: ((SvString) rettdatavalue[3])
//												.getValue();
//							}
//						}
//						((List<SysPasswordSet>) syspasbean)
//								.add(new SysPasswordSet(id, name, password));
//					}
//				}
//			}
//		}
		return setotherbeanlist;
	}

	
}
