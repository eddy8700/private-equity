
package com.cadisedm.schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.cadis.CADISComponentsComponentKey;
import org.datacontract.schemas._2004._07.cadis.EMessagePriority;


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
 *         &lt;element name="connectionToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processLaunchQueueParameterName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processType" type="{http://schemas.datacontract.org/2004/07/CADIS.Common}CADISComponents.ComponentKey" minOccurs="0"/>
 *         &lt;element name="processName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messagePriority" type="{http://schemas.datacontract.org/2004/07/CADIS.Common}eMessagePriority" minOccurs="0"/>
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
    "connectionToken",
    "processLaunchQueueParameterName",
    "processType",
    "processName",
    "messagePriority"
})
@XmlRootElement(name = "SendProcessLaunchMessage")
public class SendProcessLaunchMessage {

    @XmlElementRef(name = "connectionToken", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<String> connectionToken;
    @XmlElementRef(name = "processLaunchQueueParameterName", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<String> processLaunchQueueParameterName;
    protected CADISComponentsComponentKey processType;
    @XmlElementRef(name = "processName", namespace = "http://www.cadisedm.com/Schemas/", type = JAXBElement.class)
    protected JAXBElement<String> processName;
    protected EMessagePriority messagePriority;

    /**
     * Gets the value of the connectionToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getConnectionToken() {
        return connectionToken;
    }

    /**
     * Sets the value of the connectionToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setConnectionToken(JAXBElement<String> value) {
        this.connectionToken = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the processLaunchQueueParameterName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProcessLaunchQueueParameterName() {
        return processLaunchQueueParameterName;
    }

    /**
     * Sets the value of the processLaunchQueueParameterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProcessLaunchQueueParameterName(JAXBElement<String> value) {
        this.processLaunchQueueParameterName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the processType property.
     * 
     * @return
     *     possible object is
     *     {@link CADISComponentsComponentKey }
     *     
     */
    public CADISComponentsComponentKey getProcessType() {
        return processType;
    }

    /**
     * Sets the value of the processType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CADISComponentsComponentKey }
     *     
     */
    public void setProcessType(CADISComponentsComponentKey value) {
        this.processType = value;
    }

    /**
     * Gets the value of the processName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProcessName() {
        return processName;
    }

    /**
     * Sets the value of the processName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProcessName(JAXBElement<String> value) {
        this.processName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the messagePriority property.
     * 
     * @return
     *     possible object is
     *     {@link EMessagePriority }
     *     
     */
    public EMessagePriority getMessagePriority() {
        return messagePriority;
    }

    /**
     * Sets the value of the messagePriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link EMessagePriority }
     *     
     */
    public void setMessagePriority(EMessagePriority value) {
        this.messagePriority = value;
    }

}
