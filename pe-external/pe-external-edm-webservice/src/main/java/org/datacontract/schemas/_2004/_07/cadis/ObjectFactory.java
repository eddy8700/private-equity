
package org.datacontract.schemas._2004._07.cadis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.cadis package. 
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

    private final static QName _CADISComponentsComponentKey_QNAME = new QName("http://schemas.datacontract.org/2004/07/CADIS.Common", "CADISComponents.ComponentKey");
    private final static QName _EMessagePriority_QNAME = new QName("http://schemas.datacontract.org/2004/07/CADIS.Common", "eMessagePriority");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.cadis
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CADISComponentsComponentKey }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/CADIS.Common", name = "CADISComponents.ComponentKey")
    public JAXBElement<CADISComponentsComponentKey> createCADISComponentsComponentKey(CADISComponentsComponentKey value) {
        return new JAXBElement<CADISComponentsComponentKey>(_CADISComponentsComponentKey_QNAME, CADISComponentsComponentKey.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EMessagePriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/CADIS.Common", name = "eMessagePriority")
    public JAXBElement<EMessagePriority> createEMessagePriority(EMessagePriority value) {
        return new JAXBElement<EMessagePriority>(_EMessagePriority_QNAME, EMessagePriority.class, null, value);
    }

}
