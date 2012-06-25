package com.siteview.base.tree;


public class MonitorNode extends NameId  implements INode
{
	public MonitorNode()
	{
		
	}
	
	
	public MonitorNode(INode node)
	{
		super.setType(INode.MONITOR);
		super.setStatus(node.getStatus());
		super.setName(node.getName());
		super.setId(node.getSvId());
	}
	

}
