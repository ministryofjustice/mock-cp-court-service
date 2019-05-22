
package uk.gov.justice.digital.court.crimeportal.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jobType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="printdate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="late" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="adbox" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="means" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sessions" type="{}sessionsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jobType", propOrder = {
    "printdate",
    "username",
    "late",
    "adbox",
    "means",
    "sessions"
})
public class JobType {

    @XmlElement(required = true)
    protected String printdate;
    @XmlElement(required = true)
    protected String username;
    @XmlElement(required = true)
    protected String late;
    @XmlElement(required = true)
    protected String adbox;
    @XmlElement(required = true)
    protected String means;
    @XmlElement(required = true)
    protected SessionsType sessions;

    /**
     * Gets the value of the printdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrintdate() {
        return printdate;
    }

    /**
     * Sets the value of the printdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrintdate(String value) {
        this.printdate = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the late property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLate() {
        return late;
    }

    /**
     * Sets the value of the late property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLate(String value) {
        this.late = value;
    }

    /**
     * Gets the value of the adbox property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdbox() {
        return adbox;
    }

    /**
     * Sets the value of the adbox property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdbox(String value) {
        this.adbox = value;
    }

    /**
     * Gets the value of the means property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeans() {
        return means;
    }

    /**
     * Sets the value of the means property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeans(String value) {
        this.means = value;
    }

    /**
     * Gets the value of the sessions property.
     * 
     * @return
     *     possible object is
     *     {@link SessionsType }
     *     
     */
    public SessionsType getSessions() {
        return sessions;
    }

    /**
     * Sets the value of the sessions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SessionsType }
     *     
     */
    public void setSessions(SessionsType value) {
        this.sessions = value;
    }

}
