package com.siteview.ecc.alert.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.siteview.base.manage.View;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.dao.type.HboxWithSortValue;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.LinkCheck;

public class BaseTools {
	private final static Logger logger = Logger.getLogger(BaseTools.class);
	/**
	 * 清除list中的记录
	 * @param list
	 */
	public static void clear(Listbox list)
	{
		if (list.getItems()==null || list.getItems().size() == 0) return;
		list.getItems().clear();
	}
	/**
	 * 在listbox中，添加记录，记录的元素可以是String也可以是Component
	 * @param list
	 * @param value 该条记录的值
	 * @param cols 需要田间的记录
	 * @return 产生的Listitem
	 */
	public static Listitem setRow(Listbox list,Object value,Object...cols)
	{
		Listitem item = new Listitem();
		item.setValue(value);
		for (Object col : cols){
			Listcell cell = new Listcell();
			if (col instanceof String){
				cell.setLabel((String)col);
			}else if (col instanceof HboxWithSortValue){
				((HboxWithSortValue)col).setParent(cell);
			}else if (col instanceof Component){
				((Component)col).setParent(cell);
			}
			item.appendChild(cell);
		}
		list.appendChild(item);
		return item;
	}
	
	/**
	 * 在listbox中，添加记录，记录的元素可以是String也可以是Component//重写方法 setRow 实现 Tooltiptext功能
	 * @param list
	 * @param value 该条记录的值
	 * @param cols 需要田间的记录
	 * @return 产生的Listitem
	 */
	public static Listitem addRow(Listbox box ,Object obj, Object...cols)
	{
		Listitem item = new Listitem();
		item.setValue(obj);
		for(Object col : cols){
			Listcell cell = new Listcell();
			if(col instanceof String){
				cell.setLabel(col.toString());
				cell.setTooltiptext(col.toString());
				cell.setParent(item);
			}else if(col instanceof Component){
				Component c = (Component)col;
				cell.appendChild(c);
				cell.setParent(item);
			}
		}
		item.setParent(box);
		return item;
	}
	
	
	/**
	 * 在component中，查找id的component
	 * @param component
	 * @param id
	 * @return 找到的Component
	 */
	public static Component getComponentById(Component component,String id){
		if (id == null ) return null;
		if (component == null ) return null;
		for (Object obj : component.getChildren()){
			Component comp = (Component)obj;
			if (id.equals(comp.getId())) {
				return comp;
			}
			Component compChild = getComponentById(comp, id);
			if (compChild!=null){
				return compChild;
			}
		}
		return null;
	}

