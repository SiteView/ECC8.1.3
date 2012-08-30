/**
 * CwmpSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cwmp_1_1.dslforum_org;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import javolution.util.FastMap;

import org.apache.axis.MessageContext;
import org.apache.axis.holders.UnsignedIntHolder;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.types.UnsignedInt;
import org.ofbiz.base.util.Debug;

import com.siteview.cwmp.AlertManager;
import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.utils.AlertInforTools;

public class CwmpSoapBindingImpl implements cwmp_1_1.dslforum_org.CwmpPortType{
	private static final String module = CwmpSoapBindingImpl.class.getName();
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Jsvapi svapi = new Jsvapi();
	static{
		SDF.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}
	public void XFileEvent(DeviceIdStruct deviceId, String event, String oui) throws RemoteException {

		/*
		try {
			AlertManager.pushAlertQueueInfor(new ACSAlertInformation(deviceId, event, oui,AlertInforTools.getRealDeviceIdByDeviceId(deviceId)));
		} catch (Exception e) {
			Debug.logError(e.getMessage(), module);
			throw new RemoteException(e.getMessage());
		}
		*/
		new PushThread(deviceId, event, oui).start();
    }
	@Override
	public void cpeTransferComplete(DeviceIdStruct deviceId, String ip,
			String commandKey, FaultStruct faultStruct, Calendar startTime,
			Calendar completeTime) throws RemoteException {
		new CpeTransferCompleteThread(svapi,deviceId, ip,
				commandKey, faultStruct, startTime,
				completeTime).start();
		
	}
	@Override
	public void periodic(DeviceIdStruct deviceId, String ip,
			UnsignedIntHolder maxEnvelopes, Calendar currentTime,
			UnsignedInt retryCount, String deviceSummary)
			throws RemoteException {
    	MessageContext context = MessageContext.getCurrentContext();
    	HttpServletRequest req = (HttpServletRequest)context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    	
    	String callerIP = req.getRemoteHost();
    	int callerPort = req.getRemotePort();

    	new PeriodicThread(svapi,deviceId, maxEnvelopes, ip,callerIP,callerPort, currentTime, retryCount,deviceSummary).start();
	}

    public void boot(DeviceIdStruct deviceId, String ip, UnsignedIntHolder maxEnvelopes, Calendar currentTime, UnsignedInt retryCount,String deviceSummary) throws RemoteException {
        /*
    	StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = CwmpImpl.pushParameter(deviceId,ip, maxEnvelopes, currentTime, retryCount);
        parameters.put("dowhat", "boot");
        boolean ret = svapi.submitUnivData(CwmpImpl.pushParameterList(null),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
        */
    	MessageContext context = MessageContext.getCurrentContext();
    	HttpServletRequest req = (HttpServletRequest)context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    	
    	String callerIP = req.getRemoteHost();
    	int callerPort = req.getRemotePort();

    	new BootThread(svapi,deviceId, maxEnvelopes, ip,callerIP,callerPort, currentTime, retryCount,deviceSummary).start();
    }

    public void valueChange(DeviceIdStruct deviceId, UnsignedIntHolder maxEnvelopes, String ip, Calendar currentTime, UnsignedInt retryCount, ParameterValueStruct[] parameterList) throws RemoteException {
        /*
    	StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = CwmpImpl.pushParameter(deviceId,ip, maxEnvelopes, currentTime, retryCount);
        parameters.put("dowhat", "valueChange");
        boolean ret = svapi.submitUnivData(CwmpImpl.pushParameterList(parameterList),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
        */
    	new ValueChangeThread(svapi,deviceId, maxEnvelopes, ip, currentTime, retryCount, parameterList).start();
    }
    public static Map<String, String> pushParameter(DeviceIdStruct DeviceId,  String ip, UnsignedIntHolder maxEnvelopes, Calendar currenttime, UnsignedInt retryCount){
    	Map<String, String> ndata = new FastMap<String, String>();
    	ndata.put("manufacturer", DeviceId.getManufacturer() == null ? "" : DeviceId.getManufacturer());
    	ndata.put("oui", DeviceId.getOUI() == null ? "" : DeviceId.getOUI());
    	ndata.put("productClass", DeviceId.getProductClass() == null ? "" : DeviceId.getProductClass());
    	ndata.put("serialNumber", DeviceId.getSerialNumber() == null ? "" : DeviceId.getSerialNumber());
    	ndata.put("IP", ip == null ? "" : ip);
    	ndata.put("MaxEnvelopes", "" + maxEnvelopes.value);
    	ndata.put("CurrentTime", currenttime == null ? "" : format(currenttime));
    	ndata.put("RetryCount", "" + retryCount);
    	return ndata;
    }
    
    public static Map<String, Map<String, String>> pushParameterList(ParameterValueStruct[] parameterList){
    	Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
    	if (parameterList == null) return fmap;
    	Map<String, String> map = new HashMap<String, String>();
    	for (ParameterValueStruct value : parameterList){
   			map.put(value.getName(), value.getValue() == null ? "" : value.getValue());
    	}
		fmap.put("ParameterList", map);
    	return fmap;
    }
    
    public static String format(Calendar currenttime){
    	return SDF.format(currenttime.getTime());
    }
}
class PushThread extends Thread{
	public static final String module = PushThread.class.getName();
	private DeviceIdStruct deviceId = null;
	private String event = null;
	private String oui = null;
	public PushThread(DeviceIdStruct deviceId, String event, String oui){
		this.deviceId = deviceId;
		this.event = event;
		this.oui = oui;
	}
	public void run(){
		try {
			String realDeviceId = AlertInforTools.getRealDeviceIdByDeviceId(deviceId);
			ACSAlertInformation infor = new ACSAlertInformation(deviceId, event, oui,realDeviceId);
			AlertManager.pushAlertQueueInfor(infor);
		} catch (Exception e) {
			Debug.logError(e.getMessage(), module);
		}
	}
}
class ValueChangeThread extends Thread{
	public static final String module = ValueChangeThread.class.getName();
	Jsvapi svapi = null;
	DeviceIdStruct deviceId = null; 
	UnsignedIntHolder maxEnvelopes = null; 
	String ip = null; 
	Calendar currentTime = null; 
	UnsignedInt retryCount = null; 
	ParameterValueStruct[] parameterList = null;
	public ValueChangeThread(Jsvapi svapi,DeviceIdStruct deviceId, UnsignedIntHolder maxEnvelopes, String ip, Calendar currentTime, UnsignedInt retryCount, ParameterValueStruct[] parameterList){
		this.svapi = svapi;
		this.deviceId = deviceId;
		this.maxEnvelopes = maxEnvelopes;
		this.ip = ip;
		this.currentTime = currentTime;
		this.retryCount = retryCount;
		this.parameterList = parameterList;
	}
	public void run(){
        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = CwmpSoapBindingImpl.pushParameter(deviceId,ip, maxEnvelopes, currentTime, retryCount);
        parameters.put("dowhat", "valueChange");
        boolean ret = svapi.submitUnivData(CwmpSoapBindingImpl.pushParameterList(parameterList),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
	}
}

class BootThread extends Thread{
	public static final String module = BootThread.class.getName();
	Jsvapi svapi = null;
	DeviceIdStruct deviceId = null; 
	UnsignedIntHolder maxEnvelopes = null; 
	String ip = null; 
	String clientIp = null;
	int clientPort = -1;
	Calendar currentTime = null; 
	UnsignedInt retryCount = null; 
	String deviceSummary = null;
	public BootThread(Jsvapi svapi,DeviceIdStruct deviceId, UnsignedIntHolder maxEnvelopes, String ip,String clientIp,int clientPort, Calendar currentTime, UnsignedInt retryCount,String deviceSummary){
		this.svapi = svapi;
		this.deviceId = deviceId;
		this.maxEnvelopes = maxEnvelopes;
		this.ip = ip;
		this.clientIp = clientIp;
		this.clientPort = clientPort;
		this.currentTime = currentTime;
		this.retryCount = retryCount;
		this.deviceSummary = deviceSummary;
	}
	public void run(){
        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = CwmpSoapBindingImpl.pushParameter(deviceId,ip, maxEnvelopes, currentTime, retryCount);
        parameters.put("DeviceSummary", deviceSummary == null ? "" : deviceSummary);

        parameters.put("dowhat", "boot");
        ParameterValueStruct[] parameterList = new ParameterValueStruct[]{
        		new ParameterValueStruct("serverIP",clientIp),
        		new ParameterValueStruct("serverPort","" + clientPort)
        };
        boolean ret = svapi.submitUnivData(CwmpSoapBindingImpl.pushParameterList(parameterList),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
	}
}
class PeriodicThread extends Thread{
	public static final String module = BootThread.class.getName();
	Jsvapi svapi = null;
	DeviceIdStruct deviceId = null; 
	UnsignedIntHolder maxEnvelopes = null; 
	String ip = null; 
	String clientIp = null;
	int clientPort = -1;
	Calendar currentTime = null; 
	UnsignedInt retryCount = null; 
	String deviceSummary = null;
	public PeriodicThread(Jsvapi svapi,DeviceIdStruct deviceId, UnsignedIntHolder maxEnvelopes, String ip,String clientIp,int clientPort, Calendar currentTime, UnsignedInt retryCount,String deviceSummary){
		this.svapi = svapi;
		this.deviceId = deviceId;
		this.maxEnvelopes = maxEnvelopes;
		this.ip = ip;
		this.clientIp = clientIp;
		this.clientPort = clientPort;
		this.currentTime = currentTime;
		this.retryCount = retryCount;
		this.deviceSummary = deviceSummary;
	}
	public void run(){
        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = CwmpSoapBindingImpl.pushParameter(deviceId,ip, maxEnvelopes, currentTime, retryCount);
        parameters.put("DeviceSummary", deviceSummary == null ? "" : deviceSummary);
        parameters.put("dowhat", "Periodic");
        ParameterValueStruct[] parameterList = new ParameterValueStruct[]{
        		new ParameterValueStruct("serverIP",clientIp),
        		new ParameterValueStruct("serverPort","" + clientPort)
        };
        boolean ret = svapi.submitUnivData(CwmpSoapBindingImpl.pushParameterList(parameterList),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
	}
}
class CpeTransferCompleteThread extends Thread{
	public static final String module = CpeTransferCompleteThread.class.getName();
	Jsvapi svapi = null;
	DeviceIdStruct deviceId = null;
	String ip = null;
	String commandKey = null;
	FaultStruct faultStruct = null;
	Calendar startTime = null;
	Calendar completeTime = null;
	public CpeTransferCompleteThread(Jsvapi svapi,DeviceIdStruct deviceId, String ip,
			String commandKey, FaultStruct faultStruct, Calendar startTime,
			Calendar completeTime){
		this.svapi = svapi;
		this.deviceId = deviceId;
		this.ip = ip;
		this.commandKey = commandKey;
		this.faultStruct = faultStruct;
		this.startTime = startTime;
		this.completeTime = completeTime;
	}
	public void run(){
        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = new FastMap<String, String>();
    	parameters.put("manufacturer", deviceId.getManufacturer() == null ? "" : deviceId.getManufacturer());
    	parameters.put("oui", deviceId.getOUI() == null ? "" : deviceId.getOUI());
    	parameters.put("productClass", deviceId.getProductClass() == null ? "" : deviceId.getProductClass());
    	parameters.put("serialNumber", deviceId.getSerialNumber() == null ? "" : deviceId.getSerialNumber());
    	parameters.put("FaultCode", faultStruct.getFaultCode() == null ? "" : "" + faultStruct.getFaultCode());
    	parameters.put("FaultString", faultStruct.getFaultString() == null ? "" : faultStruct.getFaultString());
    	parameters.put("IP", ip == null ? "" : ip);
    	parameters.put("commandKey", commandKey == null ? "" : commandKey);
    	parameters.put("startTime", startTime == null ? "" : CwmpSoapBindingImpl.SDF.format(startTime.getTime()));
    	parameters.put("completeTime", completeTime == null ? "" : CwmpSoapBindingImpl.SDF.format(completeTime.getTime()));

    	parameters.put("dowhat", "CpeTransferComplete");
        boolean ret = svapi.submitUnivData(CwmpSoapBindingImpl.pushParameterList(null),parameters,  estr);
        if (ret) return;
        Debug.logError(estr.toString(), module);
	}
}