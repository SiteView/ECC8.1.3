package com.siteview.base.tree;



public class SeNode extends Fork implements INode
{
	public SeNode()
	{
		
	}
	
	
	public SeNode(INode node)
	{
		super.setType(INode.SE);
		super.setStatus(node.getStatus());
		super.setName(node.getName());
		super.setId(node.getSvId());
		
		IForkNode e= (IForkNode)node; 
		super.setSonId(e.getSonId());
	}
	
	public String getParentSvId()
	{
		return INode.ROOT;
	}
}
