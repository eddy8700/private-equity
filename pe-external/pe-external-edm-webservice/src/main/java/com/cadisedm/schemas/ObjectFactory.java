
package com.cadisedm.schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfstringstring;




/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cadisedm.schemas package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Error_QNAME = new QName("http://www.cadisedm.com/Schemas/", "Error");
    private final static QName _SendProcessLaunchMessageWithParametersConfigurableParameters_QNAME = new QName("http://www.cadisedm.com/Schemas/", "configurableParameters");
    private final static QName _SendProcessLaunchMessageWithParametersProcessLaunchQueueParameterName_QNAME = new QName("http://www.cadisedm.com/Schemas/", "processLaunchQueueParameterName");
    private final static QName _SendProcessLaunchMessageWithParametersConnectionToken_QNAME = new QName("http://www.cadisedm.com/Schemas/", "connectionToken");
    private final static QName _SendProcessLaunchMessageWithParametersProcessName_QNAME = new QName("http://www.cadisedm.com/Schemas/", "processName");
    private final static QName _SendProcessLaunchMessageResponseSendProcessLaunchMessageResult_QNAME = new QName("http://www.cadisedm.com/Schemas/", "SendProcessLaunchMessageResult");
    private final static QName _ConnectAuthenticationParameters_QNAME = new QName("http://www.cadisedm.com/Schemas/", "authenticationParameters");
    private final static QName _ErrorExceptionMessage_QNAME = new QName("http://www.cadisedm.com/Schemas/", "ExceptionMessage");
    private final static QName _ErrorExceptionDetail_QNAME = new QName("http://www.cadisedm.com/Schemas/", "ExceptionDetail");
    private final static QName _ConnectResponseErrorDetails_QNAME = new QName("http://www.cadisedm.com/Schemas/", "errorDetails");
    private final static QName _ConnectResponseConnectResult_QNAME = new QName("http://www.cadisedm.com/Schemas/", "ConnectResult");
    private final static QName _SendProcessLaunchMessageWithParametersResponseSendProcessLaunchMessageWithParametersResult_QNAME = new QName("http://www.cadisedm.com/Schemas/", "SendProcessLaunchMessageWithParametersResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cadisedm.schemas
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendProcessLaunchMessageWithParameters }
     * 
     */
    public SendProcessLaunchMessageWithParameters createSendProcessLaunchMessageWithParameters() {
        return new SendProcessLaunchMessageWithParameters();
    }

    /**
     * Create an instance of {@link SendProcessLaunchMessageResponse }
     * 
     */
    public SendProcessLaunchMessageResponse createSendProcessLaunchMessageResponse() {
        return new SendProcessLaunchMessageResponse();
    }

    /**
     * Create an instance of {@link Connect }
     * 
     */
    public Connect createConnect() {
        return new Connect();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link SendProcessLaunchMessage }
     * 
     */
    public SendProcessLaunchMessage createSendProcessLaunchMessage() {
        return new SendProcessLaunchMessage();
    }

    /**
     * Create an instance of {@link ConnectResponse }
     * 
     */
    public ConnectResponse createConnectResponse() {
        return new ConnectResponse();
    }

    /**
     * Create an instance of {@link SendProcessLaunchMessageWithParametersResponse }
     * 
     */
    public SendProcessLaunchMessageWithParametersResponse createSendProcessLaunchMessageWithParametersResponse() {
        return new SendProcessLaunchMessageWithParametersResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Error }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "Error")
    public JAXBElement<Error> createError(Error value) {
        return new JAXBElement<Error>(_Error_QNAME, Error.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueOfstringstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "configurableParameters", scope = SendProcessLaunchMessageWithParameters.class)
    public JAXBElement<ArrayOfKeyValueOfstringstring> createSendProcessLaunchMessageWithParametersConfigurableParameters(ArrayOfKeyValueOfstringstring value) {
        return new JAXBElement<ArrayOfKeyValueOfstringstring>(_SendProcessLaunchMessageWithParametersConfigurableParameters_QNAME, ArrayOfKeyValueOfstringstring.class, SendProcessLaunchMessageWithParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "processLaunchQueueParameterName", scope = SendProcessLaunchMessageWithParameters.class)
    public JAXBElement<String> createSendProcessLaunchMessageWithParametersProcessLaunchQueueParameterName(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersProcessLaunchQueueParameterName_QNAME, String.class, SendProcessLaunchMessageWithParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "connectionToken", scope = SendProcessLaunchMessageWithParameters.class)
    public JAXBElement<String> createSendProcessLaunchMessageWithParametersConnectionToken(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersConnectionToken_QNAME, String.class, SendProcessLaunchMessageWithParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "processName", scope = SendProcessLaunchMessageWithParameters.class)
    public JAXBElement<String> createSendProcessLaunchMessageWithParametersProcessName(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersProcessName_QNAME, String.class, SendProcessLaunchMessageWithParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Error }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "SendProcessLaunchMessageResult", scope = SendProcessLaunchMessageResponse.class)
    public JAXBElement<Error> createSendProcessLaunchMessageResponseSendProcessLaunchMessageResult(Error value) {
        return new JAXBElement<Error>(_SendProcessLaunchMessageResponseSendProcessLaunchMessageResult_QNAME, Error.class, SendProcessLaunchMessageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "authenticationParameters", scope = Connect.class)
    public JAXBElement<String> createConnectAuthenticationParameters(String value) {
        return new JAXBElement<String>(_ConnectAuthenticationParameters_QNAME, String.class, Connect.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "ExceptionMessage", scope = Error.class)
    public JAXBElement<String> createErrorExceptionMessage(String value) {
        return new JAXBElement<String>(_ErrorExceptionMessage_QNAME, String.class, Error.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "ExceptionDetail", scope = Error.class)
    public JAXBElement<String> createErrorExceptionDetail(String value) {
        return new JAXBElement<String>(_ErrorExceptionDetail_QNAME, String.class, Error.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "processLaunchQueueParameterName", scope = SendProcessLaunchMessage.class)
    public JAXBElement<String> createSendProcessLaunchMessageProcessLaunchQueueParameterName(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersProcessLaunchQueueParameterName_QNAME, String.class, SendProcessLaunchMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "connectionToken", scope = SendProcessLaunchMessage.class)
    public JAXBElement<String> createSendProcessLaunchMessageConnectionToken(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersConnectionToken_QNAME, String.class, SendProcessLaunchMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "processName", scope = SendProcessLaunchMessage.class)
    public JAXBElement<String> createSendProcessLaunchMessageProcessName(String value) {
        return new JAXBElement<String>(_SendProcessLaunchMessageWithParametersProcessName_QNAME, String.class, SendProcessLaunchMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Error }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "errorDetails", scope = ConnectResponse.class)
    public JAXBElement<Error> createConnectResponseErrorDetails(Error value) {
        return new JAXBElement<Error>(_ConnectResponseErrorDetails_QNAME, Error.class, ConnectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "ConnectResult", scope = ConnectResponse.class)
    public JAXBElement<String> createConnectResponseConnectResult(String value) {
        return new JAXBElement<String>(_ConnectResponseConnectResult_QNAME, String.class, ConnectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Error }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cadisedm.com/Schemas/", name = "SendProcessLaunchMessageWithParametersResult", scope = SendProcessLaunchMessageWithParametersResponse.class)
    public JAXBElement<Error> createSendProcessLaunchMessageWithParametersResponseSendProcessLaunchMessageWithParametersResult(Error value) {
        return new JAXBElement<Error>(_SendProcessLaunchMessageWithParametersResponseSendProcessLaunchMessageWithParametersResult_QNAME, Error.class, SendProcessLaunchMessageWithParametersResponse.class, value);
    }

}
