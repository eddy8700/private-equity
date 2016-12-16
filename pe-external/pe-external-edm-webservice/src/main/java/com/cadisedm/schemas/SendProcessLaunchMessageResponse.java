
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
 *         &lt;element name="SendProcessLaunchMessageResult" type="{http://www.cadisedm.com/Schemas/}Error" minOccurs="0"/>
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
    "sendProcessLaunchMessageResult"
})
@XmlRootElement(name = "SendProcessLaunchMessageResponse")
public class SendProcessLaunchMessageResponse {

    @XmlElementRef(name = "SendProcessLaunchMessageResult", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<Error> sendProcessLaunchMessageResult;

    /**
     * Gets the value of the sendProcessLaunchMessageResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public JAXBElement<Error> getSendProcessLaunchMessageResult() {
        return sendProcessLaunchMessageResult;
    }

    /**
     * Sets the value of the sendProcessLaunchMessageResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public void setSendProcessLaunchMessageResult(JAXBElement<Error> value) {
        this.sendProcessLaunchMessageResult = ((JAXBElement<Error> ) value);
    }

}
