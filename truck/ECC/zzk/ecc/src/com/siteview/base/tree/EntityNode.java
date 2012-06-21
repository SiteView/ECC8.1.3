package com.siteview.base.tree;



public class EntityNode extends Fork implements INode
{
	public EntityNode()
	{
		
	}
	
	
	public EntityNode(INode node)
	{
		super.setType(INode.ENTITY);
		super.setStatus(node.getStatus());
		super.setName(node.getName());
		super.setId(node.getSvId());
		
		IForkNode e= (IForkNode)node; 
		super.setSonId(e.getSonId());
	}
}
