
package org.datacontract.schemas._2004._07.cadis;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eMessagePriority.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eMessagePriority">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Default"/>
 *     &lt;enumeration value="VeryLow"/>
 *     &lt;enumeration value="Low"/>
 *     &lt;enumeration value="Normal"/>
 *     &lt;enumeration value="High"/>
 *     &lt;enumeration value="Highest"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eMessagePriority", namespace = "http://schemas.datacontract.org/2004/07/CADIS.Common")
@XmlEnum
public enum EMessagePriority {

    @XmlEnumValue("Default")
    DEFAULT("Default"),
    @XmlEnumValue("VeryLow")
    VERY_LOW("VeryLow"),
    @XmlEnumValue("Low")
    LOW("Low"),
    @XmlEnumValue("Normal")
    NORMAL("Normal"),
    @XmlEnumValue("High")
    HIGH("High"),
    @XmlEnumValue("Highest")
    HIGHEST("Highest");
    private final String value;

    EMessagePriority(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EMessagePriority fromValue(String v) {
        for (EMessagePriority c: EMessagePriority.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
