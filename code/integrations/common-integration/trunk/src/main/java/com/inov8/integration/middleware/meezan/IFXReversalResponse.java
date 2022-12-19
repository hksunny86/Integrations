
package com.inov8.integration.middleware.meezan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BankSvcRs" minOccurs="0" form="unqualified"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ReversalRs" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="Status" minOccurs="0" form="unqualified"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *                                       &lt;element name="StatusDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bankSvcRs"
})
@XmlRootElement(name = "IFXReversalResponse")
public class IFXReversalResponse {

    @XmlElement(name = "BankSvcRs")
    protected IFXReversalResponse.BankSvcRs bankSvcRs;

    /**
     * Gets the value of the bankSvcRs property.
     * 
     * @return
     *     possible object is
     *     {@link IFXReversalResponse.BankSvcRs }
     *     
     */
    public IFXReversalResponse.BankSvcRs getBankSvcRs() {
        return bankSvcRs;
    }

    /**
     * Sets the value of the bankSvcRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link IFXReversalResponse.BankSvcRs }
     *     
     */
    public void setBankSvcRs(IFXReversalResponse.BankSvcRs value) {
        this.bankSvcRs = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ReversalRs" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="Status" minOccurs="0" form="unqualified"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
     *                             &lt;element name="StatusDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "reversalRs"
    })
    public static class BankSvcRs {

        @XmlElement(name = "ReversalRs")
        protected IFXReversalResponse.BankSvcRs.ReversalRs reversalRs;

        /**
         * Gets the value of the reversalRs property.
         * 
         * @return
         *     possible object is
         *     {@link IFXReversalResponse.BankSvcRs.ReversalRs }
         *     
         */
        public IFXReversalResponse.BankSvcRs.ReversalRs getReversalRs() {
            return reversalRs;
        }

        /**
         * Sets the value of the reversalRs property.
         * 
         * @param value
         *     allowed object is
         *     {@link IFXReversalResponse.BankSvcRs.ReversalRs }
         *     
         */
        public void setReversalRs(IFXReversalResponse.BankSvcRs.ReversalRs value) {
            this.reversalRs = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="Status" minOccurs="0" form="unqualified"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
         *                   &lt;element name="StatusDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "status"
        })
        public static class ReversalRs {

            @XmlElement(name = "Status")
            protected IFXReversalResponse.BankSvcRs.ReversalRs.Status status;

            /**
             * Gets the value of the status property.
             * 
             * @return
             *     possible object is
             *     {@link IFXReversalResponse.BankSvcRs.ReversalRs.Status }
             *     
             */
            public IFXReversalResponse.BankSvcRs.ReversalRs.Status getStatus() {
                return status;
            }

            /**
             * Sets the value of the status property.
             * 
             * @param value
             *     allowed object is
             *     {@link IFXReversalResponse.BankSvcRs.ReversalRs.Status }
             *     
             */
            public void setStatus(IFXReversalResponse.BankSvcRs.ReversalRs.Status value) {
                this.status = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
             *         &lt;element name="StatusDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "statusCode",
                "statusDesc"
            })
            public static class Status {

                @XmlElement(name = "StatusCode", required = true)
                protected String statusCode;
                @XmlElement(name = "StatusDesc")
                protected String statusDesc;

                /**
                 * Gets the value of the statusCode property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getStatusCode() {
                    return statusCode;
                }

                /**
                 * Sets the value of the statusCode property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setStatusCode(String value) {
                    this.statusCode = value;
                }

                /**
                 * Gets the value of the statusDesc property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getStatusDesc() {
                    return statusDesc;
                }

                /**
                 * Sets the value of the statusDesc property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setStatusDesc(String value) {
                    this.statusDesc = value;
                }

            }

        }

    }

}
