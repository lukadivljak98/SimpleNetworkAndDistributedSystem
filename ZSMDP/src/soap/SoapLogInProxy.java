package soap;

public class SoapLogInProxy implements soap.SoapLogIn {
  private String _endpoint = null;
  private soap.SoapLogIn soapLogIn = null;
  
  public SoapLogInProxy() {
    _initSoapLogInProxy();
  }
  
  public SoapLogInProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoapLogInProxy();
  }
  
  private void _initSoapLogInProxy() {
    try {
      soapLogIn = (new soap.SoapLogInServiceLocator()).getSoapLogIn();
      if (soapLogIn != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)soapLogIn)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)soapLogIn)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (soapLogIn != null)
      ((javax.xml.rpc.Stub)soapLogIn)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public soap.SoapLogIn getSoapLogIn() {
    if (soapLogIn == null)
      _initSoapLogInProxy();
    return soapLogIn;
  }
  
  public boolean checkLogIn(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (soapLogIn == null)
      _initSoapLogInProxy();
    return soapLogIn.checkLogIn(username, password);
  }
  
  public void logOut(java.lang.String username) throws java.rmi.RemoteException{
    if (soapLogIn == null)
      _initSoapLogInProxy();
    soapLogIn.logOut(username);
  }
  
  
}