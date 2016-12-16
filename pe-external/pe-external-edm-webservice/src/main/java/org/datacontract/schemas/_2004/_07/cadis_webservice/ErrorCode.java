
package org.datacontract.schemas._2004._07.cadis_webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Error.Code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Error.Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="Error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Error.Code", namespace = "http://schemas.datacontract.org/2004/07/CADIS.WebService.Data")
@XmlEnum
public enum ErrorCode {

    OK("OK"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    ErrorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorCode fromValue(String v) {
        for (ErrorCode c: ErrorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
