package com.siteview.ecc.treeview.windows;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.EccSessionListener;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.controls.ISvdbControl;
import com.siteview.ecc.treeview.controls.SvdbComboBox;
import com.siteview.ecc.treeview.controls.SvdbTextBox;
import com.siteview.ecc.util.Toolkit;

public class addEntity extends GenericForwardComposer
{
	private final static Logger logger = Logger.getLogger(addEntity.class);
	private static String		Dependontree_TargetUrl	= "/main/TreeView/dependontree.zul";
	// 绑定组件
	private Rows				baserow;
	private Window				WAddEntity;													// embeded object,
	private Textbox				tbDescription;
	private Textbox				tbDepends;
	private Button				btnDepends;
	private Radio				rdGood;
	private Radio				rdWarning;
	private Radio				rdError;
	
	private Button				btnok;
	private Button				btntest;
	private Button				btncancel;
	Button						btnadd;
	private Panelchildren pc;
	// private Button btnup;
	
	// 其他定义
	private SvdbTextBox			tbTitle;
	
	private EntityTemplate		entityTemplate;
	
	private SvdbTextBox			tbport;
	
	public final static String	REG_DIGIT				= "[0-9]+";
	private View				view;
	private Tree				tree;
	private String				svid;
	private EntityEdit			entityEdit;
	private INode				node;
	
	private SvdbComboBox		cbdydata;														// 动态数据
																								
	private Include				eccbody;
	
	private static String		FastAdd_TargetUrl		= "/main/TreeView/monitorsfastadd.zul";
	
	EccTimer					eccTimer;
	Boolean						isedit;
	Vbox						vbox;
	int							idcount					= 6;
	Timer						dytimer					= null;
	Boolean						issave					= false;
	
	String						id						= "";

	HtmlBasedComponent 			control 				= null;
	boolean						autoName				= true;
	
	public void onCreate$WAddEntity()
	{
		node = (INode) WAddEntity.getAttribute("inode");
		view = (View) WAddEntity.getAttribute("view");
		//
		node = view.getNode(node.getSvId());
		eccTimer = (EccTimer) WAddEntity.getAttribute("eccTimer");
		isedit = (Boolean) WAddEntity.getAttribute("isedit");
		tree = (Tree) WAddEntity.getAttribute("tree");
		int maxhint=0;
		try
		{
		int maxh= (Integer) tree.getAttribute("desktopHeight");
		maxhint= maxh-140;
		if (maxhint>500)
		{
			maxhint=500;
		}
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		if(maxhint==0)
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:480px;");
		}else
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:"+ maxhint+"px;");
		}
        
		
		try
		{
			if (isedit)
			{
				
				this.entityEdit = this.view.getEntityEdit(node);
				this.entityTemplate = this.entityEdit.getDeviceTemplate();
				String templatename = entityTemplate.get_sv_name();
				WAddEntity.setTitle("编辑(" + templatename + ")设备");
				CreateUI();
				String name = entityEdit.getName();
				String depends = entityEdit.getSvDependsOn();
				String description = entityEdit.getSvDescription();
				String condtion = entityEdit.getProperty().get("sv_dependscondition");
				String value = entityEdit.getProperty().get("sv_dependson");
				if (value == null)
				{
					value = "";
				}
				tbDepends.setAttribute("value", value);
				tbTitle.setSvdbValue(name);
				tbDepends.setValue(depends);
				tbDescription.setValue(description);
				if (condtion != null)
				{
					if (condtion.equals("2"))
					{
						rdWarning.setChecked(true);
					} else if (condtion.equals("1"))
					{
						rdGood.setChecked(true);
					}
				}
				buildUIdata(this.entityEdit);
				btntest.setVisible(false);
				btnadd.setVisible(false);
				btnok.setLabel("保存");
				
			} else
			{
				
				String templateId = (String) WAddEntity.getAttribute("templateId");
				this.entityTemplate = TemplateManager.getEntityTemplate(templateId);
				String templatename = entityTemplate.get_sv_name();
				WAddEntity.setTitle("添加(" + templatename + ")设备");
				CreateUI();
				if (node.getType().equals(INode.GROUP))
				{
					GroupInfo groupinfo = this.view.getGroupInfo(node);
					this.entityEdit = groupinfo.AddDevice(templateId);
				} else
				{
					SeInfo seinfo = this.view.getSeInfo(node);
					this.entityEdit = seinfo.AddDevice(templateId);
				}
				tree = (Tree) WAddEntity.getAttribute("tree");
				
				tbDepends.setAttribute("value", "");
				String svdll = this.entityTemplate.get_sv_dll();
				if (svdll == null)
				{
					btntest.setVisible(false);
				}
				btntest.setVisible(false);
			}
			
			if (cbdydata != null)
			{
				cbdydata.setFocus(true);
				cbdydata.addEventListener("onFocus", new GetDydataOnCreate());
			}
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		WAddEntity.setPosition("center");
	}
	
