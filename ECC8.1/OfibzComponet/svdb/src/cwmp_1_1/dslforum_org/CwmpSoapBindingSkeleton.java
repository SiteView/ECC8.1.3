/**
 * CwmpSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cwmp_1_1.dslforum_org;

public class CwmpSoapBindingSkeleton implements cwmp_1_1.dslforum_org.CwmpPortType, org.apache.axis.wsdl.Skeleton {
    private cwmp_1_1.dslforum_org.CwmpPortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"), cwmp_1_1.dslforum_org.DeviceIdStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Event"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "OUI"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("XFileEvent", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "XFileEvent"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("XFileEvent") == null) {
            _myOperations.put("XFileEvent", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("XFileEvent")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"), cwmp_1_1.dslforum_org.DeviceIdStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "MaxEnvelopes"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CurrentTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RetryCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceSummary"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("boot", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "Boot"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("boot") == null) {
            _myOperations.put("boot", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("boot")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"), cwmp_1_1.dslforum_org.DeviceIdStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "MaxEnvelopes"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CurrentTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RetryCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ParameterList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "ParameterValueStruct"), cwmp_1_1.dslforum_org.ParameterValueStruct[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("valueChange", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "ValueChange"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("valueChange") == null) {
            _myOperations.put("valueChange", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("valueChange")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"), cwmp_1_1.dslforum_org.DeviceIdStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "MaxEnvelopes"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CurrentTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "RetryCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedInt"), org.apache.axis.types.UnsignedInt.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceSummary"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("periodic", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "Periodic"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("periodic") == null) {
            _myOperations.put("periodic", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("periodic")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DeviceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"), cwmp_1_1.dslforum_org.DeviceIdStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CommandKey"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "FaultStruct"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "FaultStruct"), cwmp_1_1.dslforum_org.FaultStruct.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "StartTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CompleteTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("cpeTransferComplete", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "CpeTransferComplete"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("cpeTransferComplete") == null) {
            _myOperations.put("cpeTransferComplete", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("cpeTransferComplete")).add(_oper);
    }

    public CwmpSoapBindingSkeleton() {
        this.impl = new cwmp_1_1.dslforum_org.CwmpSoapBindingImpl();
    }

    public CwmpSoapBindingSkeleton(cwmp_1_1.dslforum_org.CwmpPortType impl) {
        this.impl = impl;
    }
    public void XFileEvent(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String event, java.lang.String OUI) throws java.rmi.RemoteException
    {
        impl.XFileEvent(deviceId, event, OUI);
    }

    public void boot(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, java.lang.String deviceSummary) throws java.rmi.RemoteException
    {
        impl.boot(deviceId, ip, maxEnvelopes, currentTime, retryCount, deviceSummary);
    }

    public void valueChange(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.lang.String ip, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, cwmp_1_1.dslforum_org.ParameterValueStruct[] parameterList) throws java.rmi.RemoteException
    {
        impl.valueChange(deviceId, maxEnvelopes, ip, currentTime, retryCount, parameterList);
    }

    public void periodic(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, org.apache.axis.holders.UnsignedIntHolder maxEnvelopes, java.util.Calendar currentTime, org.apache.axis.types.UnsignedInt retryCount, java.lang.String deviceSummary) throws java.rmi.RemoteException
    {
        impl.periodic(deviceId, ip, maxEnvelopes, currentTime, retryCount, deviceSummary);
    }

    public void cpeTransferComplete(cwmp_1_1.dslforum_org.DeviceIdStruct deviceId, java.lang.String ip, java.lang.String commandKey, cwmp_1_1.dslforum_org.FaultStruct faultStruct, java.util.Calendar startTime, java.util.Calendar completeTime) throws java.rmi.RemoteException
    {
        impl.cpeTransferComplete(deviceId, ip, commandKey, faultStruct, startTime, completeTime);
    }

}
