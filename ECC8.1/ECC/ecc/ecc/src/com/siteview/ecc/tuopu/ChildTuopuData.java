package com.siteview.ecc.tuopu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.zk.ui.Desktop;

import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class ChildTuopuData {
	private final static Logger logger = Logger.getLogger(ChildTuopuData.class);
	
    private Document domTree = null;
    private String strStatu = "ok";
    
    public String getStatu() {
		return strStatu;
	}


	public void setStatu(String strStatu) {
		this.strStatu = strStatu;
	}

	Map<String, Map<String, String>> mapIn;
    Desktop desktop=null;
    
    public ChildTuopuData(Desktop desktop, String strName)
    {
		try
		{
			this.desktop = desktop;
			
			String strPath = EccWebAppInit.getWebDir() + "main\\tuoplist\\";
			strPath += strName;			
			strPath += ".files\\data.xml";
			
	    	//1、初始化并读取data.xml数据
			this.domTree = MakeTuopuData.ReadXml(strPath);
	        String xpathString = ".//Page";  
	        
			NodeList pPageList = MakeTuopuData.selectNodes(this.domTree, xpathString);
			mapIn =  new HashMap();
			
			//2、取得所有用户定义的shape属性
			for (int i=0; i<pPageList.getLength(); i++) 
			{
				Node pPage = pPageList.item(i);
				
				xpathString = ".//Shape";
				NodeList pShapeList = MakeTuopuData.selectNodes(pPage, xpathString);
				for (int j=0; j<pShapeList.getLength(); j++)
				{
					Node pShape = pShapeList.item(j);
					
					if(MakeTuopuData.IsHyperlinkNodesExisted(pShape))
					{
						Map<String, String> value = new LinkedHashMap(); 
						
						value.put("Des", MakeTuopuData.RetrievePropertyValue(pShape, "SV_Des"));
						value.put("IP", MakeTuopuData.RetrievePropertyValue(pShape, "SV_IP"));
						value.put("Link", MakeTuopuData.RetrievePropertyValue(pShape, "SV_Link"));
//						value.put("SV_App", RetrievePropertyValue(pShape, "SV_App"));
						value.put("Group", MakeTuopuData.RetrievePropertyValue(pShape, "SV_Group"));
						value.put("Entity", MakeTuopuData.RetrievePropertyValue(pShape, "SV_Entity"));
						value.put("Monitor", MakeTuopuData.RetrievePropertyValue(pShape, "SV_Monitor"));
						
						mapIn.put(pShape.getAttributes().getNamedItem("ID").getNodeValue(), value);
					}
				}
			}
			
			//调试信息
			if(strName.equals("227")){
				logger.info("ChildTuopuData mapIn:-----------------------------------------------------------");
				for(String key1 : mapIn.keySet())
		        {
					logger.info(key1);
					
					for(String key2 : mapIn.get(key1).keySet())
		            {
		                if (!mapIn.get(key1).get(key2).equals(""))
		                {
		                	logger.info("     " + key2 + " : " + mapIn.get(key1).get(key2).toString());
		                }
		            }
		        }
			}
			
			//3、从服务器得子拓扑图状态。
			GetChildTuopuStatuFromServer(mapIn);
	        
			//调试信息
			logger.info("GetChildTuopuStatuFromServer Statu : " +  strStatu);			

		}
		catch (Exception ex) 
		{
			
		}	
    }
	
    
	//取子拓扑图状态, 一旦错误等就返回， 不需要获取出所有数据
	public void GetChildTuopuStatuFromServer(Map<String, Map<String, String>> mapIn)
	{
		try
		{
			//获取匹配条件参数所需要的值
			Map<String, Map<String, String>> mapName = MakeTuopuData.QueryNameInfo();// Group Entity Monitor key(svid) + value(needtype + sv_name)
			Map<String, Map<String, String>> mapMachineName = MakeTuopuData.QueryMachineNameInfo();//Ip　key(svid) + value(needtype + _MachineName) ==
			//Des(直接赋值给列表) 、App(暂时没用) ？
			
			if(mapName == null || mapMachineName == null)
			{
				strStatu = "error";
				return ;
			}
	
			//获取最新的所有监测数据
			Map<String, Map<String, String>> mapTree = new HashMap(); //用于monitor比较
			
			List<Map<String, String>> tree =  Toolkit.getToolkit().getSvdbView(desktop).getRawTreeData();
			for(Map<String, String> map : tree)
			{
				mapTree.put(map.get("sv_id"), map);
			}
			
	        String strTmpStatu = "ok";
			//匹配条件参数 + 监测数据 --> TuopuData 
	        for (String key : mapIn.keySet())
	        {
	        	Map<String, String> tmpValue = new HashMap();
	
	            for(String key1 : mapIn.get(key).keySet())
	            {
	                //
	                if(key1.equals("Group") && !mapIn.get(key).get("Group").equals(""))
	                {
	                    tmpValue.put("Group", mapIn.get(key).get("Group"));
	                    for(String key2 : mapName.keySet())
	                    {
	                    	if (mapName.get(key2).get("needtype").equals("group") && mapName.get(key2).get("sv_name").indexOf(mapIn.get(key).get("Group")) != -1)
	                        {
	                            //构成监测器列表出来
	                    		strTmpStatu = GetMonitorStatuFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);	                    		
	                    		
	                    		if(strTmpStatu.equals("error"))
	                    		{
	                    			strStatu = "error";
	                    			return;
	                    		}
	                    		
	                    		if(strTmpStatu.equals("warning"))
	                    		{
	                    			strStatu = "warning";
	                    			
	                    		}
	                        }
	                    }
	                }
	                else if (key1.equals("Entity") && !mapIn.get(key).get("Entity").equals(""))
	                {
	                    tmpValue.put("Entity", mapIn.get(key).get("Entity"));
	                    for(String key2 : mapName.keySet())
	                    {
	                    	
	                    	if (mapName.get(key2).get("needtype").equals("entity") && mapName.get(key2).get("sv_name").indexOf(mapIn.get(key).get("Entity")) != -1)
	                        {
	                            //构成监测器列表出来
	                    		strTmpStatu = GetMonitorStatuFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);
	                    		
	                    		if(strTmpStatu.equals("error"))
	                    		{
	                    			strStatu = "error";
	                    			return;
	                    		}
	                    		
	                    		if(strTmpStatu.equals("warning"))
	                    		{
	                    			strStatu = "warning";
	                    		}
	                        }
	                    }
	                }
	                else if (key1.equals("Monitor") && !mapIn.get(key).get("Monitor").equals(""))
	                {
	                    tmpValue.put("Monitor", mapIn.get(key).get("Monitor"));
	                    for(String key2 : mapName.keySet())
	                    {
	                        if (mapName.get(key2).get("needtype").equals("monitor") && mapName.get(key2).get("sv_name").indexOf(mapIn.get(key).get("Monitor")) != -1)
	                        {
	                            //构成监测器列表出来
	                        	strTmpStatu = GetMonitorStatuFromTree(tree, mapTree, key2, tmpValue.keySet().size(), true);
	                    		
	                    		if(strTmpStatu.equals("error"))
	                    		{
	                    			strStatu = "error";
	                    			return;
	                    		}
	                    		
	                    		if(strTmpStatu.equals("warning"))
	                    		{
	                    			strStatu = "warning";
	                    		}
	                        }
	                    }
	                }                    
	                else if (key1.equals("IP") && !mapIn.get(key).get("IP").equals(""))
	                {
	                    tmpValue.put("IP", mapIn.get(key).get("IP"));
	                    for(String key2 : mapMachineName.keySet())
	                    {
	                    	if(!mapMachineName.get(key2).containsKey("_MachineName"))
	                    		continue;
	                    	
	                        if (mapMachineName.get(key2).get("needtype").equals("entity") && mapMachineName.get(key2).get("_MachineName").equals(mapIn.get(key).get("IP")))
	                        {
	                            //构成监测器列表出来
	                        	strTmpStatu = GetMonitorStatuFromTree(tree, mapTree, key2, tmpValue.keySet().size(), false);
	                    		
	                    		if(strTmpStatu.equals("error"))
	                    		{
	                    			strStatu = "error";
	                    			return;
	                    		}
	                    		
	                    		if(strTmpStatu.equals("warning"))
	                    		{
	                    			strStatu = "warning";
	                    		}

	                        }
	                    }
	                }
	                else if (key1.equals("Des") && !mapIn.get(key).get("Des").equals(""))
	                {
	                    //直接加入返回列表项 且在第一位 暂时不管
	
	                }
	                else if (key1.equals("App") && !mapIn.get(key).get("App").equals(""))
	                {
	                    //暂时不管
	                }
	                else if (key1.equals("Link") && !mapIn.get(key).get("Link").equals(""))
	                {
						//递归获取关联的其他子图的状态
	                	ChildTuopuData childTuopu = new ChildTuopuData(desktop, mapIn.get(key).get("Link"));
	                	strTmpStatu = childTuopu.getStatu();
	                	
                		if(strTmpStatu.equals("error"))
                		{
                			strStatu = "error";
                			return;
                		}
                		
                		if(strTmpStatu.equals("warning"))
                		{
                			strStatu = "warning";
                		}

	                }
	                else
	                {
	                	
	                }
	            }
	        }
		}
		catch (Exception ex)
		{
			logger.info(ex.toString());			
		}
	}
	
	//获取监测器列表数据
    //strSvid + .  -->Group Entity IP
    //strSvid == --> monitor	
	public String GetMonitorStatuFromTree(List<Map<String, String>>  tree, Map<String, Map<String, String>> mapTree, String strSvid, int index, boolean bIsMonitor)
	{
		String strTmp = "ok";
        int k = index;

        if (bIsMonitor)
        {
            if (mapTree.containsKey(strSvid))
            {
                //Monitor            	
//            	if(mapTree.get(strSvid).get("status").equals("error"))       
            	if(mapTree.get(strSvid).get("status").equals("error") || mapTree.get(strSvid).get("status").equals("bad"))
            	{
            		return "error";
            	}
            	
            	if(mapTree.get(strSvid).get("status").equals("warning"))
            	{
            		strTmp = "warning";
            	}
            	
//            	if(strTmp.equals("warning") && mapTree.get(strSvid).get("status").equals("ok"))
//            	{
//            		;
//            	}            	
            }
        }
        else
        {
            strSvid += ".";
            for (int i = 0; i < tree.size(); i++)
            {
                String strId = "";

                //获节点svid
                strId = tree.get(i).get("sv_id");

                if (tree.get(i).get("type").equals("monitor"))
                {
                    if (strId.indexOf(strSvid) == 0)
                    {
                        //Group Entity IP
                        if(tree.get(i).get("status").equals("error") || "bad".equals(tree.get(i).get("status")))
                        {
                        	return "error";
                        }
                        
                    	if(tree.get(i).get("status").equals("warning"))
                    	{
                    		strTmp = "warning";
                    	}
                    	
//                    	if(strTmp.equals("warning") && tree.get(i).get("status").equals("ok"))
//                    	{
//                    		continue;
//                    	}
                        
                        k++;
                    }
                    else
                    {
                        //
                    }
                }
            }
        }
        
		return strTmp;
	}
    
}
