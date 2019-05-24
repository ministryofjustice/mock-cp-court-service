
package uk.gov.justice.digital.court.crimeportal.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for documentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="info" type="{}infoType"/>
 *         &lt;element name="data" type="{}dataType"/>
 *         &lt;element name="end_time" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elapsedsecs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentType", propOrder = {
    "info",
    "data",
    "endTime",
    "elapsedsecs"
})
@XmlRootElement(name="document")
public class DocumentType {

    @XmlElement(required = true)
    protected InfoType info;
    @XmlElement(required = true)
    protected DataType data;
    @XmlElement(name = "end_time", required = true)
    protected String endTime;
    @XmlElement(required = true)
    protected String elapsedsecs;

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link InfoType }
     *     
     */
    public InfoType getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoType }
     *     
     */
    public void setInfo(InfoType value) {
        this.info = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link DataType }
     *     
     */
    public DataType getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataType }
     *     
     */
    public void setData(DataType value) {
        this.data = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @JsonProperty("end_time")
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the elapsedsecs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElapsedsecs() {
        return elapsedsecs;
    }

    /**
     * Sets the value of the elapsedsecs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElapsedsecs(String value) {
        this.elapsedsecs = value;
    }

}
