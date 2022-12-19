
package com.inov8.integration.middleware.meezan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="SignonRq" minOccurs="0" form="unqualified"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="SignonPswd" minOccurs="0" form="unqualified"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CustId" minOccurs="0" form="unqualified"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="CustLoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="CustPswd" minOccurs="0" form="unqualified"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="CryptPswd" minOccurs="0" form="unqualified"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;complexContent&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                               &lt;sequence&gt;
 *                                                 &lt;element name="BinLength" type="{http://www.w3.org/2001/XMLSchema}long" form="unqualified"/&gt;
 *                                                 &lt;element name="BinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                                                 &lt;element name="CustPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                 &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                               &lt;/sequence&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/complexContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
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
 *         &lt;element name="BankSvcRq" minOccurs="0" form="unqualified"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RqUID" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *                   &lt;element name="SvcName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                   &lt;element name="T24ReversalRq" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="ChannelSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="TransactionSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="TranDateAndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                             &lt;element name="STAN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="PAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="OriginalID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="Reserved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "signonRq",
    "bankSvcRq"
})
@XmlRootElement(name = "IFXReversalRequest")
public class IFXReversalRequest {

    @XmlElement(name = "SignonRq")
    protected IFXReversalRequest.SignonRq signonRq;
    @XmlElement(name = "BankSvcRq")
    protected IFXReversalRequest.BankSvcRq bankSvcRq;

    /**
     * Gets the value of the signonRq property.
     * 
     * @return
     *     possible object is
     *     {@link IFXReversalRequest.SignonRq }
     *     
     */
    public IFXReversalRequest.SignonRq getSignonRq() {
        return signonRq;
    }

    /**
     * Sets the value of the signonRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link IFXReversalRequest.SignonRq }
     *     
     */
    public void setSignonRq(IFXReversalRequest.SignonRq value) {
        this.signonRq = value;
    }

    /**
     * Gets the value of the bankSvcRq property.
     * 
     * @return
     *     possible object is
     *     {@link IFXReversalRequest.BankSvcRq }
     *     
     */
    public IFXReversalRequest.BankSvcRq getBankSvcRq() {
        return bankSvcRq;
    }

