package com.siteview.ecc.tuopu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class SubTuolistModel extends TuopulistModel{

	private static final long serialVersionUID = 6601229130583842607L;
	private static final String strPath = "main\\tuoplist\\";
	private static final Logger logger = Logger.getLogger(SubTuolistModel.class);
	private static final int columnSize = 5;
	
	public SubTuolistModel(View view, EccTreeItem selectedNode) {
		super(view, selectedNode);
	}
	//获取拓扑列表数据
	public LinkedHashMap<String, List<LinkedHashMap<String, String>>> getTuopuList(){
		LinkedHashMap<String, List<LinkedHashMap<String, String>>> returnmap = new LinkedHashMap<String,List<LinkedHashMap<String, String>>>();
		LinkedHashMap<String, LinkedHashMap<String, String>> rtnMap = super.GetTuopuList();
		Collection<LinkedHashMap<String, String>> values = rtnMap.values();
		
		
		int size = values.size()%columnSize>0?values.size()/columnSize+1:values.size()/columnSize;//共有多少行
		
		for(int i=0;i<size;i++){
			List<LinkedHashMap<String, String>> value = new ArrayList<LinkedHashMap<String, String>>();
			int count = 1;
			for(Object tmpValue : values.toArray()){
				value.add((LinkedHashMap<String, String>)tmpValue);
				values.remove((LinkedHashMap<String, String>)tmpValue);
				if(count==columnSize) break;
				count++;
			}
			returnmap.put(UUID.randomUUID().toString(), value);
		}
		return returnmap;
	}
	//刷新拓扑列表
	public void refresh()
	{
		clear();
		addAll(getTuopuList().values());
	}
	
	@Override
	public void render(final Row parent, Object data) throws Exception{
		parent.setHeight("170px");
		parent.setStyle("border:none");
		List<LinkedHashMap<String, String>> dataItem = (List<LinkedHashMap<String, String>>)data;
		for(final LinkedHashMap<String, String> item : dataItem){
			int index = item.get("htmlname").indexOf(".htm");
			String url = "/main/tuoplist/image?imageName="+strPath+item.get("htmlname").substring(0,index)+".files\\gif_1.gif";
			final TuoplistImage image = new TuoplistImage();
			image.setTitle(item.get("name").replaceAll(".htm", ""));
			image.setSrc(url);
			image.setOpenListener(new EventListener(){
				@Override
				public void onEvent(Event arg0) throws Exception {
					Executions.getCurrent().sendRedirect(item.get("showurl"), "_blank");
				}});
			image.setEditListener(new EventListener(){
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					OnEditTuopuName(item,parent.getGrid());
				}});
			image.setDownloadListener(new EventListener(){
				@Override
				public void onEvent(Event arg0) throws Exception {
//				Executions.getCurrent().sendRedirect(item.get("downvsdurl").toString(),"_blank");
					String strPath = EccWebAppInit.getWebDir()+item.get("downvsdurl");
					File file = new File(strPath);
					if(file.exists()){
						Filedownload.save(file, "application/vnd.visio");
					}
				}});
			image.setDeleteListener(new EventListener(){
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					onClick$btnDel(item,parent.getGrid());				
				}});
			logger.info(url);
			image.setSrc(url);
			image.onCreate();
			image.setParent(parent);
		}
		
	}
	
	public void OnEditTuopuName(LinkedHashMap<String, String> item,Grid grid){
		String strName = item.get("htmlname");
		String strShowName = item.get("name");
		
		try 
		{
			//跳编辑名称窗口
			final Window win = (Window) Executions.createComponents(
					"tuoplist/edittuopname.zul", null, null);					
//					"/main/tuoplist/edittuopname.zul?name=" + strName, null, null);
			
			//设置窗口间共享参数
//			win.setAttribute("name", strName);
//			win.setAttribute("showname", strShowName);
			((Textbox)win.getFellow("name")).setValue(strName);
			((Textbox)win.getFellow("showname")).setValue(strShowName);
			win.setAttribute("tuopList", grid);
//			win.setAttribute("parent", );
			
			//弹出窗口			
			win.doModal();
//			txt.setReadonly(true);
//			txt.setFocus(false);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//删除选中的拓扑图
	public void onClick$btnDel(LinkedHashMap<String, String> item,Grid tuopListBox)
	{
		try 
		{
			String strPath = EccWebAppInit.getWebDir() + "\\main\\tuoplist\\";
			String strName = "", strTmp = "";
			IniFile ini = new IniFile("tuopfile.ini");
			ini.load();

			if(Messagebox.OK == Messagebox.show("是否删除所选视图？", "询问", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION))
			{
	            if (item != null) 
	            	
	            {
	            	//删除选中拓扑图文件等
	            	strName = item.get("htmlname");
	            	strName = item.get("htmlname").substring(0, strName.length() - 4);
	            	strTmp = strPath + strName + ".htm";
	            	MakeTuopuData.delFile(strTmp);
	            	strTmp = strPath + strName + ".vsd";
	            	MakeTuopuData.delFile(strTmp);
	            	strTmp = strPath + strName + ".files\\";
	            	MakeTuopuData.delFolder(strTmp);
	            	
	            	//删除选中拓扑图在ini里的信息
	    			ini.deleteKey("filename", strName + ".htm");
	    			ini.deleteKey("filename", strName + ".vsd");
	            	
//			            	item.setParent(null);
//			            	tuopListBox.removeItemFromSelection(item);
//			            	tuopListBox.removeItemFromSelectionApi(item);
	            }
				View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
				String loginname = view.getLoginName();
				String minfo = loginname + "在" + OpObjectId.tupo_view.name + "中进行了"+OpTypeId.del.name+""+OpObjectId.tupo_view.name+"操作";
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.tupo_view);
		        ini.saveChange();
		        ((TuopulistModel)tuopListBox.getModel()).refresh();
//			        tuopListBox.clearSelection();
//			        tuopListBox.getPage().invalidate();
		        tuopListBox.invalidate();
			}
			
			        
	        	        
		}
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		
	}
}
