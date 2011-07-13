/**
 * SvdbApiImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.siteview.svdb;

public class SvdbApiImplServiceLocator extends org.apache.axis.client.Service implements com.siteview.svdb.SvdbApiImplService {

    public SvdbApiImplServiceLocator() {
    }


    public SvdbApiImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SvdbApiImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SvdbApiImpl
    private java.lang.String SvdbApiImpl_address = "http://localhost/SvdbCache/services/SvdbApiImpl";

    public java.lang.String getSvdbApiImplAddress() {
        return SvdbApiImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SvdbApiImplWSDDServiceName = "SvdbApiImpl";

    public java.lang.String getSvdbApiImplWSDDServiceName() {
        return SvdbApiImplWSDDServiceName;
    }

    public void setSvdbApiImplWSDDServiceName(java.lang.String name) {
        SvdbApiImplWSDDServiceName = name;
    }

    public com.siteview.svdb.SvdbApiImplClient getSvdbApiImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SvdbApiImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSvdbApiImpl(endpoint);
    }

    public com.siteview.svdb.SvdbApiImplClient getSvdbApiImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.siteview.svdb.SvdbApiImplSoapBindingStub _stub = new com.siteview.svdb.SvdbApiImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getSvdbApiImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSvdbApiImplEndpointAddress(java.lang.String address) {
        SvdbApiImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.siteview.svdb.SvdbApiImplClient.class.isAssignableFrom(serviceEndpointInterface)) {
                com.siteview.svdb.SvdbApiImplSoapBindingStub _stub = new com.siteview.svdb.SvdbApiImplSoapBindingStub(new java.net.URL(SvdbApiImpl_address), this);
                _stub.setPortName(getSvdbApiImplWSDDServiceName());
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
        if ("SvdbApiImpl".equals(inputPortName)) {
            return getSvdbApiImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://svdb.siteview.com", "SvdbApiImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://svdb.siteview.com", "SvdbApiImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SvdbApiImpl".equals(portName)) {
            setSvdbApiImplEndpointAddress(address);
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
