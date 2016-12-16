
package com.cadisedm.schemas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ProcessLauncher", targetNamespace = "http://www.cadisedm.com/Schemas/", wsdlLocation = "http://edm-webui-flm.markit.partners/ProcessLauncher.svc?singleWsdl")
public class ProcessLauncher
    extends Service
{

    private final static URL PROCESSLAUNCHER_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.cadisedm.schemas.ProcessLauncher.class.getName());
    
    
    private static String edmUrl;

    
    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.cadisedm.schemas.ProcessLauncher.class.getResource(".");
            url = new URL(baseUrl, "http://edm-webui-flm.markit.partners/ProcessLauncher.svc?singleWsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://edm-webui-flm.markit.partners/ProcessLauncher.svc?singleWsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        PROCESSLAUNCHER_WSDL_LOCATION = url;
    }

    public ProcessLauncher(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ProcessLauncher(String wsdlLocation) throws MalformedURLException {
        super(new URL(wsdlLocation), new QName("http://www.cadisedm.com/Schemas/", "ProcessLauncher"));
    }

    /**
     * 
     * @return
     *     returns IProcessLauncher1
     */
    @WebEndpoint(name = "CADIS.WebService")
    public IProcessLauncher1 getCADISWebService() {
        return super.getPort(new QName("http://www.cadisedm.com/Schemas/", "CADIS.WebService"), IProcessLauncher1.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IProcessLauncher1
     */
    @WebEndpoint(name = "CADIS.WebService")
    public IProcessLauncher1 getCADISWebService(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.cadisedm.com/Schemas/", "CADIS.WebService"), IProcessLauncher1.class, features);
    }

}
