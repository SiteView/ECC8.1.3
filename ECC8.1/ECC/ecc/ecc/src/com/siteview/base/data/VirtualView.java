package com.siteview.base.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.RetMapInVector;
import com.siteview.base.manage.View;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;

/**
 * public VirtualView(String viewName, String fileName,IniFile userini, View view)
 */
public class VirtualView implements Serializable
{
	private static final long serialVersionUID = 1678812865091450493L;
	private String										m_name;
	private String										m_filename;
	private IniFile										m_userini;
	private View										m_view;
	private Map<String, VirtualItem>		m_items		= new LinkedHashMap<String, VirtualItem>();
	private VirtualItem						m_root_item	
		= new VirtualItem("", Item, null, VirtualItem.ECCRoot.zulName, VirtualItem.ECCRoot.zulType, new HashMap<String, String>());
	
	private String										m_item_todel;
	private Map<String, Map<String, String>>	        m_item_add;
	private String										m_item_newid;
	private String										m_item_oldid;
	private IniFile										m_item_add_WithConstruction;
	
	private Map<String, String>						    m_ldata;
	private boolean										m_has_loaded= false;
	
	/**
	 * Ĭ��������ͼ,������ڵ��Զ�����,���Ƕ�ȡ�����ļ�ȡ��
	 */
	public final static String							DefaultView		= "Ĭ����ͼ";// "defaultVirtualView";
																													
	public final static String							CreateNewView	= "���½�������ͼ��";
	public final static String							Garbage			= "�����䣬����ɾ��";
	public final static String							NewVirGroup		= "�����飬�������";

	public final static String							GarbageType		= "garbage";
	public final static String							NewVirGroupType	= "newVirGroup";
	public final static String							ViewEdittingType= "virtualViewEditting";
	
	/**
	 * ����ڵ�����Ϊ���ڵ�,���� View.getNode(String id) ȡ�øü��ڵ�
	 */
	public final static String							INode			= "INode";
	
	/**
	 * ����ڵ�����Ϊ�̶����ڵ�,���� VirtualItem.getItemData() ȡ����������
	 */
	public final static String							Item			= "Item";
	
	/**
	 * @param viewName ��������ͼ������, ��ʾ�������б���
	 * @param fileName ��������ͼ�������ļ���,���ھ���������Ŀ������
	 * @param userini ���û����ڹ̶����ڵ�����ã��������ԭ7.0�������˵���Ŀ��
	 * @param viewini ��������ͼ���ڹ̶����ڵ������
	 * @param view ��������û��ĺ�̨���
	 */
	public VirtualView(String viewName, String fileName,IniFile userini, View view)
	{
		m_name = viewName;
		m_filename= fileName;
		m_userini= userini;
		m_view= view;
		
		m_items.put(getRootItemId(), m_root_item);
		m_ldata= view.getLoginData();
	}
	
	/**
	 * �Ƿ�Ĭ��������ͼ������ǣ�������ڵ��Զ�����,���Ƕ�ȡ�����ļ�ȡ��
	 */
	public boolean isDefaultView()
	{
		return m_name.equals(DefaultView);
	}
	
	/**
	 * ȡ��������ͼ������,������ʾ 
	 */
	public String getViewName()
	{
		return m_name;
	}
	
	/**
	 * ȡ��������ͼ���ļ��� 
	 */
	public String getFileName()
	{
		return m_filename;
	}
	
	/**
	 * ȡ���������ĸ� ItemId, ��ֵΪ���� , �ø���û����ʾ��ҳ����
	 */
	public String getRootItemId()
	{
		return m_root_item.getItemId();
	}
	
	/**
	 * ȡ���������Ķ�������ڵ� 
	 */
	public ArrayList<VirtualItem> getTopItems()
	{
		m_has_loaded= true;
		if( isDefaultView() )
			createDefault();
		else
			loadDataOfViewIni();
		return getSonItems(m_root_item);
	}

	/**
	 * ȡ��ĳ������ڵ���ӽڵ� 
	 */
	public ArrayList<VirtualItem> getSonItems(VirtualItem item)
	{		
		ArrayList<VirtualItem> ret= new ArrayList<VirtualItem>();
		if(item==null)
			return ret;
		
		ArrayList<String> a= item.getSonList();
		for(String id:a)
		{
			VirtualItem i= m_items.get(id);
			if(i!=null)
				ret.add(i);
		}
		return ret;
	}
	
