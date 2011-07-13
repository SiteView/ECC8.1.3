package com.siteview.svdb;

import java.util.Map;

import javax.jws.WebService;

import javolution.util.FastMap;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.IInfo;

@WebService(name = "eccstatapi", targetNamespace = "http://com.siteview.cxf")
public class StatisticsStatusImpl implements StatisticsStatus {

	@Override
	public Map<String, Integer> getStatisticsStatus(String username,
			String password, String id) throws Exception {
		String sessionid = null;
		try{
			Map<String, Integer> retmap = new FastMap<String, Integer>();
			sessionid = Manager.createView(username, password);
			View view=Manager.getView(sessionid);
			if(view!=null)
			{
				INode[] nodes = null;
				if (id == null){
					nodes = view.getSe();
				}else{
					INode node = view.getNode(id);
					if (node!=null)
						nodes = new INode[]{node};
				}
				if(nodes==null || nodes.length == 0)
					throw new Exception("节点没有找到:" + id);
				int ok=0,error=0,warning=0,disable=0,monitors=0,entities=0;
				for(INode iNode:nodes)
				{
					if (iNode == null) continue;
					IInfo info = null;
					if (INode.ENTITY.equals(iNode.getType())){
						info=view.getEntityInfo(iNode);
					}else if (INode.GROUP.equals(iNode.getType())){
						info=view.getGroupInfo(iNode);
					}else if (INode.SE.equals(iNode.getType())){
						info=view.getSeInfo(iNode);
					}else{
						throw new Exception("是检测器,不需要统计:" + id);
					}
					entities+=info.get_sub_entity_sum(view);
					monitors+=info.get_sub_monitor_sum(view);
					ok+=info.get_sub_monitor_ok_sum(view);
					warning+=info.get_sub_monitor_warning_sum(view);
					error+=info.get_sub_monitor_error_sum(view);
					disable+=info.get_sub_monitor_disable_sum(view);
				}
				retmap.put(INode.OK, ok);
				retmap.put(INode.ERROR, error);
				retmap.put(INode.WARNING, warning);
				retmap.put(INode.DISABLE, disable);
				retmap.put(INode.ENTITY, entities);
				retmap.put(INode.MONITOR, monitors);
			}
			return retmap;
		}finally{
			if (sessionid != null && !"".equals(sessionid))
				Manager.invalidateView(sessionid);
		}
	}

}
