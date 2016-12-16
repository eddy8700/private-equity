
package com.cadisedm.schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConnectResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorDetails" type="{http://www.cadisedm.com/Schemas/}Error" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "connectResult",
    "errorDetails"
})
@XmlRootElement(name = "ConnectResponse")
public class ConnectResponse {

    @XmlElementRef(name = "ConnectResult", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<String> connectResult;
    @XmlElementRef(name = "errorDetails", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<Error> errorDetails;

    /**
     * Gets the value of the connectResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getConnectResult() {
        return connectResult;
    }

    /**
     * Sets the value of the connectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setConnectResult(JAXBElement<String> value) {
        this.connectResult = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the errorDetails property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public JAXBElement<Error> getErrorDetails() {
        return errorDetails;
    }

    /**
     * Sets the value of the errorDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public void setErrorDetails(JAXBElement<Error> value) {
        this.errorDetails = ((JAXBElement<Error> ) value);
    }

}
