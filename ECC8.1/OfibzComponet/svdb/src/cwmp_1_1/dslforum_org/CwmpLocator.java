/**
 * CwmpLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cwmp_1_1.dslforum_org;

public class CwmpLocator extends org.apache.axis.client.Service implements cwmp_1_1.dslforum_org.Cwmp {

    public CwmpLocator() {
    }


    public CwmpLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CwmpLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for cwmp
    private java.lang.String cwmp_address = "http://localhost:8080/svdb/services/cwmp";

    public java.lang.String getcwmpAddress() {
        return cwmp_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String cwmpWSDDServiceName = "cwmp";

    public java.lang.String getcwmpWSDDServiceName() {
        return cwmpWSDDServiceName;
    }

    public void setcwmpWSDDServiceName(java.lang.String name) {
        cwmpWSDDServiceName = name;
    }

    public cwmp_1_1.dslforum_org.CwmpPortType getcwmp() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(cwmp_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getcwmp(endpoint);
    }

    public cwmp_1_1.dslforum_org.CwmpPortType getcwmp(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cwmp_1_1.dslforum_org.CwmpSoapBindingStub _stub = new cwmp_1_1.dslforum_org.CwmpSoapBindingStub(portAddress, this);
            _stub.setPortName(getcwmpWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setcwmpEndpointAddress(java.lang.String address) {
        cwmp_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cwmp_1_1.dslforum_org.CwmpPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                cwmp_1_1.dslforum_org.CwmpSoapBindingStub _stub = new cwmp_1_1.dslforum_org.CwmpSoapBindingStub(new java.net.URL(cwmp_address), this);
                _stub.setPortName(getcwmpWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("cwmp".equals(inputPortName)) {
            return getcwmp();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "cwmp");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "cwmp"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("cwmp".equals(portName)) {
            setcwmpEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
