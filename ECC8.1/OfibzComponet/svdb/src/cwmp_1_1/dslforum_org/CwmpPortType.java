/**
 * CwmpPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cwmp_1_1.dslforum_org;

public interface CwmpPortType extends java.rmi.Remote {
    public void XFileEvent(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String event, java.lang.String OUI) throws java.rmi.RemoteException;
    public void boot(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, java.lang.String deviceSummary) throws java.rmi.RemoteException;
    public void valueChange(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.lang.String ip, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, cwmp_1_1.dslforum_org.ParameterValueStruct[] parameterList) throws java.rmi.RemoteException;
    public void periodic(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, java.lang.String deviceSummary) throws java.rmi.RemoteException;
    public void cpeTransferComplete(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, java.lang.String commandKey, cwmp_1_1.dslforum_org.FaultStruct faultStruct, java.util.Calendar startTime, java.util.Calendar completeTime) throws java.rmi.RemoteException;
}
