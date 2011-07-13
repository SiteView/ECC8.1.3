package com.siteview.svdb;

public class SvdbApiImplProxy implements com.siteview.svdb.SvdbApiImplClient {
  private String _endpoint = null;
  private com.siteview.svdb.SvdbApiImplClient svdbApiImpl = null;
  
  public SvdbApiImplProxy() {
    _initSvdbApiImplProxy();
  }
  
  public SvdbApiImplProxy(String endpoint) {
    _endpoint = endpoint;
    _initSvdbApiImplProxy();
  }
  
  private void _initSvdbApiImplProxy() {
    try {
      svdbApiImpl = (new com.siteview.svdb.SvdbApiImplServiceLocator()).getSvdbApiImpl();
      if (svdbApiImpl != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)svdbApiImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)svdbApiImpl)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (svdbApiImpl != null)
      ((javax.xml.rpc.Stub)svdbApiImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.siteview.svdb.SvdbApiImplClient getSvdbApiImpl() {
    if (svdbApiImpl == null)
      _initSvdbApiImplProxy();
    return svdbApiImpl;
  }
  
  public void appendRecord(java.lang.String id, java.lang.String text) throws java.rmi.RemoteException{
    if (svdbApiImpl == null)
      _initSvdbApiImplProxy();
    svdbApiImpl.appendRecord(id, text);
  }
  
  
}