	/**
	 * 创建UI
	 * 
	 * @throws InterruptedException
	 */
	private void CreateUI() throws InterruptedException
	{
		
		Label label = null;
		Row row = null;
		Boolean first = true;
		
		for (Map<String, String> item : entityTemplate.getItems())
		{
			if (item.get("sv_hidden") != null)
			{
				if (item.get("sv_hidden").trim().toLowerCase().equals("true"))
				{
					continue;
				}
			}
			if (item.get("sv_type").isEmpty())
			{
				continue;
			}
			
			String heltext = item.get("sv_helptext");
			if (heltext == null)
			{
				heltext = "";
			}
			
			if (item.get("sv_type").trim().toLowerCase().equals("textbox"))
			{
				vbox = new Vbox();
				Label labelh = new Label();
				labelh.setId("lbp" + idcount);
				labelh.setValue(heltext);
				labelh.setSclass("helplabel");
				labelh.setVisible(false);
				idcount++;
				
				SvdbTextBox tb = new SvdbTextBox();
				tb.setWidth("350px");
				tb.setHeight("15px");
				tb.setSvdbField(item.get("sv_name"));
				tb.setValue(item.get("sv_value"));
				tb.setHelptext(heltext);
				
				tb.setId("svctrl" + item.get("sv_name"));
				
				logger.info("label:" + item.get("sv_label"));
				if("服务器地址".equals(item.get("sv_label"))){
					id = tb.getId();
					tb.addEventListener("onChange", new EventListener(){
						@Override
						public void onEvent(Event event) throws Exception {
							SvdbTextBox text = (SvdbTextBox)event.getTarget();
							if(text!=null && text.getValue().trim().length()>0){
								btnok.setDisabled(false);
								btnadd.setDisabled(false);
								issave = false;
							}
						}
					});
				}
				label = new Label();
				// label.setId("lbl_" + item.get("sv_name"));
				if ("false".equals(item.get("sv_allownull")))
				{
					label.setValue(item.get("sv_label") + "*:");
				} else
				{
					label.setValue(item.get("sv_label") + ":");
				}
				
				vbox.appendChild(tb);
				vbox.appendChild(labelh);
				row = new Row();
				label.setParent(row);
				vbox.setParent(row);
				row.setParent(baserow);
				// 添加Unix设备，根据所选的连接方式，自动填写端口
				if ("_unix".equals(this.entityTemplate.get_sv_id()))
				{
					if (item.get("sv_name").equals("_Port"))
					{
						this.tbport = tb;
					}
				}
				
				// 设置标题
				if (first && !isedit)
				{
					tb.addEventListener("onChanging", new TextBoxOnchange());
				}
				
			} else if (item.get("sv_type").trim().toLowerCase().equals("password"))
			{
				vbox = new Vbox();
				Label labelh = new Label();
				labelh.setId("lbp" + idcount);
				labelh.setValue(heltext);
				labelh.setSclass("helplabel");
				labelh.setVisible(false);
				idcount++;
				
				SvdbTextBox pw = new SvdbTextBox();
				pw.setWidth("350px");
				pw.setHeight("15px");
				pw.setType("password");
				pw.setSvdbField(item.get("sv_name"));
				pw.setValue(item.get("sv_value"));
				pw.setHelptext(heltext);
				
				pw.setId("svctrl" + item.get("sv_name"));
				label = new Label();
				// label.setId("lbl_" + item.get("sv_name"));
				if ("false".equals(item.get("sv_allownull")))
				{
					label.setValue(item.get("sv_label") + "*:");
				} else
				{
					label.setValue(item.get("sv_label") + ":");
				}
				
				vbox.appendChild(pw);
				vbox.appendChild(labelh);
				row = new Row();
				label.setParent(row);
				vbox.setParent(row);
				row.setParent(baserow);
				first = false;
				
			} else if (item.get("sv_type").trim().toLowerCase().equals("combobox"))
			{
				vbox = new Vbox();
				Label labelh = new Label();
				labelh.setId("lbp" + idcount);
				labelh.setValue(heltext);
				labelh.setSclass("helplabel");
				labelh.setVisible(false);
				idcount++;
				
				SvdbComboBox cb = new SvdbComboBox();
				cb.setWidth("335px");
				cb.setHeight("15px");
				cb.setSvdbField(item.get("sv_name"));
				// cb.setRows(25);
				// cb.setReadonly(true);
				Comboitem cbitem;
				first = false;
				
				if (!item.containsKey("sv_dll") || item.get("sv_dll").isEmpty())
				{
					String templateId = (String) WAddEntity.getAttribute("templateId");
					
					if("_weblogic".equals(templateId)){
						ArrayList<String> list = new ArrayList<String>();
						for (String key : item.keySet())
						{
							if (key.startsWith("sv_itemlabel"))
							{
								list.add(item.get(key));
							}
						}
						HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
						ArrayList<Integer> tempList = new ArrayList<Integer>();
						int tempInt;
						for(String version : list){
							tempInt = Integer.parseInt(version.substring(0, version.indexOf('.')));
							tempList.add(tempInt);
							tempMap.put(tempInt, version);
						}
						Collections.sort(tempList);
						list = new ArrayList<String>();
						for(int index : tempList){
							list.add(tempMap.get(index));
						}

						for(String ver : list){
							cbitem = new Comboitem();
							cbitem.setLabel(ver);
							cbitem.setValue(ver);
							cbitem.setParent(cb);
						}
					}else{
						ArrayList<String> list = new ArrayList<String>();
						for (String key : item.keySet())
						{
							if (key.startsWith("sv_itemlabel"))
							{
								list.add(item.get(key));
							}
						}
						Collections.sort(list);
						for(String ver : list){
							cbitem = new Comboitem();
							cbitem.setLabel(ver);
							cbitem.setValue(ver);
							cbitem.setParent(cb);
						}
					}

					
					if (("_unix".equals(this.entityTemplate.get_sv_id())) && ("_OsType".equals(item.get("sv_name"))))
					{
						InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("oscmd.properties");
						Properties oscmd = new Properties();
						try
						{
							oscmd.load(inputStream);
							
							HashMap<String, HashMap<String, String>> oss = new HashMap<String, HashMap<String, String>>();
							if (!oscmd.isEmpty())
							{
								Iterator iter = oscmd.keySet().iterator();
								while (iter.hasNext())
								{
									
									String str = (String) iter.next();
									if (str.isEmpty())
									{
										continue;
									}
									String substr = str.substring(0, str.indexOf("."));
									if (oss.containsKey(substr))
									{
										oss.get(substr).put(str.substring(str.indexOf(".") + 1, str.length()), oscmd.getProperty(str));
									} else
									{
										HashMap<String, String> keyvalue = new HashMap<String, String>();
										String valu = oscmd.getProperty(str);
										valu = new String(valu.getBytes("GBK"));
										keyvalue.put(str.substring(str.indexOf(".") + 1, str.length()), valu);
										oss.put(substr, keyvalue);
									}
									
								}
								
							}
							
							for (String key : oss.keySet())
							{
								if (key.isEmpty())
								{
									continue;
								}
								HashMap<String, String> os = oss.get(key);
								if (os.get("hidden").trim().equals("true"))
								{
									continue;
								}
								cbitem = new Comboitem();
								cbitem.setLabel(os.get("description"));
								cbitem.setValue(os.get("name"));
								cbitem.setParent(cb);
							}
							
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
						
					}
					// 添加Unix设备，根据所选的连接方式，自动填写端口
					if ("_unix".equals(this.entityTemplate.get_sv_id()))
					{
						if (item.get("sv_name").equals("_ProtocolType"))
						{
							cb.addEventListener("onChange", new comboboxOnchange());
						}
					}
					
				} else
				{
					cbitem = new Comboitem();
					cbitem.setLabel("正在获取数据...");
					cbitem.setValue("1");
					cbitem.setParent(cb);
					cb.setSelectedIndex(0);
					cbdydata = cb;
					
				}
				cb.setId("svctrl" + item.get("sv_name"));
				if ("_unix".equals(this.entityTemplate.get_sv_id()))
				{
					String cbvalue;
					try{
						cbvalue = entityEdit.getProperty().get(cb.getSvdbField());
					}catch(Exception e){
						cbvalue = "2";
					}
					cb.setSvdbValue(cbvalue);
				} else
				{
					cb.setSvdbValue(item.get("sv_value"));
				}
				label = new Label();
				// label.setId("lbl_" + item.get("sv_name"));
				if ("false".equals(item.get("sv_allownull")))
				{
					label.setValue(item.get("sv_label") + "*:");
				} else
				{
					label.setValue(item.get("sv_label") + ":");
				}
				
				vbox.appendChild(cb);
				vbox.appendChild(labelh);
				row = new Row();
				label.setParent(row);
				vbox.setParent(row);
				row.setParent(baserow);
				// cb.focus();
				// cb.setFocus(true);
				cb.setReadonly(true);
				
			} else
			{
				// Messagebox.show("未知的控件类型:" + item.get("sv_type"));
			}
			
			// 添加到主窗体
			
		}
		label = new Label();
		label.setId("lbl_title");
		label.setValue("标题*:");
		tbTitle = new SvdbTextBox();
		tbTitle.setWidth("350px");
		tbTitle.setHeight("15px");
		tbTitle.setName("tb_title");
		tbTitle.setSvdbField("sv_name");
		tbTitle.setHelptext("设备的显示名称");
		tbTitle.addEventListener("onChange", new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				if(tbTitle!=null && tbTitle.getValue().trim().length()>0){
					btnok.setDisabled(false);
					btnadd.setDisabled(false);
					issave = false;
					autoName = false;
				}else{
					autoName = true;
				}
			}
		});
		