	public static boolean delete(Component component,String id){
		if (id == null ) return false;
		if (component == null ) return false;
		for (Object obj : component.getChildren()){
			Component comp = (Component)obj;
			if (id.equals(comp.getId())) {
				component.removeChild(comp);
				return true;
			}
			if (delete(comp, id)) return true;
		}
		return false;
	}
	

	
	/**
	 * 在ids(型如id1,id2,id3....,)中是否存在id
	 * @param id
	 * @param ids
	 * @return 是，否
	 */
	public static boolean existId(String id,String ids)
	{
		if (id == null) return false;
		if (ids == null) return false;
		String[] idsArray = ids.split(",");
		for (String idstr : idsArray){
			if (id.equals(idstr)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取得带有图片的按钮的Component
	 * @param labelText  按钮旁边的文字
	 * @param buttonLabelText  按钮上的文字
	 * @param imagesrc   图片链接
	 * @param eventlistener  点击后的事件
	 * @return Component 按钮组件
	 */
	public static Component getWithLink(String labelText,String buttonLabelText, String imagesrc,EventListener eventlistener){
		Hbox hbox = new Hbox();
		Image button =  new Image();
		button.setSrc(imagesrc);
		button.setTooltiptext(buttonLabelText);
		button.addEventListener(Events.ON_CLICK, eventlistener);
		Label label = new Label("   " + labelText);
		button.setParent(hbox);
		label.setParent(hbox);
		return hbox;
	}
	
	/**
	 * @param MonitorBean value 
	 * @param eventlistener  点击后的事件
	 * @return Listcell 按钮组件
	 */
	public static Component getWithEntityLink(MonitorBean value,EventListener eventlistener){
		Label label = new Label("  "+value.getEntity());
		label.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
		label.setTooltiptext(value.getEntity());
		label.addEventListener(Events.ON_CLICK, eventlistener);
		return label;
	}
	/**
	 * @param String value 
	 * @param eventlistener  点击后的事件
	 * @return Listcell 按钮组件
	 */
	public static Component getWithEntityLink(String value,EventListener eventlistener){
		Label label = new Label("  "+value);
		label.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
		label.setTooltiptext(value);
		label.addEventListener(Events.ON_CLICK, eventlistener);
		return label;
	}
	/**
	 * @param MonitorBean value 
	 * @param eventlistener  点击后的事件
	 * @return Listcell 按钮组件
	 */
	public static Component getWithMonitorLink(MonitorBean value,EventListener eventlistener){
		Label label = new Label("  "+value.getMonitorName());
		label.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
		label.setTooltiptext(value.getMonitorName());
		label.addEventListener(Events.ON_CLICK, eventlistener);
		return label;
	}
	/**
	 * @param String value 
	 * @param eventlistener  点击后的事件
	 * @return Listcell 按钮组件
	 */
	public static Component getWithMonitorLink(String value,EventListener eventlistener){
		Label label = new Label("  "+value);
		label.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
		label.setTooltiptext(value);
		label.addEventListener(Events.ON_CLICK, eventlistener);
		return label;
	}
	
	/**
	 * 给指定的num的左边补足len长度的"0"
	 * @param num
	 * @param len
	 * @return 补足后的字符串
	 */
	public static String getString(int num , int len){
		return getString("" + num , len ,'0');
	}
	
	/**
	 * 给指定的num的左边补足len长度的ch
	 * @param num
	 * @param len
	 * @param ch
	 * @return 补足后的字符串
	 */
	public static String getString(String num , int len, char ch){
		StringBuffer bf = new StringBuffer();
		String str = num;
		for (int i = 0 ; i < (len - str.length()) ; i++){
			bf.append(ch);
		}
		bf.append(str);
		return bf.toString();
	}
	/**
	 * 给定id ,取得它的所有监测器id
	 */
	public static List<String> getAllMonitors(View view,String id){
		List<String> retlist = new FastList<String>();
		INode node = view.getNode(id);
		if (node==null) return retlist;
		if (INode.MONITOR.equals(node.getType())){
			retlist.add(id);
			return retlist;
		}
		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		for (String sonid : ids){
			retlist.addAll(getAllMonitors(view,sonid));
		}
		return retlist;
	}

	/**
	 * 给定id ,取得它的所有设备id
	 */
	public static List<String> getAllEntites(View view,String id){
		List<String> retlist = new FastList<String>();
		INode node = view.getNode(id);
		if (node==null) return retlist;
		if (INode.ENTITY.equals(node.getType())){
			retlist.add(id);
			return retlist;
		}
		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		for (String sonid : ids){
			retlist.addAll(getAllEntites(view,sonid));
		}
		return retlist;
	}
	/**
	 * 给定id ,取得它的所有组id
	 */
	public static List<String> getAllGroups(View view,String id){
		List<String> retlist = new FastList<String>();
		INode node = view.getNode(id);
		if (node==null) return retlist;
		if (INode.GROUP.equals(node.getType())){
			retlist.add(id);
			return retlist;
		}
		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		for (String sonid : ids){
			retlist.addAll(getAllGroups(view,sonid));
		}
		return retlist;
	}

	public static List<String> getAllMonitors(EccTreeModel treemodel,String nodeId){
		EccTreeItem node = treemodel.findNode(nodeId);
		return getAllMonitors(treemodel,node);
	}
	public static List<String> getAllMonitors(EccTreeModel treemodel,EccTreeItem node){
		List<String> retlist = new FastList<String>();
		
		INode inode = node.getValue();
		if (inode == null) return retlist;
		if (INode.MONITOR.equals(inode.getType())){
			retlist.add(node.getId());
			return retlist;
		}
		for (EccTreeItem son : node.getChildRen()){
			retlist.addAll(getAllMonitors(treemodel,son));
		}
		return retlist;
	}
	public static List<String> getAllMonitors(String monitortype,EccTreeModel treemodel,EccTreeItem node){
		List<String> retlist = new FastList<String>();
		
		INode inode = node.getValue();
		if (inode!=null && INode.MONITOR.equals(inode.getType())){
			if (monitortype != null){
				MonitorInfo monitorinfo = treemodel.getView().getMonitorInfo(inode);
				if (!monitortype.equals(monitorinfo.getMonitorType())){
					return retlist;
				}
			}
			retlist.add(node.getId());
			return retlist;
		}
		for (EccTreeItem son : node.getChildRen()){
			retlist.addAll(getAllMonitors(treemodel,son));
		}
		return retlist;

	}
	
	public static Button getDefaultButton()
	{
		Button button = new Button();
		button.setClass("btnDefault");
		button.setWidth("74px");
		button.setHeight("23px");
		return button;
	}

	public static Comparator<Listitem> getSortComparator(int index,boolean desc){
		
		class MyListitemComparator implements Comparator<Listitem>{
			boolean desc = false;
			int index = -1;
			public MyListitemComparator(int index,boolean desc){
				this.desc = desc;
				this.index = index;
			}
			@Override
			public int compare(Listitem item1, Listitem item2) {
				HboxWithSortValue obj1 = getComponent(item1,index);
				HboxWithSortValue obj2 = getComponent(item2,index);
				if (obj1!=null && obj2!=null){
					String sort1 = obj1.getSortValue();
					String sort2 = obj2.getSortValue();
					if (sort1!=null && sort2!=null){
						return desc ? sort2.compareTo(sort1) : sort1.compareTo(sort2);
					}
				}
				return 0;
			}

			
		};
		return new MyListitemComparator(index,desc);
	}
	
	public static HboxWithSortValue getComponent(Listitem item,int index){
		Component object = (Component) item.getChildren().get(index);
		if (object instanceof Listcell){
			Component retComponent = object.getFirstChild();
			if (retComponent instanceof HboxWithSortValue){
				return (HboxWithSortValue) retComponent;
			}
		}
		return null;
	}
	public static String inputStream2String(InputStream is) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BufferedReader in =null;
		try
		{
			in=new BufferedReader(new InputStreamReader(is,"UTF-8"));
		    String line = "";
		    while ((line = in.readLine()) != null){
		      buffer.append(line);
		    }
		}finally
		{
			try{in.close();}catch(Exception e){}
		}
	    return buffer.toString();
	}	
	
	public static boolean validateEmail(String emailAddress){
//		String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//email正则表达式
		//新的规则
		String regex = "^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+(,\\w+(\\.\\w+)*@\\w+(\\.\\w+)+)*$";

		Pattern ptn = Pattern.compile(regex);
		Matcher mch = ptn.matcher(emailAddress);
		return mch.matches();
	}
	
	/**
	 * 支持以130,131,132,133,134,135,136,137,138,139
	 * ,151,152,153,155,156,157,158,159
	 * ,186,188,189开头的11位手机号码
	 * @param phone
	 * @return
	 */
	public static boolean validatePhoneNO(String phone){
		String regex = "^(13[0-9]|15[^4]|18[6|8|9])\\d{8}";
		Pattern ptn = Pattern.compile(regex);
		Matcher mch = ptn.matcher(phone);
		return mch.matches();
		
	}
	
	public static String emailRegex(){
		return "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//email正则表达式
	}
	public static void checkLinkLabel(Label linkLabel,String userRightId,EventListener listener){
		String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(userRightId);
		if(isLink){
			linkLabel.setStyle(style);
			linkLabel.addEventListener(Events.ON_CLICK, listener);
		}
	}
	
	/**
	 * 检查字符串中是否存在空格字符
	 * @param value
	 * @return
	 */
	public static boolean validateBackspace(String value){
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(value);
		if(value==null || value.isEmpty() || matcher.find()){
			return true;
		}
		return false;
	}
	public static void loginToNTLM(final String domain,final String name,final String passwd) { 
        Authenticator.setDefault(new Authenticator() { 
            protected java.net.PasswordAuthentication getPasswordAuthentication() { 
                StringBuffer buf = new StringBuffer(); 
                buf.append(domain);
                buf.append("\\");
                buf.append(name);
                PasswordAuthentication passwordauthentication =  new java.net.PasswordAuthentication(buf.toString(), passwd.toCharArray());
                return passwordauthentication; 

            } 

        });
	}
	private static final Map<String,Properties> dictpropMap = new FastMap<String,Properties>();
	
	public static String getConfigString(String key){
		return getConfigString("config.properties",key);
	}

	public static String getMakeTuopuDataString(String key){
		return getConfigString("maketuopudata.properties",key);
	}
	
	public static String getConfigString(String filename,String key){
		Properties prop = getProperties(filename);
		return prop.getProperty(key);
	}
	
	public static Properties getProperties(String filename){
		Properties prop = dictpropMap.get(filename);
		if (prop == null){
			prop = new Properties();
			loadFile(prop,filename);
			dictpropMap.put(filename, prop);
		}else if (prop.isEmpty()){
			loadFile(prop,filename);
			dictpropMap.put(filename, prop);
		}
		return prop;
	}
	public static void loadFile(Properties prop,String path) {
		InputStream in = null;
		try {
			in = BaseTools.class.getClassLoader()
					.getResourceAsStream(path);
			if (path.endsWith(".xml"))
				prop.loadFromXML(in);
			else
				prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{
			try {
				if (in != null) in.close();
			} catch (Exception e) {
			}
		}
	}
	public static void main(String[] args) throws Exception{

	}
}
