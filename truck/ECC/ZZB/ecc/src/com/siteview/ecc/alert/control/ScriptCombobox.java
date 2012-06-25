package com.siteview.ecc.alert.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.manage.View;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.ecc.util.Toolkit;

/**
 * 脚本报警的选择服务器component
 * @author hailong.yi
 *
 */
public class ScriptCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 4965615794687662728L;
	private View view  = null;
	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		if (view == null) view = Toolkit.getToolkit().getSvdbView(this.getDesktop());
		map.put("127.0.0.1","127.0.0.1");
		
		INode[] ses = view.getSe();
		for (INode se : ses){
			map.putAll(this.getServerByNodeId(se.getSvId()));
		}
//		return map;
		
		//排序
		ArrayList keylist = new ArrayList();
		for (String key : map.keySet()){
			keylist.add(key);
		}
		Object[] strKeylist = keylist.toArray();
		Arrays.sort(strKeylist);//排序
		Map<String,String> changedMap = new LinkedHashMap<String,String>();
		for(Object key : strKeylist){
			String value = map.get(key);
			changedMap.put(key.toString(), value);
		}
		return changedMap;
	}

	private Map<String,String> getServerByNodeId(String id)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
		INode node = view.getNode(id);
		if(node==null) return map;
		if (INode.MONITOR.equals(node.getType())){
			return map;
		}
		if (INode.ENTITY.equals(node.getType())){
			EntityInfo entityinfo = view.getEntityInfo(node);
			if (entityinfo.getDeviceType()!=null) {
				if ("_win".equals(entityinfo.getDeviceType()) || "_unix".equals(entityinfo.getDeviceType())){
					map.put(entityinfo.getSvId(),entityinfo.getName() + "(" + entityinfo.getDeviceType() + ")");
					return map;
				}
			}
		}
		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		for (String newid : ids){
			map.putAll(this.getServerByNodeId(newid));
		}
		return map;
	}
}