	/**
	 * �õ�½�û��Ƿ����Ա 
	 */
	public boolean isAdmin()
	{
		if(m_userini==null)
			return false;
		String s= m_userini.getValue(m_userini.getSections(), "nAdmin");
		if(s!=null && s.compareTo("1")==0)
			return true;
		return false;
	}
	
	private boolean checkLicenseInDog(ZulItem zi)
	{
//		 (ģ����Ȩ)subMenu= XXX (���磺 101110000000000 , ���й�ʱ���д�ֵ)
		// 0  ���
		// 1  ����
		// 2  ����
		// 3  ���� 
		// 4  ����
		
		int index= 100;
		if(zi.zulType.compareTo("TopoView")==0 )
			index= 1;
		if(zi.zulType.contains("Alert"))
			index= 2;
		if(zi.zulType.contains("Report"))
			index= 3;	
		if(zi.zulType.contains("Set"))
			index= 4;
		
		if( index>50 )
			return true;
		if(m_ldata==null)
			return true;
		String license= m_ldata.get("subMenu");
		if(license==null || license.isEmpty())
			return true;
		if( (index+1) > license.length())
			return true;
		if( license.charAt(index)=='1')
			return true;
		
		return false;
	}
	
	/**
	 * û���κ�һ��editMonitor=1���� ����������á� ���ܽڵ��й� 
	 */
	private boolean notEvenOne_editMonitor()
	{
		try
		{
			if(m_userini==null)
				return true;
			
			String groupright = m_userini.getValue(m_userini.getSections(), "groupright");
			if(groupright==null || groupright.isEmpty())
				return true;
			
			String[] ids = groupright.split(",");
			if(ids==null || ids.length ==0 )
				return true;
			
			for (String key : ids) 
			{
				String value= m_userini.getValue(m_userini.getSections(), key);
				if(value==null)
					continue;
				if (value.contains("editmonitor=1")) 
					return false;
			}
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * �ܷ���Ӹù̶����ڵ�
	 */
	public boolean canAddItem(ZulItem zi)
	{
		if(!checkLicenseInDog(zi))
			return false;
			
		if(isAdmin() || zi.license.isEmpty())
			return true;
		if( zi.zulType.equals(VirtualItem.SetMonitor.zulType) )
		{
			if(notEvenOne_editMonitor())
				return false;
		}
		if(m_userini==null)
			return false;
		
		//m_AlertRuleAdd, m_AlertRuleDel, m_AlertRuleEdit
		//m_reportlistAdd, m_reportlistDel, m_reportlistEdit
		int count=1;
		if( zi.license.compareTo("m_AlertRuleAdd")==0 || zi.license.compareTo("m_reportlistAdd")==0 )
			count=3;
		
		for (int index = 1; index <= count; ++index)
		{
			String license= zi.license;
			if(zi.license.compareTo("m_AlertRuleAdd")==0 && index==2)
				license= "m_AlertRuleDel";
			if(zi.license.compareTo("m_AlertRuleAdd")==0 && index==3)
				license= "m_AlertRuleEdit";
			
			if(zi.license.compareTo("m_reportlistAdd")==0 && index==2)
				license= "m_reportlistDel";
			if(zi.license.compareTo("m_reportlistAdd")==0 && index==3)
				license= "m_reportlistEdit";
			
			String s = m_userini.getValue(m_userini.getSections(), license);
			if (s != null && s.compareTo("1") == 0)
				return true;
		}
		return false;
	}
	
	/**
	 * �����޸�һ���̶����ڵ㣬��Ҫ������ saveChange()
	 * <br/> Item ���ܹ��� INode �� 
	 * @param itemId ���޸ĵĽڵ�� id ,��ֵ��Ϊ null ���½�һ���̶����ڵ�
	 */
	public boolean changeItem(String itemId, String parentItemId, ZulItem zi, Map<String, String> map) throws Exception
	{
		if( isDefaultView() )
			throw new Exception(" DefaultView can't be changed ! ");
		if (!m_has_loaded)
			getTopItems();
		if( m_item_add==null )
			m_item_add= new HashMap<String, Map<String, String>>();	
	
		if(map==null)
			map= new HashMap<String, String>();
		if (zi != null)
		{
			if(!canAddItem(zi))
				throw new Exception(" You are not licensed to add " +zi.zulName + "! ");
			map.put("CheckNothing", "true");
			map.put("zul_type", zi.zulType);
			map.put("zul_name", zi.zulName);
		}
		if(map.isEmpty())
			throw new Exception(" Something wrong( map is empty ) ! ");
		
		if(parentItemId==null)
			throw new Exception(" parentItemId is null ! ");	
		VirtualItem pv = m_items.get(parentItemId);
		if (parentItemId.equals(ViewEdittingType))
		{
			pv = m_root_item;
			parentItemId= "";
		}
		if (pv == null)
			throw new Exception("  parent VirtualItem(" + parentItemId + ") is null! ");
		
		if(zi!=null)
		{
			if( pv.getType().equals(INode) )
				throw new Exception("  A Item's parent can't be a INode! ");
			String pztype= pv.getItemDataZulType();
			if(pztype==null)
				throw new Exception("  parent VirtualItem is a ITem, but it's zul_type is null ! ");
			if (pztype.equals(VirtualItem.WholeView.zulType) || pztype.equals(VirtualItem.VirtualGroup.zulType))
			{
				if ( !zi.zulType.equals(VirtualItem.VirtualGroup.zulType))
					throw new Exception(" WholeView or VirtualGroup's son can only be a VirtualGroup or INode! ");
			}
			if ( zi.zulType.equals(VirtualItem.VirtualGroup.zulType))
			{
				if (!pztype.equals(VirtualItem.WholeView.zulType) && !pztype.equals(VirtualItem.VirtualGroup.zulType))
					throw new Exception(" VirtualGroup's parent can only be WholeView or VirtualGroup! ");
			}
		}
		
		if (itemId == null && !parentItemId.isEmpty())
		{
			map.put("parent_item_id", parentItemId);
			Integer count = new Integer(m_item_add.size() + 1);
			String index = "i-" + count;
			while (m_item_add.containsKey(index))
				index = "i-" + ++count;
			m_item_add.put(index, map);
		} else
		{
			if (itemId == null)
			{	
				String selfItemId =  pv.recommendNextSonId().toString();
				if(parentItemId.isEmpty())
					itemId = selfItemId;
				else
					itemId = parentItemId + "." + selfItemId;			
			}
			
			if( !getParentId(itemId).equals(parentItemId) )
				throw new Exception(" getParentId( itemId ) != parentItemId ");
			m_item_add.put(itemId, map);
		}
		return true;
	}
	
	/**
	 * �����½�һ���̶����ڵ㣬��Ҫ������ saveChange()��
	 * <br/> <br/> Item ���ܹ��� INode �� 
	 * <br/> ÿ�� saveChange ������������ڵ㣬������ڵ��Ҳ����丸�ڵ�
	 * @param parentItemId ���ڵ�� item_id ����ֵ����Ϊ null
	 * @param zi �ýڵ��ZulItem 
	 * @param map �ýڵ�������������ݣ����������������Ҫ��������
	 */
	public boolean addItem(String parentItemId, ZulItem zi, Map<String, String> map)  throws Exception
	{
		return changeItem(null, parentItemId, zi, map);
	}
	
	/**
	 * �����޸�һ�����ڵ㣬��Ҫ������ saveChange()
	 * <br/> INode ֻ�ܹ��� WholeView��VirtualGroup��INode ��, ������²��ɹ��κνڵ� 
	 * @param itemId ���޸ĵĽڵ�� id ,��ֵ��Ϊ null ���½�һ���̶����ڵ�
	 */
	public boolean changeINode(String itemId, String parentItemId, INode node, boolean withAllSubMonitor)  throws Exception
	{
		//�� VirtualGroup ��ͬʱ�� monitor �� entity ,��ʹ���������Щ  monitor
		//��һ�� entity û�� withAllSubMonitor ,�ᵼ�� EccTreeItem.refreshStatus() �����޷�������
		
		if(parentItemId==null)
			throw new Exception(" parentItemId is null ! ");
		VirtualItem pv = m_items.get(parentItemId);
		if (pv == null)
			throw new Exception("  parent VirtualItem:" + parentItemId + " is null! ");
		if( pv.getType().equals(INode))
		{
			String pid= pv.getSvId();
			INode pnode= m_view.getNode(pid);
			if(pnode==null)
				throw new Exception("  parent INode is null ! ");
			if(pnode.getType().equals(com.siteview.base.tree.INode.MONITOR))
				throw new Exception("  parent INode is a Monitor ! ");
			if (node.getType().equals(com.siteview.base.tree.INode.MONITOR))
			{
				if (!pnode.getType().equals(com.siteview.base.tree.INode.ENTITY) && !pnode.getType().equals(com.siteview.base.tree.INode.GROUP))
					throw new Exception(" Monitor's parent can only be a Entity or Group ! ");;
			}
			if (node.getType().equals(com.siteview.base.tree.INode.ENTITY) || node.getType().equals(com.siteview.base.tree.INode.GROUP))
			{
				if (!pnode.getType().equals(com.siteview.base.tree.INode.GROUP))
					throw new Exception(" Parent of a Entity(Group) can only be a Group ! ");;
			}
		}
		else
		{
			String ztype= pv.getItemDataZulType();
			if(ztype==null)
				throw new Exception("  parent VirtualItem is a ITem, but it's zul_type is null ! ");
			if(!ztype.equals(VirtualItem.WholeView.zulType) && !ztype.equals(VirtualItem.VirtualGroup.zulType))
				throw new Exception("  A INode's parent can only be WholeView or VirtualGroup or INode! ");
		}
		
		
		Map<String, String> map= new HashMap<String, String>();
		if(withAllSubMonitor)
			map.put("withAllSubMonitor", "true");
		map.put("type", node.getType());
		map.put("sv_id", node.getSvId());
		return changeItem(itemId, parentItemId, null, map);
	}
	
	/**
	 * �����½�һ�����ڵ㣬��Ҫ������ saveChange()
	 * <br/> INode ֻ�ܹ��� WholeView��VirtualGroup��INode ��, ������²��ɹ��κνڵ� 
	 * <br/> ÿ�� saveChange ������������ڵ㣬������ڵ��Ҳ����丸�ڵ�
	 * @param parentItemId ���ڵ�� item_id ����ֵ����Ϊ null
	 * @param withAllSubMonitor �Ƿ��ڶ�ȡʱ���Զ������ýڵ��µ����м����
	 */
	public boolean addINode(String parentItemId, INode node, boolean withAllSubMonitor)  throws Exception
	{
		return changeINode(null, parentItemId, node, withAllSubMonitor);
	}
	
	/**
	 * �����½�һ�����ڵ㼰�������ݹ��¼��ṹ
	 * @param parentItemId ���ڵ�� item_id ����ֵ����Ϊ null
	 */
	public boolean addINodeWithConstruction(String parentItemId, INode node)  throws Exception
	{
		cancelAllChange();
		try
		{
			addINode(parentItemId, node, false);
			cancelAllChange();
			
			VirtualItem pv = m_items.get(parentItemId);
			if (parentItemId.equals(ViewEdittingType))
				pv = m_root_item;
			if (pv == null)
				throw new Exception("  parent VirtualItem:" + parentItemId + " is null! ");
		
			String newid=null;
			if (parentItemId.equals(ViewEdittingType))
				newid = pv.recommendNextSonId().toString();
			else
				newid = parentItemId + "." + pv.recommendNextSonId().toString();
			
			m_item_add_WithConstruction= new IniFile(m_filename);
			addInodeCons(node, newid);
		} catch (Exception e)
		{
			cancelAllChange();
			throw e;
		}
		m_item_todel=null;
		m_item_add=null;
		m_item_newid = null;
		m_item_oldid = null;
		return true;
	}
	
	private void addInodeCons(INode node, String newid)throws Exception
	{
		if(node==null)
			return;
		m_item_add_WithConstruction.createSection(newid);
		m_item_add_WithConstruction.setKeyValue(newid, "sv_id", node.getSvId());
		m_item_add_WithConstruction.setKeyValue(newid, "type", node.getType());
		if (node.getType().equals(com.siteview.base.tree.INode.MONITOR))
			return;
		
		IForkNode f = (IForkNode) node;
		List<String> a = f.getSonList();
		int size = a.size();
		Integer index= new Integer(0);
		for (int i = 0; i < size; ++i)
		{
			String id= a.get(i);
			INode n = m_view.getNode(id);
			String sonid= newid + "." + ++index;
			addInodeCons(n, sonid);
		}
	}
	
	/**
	 * �����ƶ�һ���ڵ㼰�������ӽڵ㣬��Ҫ������ saveChange()
	 * <br/> �÷����ᶪʧ֮ǰ���е��޸�
	 */
	public boolean moveItem(String itemId, String newParentItemId)  throws Exception
	{
		cancelAllChange();
		if(itemId.isEmpty() || newParentItemId.isEmpty())
			throw new Exception(" itemId or newParentItemId is empty! ");
		if(itemId.equals(newParentItemId))
			throw new Exception(" itemId equals newParentItemId! ");
		if(newParentItemId.startsWith(itemId))
			throw new Exception(" "+newParentItemId+" is descendant of " + itemId + "! ");
		VirtualItem mv = m_items.get(itemId);
		if (mv == null)
			throw new Exception("  Item to move:" + itemId + " is null! ");
		VirtualItem pv = m_items.get(newParentItemId);
		if (newParentItemId.equals(ViewEdittingType))
			pv = m_root_item;
		if (pv == null)
			throw new Exception("  new parent VirtualItem:" + newParentItemId + " is null! ");
		
		try
		{
			if(mv.getType().equals(INode))
			{
				INode node= m_view.getNode(mv.getSvId());
				if(node==null)
					throw new Exception(" INode of " + itemId + " is null! ");
				addINode(newParentItemId, node, mv.isWithAllSubMonitor());
			}
			else
			{
				ZulItem zi= new ZulItem(mv.getItemDataZulType(), mv.getItemDataZulName(), "");
				addItem(newParentItemId, zi, mv.getItemData());
			}
			if (newParentItemId.equals(ViewEdittingType))
				m_item_newid = pv.recommendNextSonId().toString();
			else
				m_item_newid = newParentItemId + "." + pv.recommendNextSonId().toString();
			m_item_oldid = itemId;
		} catch (Exception e)
		{
			cancelAllChange();
			throw e;
		}
		m_item_todel=null;
		m_item_add=null;
		m_item_add_WithConstruction= null;
		return true;
	}
	
	
	private void delSelfAndSubItem(VirtualItem v)
	{
		if(v==null)
			return;
		ArrayList<VirtualItem> sis= getSonItems(v);
		for(VirtualItem si: sis)
			delSelfAndSubItem(si);
		m_items.remove(v);
	}
	
	/**
	 * ɾ��һ���ڵ㣬��Ҫ������ saveChange()
	 */
	public void deleteItem(String ItemId) throws Exception
	{
		if( isDefaultView() )
			throw new Exception(" DefaultView can't be changed! ");
		if (ItemId == null || ItemId.isEmpty())
			throw new Exception(" ItemId is null or empty! ");	
		
		VirtualItem item = m_items.get(ItemId);
		if (item == null)
			throw new Exception("  VirtualItem:" + ItemId + " is null! ");
		if( item.getType().equals(INode))
		{
			String pid= item.getParentItemId();
			VirtualItem pi = m_items.get(pid);
			if(pi.isWithAllSubMonitor())
				throw new Exception("  Parent of VirtualItem:" + ItemId + " is WithAllSubMonitor, so it can't delete ! ");
		}
		delSelfAndSubItem(m_items.get(ItemId));
		
		if (m_item_todel == null)
			m_item_todel = ItemId;
		else
			m_item_todel += ItemId + ",";
	}
	
	/**
	 * ���������������޸�
	 */
	public void cancelAllChange()
	{
		m_item_todel=null;
		m_item_add=null;
		m_item_newid = null;
		m_item_oldid = null;
		m_item_add_WithConstruction= null;
	}

	private void subItemMove(VirtualItem v, String newParentId)throws Exception
	{
		Integer index= new Integer(0);
		ArrayList<VirtualItem> sis= getSonItems(v);
		for(VirtualItem si: sis)
		{
			String oldid= si.getItemId();
			String newid= newParentId + "." + ++index; 
			EditIniFileSection(oldid, newid);
			
			subItemMove(si, newid);
		}
	}
	
	private void itemMove(String itemId, String newItemId)throws Exception
	{
		if( isDefaultView() )
			throw new Exception(" DefaultView can't be changed! ");
		if (itemId == null || itemId.isEmpty())
			throw new Exception(" itemId is null or empty! ");
		if (newItemId == null || newItemId.isEmpty())
			throw new Exception(" newItemId is null or empty! ");
		VirtualItem mv = m_items.get(itemId);
		if (mv == null)
			throw new Exception("  Item to move:" + itemId + " is null! ");
		EditIniFileSection(itemId, newItemId);
		
		try
		{
			subItemMove(mv, newItemId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean EditIniFileSection(String section, String new_section)  throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "EditIniFileSection");
		ndata.put("filename", m_filename);
		ndata.put("section", section);
		ndata.put("user", "default");
		ndata.put("new_section", new_section);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		Thread.sleep(20);
		if (!ret.getRetbool())
		{
			Thread.sleep(100);
			ret = ManageSvapi.GetUnivData(ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to EditIniFileSection of(" + m_filename + "  " + section + "  => " + new_section + ") :" + ret.getEstr());
		}
		return true;	
	}
	
	
	/**
	 * ���޸ı��浽�������ϣ�ÿ�ε��ò�����������ڵ㣬������ڵ��Ҳ����丸�ڵ�
	 * <br/> ����Ϊ withAllSubMonitor �Ľڵ㣬���ӽڵ㣬��Ҫ��ʾ��������ͼ�ı༭������
	 * <br/> ��ʵ�ϣ�monitor �ڵ���Ҳ��Ӧ�ù��ӽڵ�
	 */
	public boolean saveChange() throws Exception
	{
		if( isDefaultView() )
			throw new Exception(" DefaultView can't be changed! ");
		if(m_item_newid != null && m_item_oldid != null)
			itemMove(m_item_oldid, m_item_newid);
		if(m_item_add_WithConstruction!=null)
		{
			m_item_add_WithConstruction.saveChange();
			m_item_add_WithConstruction= null;
		}

		if(m_item_todel!=null && !m_item_todel.isEmpty())
		{
			Map<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "DeleteViewItem");
			ndata.put("fileName", m_filename);
			ndata.put("item_id", m_item_todel);
			RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to DeleteViewItem :" + ret.getEstr());
			m_item_todel= null;
		}
		if(m_item_add!=null && !m_item_add.isEmpty())
		{
			Map<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "AddViewItem");
			ndata.put("fileName", m_filename);
			RetMapInMap ret = ManageSvapi.SubmitUnivData(m_item_add, ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to AddViewItem :" + ret.getEstr());
			m_item_add= null;
		}
		m_has_loaded= false;
		return true;
	}
	
	private String showOneSon(String parentItemId, String selfItemId,String type, String svId, ZulItem zi, Map<String, String> map)
	{
		String id=null;
		VirtualItem pv=null; 
		if(parentItemId==null || !canAddItem(zi))
			return null;
		pv = m_items.get(parentItemId);
		if (pv == null)
			return null;
		
		if (selfItemId == null || selfItemId.isEmpty())
		{
			Integer nextId= pv.recommendNextSonId();
			if( isDefaultView() && parentItemId.equals(getRootItemId()) && !zi.equals(VirtualItem.WholeView) )
			{
				if( nextId==2 )
					nextId += 998;
			}
			selfItemId = nextId.toString();
			if(parentItemId.isEmpty())
				id = selfItemId;
			else
				id = parentItemId + "." + selfItemId;
		}
		else
			id = selfItemId;
		pv.addSonId(id);
		
		VirtualItem v = new VirtualItem(id, type, svId, zi.zulName, zi.zulType, map);
		m_items.put(id, v);
		return id;
	}
	
	private void createDefault()
	{
		boolean isDaMao = false;//��è��ʶ
		IniFile ini_general = new IniFile("general.ini");
		String ismaster_str = "";
		try{
			ini_general.load();
			ismaster_str = ini_general.getValue("version","ismaster");
		}catch(Exception e){e.printStackTrace();}
		
		if("1".equals(ismaster_str)){// ��è
			isDaMao = true;
		}
		
		String rootid= getRootItemId();
		if(isDaMao == false){// Сè �� ��ͨ ecc ���ӵĽڵ�
			showOneSon(rootid, null,  Item, null, VirtualItem.WholeView, new HashMap<String, String>());
		}else{
			VirtualItem.WholeView.zulName = "ECC�������̨";
			showOneSon(rootid, null,  Item, null, VirtualItem.WholeView, new HashMap<String, String>());
		}
//		showOneSon(rootid, null,  Item, null, VirtualItem.TreeView, new HashMap<String, String>());
		
		
		showOneSon(rootid, null,  Item, null, VirtualItem.MonitorBrower, new HashMap<String, String>());
		if(isDaMao == false){// Сè �� ��ͨ ecc ���ӵĽڵ�
			showOneSon(rootid, null,  Item, null, VirtualItem.SetMonitor, new HashMap<String, String>());
		}
		showOneSon(rootid, null,  Item, null, VirtualItem.TopoView, new HashMap<String, String>());
		String alertid= showOneSon(rootid, null,  Item, null, VirtualItem.Alert, new HashMap<String, String>());
		String reportid= showOneSon(rootid, null,  Item, null, VirtualItem.Report, new HashMap<String, String>());
		String setid= showOneSon(rootid, null,  Item, null, VirtualItem.Set, new HashMap<String, String>());
		
		showOneSon(alertid, null,  Item, null, VirtualItem.AlertRule, new HashMap<String, String>());
		showOneSon(alertid, null,  Item, null, VirtualItem.AlertLog, new HashMap<String, String>());
		showOneSon(alertid, null,  Item, null, VirtualItem.AlertStrategy, new HashMap<String, String>());
		
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportStatistic, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportTrend, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportTopN, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportStatus, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportContrast, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.ReportTimeContrast, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.MonitorInfo, new HashMap<String, String>());
		showOneSon(reportid, null,  Item, null, VirtualItem.SysLogQuery, new HashMap<String, String>());
		
		showOneSon(setid, null,  Item, null, VirtualItem.SetGeneral, new HashMap<String, String>());
		if(isDaMao == false){// Сè �� ��ͨ ecc ���ӵĽڵ�
			showOneSon(setid, null,  Item, null, VirtualItem.SetMail, new HashMap<String, String>());
			showOneSon(setid, null,  Item, null, VirtualItem.SetSms, new HashMap<String, String>());
			showOneSon(setid, null,  Item, null, VirtualItem.SetMaintain, new HashMap<String, String>());
		}
		showOneSon(setid, null,  Item, null, VirtualItem.UserManager, new HashMap<String, String>());
		//showOneSon(setid, null,  Item, null, VirtualItem.importDataBase, new HashMap<String, String>());
		
		String taskid= "";
		if(isDaMao == false){// Сè �� ��ͨ ecc ���ӵĽڵ�
			taskid= showOneSon(setid, null,  Item, null, VirtualItem.Task, new HashMap<String, String>());
		}
		showOneSon(setid, null,  Item, null, VirtualItem.SysLogSet, new HashMap<String, String>());
//		showOneSon(setid, null,  Item, null, VirtualItem.BackupRestore, new HashMap<String, String>());
		showOneSon(setid, null,  Item, null, VirtualItem.OperateLog, new HashMap<String, String>());
		showOneSon(setid, null,  Item, null, VirtualItem.m_userOtherPublic, new HashMap<String, String>());
		showOneSon(setid, null,  Item, null, VirtualItem.SystemDiagnosis, new HashMap<String, String>());
		showOneSon(setid, null,  Item, null, VirtualItem.License, new HashMap<String, String>());
		showOneSon(setid, null,  Item, null, VirtualItem.About, new HashMap<String, String>());
		if(isDaMao == false){// Сè �� ��ͨ ecc ���ӵĽڵ�
			showOneSon(taskid, null,  Item, null, VirtualItem.TaskAbsolute, new HashMap<String, String>());
			showOneSon(taskid, null,  Item, null, VirtualItem.TaskPeriod, new HashMap<String, String>());
			showOneSon(taskid, null,  Item, null, VirtualItem.TaskRelative, new HashMap<String, String>());
		}
		eraseNodeChildless(alertid);
		eraseNodeChildless(reportid);
		if(isDaMao == false){
			eraseNodeChildless(taskid);
		}
		eraseNodeChildless(setid);
	}
	
	private void eraseNodeChildless(String id)
	{
		if(id==null)
			return;
		VirtualItem v= m_items.get(id);
		if(v==null)
			return;
		ArrayList<String> a= v.getSonList();
		if (a == null || a.isEmpty())
		{
			m_items.remove(id);
			String pid= getParentId(id);
			VirtualItem pv= m_items.get(pid);
			if(pv!=null)
				pv.eraseSonId(id);
		}
	}
	
	private void loadDataOfViewIni()
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetViewData");
		ndata.put("fileName", m_filename);
		RetMapInVector ret = ManageSvapi.GetForestData(ndata);
		if (!ret.getRetbool())
			return;
		boolean isDaMao = false;//��è��ʶ
		IniFile ini_general = new IniFile("general.ini");
		String ismaster_str = "";
		try{
			ini_general.load();
			ismaster_str = ini_general.getValue("version","ismaster");
		}catch(Exception e){}		
		if("1".equals(ismaster_str)){// ��è
			isDaMao = true;
		}
		
		String rootid= getRootItemId();
		List<Map<String, String>>	vmap = ret.getVmap();
		for(Map<String, String> d: vmap)
		{
			String sv_id= d.get("sv_id");
			String item_id= d.get("item_id");
			if(item_id==null || item_id.isEmpty())
				continue;
			String pid= getParentId(item_id);
			
			if(sv_id==null)
			{
				String zul_type= d.get("zul_type");
				if(zul_type==null || zul_type.isEmpty())
					continue;
				if(isDaMao){//��è ����  �� һЩ ����
					if(zul_type=="SetMonitor"||zul_type=="SetMail"||zul_type=="SetSms"||zul_type=="SetMaintain"){
						continue;
					}
				}
				String zul_name= d.get("zul_name");
				if(zul_name==null || zul_name.isEmpty())
					zul_name= zul_type;
				if(zul_type.equals(VirtualItem.VirtualGroup.zulType))
					zul_name= "�����飺" + zul_name;
				
				ZulItem zi= VirtualItem.allZulItem.get(zul_type);
				if(zi==null)
					continue;
				zi= new ZulItem(zul_type, zul_name, zi.license);
				
				if (pid.isEmpty())
					showOneSon(rootid, item_id, Item, null, zi, new HashMap<String, String>());
				else
				{
					VirtualItem pvi= m_items.get(pid);
					if(pvi==null)
						continue;
					showOneSon(pvi.getItemId(), item_id, Item, null, zi, d);
				}
			}
			
			INode node = null;
			if (sv_id != null)
				node = m_view.getNode(sv_id);
			if(node!=null)
			{
				VirtualItem pv = m_items.get(pid);
				if (pv == null)
					continue;
				pv.addSonId(item_id);
				VirtualItem v= new VirtualItem(item_id, INode, sv_id, node.getName(), node.getType(), d);
				m_items.put(item_id, v);
			}
		}
	}
	

	private String getParentId(String id)
	{
		String pid = new String(id);
		try
		{
			if (pid.contains("."))
				return pid.substring(0,pid.lastIndexOf("."));
			else
				return "";
		} catch (Exception e)
		{
			return id;
		}
	}
	
}


// node NO: 1
// CheckNothing = true
// item_id = viewName
// viewName = ������1
//
// node NO: 2
//CheckNothing = true
//item_id = 1
//zul_type= WholeView
//zul_name= ������ͼ
//
// node NO: 3
//CheckNothing = true
//item_id = 1000
//zul_name = ����
//zul_type = Report
//
// node NO: 4
// CheckNothing = true
// item_id = 1000.1
// zul_name = ͳ�Ʊ���
// zul_type = ReportStatistic
//
// node NO: 5
// item_id = 1.1
// sv_id = 1.62
// type = group
// withAllSubMonitor = true
//
// node NO: 6
// item_id = 1.1.1
// sv_id = 1.62.7.1
// type = monitor
//
// node NO: 7
// item_id = 1.1.2
// sv_id = 1.62.7.2
// type = monitor
//
// node NO: 8
// item_id = 1.1.3
// sv_id = 1.62.7.10
// type = monitor
//
// node NO: 9
// item_id = 1.1.4
// sv_id = 1.62.7.22
// type = monitor

