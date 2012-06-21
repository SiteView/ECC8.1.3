package com.siteview.ecc.controlpanel;

import java.util.List;

import com.siteview.ecc.timer.NodeInfoBean;
import com.siteview.ecc.treeview.EccTreeItem;

public class Toolkits {
	private int all;
	private int ok;
	private int warning;
	private int error;
	private int disabled;
	private int device;
	
	public NodeInfoBean refreshNodeInfoInList(EccTreeItem eccTreeItem){
		NodeInfoBean bean = new NodeInfoBean();
		all = 0;
		ok = 0;
		warning = 0;
		error = 0;
		disabled = 0;
		device = 0;

		getSubItemWithBean(eccTreeItem);
		bean.setAll(all);
		bean.setOk(ok);
		bean.setWarning(warning);
		bean.setError(error);
		bean.setDisabled(disabled);
		bean.setDevice(device);
		return bean;
	}
	
	/**
	 * 
	 * @param eccItem
	 * @param bean
	 * @return bean
	 */
	private void getSubItemWithBean(EccTreeItem eccItem){
		if(eccItem == null){
			return;
		}
		if("monitor".equals(eccItem.getType())){
			all++;
			
			if(EccTreeItem.STATUS_OK == eccItem.getStatus()){
				ok++;
			}else if(EccTreeItem.STATUS_WARNING == eccItem.getStatus()){
				warning++;
			}else if(EccTreeItem.STATUS_ERROR == eccItem.getStatus() || EccTreeItem.STATUS_BAD == eccItem.getStatus() || EccTreeItem.STATUS_NULL == eccItem.getStatus()){
				error++;
			}else if(EccTreeItem.STATUS_DISABLED == eccItem.getStatus()){
				disabled++;
			}
		}else{
			if(eccItem.getChildRen() != null && eccItem.getChildRen().size() > 0){
				List<EccTreeItem> list = eccItem.getChildRen();
				for(EccTreeItem _item : list){
					if("entity".equals(_item.getType())){
						device++;
					}
					this.getSubItemWithBean(_item);
				}
			}
		}
	}
}
