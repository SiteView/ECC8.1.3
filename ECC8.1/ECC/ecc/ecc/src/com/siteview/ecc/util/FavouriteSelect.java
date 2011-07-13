package com.siteview.ecc.util;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.controls.EccComboitem;

public class FavouriteSelect extends Combobox {

	public FavouriteSelect() {
		super();
		
		Comboitem item;
		item=new EccComboitem("��ʾȫ��״̬",EccTreeItem.STATUS_ALL,"/main/control/images/filter.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_ALL));
		super.setSelectedItem(item);
		
		item=new EccComboitem("�����Σ��", EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_WARNING,"/main/control/images/state_red_yellow.gif")	;
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_WARNING));
		
		item=new EccComboitem("ֻ��ʾ����", EccTreeItem.STATUS_ERROR,"/main/control/images/state_red.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_ERROR));
		
		item=new EccComboitem("ֻ��ʾΣ��", EccTreeItem.STATUS_WARNING,"/main/control/images/state_yellow.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_WARNING));
		
		item=new EccComboitem("ֻ��ʾ����", EccTreeItem.STATUS_OK,"/main/control/images/state_green.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_OK));
		
		item=new EccComboitem("ֻ��ʾ��ֹ", EccTreeItem.STATUS_DISABLED,"/main/control/images/state_stop.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_DISABLED));
		
		item=new EccComboitem("ֻ��ʾ������", EccTreeItem.STATUS_NULL,"/main/control/images/state_grey.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_NULL));
		
		item=new EccComboitem("ֻ��ʾ�������", EccTreeItem.STATUS_BAD,"/main/control/images/state_grey.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_BAD));

		item=new EccComboitem("ȫ������������", EccTreeItem.STATUS_BAD|EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_NULL,"/main/control/images/state_error_all.gif");
		item.setParent(this);
		item.setStyle(Toolkit.getToolkit().getStatusStyle(EccTreeItem.STATUS_BAD|EccTreeItem.STATUS_ERROR|EccTreeItem.STATUS_NULL));

	}

}
