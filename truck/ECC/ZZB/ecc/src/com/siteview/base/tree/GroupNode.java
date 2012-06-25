package com.siteview.base.tree;



public class GroupNode extends Fork implements INode
{
	public GroupNode()
	{
		
	}
	
	
	public GroupNode(INode node)
	{
		super.setType(INode.GROUP);
		super.setStatus(node.getStatus());
		super.setName(node.getName());
		super.setId(node.getSvId());
		
		IForkNode e= (IForkNode)node; 
		super.setSonId(e.getSonId());
	}
}