		vbox = new Vbox();
		Label labelh = new Label();
		labelh.setId("lbp" + idcount);
		labelh.setValue("设备的显示名称");
		labelh.setSclass("helplabel");
		labelh.setVisible(false);
		idcount++;
		// tbTitle.addEventListener("onChange", new TitleOnchange());
		vbox.appendChild(tbTitle);
		vbox.appendChild(labelh);
		row = new Row();
		label.setParent(row);
		vbox.setParent(row);
		row.setParent(baserow);
		
	}
	
	/**
	 * 构建界面数据
	 * 
	 * @param entityedit
	 */
	private void buildUIdata(EntityEdit entityedit)
	{
		for (Map<String, String> item : entityTemplate.getItems())
		{
			if (item.get("sv_hidden") != null)
			{
				if (item.get("sv_hidden").trim().toLowerCase().equals("true"))
				{
					continue;
				}
			}
			HtmlBasedComponent control = null;
			try
			{
				control = (HtmlBasedComponent) WAddEntity.getFellow("svctrl" + item.get("sv_name"));
			} catch (Exception ex)
			{
				
			}
			if (control == null)
			{
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;
			String svalue = entityedit.getProperty().get(item.get("sv_name"));
			svControl.setSvdbValue(svalue);
		}
		
	}
	
	public class GetDydataOnCreate implements EventListener
	{
		private Comboitem		item;
		private SvdbComboBox	cb;
		
		public GetDydataOnCreate()
		{
			
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			try
			{
				cb = (SvdbComboBox) arg0.getTarget();
				if (cb.getItemCount() > 1)
				{
					
					return;
				}
				if (dytimer != null)
				{
					return;
				}
				entityEdit.startEntityDynamicData(view);
				dytimer = new Timer();
				dytimer.setParent(WAddEntity);
				dytimer.setDelay(1000);
				dytimer.setRepeats(true);
				dytimer.setRunning(false);
				dytimer.addEventListener("onTimer", new ontime(cb));
				dytimer.start();
			} catch (Exception ex)
			{
				
			}
			// try
			// {
			// cb = (SvdbComboBox) arg0.getTarget();
			// if (cb.getItemCount() > 1)
			// {
			//					
			// return;
			// }
			// entityEdit.startEntityDynamicData(view);
			// Map<String, String> dydata = null;
			// while(dydata==null)
			// {
			// logger.info(" get entity Dynamic Data ");
			// Thread.sleep(1000);
			// dydata= entityEdit.getEntityDynamicData(view);
			// }
			//				
			// TreeMap<String, String> sortdydata = new TreeMap<String, String>();
			//				
			// for (String key : dydata.keySet())
			// {
			// String Key = dydata.get(key);
			// String Value = key;
			// if (!sortdydata.containsKey(Key))
			// {
			// sortdydata.put(Key, Value);
			//						
			// }
			// }
			// cb.getItems().clear();
			// this.cb.setValue("");
			// for (String key : sortdydata.keySet())
			// {
			// item = new Comboitem();
			// item.setLabel(key);
			// item.setValue(sortdydata.get(key));
			// item.setParent(cb);
			// }
			// cb.setSelectedIndex(0);
			// if (isedit)
			// {
			// String cbvalue = entityEdit.getProperty().get(cb.getSvdbField());
			// if (cbvalue != null)
			// {
			// cb.setSvdbValue(cbvalue);
			// }
			// }
			//				
			// } catch (Exception e)
			// {
			//				
			// this.cb.getItems().clear();
			// this.cb.setValue("");
			//				
			// }
			
		}
	}
	
	public class ontime implements EventListener
	{
		SvdbComboBox		cb;
		Boolean				stoptag	= false;
		private Comboitem	item;
		
		public ontime(SvdbComboBox cb)
		{
			this.cb = cb;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			if (stoptag)
			{
				try
				{
					((Timer) arg0.getTarget()).stop();
					((Timer) arg0.getTarget()).setRepeats(false);
				} catch (Exception e)
				{
					
				}
				return;
			}
			try
			{
				
				Map<String, String> dydata = null;
				
				dydata = entityEdit.getEntityDynamicData(view);
				if (dydata == null)
				{
					return;
				} else
				{
					stoptag = true;
				}
				TreeMap<String, String> sortdydata = new TreeMap<String, String>();
				
				for (String key : dydata.keySet())
				{
					String Key = dydata.get(key);
					String Value = key;
					if (!sortdydata.containsKey(Key))
					{
						sortdydata.put(Key, Value);
						
					}
				}
				cb.getItems().clear();
				this.cb.setValue("");
				for (String key : sortdydata.keySet())
				{
					item = new Comboitem();
					item.setLabel(key);
					item.setValue(sortdydata.get(key));
					item.setParent(cb);
				}
				cb.setSelectedIndex(0);
				if (isedit)
				{
					String cbvalue = entityEdit.getProperty().get(cb.getSvdbField());
					if (cbvalue != null)
					{
						cb.setSvdbValue(cbvalue);
					}
				}
				
			} catch (Exception e)
			{
				
				this.cb.getItems().clear();
				this.cb.setValue("");
				stoptag = true;
			}
		}
	}
	
	/**
	 * 标题设置
	 * 
	 * @author Administrator 添加Unix设备，根据所选的连接方式，自动填写端口
	 */
	public class comboboxOnchange implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			String va = ((SvdbComboBox) arg0.getTarget()).getValue();
			if ("SSH".equals(va))
			{
				tbport.setValue("22");
			} else if ("Telnet".equals(va))
			{
				tbport.setValue("23");
			} else
			{
				tbport.setValue("22");
			}
			
		}
		
	}
	
	/**
	 * 标题设置
	 * 
	 * @author Administrator
	 * 
	 */
	public class TextBoxOnchange implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			if(!autoName){
				return;
			}
			//((InputEvent)arg0).getValue()
			String templatename = entityTemplate.get_sv_name();
			String nm=((InputEvent)arg0).getValue();
			String nmCopy = nm.replace(" ", "");