    /**
     * Sets the value of the bankSvcRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link IFXReversalRequest.BankSvcRq }
     *     
     */
    public void setBankSvcRq(IFXReversalRequest.BankSvcRq value) {
        this.bankSvcRq = value;
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
     *         &lt;element name="RqUID" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
     *         &lt;element name="SvcName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *         &lt;element name="T24ReversalRq" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ChannelSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="TransactionSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="TranDateAndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *                   &lt;element name="STAN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="PAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="OriginalID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="Reserved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "rqUID",
        "svcName",
        "t24ReversalRq"
    })
    public static class BankSvcRq {

        @XmlElement(name = "RqUID", required = true, nillable = true)
        protected String rqUID;
        @XmlElement(name = "SvcName")
        protected String svcName;
        @XmlElement(name = "T24ReversalRq")
        protected IFXReversalRequest.BankSvcRq.T24ReversalRq t24ReversalRq;

        /**
         * Gets the value of the rqUID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRqUID() {
            return rqUID;
        }

        /**
         * Sets the value of the rqUID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRqUID(String value) {
            this.rqUID = value;
        }

        /**
         * Gets the value of the svcName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSvcName() {
            return svcName;
        }

        /**
         * Sets the value of the svcName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSvcName(String value) {
            this.svcName = value;
        }

        /**
         * Gets the value of the t24ReversalRq property.
         * 
         * @return
         *     possible object is
         *     {@link IFXReversalRequest.BankSvcRq.T24ReversalRq }
         *     
         */
        public IFXReversalRequest.BankSvcRq.T24ReversalRq getT24ReversalRq() {
            return t24ReversalRq;
        }

        /**
         * Sets the value of the t24ReversalRq property.
         * 
         * @param value
         *     allowed object is
         *     {@link IFXReversalRequest.BankSvcRq.T24ReversalRq }
         *     
         */
        public void setT24ReversalRq(IFXReversalRequest.BankSvcRq.T24ReversalRq value) {
            this.t24ReversalRq = value;
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
         *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ChannelSubType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="TransactionSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="TranDateAndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
         *         &lt;element name="STAN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="PAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="OriginalID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="Reserved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "channelType",
            "channelSubType",
            "transactionType",
            "transactionSubType",
            "tranDateAndTime",
            "stan",
            "pan",
            "originalID",
            "reserved"
        })
        public static class T24ReversalRq {

            @XmlElement(name = "ChannelType", required = true)
            protected String channelType;
            @XmlElement(name = "ChannelSubType", required = true)
            protected String channelSubType;
            @XmlElement(name = "TransactionType", required = true)
            protected String transactionType;
            @XmlElement(name = "TransactionSubType")
            protected String transactionSubType;
            @XmlElement(name = "TranDateAndTime", required = true)
            @XmlSchemaType(name = "dateTime")
            protected XMLGregorianCalendar tranDateAndTime;
            @XmlElement(name = "STAN", required = true)
            protected String stan;
            @XmlElement(name = "PAN")
            protected String pan;
            @XmlElement(name = "OriginalID", required = true)
            protected String originalID;
            @XmlElement(name = "Reserved")
            protected String reserved;

            /**
             * Gets the value of the channelType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getChannelType() {
                return channelType;
            }

            /**
             * Sets the value of the channelType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setChannelType(String value) {
                this.channelType = value;
            }

            /**
             * Gets the value of the channelSubType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getChannelSubType() {
                return channelSubType;
            }

            /**
             * Sets the value of the channelSubType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setChannelSubType(String value) {
                this.channelSubType = value;
            }

            /**
             * Gets the value of the transactionType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTransactionType() {
                return transactionType;
            }

            /**
             * Sets the value of the transactionType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTransactionType(String value) {
                this.transactionType = value;
            }

            /**
             * Gets the value of the transactionSubType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTransactionSubType() {
                return transactionSubType;
            }

            /**
             * Sets the value of the transactionSubType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTransactionSubType(String value) {
                this.transactionSubType = value;
            }

            /**
             * Gets the value of the tranDateAndTime property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getTranDateAndTime() {
                return tranDateAndTime;
            }

            /**
             * Sets the value of the tranDateAndTime property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setTranDateAndTime(XMLGregorianCalendar value) {
                this.tranDateAndTime = value;
            }

            /**
             * Gets the value of the stan property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSTAN() {
                return stan;
            }

            /**
             * Sets the value of the stan property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSTAN(String value) {
                this.stan = value;
            }

            /**
             * Gets the value of the pan property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPAN() {
                return pan;
            }

            /**
             * Sets the value of the pan property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPAN(String value) {
                this.pan = value;
            }

            /**
             * Gets the value of the originalID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOriginalID() {
                return originalID;
            }

            /**
             * Sets the value of the originalID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOriginalID(String value) {
                this.originalID = value;
            }

            /**
             * Gets the value of the reserved property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getReserved() {
                return reserved;
            }

            /**
             * Sets the value of the reserved property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setReserved(String value) {
                this.reserved = value;
            }

        }

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
     *         &lt;element name="SignonPswd" minOccurs="0" form="unqualified"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CustId" minOccurs="0" form="unqualified"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="CustLoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="CustPswd" minOccurs="0" form="unqualified"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="CryptPswd" minOccurs="0" form="unqualified"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="BinLength" type="{http://www.w3.org/2001/XMLSchema}long" form="unqualified"/&gt;
     *                                       &lt;element name="BinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                                       &lt;element name="CustPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "signonPswd"
    })
    public static class SignonRq {

        @XmlElement(name = "SignonPswd")
        protected IFXReversalRequest.SignonRq.SignonPswd signonPswd;

        /**
         * Gets the value of the signonPswd property.
         * 
         * @return
         *     possible object is
         *     {@link IFXReversalRequest.SignonRq.SignonPswd }
         *     
         */
        public IFXReversalRequest.SignonRq.SignonPswd getSignonPswd() {
            return signonPswd;
        }

        /**
         * Sets the value of the signonPswd property.
         * 
         * @param value
         *     allowed object is
         *     {@link IFXReversalRequest.SignonRq.SignonPswd }
         *     
         */
        public void setSignonPswd(IFXReversalRequest.SignonRq.SignonPswd value) {
            this.signonPswd = value;
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
         *         &lt;element name="CustId" minOccurs="0" form="unqualified"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="CustLoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="CustPswd" minOccurs="0" form="unqualified"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="CryptPswd" minOccurs="0" form="unqualified"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="BinLength" type="{http://www.w3.org/2001/XMLSchema}long" form="unqualified"/&gt;
         *                             &lt;element name="BinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *                             &lt;element name="CustPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "custId",
            "custPswd"
        })
        public static class SignonPswd {

            @XmlElement(name = "CustId")
            protected IFXReversalRequest.SignonRq.SignonPswd.CustId custId;
            @XmlElement(name = "CustPswd")
            protected IFXReversalRequest.SignonRq.SignonPswd.CustPswd custPswd;

            /**
             * Gets the value of the custId property.
             * 
             * @return
             *     possible object is
             *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustId }
             *     
             */
            public IFXReversalRequest.SignonRq.SignonPswd.CustId getCustId() {
                return custId;
            }

            /**
             * Sets the value of the custId property.
             * 
             * @param value
             *     allowed object is
             *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustId }
             *     
             */
            public void setCustId(IFXReversalRequest.SignonRq.SignonPswd.CustId value) {
                this.custId = value;
            }

            /**
             * Gets the value of the custPswd property.
             * 
             * @return
             *     possible object is
             *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustPswd }
             *     
             */
            public IFXReversalRequest.SignonRq.SignonPswd.CustPswd getCustPswd() {
                return custPswd;
            }

            /**
             * Sets the value of the custPswd property.
             * 
             * @param value
             *     allowed object is
             *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustPswd }
             *     
             */
            public void setCustPswd(IFXReversalRequest.SignonRq.SignonPswd.CustPswd value) {
                this.custPswd = value;
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
             *         &lt;element name="CustLoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
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
                "custLoginId"
            })
            public static class CustId {

                @XmlElement(name = "CustLoginId")
                protected String custLoginId;

                /**
                 * Gets the value of the custLoginId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCustLoginId() {
                    return custLoginId;
                }

                /**
                 * Sets the value of the custLoginId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCustLoginId(String value) {
                    this.custLoginId = value;
                }

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
             *         &lt;element name="CryptPswd" minOccurs="0" form="unqualified"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="BinLength" type="{http://www.w3.org/2001/XMLSchema}long" form="unqualified"/&gt;
             *                   &lt;element name="BinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
             *                   &lt;element name="CustPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
                "cryptPswd"
            })
            public static class CustPswd {

                @XmlElement(name = "CryptPswd")
                protected IFXReversalRequest.SignonRq.SignonPswd.CustPswd.CryptPswd cryptPswd;

                /**
                 * Gets the value of the cryptPswd property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustPswd.CryptPswd }
                 *     
                 */
                public IFXReversalRequest.SignonRq.SignonPswd.CustPswd.CryptPswd getCryptPswd() {
                    return cryptPswd;
                }

                /**
                 * Sets the value of the cryptPswd property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IFXReversalRequest.SignonRq.SignonPswd.CustPswd.CryptPswd }
                 *     
                 */
                public void setCryptPswd(IFXReversalRequest.SignonRq.SignonPswd.CustPswd.CryptPswd value) {
                    this.cryptPswd = value;
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
                 *         &lt;element name="BinLength" type="{http://www.w3.org/2001/XMLSchema}long" form="unqualified"/&gt;
                 *         &lt;element name="BinData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
                 *         &lt;element name="CustPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="AuthenticationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
                    "binLength",
                    "binData",
                    "custPassword",
                    "authenticationCode"
                })
                public static class CryptPswd {

                    @XmlElement(name = "BinLength")
                    protected long binLength;
                    @XmlElement(name = "BinData")
                    protected String binData;
                    @XmlElement(name = "CustPassword")
                    protected String custPassword;
                    @XmlElement(name = "AuthenticationCode")
                    protected String authenticationCode;

                    /**
                     * Gets the value of the binLength property.
                     * 
                     */
                    public long getBinLength() {
                        return binLength;
                    }

                    /**
                     * Sets the value of the binLength property.
                     * 
                     */
                    public void setBinLength(long value) {
                        this.binLength = value;
                    }

                    /**
                     * Gets the value of the binData property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBinData() {
                        return binData;
                    }

                    /**
                     * Sets the value of the binData property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBinData(String value) {
                        this.binData = value;
                    }

                    /**
                     * Gets the value of the custPassword property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCustPassword() {
                        return custPassword;
                    }

                    /**
                     * Sets the value of the custPassword property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCustPassword(String value) {
                        this.custPassword = value;
                    }

                    /**
                     * Gets the value of the authenticationCode property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAuthenticationCode() {
                        return authenticationCode;
                    }

                    /**
                     * Sets the value of the authenticationCode property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAuthenticationCode(String value) {
                        this.authenticationCode = value;
                    }

                }

            }

        }

    }

}
