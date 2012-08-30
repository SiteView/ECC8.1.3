package com.siteview.cwmp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;

import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityListIterator;

import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;
import com.siteview.utils.AlertInforTools;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

public class AlertInformationDAOImpl implements AlertInformationDAO {
	/**
	 * 取得数据库中的所有告警信息的ID
	 * @return ID列表
	 * @throws Exception
	 */
	public List<String> getAllIds() throws Exception
	{
		List<GenericValue> list = getGenericDelegator().findList("ACSAlertInformation", null, null, null, null, false);
		List<String> retlist = new ArrayList<String>();
		for (GenericValue val : list){
			retlist.add(val.getString("Id"));
		}
		return retlist;
	}
	
	/**
	 * 取数据库中的所有的ID
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	
	public List<String> getIds(String begin,String end) throws Exception
	{
		List<String> retlist = new ArrayList<String>();
		for (String id : this.getAllIds()){
			if (id.compareTo(begin) >= 0 && id.compareTo(end)<=0){
				retlist.add(id);
			}
		}
		return retlist;
	}
	
	/**
	 * 删除数据库中的告警信息
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @throws Exception
	 */
	public void deleteAlertInfor(String begin,String end) throws Exception
	{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("CreateDateTime", EntityOperator.BETWEEN, UtilMisc.toList(begin,end)));
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		getGenericDelegator().removeByCondition("ACSAlertInformation", condition);
	}
	public void deleteAlertInfor(String id) throws Exception
	{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("Id", EntityOperator.EQUALS, id));
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		getGenericDelegator().removeByCondition("ACSAlertInformation", condition);
	}
	public void deleteAlertInfors(String[] ids) throws Exception
	{
		List<EntityExpr> exprs = FastList.newInstance();
		exprs.add(EntityCondition.makeCondition("Id", EntityOperator.IN, UtilMisc.toListArray(ids)));
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		getGenericDelegator().removeByCondition("ACSAlertInformation", condition);
	}


	public long getSize() throws Exception{
		return this.getAlertLogHistrorySize(null);
	}
	public long getSizeByDeviceId(String realDeviceId,DeviceIdStruct sdeviceId) throws Exception{
		List<EntityExpr> exprs = FastList.newInstance();
		if (realDeviceId!=null)
			exprs.add(EntityCondition.makeCondition("RealDeviceId", EntityOperator.EQUALS, realDeviceId));

		if (sdeviceId!=null){
			if (sdeviceId.getManufacturer()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdManufacturer", EntityOperator.EQUALS, sdeviceId.getManufacturer()));
			}
			if (sdeviceId.getOUI()!=null) {
				exprs.add(EntityCondition.makeCondition("deviceIdOUI", EntityOperator.EQUALS, sdeviceId.getOUI()));
			}
			if (sdeviceId.getProductClass()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdProductClass", EntityOperator.EQUALS, sdeviceId.getProductClass()));
			}
			if (sdeviceId.getSerialNumber()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdSerialNumber", EntityOperator.EQUALS, sdeviceId.getSerialNumber()));
			}
		}
	
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		return this.getAlertLogHistrorySize(condition);
	}
	private long getAlertLogHistrorySize(EntityCondition  condition) throws Exception{
		return getGenericDelegator().findCountByCondition("ACSAlertInformation", condition, null,null);
	}
	
	private List<ACSAlertInformation> getAlertLogHistroryByCondition(int pageNo,int length,EntityCondition  condition)throws Exception
	{
		boolean bbegin = false;
		EntityListIterator entitylistiterator = null;
		try{
			bbegin = TransactionUtil.begin();
			entitylistiterator = getGenericDelegator().find("ACSAlertInformation", condition, null, null, UtilMisc.toList("CreateDateTime DESC"), null);
			
			//总的记录数：
			long totalsize = this.getSize();

			int start = length * pageNo; 

			List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
			
			if (totalsize<=start) return retlist;
			
			GenericValue value = null;//entitylistiterator.getPartialList(start, length);
			
			int count = 0;
			int num = 0;
			while ((value = entitylistiterator.next())!=null){
				count++;
				if (count <= start) continue;
				retlist.add(new ACSAlertInformation(value));
				num++;
				if (num >= length) break;
			}
			return retlist;
			
		}finally{
			try{
				if (entitylistiterator!=null)entitylistiterator.close();
				if (bbegin) TransactionUtil.rollback();
			}catch(Exception e){
				
			}
		}
	}
	
	public List<ACSAlertInformation> getAlertInfors(int pageNo,int length)throws Exception
	{
		return this.getAlertLogHistroryByCondition(pageNo, length, null);
	}
	
	public List<ACSAlertInformation> getAlertInforsByDeviceId(int pageNo,int length,String realDeviceId,DeviceIdStruct sdeviceId)throws Exception
	{
		List<EntityExpr> exprs = FastList.newInstance();
		if (realDeviceId!=null)
			exprs.add(EntityCondition.makeCondition("RealDeviceId", EntityOperator.EQUALS, realDeviceId));

		if (sdeviceId!=null){
			if (sdeviceId.getManufacturer()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdManufacturer", EntityOperator.EQUALS, sdeviceId.getManufacturer()));
			}
			if (sdeviceId.getOUI()!=null) {
				exprs.add(EntityCondition.makeCondition("deviceIdOUI", EntityOperator.EQUALS, sdeviceId.getOUI()));
			}
			if (sdeviceId.getProductClass()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdProductClass", EntityOperator.EQUALS, sdeviceId.getProductClass()));
			}
			if (sdeviceId.getSerialNumber()!=null){
				exprs.add(EntityCondition.makeCondition("deviceIdSerialNumber", EntityOperator.EQUALS, sdeviceId.getSerialNumber()));
			}
		}
	
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		return this.getAlertLogHistroryByCondition(pageNo, length, condition);
	}
	
	private GenericDelegator genericdelegator = null;
	private GenericDelegator getGenericDelegator(){
		if (genericdelegator==null) genericdelegator = GenericDelegator.getGenericDelegator("default");
		return genericdelegator;
	}
	public void save(ACSAlertInformation infor)throws Exception{
		if ("".equals(infor.getId()) || infor.getId()==null){
			GenericValue genericvalue = getGenericDelegator().makeValue("ACSAlertInformation");
			infor.setId(getGenericDelegator().getNextSeqId("ACSAlertInformation"));
			Map<String,String> map = AlertInforTools.toMap(infor);
			AlertInforTools.initGenericValue(genericvalue, map);
			genericvalue.create();
		}else{
			GenericValue genericvalue = getGenericDelegator().findOne("ACSAlertInformation", getGenericDelegator().makePKSingle("ACSAlertInformation", infor.getId()), false);
			Map<String,String> map = AlertInforTools.toMap(infor);
			AlertInforTools.initGenericValue(genericvalue, map);
			genericvalue.store();
		}
	}	
	/**
	 * 根据ID,从数据库中取得告警信息,初始化bean
	 * @param id 该告警信息的id
	 * @throws Exception
	 */
	
	public ACSAlertInformation read(String id) throws Exception{
		GenericValue genericvalue = getGenericDelegator().findOne("ACSAlertInformation", getGenericDelegator().makePKSingle("ACSAlertInformation", id), false);
		return new ACSAlertInformation(genericvalue);
	}

	@Override
	public ACSAlertList queryHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime, int pageNo,
			int length) throws Exception {
		if ( begintime != null && endtime == null ) throw new Exception("0008:endtime 是空");
		if ( begintime == null && endtime != null ) throw new Exception("0009:begintime 是空");
		List<EntityExpr> exprs = FastList.newInstance();
		if (deviceids!=null)
			exprs.add(EntityCondition.makeCondition("RealDeviceId", EntityOperator.IN, UtilMisc.toListArray(deviceids)));
		if (sdeviceIds!=null){
			List<String>  manufacturerList = new ArrayList<String>();

		    List<String>   OUIList = new ArrayList<String>();

		    List<String>   productClassList = new ArrayList<String>();

		    List<String>   serialNumberList = new ArrayList<String>();

			for (DeviceIdStruct deviceId : sdeviceIds){
				if (deviceId.getManufacturer()!=null) manufacturerList.add(deviceId.getManufacturer());
				if (deviceId.getOUI()!=null) OUIList.add(deviceId.getOUI());
				if (deviceId.getProductClass()!=null) productClassList.add(deviceId.getProductClass());
				if (deviceId.getSerialNumber()!=null) serialNumberList.add(deviceId.getSerialNumber());
			}
			if (manufacturerList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdManufacturer", EntityOperator.IN, manufacturerList));
			if (OUIList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdOUI", EntityOperator.IN, OUIList));
			if (productClassList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdProductClass", EntityOperator.IN, productClassList));
			if (serialNumberList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdSerialNumber", EntityOperator.IN, serialNumberList));
		}
		
		if (alertStatus!=null)
			exprs.add(EntityCondition.makeCondition("AlertStatus", EntityOperator.EQUALS, alertStatus.toString()));
		if (begintime!=null && endtime!=null){
			exprs.add(EntityCondition.makeCondition("CreateDateTime", EntityOperator.GREATER_THAN_EQUAL_TO, begintime));
			exprs.add(EntityCondition.makeCondition("CreateDateTime", EntityOperator.LESS_THAN_EQUAL_TO, endtime));
		}
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		
		ACSAlertList ret = new ACSAlertList();
		ret.setAllSize(getAlertLogHistrorySize(condition));
		ret.setList(this.getAlertLogHistroryByCondition(pageNo, length, condition));
		return ret;
	}

	@Override
	public int deleteHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,
			String begintime, String endtime) throws Exception {
		List<EntityExpr> exprs = FastList.newInstance();
		if (deviceids!=null)
			exprs.add(EntityCondition.makeCondition("RealDeviceId", EntityOperator.IN, UtilMisc.toListArray(deviceids)));
		if (sdeviceIds!=null){
			List<String>  manufacturerList = new ArrayList<String>();

		    List<String>   OUIList = new ArrayList<String>();

		    List<String>   productClassList = new ArrayList<String>();

		    List<String>   serialNumberList = new ArrayList<String>();

			for (DeviceIdStruct deviceId : sdeviceIds){
				if (deviceId.getManufacturer()!=null) manufacturerList.add(deviceId.getManufacturer());
				if (deviceId.getOUI()!=null) OUIList.add(deviceId.getOUI());
				if (deviceId.getProductClass()!=null) productClassList.add(deviceId.getProductClass());
				if (deviceId.getSerialNumber()!=null) serialNumberList.add(deviceId.getSerialNumber());
			}
			if (manufacturerList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdManufacturer", EntityOperator.IN, manufacturerList));
			if (OUIList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdOUI", EntityOperator.IN, OUIList));
			if (productClassList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdProductClass", EntityOperator.IN, productClassList));
			if (serialNumberList.size()>0)
				exprs.add(EntityCondition.makeCondition("deviceIdSerialNumber", EntityOperator.IN, serialNumberList));
		}
		if (alertStatus!=null)
			exprs.add(EntityCondition.makeCondition("AlertStatus", EntityOperator.EQUALS, alertStatus.toString()));
		exprs.add(EntityCondition.makeCondition("CreateDateTime", EntityOperator.GREATER_THAN_EQUAL_TO, begintime));
		exprs.add(EntityCondition.makeCondition("CreateDateTime", EntityOperator.LESS_THAN_EQUAL_TO, endtime));
		EntityCondition  condition = EntityCondition.makeCondition(exprs,EntityOperator.AND);
		return getGenericDelegator().removeByCondition("ACSAlertInformation", condition);
	}
	
}
