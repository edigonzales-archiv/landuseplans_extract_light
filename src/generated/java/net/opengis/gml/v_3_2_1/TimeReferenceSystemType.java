
package net.opengis.gml.v_3_2_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeReferenceSystemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeReferenceSystemType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/gml/3.2}DefinitionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="domainOfValidity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeReferenceSystemType", propOrder = {
    "domainOfValidity"
})
@XmlSeeAlso({
    TimeCoordinateSystemType.class,
    TimeCalendarType.class,
    TimeClockType.class,
    TimeOrdinalReferenceSystemType.class
})
public class TimeReferenceSystemType
    extends DefinitionType
{

    @XmlElement(required = true)
    protected String domainOfValidity;

    /**
     * Gets the value of the domainOfValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainOfValidity() {
        return domainOfValidity;
    }

    /**
     * Sets the value of the domainOfValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainOfValidity(String value) {
        this.domainOfValidity = value;
    }

    public boolean isSetDomainOfValidity() {
        return (this.domainOfValidity!= null);
    }

}
