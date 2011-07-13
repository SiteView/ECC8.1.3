package com.siteview.ecc.userRole;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.AbstractCombobox;
import com.siteview.ecc.util.Toolkit;

public class RuleaddressOption extends AbstractCombobox{

	@Override
	public Map<String, String> getSelectArray() {
		// TODO Auto-generated method stub
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			View view = Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
			List<VirtualView> viewAll = view.getAllVirtualView();
			Map<String, INode> map2=view.m_profile_data.m_inode;
			List<ChangeDetailEvent> sdfasd = view.getChangeTree();
			Collection<INode> inodes=map2.values();
			Iterator<INode> inode=inodes.iterator();
			for (; inode.hasNext();) {
				INode inodegroup = inode.next();
				System.out.println(inodegroup.getType());
				if(!inodegroup.getType().equals("monitor"))
					map.put(inodegroup.getSvId(),inodegroup.getName());
	        }
		} catch (Exception e) {
		}
		return map;
	}

}
