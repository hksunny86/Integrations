
package com.inov8.integration.middleware.bop.m3Tech;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.bop.m3Tech package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.bop.m3Tech
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CheckRegistration }
     * 
     */
    public CheckRegistration createCheckRegistration() {
        return new CheckRegistration();
    }

    /**
     * Create an instance of {@link CheckRegistrationResponse }
     * 
     */
    public CheckRegistrationResponse createCheckRegistrationResponse() {
        return new CheckRegistrationResponse();
    }

    /**
     * Create an instance of {@link SendSMS }
     * 
     */
    public SendSMS createSendSMS() {
        return new SendSMS();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     * 
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

}
