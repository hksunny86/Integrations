
package com.inov8.integration.middleware.bop.avenza;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.bop.avenza package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.bop.avenza
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustomerEmailUpdateRequest }
     * 
     */
    public CustomerEmailUpdateRequest createCustomerEmailUpdateRequest() {
        return new CustomerEmailUpdateRequest();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link CustomerEmailUpdateResponse }
     * 
     */
    public CustomerEmailUpdateResponse createCustomerEmailUpdateResponse() {
        return new CustomerEmailUpdateResponse();
    }

    /**
     * Create an instance of {@link CustomerEmailVerificationRequest }
     * 
     */
    public CustomerEmailVerificationRequest createCustomerEmailVerificationRequest() {
        return new CustomerEmailVerificationRequest();
    }

    /**
     * Create an instance of {@link CustomerEmailVerificationResponse }
     * 
     */
    public CustomerEmailVerificationResponse createCustomerEmailVerificationResponse() {
        return new CustomerEmailVerificationResponse();
    }

    /**
     * Create an instance of {@link ViewAccountStatementRequest }
     * 
     */
    public ViewAccountStatementRequest createViewAccountStatementRequest() {
        return new ViewAccountStatementRequest();
    }

    /**
     * Create an instance of {@link ViewAccountStatementResponse }
     * 
     */
    public ViewAccountStatementResponse createViewAccountStatementResponse() {
        return new ViewAccountStatementResponse();
    }

    /**
     * Create an instance of {@link MiniStatementBlk }
     * 
     */
    public MiniStatementBlk createMiniStatementBlk() {
        return new MiniStatementBlk();
    }

    /**
     * Create an instance of {@link EmailAccountStatementRequest }
     * 
     */
    public EmailAccountStatementRequest createEmailAccountStatementRequest() {
        return new EmailAccountStatementRequest();
    }

    /**
     * Create an instance of {@link EmailAccountStatementResponse }
     * 
     */
    public EmailAccountStatementResponse createEmailAccountStatementResponse() {
        return new EmailAccountStatementResponse();
    }

}