//			((SvdbTextBox)arg0.getTarget()).setValue(nmCopy);
			tbTitle.setSvdbValue(nmCopy + "(" + templatename + ")");
			if (tbTitle.getSvdbValue().isEmpty())
			{
				btntest.setDisabled(true);
			} else
			{
				btntest.setDisabled(false);
			}
		}
		
	}
	
	/**
	 * 标题改变
	 * 
	 * @author Administrator
	 * 
	 */
	public class TitleOnchange implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			
		}
		
	}
	
	// <summary>
	// 验证
	// </summary>
	// <param name="message"></param>
	// <returns></returns>
	public String validate()
	{
		String message = "";
		for (Map<String, String> item : entityTemplate.getItems())
		{
			if (item.get("sv_hidden") != null)
			{
				if (item.get("sv_hidden").trim().toLowerCase().equals("true"))
				{
					continue;
				}
			}
			try
			{
				control = (HtmlBasedComponent) WAddEntity.getFellow("svctrl" + item.get("sv_name"));
			} catch (Exception ex)
			{
			}
			if (control == null)
			{
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;
			if (("false".equals(item.get("sv_allownull")) && svControl.getSvdbValue().isEmpty())
				||("false".equals(item.get("sv_allownull")) && "".equals(svControl.getSvdbValue().trim())))
			{
				control.focus();
				message = item.get("sv_tip");
				if (message == null)
				{
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}
			if ("false".equals(item.get("sv_allownull")) && "true".equals(item.get("sv_isnumeric")) && !svControl.getSvdbValue().matches(REG_DIGIT))
			{
				
				control.focus();
				message = item.get("sv_tip") + "(数字)";
				if (message == "null(数字)")
				{
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}
			
		}
		
		message = "";
		if (tbTitle.getSvdbValue().trim().isEmpty())
		{
			message = "标题不能为空";
			return message;
		}
		return null;
	}
	
	/**
	 * 构建设备数据
	 */
	private void buildEntityData()
	{
		this.buildBaseData();
		
		for (Map<String, String> item : entityTemplate.getItems())
		{
			if (item.get("sv_hidden") != null)
			{
				if (item.get("sv_hidden").trim().toLowerCase().equals("true"))
				{
					continue;
				}
			}
			HtmlBasedComponent control = null;
			try
			{
				control = (HtmlBasedComponent) WAddEntity.getFellow("svctrl" + item.get("sv_name"));
			} catch (Exception ex)
			{
				
			}
			if (control == null)
			{
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;			//getItemAtIndex(j).getValue()
			if("_ProtocolType".equals(svControl.getSvdbField())){
				if("Telnet".equals(svControl.getSvdbValue())){
					this.entityEdit.getProperty().put(svControl.getSvdbField(), "2");
				}
				if("SSH".equals(svControl.getSvdbValue())){
					this.entityEdit.getProperty().put(svControl.getSvdbField(), "1");
				}
			}else{
				this.entityEdit.getProperty().put(svControl.getSvdbField(), svControl.getSvdbValue());
			}
		}
		
	}
	
	/**
	 * 构建基本数据
	 */
	private void buildBaseData()
	{
		this.entityEdit.getProperty().put("sv_name", tbTitle.getValue());
		String svnetwork = this.entityTemplate.get_sv_network() ? "true" : "false";
		// 是否为网络设备
		this.entityEdit.getProperty().put("sv_network", svnetwork);
		if(!this.isedit)
			this.entityEdit.getProperty().put("creat_timeb", Toolkit.getToolkit().formatDate());
		
		this.entityEdit.getProperty().put("sv_description", tbDescription.getValue());
		String value = (String) tbDepends.getAttribute("value");
		this.entityEdit.getProperty().put("sv_dependson", value);
		String DependsCondition = "3";
		if (rdWarning.isChecked())
		{
			DependsCondition = "2";
		}
		if (rdGood.isChecked())
		{
			DependsCondition = "1";
		}
		this.entityEdit.getProperty().put("sv_dependscondition", DependsCondition);
	}
	
	private void savedata() throws Exception 
	{
		String message = validate();
		if (message != null)
		{
			throw new Exception(message);
		}
		if(issave)
		{
			return;
		}else
		{
			issave=true;
			btnok.setDisabled(true);
			btnadd.setDisabled(true);
		}
		// 构建数据
		this.buildEntityData();
		// 保存数据
		try
		{
		 Boolean savesuccess=	this.entityEdit.teleSave(this.view);
			
			// Toolkit.expandTreeAndShowList(tree.getDesktop(), node);
			// Messagebox.show("添加设备成功");
			entityEdit.setName(tbTitle.getValue());
			INode[] ids = new INode[] { entityEdit };
			if (isedit)
			{
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
			} else
			{
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_ADD);
			}
			String name=tbTitle.getValue();
			String id=entityEdit.getSvId();
			if (savesuccess)
			{
				String loginname = view.getLoginName();
				String minfo = "";

				if (isedit )
				{
					minfo = "编辑设备：" + name + "(" + node.getSvId() + ") " ;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.entity);
				} else
				{
					minfo = "添加设备：" + name+ "(" +id + ") parent is " + node.getName() + "(" + node.getSvId() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.entity);
				}
				Session session = Executions.getCurrent().getDesktop().getSession();
				session.setAttribute(ConstantValues.RefreshEntityId, id);
			
			}
			
		} catch (Exception ex)
		{
			
		}
		if (entityTemplate.get_sv_quickadd() != null && !isedit)
		{
			// 快速添加
			WAddEntity.detach();
			final Window win = (Window) Executions.createComponents(FastAdd_TargetUrl, null, null);
			win.setAttribute("entityEdit", entityEdit);
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", eccTimer);
			win.setAttribute("templateId", WAddEntity.getAttribute("templateId"));
			try
			{
				win.doModal();
			} catch (SuspendNotAllowedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			WAddEntity.detach();
		}
		Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
	}
	
	/**
	 * 添加
	 * 
	 * @throws InterruptedException
	 */
	public void onClick$btnok()
	{
		try {
			savedata();
		} catch (Exception e) {
			try {
				Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				if(e.getMessage().contains("请检查输入的")){
					control.focus();
				}else if("标题不能为空".equals(e.getMessage())){
					tbTitle.focus();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public void onOK$WAddEntity()
	{
		try {
			savedata();
		} catch (Exception e) {
			try {
				Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				if(e.getMessage().contains("请检查输入的")){
					control.focus();
				}else if("标题不能为空".equals(e.getMessage())){
					tbTitle.focus();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void onClick$btnadd()
	{
		try {
			savedata();
		} catch (Exception e) {
			try {
				Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				if(e.getMessage().contains("请检查输入的")){
					control.focus();
				}else if("标题不能为空".equals(e.getMessage())){
					tbTitle.focus();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void onClick$btnDepends()
	{
		final Window win = (Window) Executions.createComponents(Dependontree_TargetUrl, null, null);
		win.setAttribute("tb", tbDepends);
		try
		{
			win.doModal();
		} catch (Exception e)
		{
		}
	}
	
	public void onClick$btnhelp()
	{
		for (int i = 1; i < idcount; i++)
		{
			Label lb = null;
			try
			{
				lb = (Label) WAddEntity.getFellow("lbp" + i);
			} catch (Exception ex)
			{
				
			}
			if (lb != null)
			{
				lb.setVisible(!lb.isVisible());
			}
		}
		WAddEntity.setPosition("center");
	}
	
	/**
	 * 取消
	 */
	public void onClick$btncancel()
	{
		WAddEntity.detach();
		// eccbody.setSrc("/main/eccbody.zul?type=" + node.getType() + "&id=" + svid);
		
	}
	
	// public void onClick$btnup()
	// {
	// eccbody.setSrc("/main/TreeView/EntityList.zul");
	// }
	
}
