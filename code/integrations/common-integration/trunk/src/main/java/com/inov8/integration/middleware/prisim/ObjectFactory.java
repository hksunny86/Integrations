
package com.inov8.integration.middleware.prisim;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inov8.integration.middleware.prisim package. 
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

    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _InputHeader_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "InputHeader");
    private final static QName _CustomerRequestOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CustomerRequestOutputParams");
    private final static QName _CustomerDetail_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CustomerDetail");
    private final static QName _OutputHeader_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "OutputHeader");
    private final static QName _AccountStatementFilter_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AccountStatementFilter");
    private final static QName _AccountStatementOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AccountStatementOutputParams");
    private final static QName _ArrayOfTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfTransactionList");
    private final static QName _TransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "TransactionList");
    private final static QName _BankDetailsOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BankDetailsOutputParams");
    private final static QName _ArrayOfBankList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBankList");
    private final static QName _BankList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BankList");
    private final static QName _BeneficiaryRelationshipOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BeneficiaryRelationshipOutputParams");
    private final static QName _ArrayOfRelationship_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfRelationship");
    private final static QName _Relationship_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "Relationship");
    private final static QName _BeneficiaryAccountOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BeneficiaryAccountOutputParams");
    private final static QName _ArrayOfBeneficiaryAccount_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBeneficiaryAccount");
    private final static QName _BeneficiaryAccount_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BeneficiaryAccount");
    private final static QName _OTP_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "OTP");
    private final static QName _AddBeneficiaryOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AddBeneficiaryOutputParams");
    private final static QName _ModifyBeneficiaryAccountOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ModifyBeneficiaryAccountOutputParams");
    private final static QName _DeleteBeneficiaryAccountOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "DeleteBeneficiaryAccountOutputParams");
    private final static QName _BillPayeeOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillPayeeOutputParams");
    private final static QName _ArrayOfBillPayee_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBillPayee");
    private final static QName _BillPayee_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillPayee");
    private final static QName _AddBillPayeeOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AddBillPayeeOutputParams");
    private final static QName _DeleteBillPayeeOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "DeleteBillPayeeOutputParams");
    private final static QName _BillingCompanyTypeOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillingCompanyTypeOutputParams");
    private final static QName _ArrayOfBillingCompanyType_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBillingCompanyType");
    private final static QName _BillingCompanyType_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillingCompanyType");
    private final static QName _BillingCompaniesOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillingCompaniesOutputParams");
    private final static QName _ArrayOfBillingCompany_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBillingCompany");
    private final static QName _BillingCompany_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillingCompany");
    private final static QName _PaymentRule_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "PaymentRule");
    private final static QName _GenerateFinancialPinOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "GenerateFinancialPinOutputParams");
    private final static QName _VerifyFinancialPinOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "VerifyFinancialPinOutputParams");
    private final static QName _ExpireFinancialPinOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ExpireFinancialPinOutputParams");
    private final static QName _GenerateOTPOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "GenerateOTPOutputParams");
    private final static QName _Registration_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "Registration");
    private final static QName _SecretQuestion_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "SecretQuestion");
    private final static QName _RegistrationOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "RegistrationOutputParams");
    private final static QName _SecretQuestionOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "SecretQuestionOutputParams");
    private final static QName _ArrayOfSecretQuestion_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfSecretQuestion");
    private final static QName _SetFirstTimeLoginOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "SetFirstTimeLoginOutputParams");
    private final static QName _CardStatementFilter_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CardStatementFilter");
    private final static QName _CardStatementOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CardStatementOutputParams");
    private final static QName _ArrayOfCardTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfCardTransactionList");
    private final static QName _CardTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CardTransactionList");
    private final static QName _PurposeOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "PurposeOutputParams");
    private final static QName _ArrayOfPurpose_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfPurpose");
    private final static QName _Purpose_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "Purpose");
    private final static QName _AuditLoggingRequest_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AuditLoggingRequest");
    private final static QName _AuditLoggingOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "AuditLoggingOutputParams");
    private final static QName _ForgotIdOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ForgotIdOutputParams");
    private final static QName _LoginHistoryOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "LoginHistoryOutputParams");
    private final static QName _ArrayOfLoginHistoryTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfLoginHistoryTransactionList");
    private final static QName _LoginHistoryTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "LoginHistoryTransactionList");
    private final static QName _TransactionHistoryOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "TransactionHistoryOutputParams");
    private final static QName _ArrayOfTransactionHistoryList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfTransactionHistoryList");
    private final static QName _TransactionHistoryList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "TransactionHistoryList");
    private final static QName _CustomerAccount_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "CustomerAccount");
    private final static QName _PaymentDetailDTO_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "PaymentDetailDTO");
    private final static QName _FundTransferScheduleOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "FundTransferScheduleOutputParams");
    private final static QName _BillPaymentScheduleOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BillPaymentScheduleOutputParams");
    private final static QName _ChangeEmailOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ChangeEmailOutputParams");
    private final static QName _ChangeSecretAnswerOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ChangeSecretAnswerOutputParams");
    private final static QName _ForgotSecretAnswerOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ForgotSecretAnswerOutputParams");
    private final static QName _BlockInternetBankingOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BlockInternetBankingOutputParams");
    private final static QName _ViewScheduleFundsTransferOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ViewScheduleFundsTransferOutputParams");
    private final static QName _ArrayOfFTScheduleTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfFTScheduleTransactionList");
    private final static QName _FTScheduleTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "FTScheduleTransactionList");
    private final static QName _ViewScheduledBillPaymentOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ViewScheduledBillPaymentOutputParams");
    private final static QName _ArrayOfBPScheduleTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "ArrayOfBPScheduleTransactionList");
    private final static QName _BPScheduleTransactionList_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "BPScheduleTransactionList");
    private final static QName _EditScheduleTransactionOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "EditScheduleTransactionOutputParams");
    private final static QName _DeleteScheduleTransactionOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "DeleteScheduleTransactionOutputParams");
    private final static QName _GenerateMobilePinOutputParams_QNAME = new QName("http://schemas.datacontract.org/2004/07/SBLServiceModels", "GenerateMobilePinOutputParams");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inov8.integration.middleware.prisim
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCustomer }
     * 
     */
    public GetCustomer createGetCustomer() {
        return new GetCustomer();
    }

    /**
     * Create an instance of {@link InputHeader }
     * 
     */
    public InputHeader createInputHeader() {
        return new InputHeader();
    }

    /**
     * Create an instance of {@link GetCustomerResponse }
     * 
     */
    public GetCustomerResponse createGetCustomerResponse() {
        return new GetCustomerResponse();
    }

    /**
     * Create an instance of {@link CustomerRequestOutputParams }
     * 
     */
    public CustomerRequestOutputParams createCustomerRequestOutputParams() {
        return new CustomerRequestOutputParams();
    }

    /**
     * Create an instance of {@link GetAccountStatement }
     * 
     */
    public GetAccountStatement createGetAccountStatement() {
        return new GetAccountStatement();
    }

    /**
     * Create an instance of {@link AccountStatementFilter }
     * 
     */
    public AccountStatementFilter createAccountStatementFilter() {
        return new AccountStatementFilter();
    }

    /**
     * Create an instance of {@link GetAccountStatementResponse }
     * 
     */
    public GetAccountStatementResponse createGetAccountStatementResponse() {
        return new GetAccountStatementResponse();
    }

    /**
     * Create an instance of {@link AccountStatementOutputParams }
     * 
     */
    public AccountStatementOutputParams createAccountStatementOutputParams() {
        return new AccountStatementOutputParams();
    }

    /**
     * Create an instance of {@link GetBankDetails }
     * 
     */
    public GetBankDetails createGetBankDetails() {
        return new GetBankDetails();
    }

    /**
     * Create an instance of {@link GetBankDetailsResponse }
     * 
     */
    public GetBankDetailsResponse createGetBankDetailsResponse() {
        return new GetBankDetailsResponse();
    }

    /**
     * Create an instance of {@link BankDetailsOutputParams }
     * 
     */
    public BankDetailsOutputParams createBankDetailsOutputParams() {
        return new BankDetailsOutputParams();
    }

    /**
     * Create an instance of {@link GetBeneficiaryRelationships }
     * 
     */
    public GetBeneficiaryRelationships createGetBeneficiaryRelationships() {
        return new GetBeneficiaryRelationships();
    }

    /**
     * Create an instance of {@link GetBeneficiaryRelationshipsResponse }
     * 
     */
    public GetBeneficiaryRelationshipsResponse createGetBeneficiaryRelationshipsResponse() {
        return new GetBeneficiaryRelationshipsResponse();
    }

    /**
     * Create an instance of {@link BeneficiaryRelationshipOutputParams }
     * 
     */
    public BeneficiaryRelationshipOutputParams createBeneficiaryRelationshipOutputParams() {
        return new BeneficiaryRelationshipOutputParams();
    }

    /**
     * Create an instance of {@link GetBeneficiaryAccounts }
     * 
     */
    public GetBeneficiaryAccounts createGetBeneficiaryAccounts() {
        return new GetBeneficiaryAccounts();
    }

    /**
     * Create an instance of {@link GetBeneficiaryAccountsResponse }
     * 
     */
    public GetBeneficiaryAccountsResponse createGetBeneficiaryAccountsResponse() {
        return new GetBeneficiaryAccountsResponse();
    }

    /**
     * Create an instance of {@link BeneficiaryAccountOutputParams }
     * 
     */
    public BeneficiaryAccountOutputParams createBeneficiaryAccountOutputParams() {
        return new BeneficiaryAccountOutputParams();
    }

    /**
     * Create an instance of {@link AddBeneficiaryAccount }
     * 
     */
    public AddBeneficiaryAccount createAddBeneficiaryAccount() {
        return new AddBeneficiaryAccount();
    }

    /**
     * Create an instance of {@link BeneficiaryAccount }
     * 
     */
    public BeneficiaryAccount createBeneficiaryAccount() {
        return new BeneficiaryAccount();
    }

    /**
     * Create an instance of {@link OTP }
     * 
     */
    public OTP createOTP() {
        return new OTP();
    }

    /**
     * Create an instance of {@link AddBeneficiaryAccountResponse }
     * 
     */
    public AddBeneficiaryAccountResponse createAddBeneficiaryAccountResponse() {
        return new AddBeneficiaryAccountResponse();
    }

    /**
     * Create an instance of {@link AddBeneficiaryOutputParams }
     * 
     */
    public AddBeneficiaryOutputParams createAddBeneficiaryOutputParams() {
        return new AddBeneficiaryOutputParams();
    }

    /**
     * Create an instance of {@link ModifyBeneficiaryAccount }
     * 
     */
    public ModifyBeneficiaryAccount createModifyBeneficiaryAccount() {
        return new ModifyBeneficiaryAccount();
    }

    /**
     * Create an instance of {@link ModifyBeneficiaryAccountResponse }
     * 
     */
    public ModifyBeneficiaryAccountResponse createModifyBeneficiaryAccountResponse() {
        return new ModifyBeneficiaryAccountResponse();
    }

    /**
     * Create an instance of {@link ModifyBeneficiaryAccountOutputParams }
     * 
     */
    public ModifyBeneficiaryAccountOutputParams createModifyBeneficiaryAccountOutputParams() {
        return new ModifyBeneficiaryAccountOutputParams();
    }

    /**
     * Create an instance of {@link DeleteBeneficiaryAccount }
     * 
     */
    public DeleteBeneficiaryAccount createDeleteBeneficiaryAccount() {
        return new DeleteBeneficiaryAccount();
    }

    /**
     * Create an instance of {@link DeleteBeneficiaryAccountResponse }
     * 
     */
    public DeleteBeneficiaryAccountResponse createDeleteBeneficiaryAccountResponse() {
        return new DeleteBeneficiaryAccountResponse();
    }

    /**
     * Create an instance of {@link DeleteBeneficiaryAccountOutputParams }
     * 
     */
    public DeleteBeneficiaryAccountOutputParams createDeleteBeneficiaryAccountOutputParams() {
        return new DeleteBeneficiaryAccountOutputParams();
    }

    /**
     * Create an instance of {@link GetBillPayee }
     * 
     */
    public GetBillPayee createGetBillPayee() {
        return new GetBillPayee();
    }

    /**
     * Create an instance of {@link GetBillPayeeResponse }
     * 
     */
    public GetBillPayeeResponse createGetBillPayeeResponse() {
        return new GetBillPayeeResponse();
    }

    /**
     * Create an instance of {@link BillPayeeOutputParams }
     * 
     */
    public BillPayeeOutputParams createBillPayeeOutputParams() {
        return new BillPayeeOutputParams();
    }

    /**
     * Create an instance of {@link AddBillPayee }
     * 
     */
    public AddBillPayee createAddBillPayee() {
        return new AddBillPayee();
    }

    /**
     * Create an instance of {@link BillPayee }
     * 
     */
    public BillPayee createBillPayee() {
        return new BillPayee();
    }

    /**
     * Create an instance of {@link AddBillPayeeResponse }
     * 
     */
    public AddBillPayeeResponse createAddBillPayeeResponse() {
        return new AddBillPayeeResponse();
    }

    /**
     * Create an instance of {@link AddBillPayeeOutputParams }
     * 
     */
    public AddBillPayeeOutputParams createAddBillPayeeOutputParams() {
        return new AddBillPayeeOutputParams();
    }

    /**
     * Create an instance of {@link DeleteBillPayee }
     * 
     */
    public DeleteBillPayee createDeleteBillPayee() {
        return new DeleteBillPayee();
    }

    /**
     * Create an instance of {@link DeleteBillPayeeResponse }
     * 
     */
    public DeleteBillPayeeResponse createDeleteBillPayeeResponse() {
        return new DeleteBillPayeeResponse();
    }

    /**
     * Create an instance of {@link DeleteBillPayeeOutputParams }
     * 
     */
    public DeleteBillPayeeOutputParams createDeleteBillPayeeOutputParams() {
        return new DeleteBillPayeeOutputParams();
    }

    /**
     * Create an instance of {@link GetBillingCompanyTypes }
     * 
     */
    public GetBillingCompanyTypes createGetBillingCompanyTypes() {
        return new GetBillingCompanyTypes();
    }

    /**
     * Create an instance of {@link GetBillingCompanyTypesResponse }
     * 
     */
    public GetBillingCompanyTypesResponse createGetBillingCompanyTypesResponse() {
        return new GetBillingCompanyTypesResponse();
    }

    /**
     * Create an instance of {@link BillingCompanyTypeOutputParams }
     * 
     */
    public BillingCompanyTypeOutputParams createBillingCompanyTypeOutputParams() {
        return new BillingCompanyTypeOutputParams();
    }

    /**
     * Create an instance of {@link GetBillingCompanies }
     * 
     */
    public GetBillingCompanies createGetBillingCompanies() {
        return new GetBillingCompanies();
    }

    /**
     * Create an instance of {@link GetBillingCompaniesResponse }
     * 
     */
    public GetBillingCompaniesResponse createGetBillingCompaniesResponse() {
        return new GetBillingCompaniesResponse();
    }

    /**
     * Create an instance of {@link BillingCompaniesOutputParams }
     * 
     */
    public BillingCompaniesOutputParams createBillingCompaniesOutputParams() {
        return new BillingCompaniesOutputParams();
    }

    /**
     * Create an instance of {@link GenerateFinancialPIN }
     * 
     */
    public GenerateFinancialPIN createGenerateFinancialPIN() {
        return new GenerateFinancialPIN();
    }

    /**
     * Create an instance of {@link GenerateFinancialPINResponse }
     * 
     */
    public GenerateFinancialPINResponse createGenerateFinancialPINResponse() {
        return new GenerateFinancialPINResponse();
    }

    /**
     * Create an instance of {@link GenerateFinancialPinOutputParams }
     * 
     */
    public GenerateFinancialPinOutputParams createGenerateFinancialPinOutputParams() {
        return new GenerateFinancialPinOutputParams();
    }

    /**
     * Create an instance of {@link VerifyFinancialPIN }
     * 
     */
    public VerifyFinancialPIN createVerifyFinancialPIN() {
        return new VerifyFinancialPIN();
    }

    /**
     * Create an instance of {@link VerifyFinancialPINResponse }
     * 
     */
    public VerifyFinancialPINResponse createVerifyFinancialPINResponse() {
        return new VerifyFinancialPINResponse();
    }

    /**
     * Create an instance of {@link VerifyFinancialPinOutputParams }
     * 
     */
    public VerifyFinancialPinOutputParams createVerifyFinancialPinOutputParams() {
        return new VerifyFinancialPinOutputParams();
    }

    /**
     * Create an instance of {@link ExpireFinancialPIN }
     * 
     */
    public ExpireFinancialPIN createExpireFinancialPIN() {
        return new ExpireFinancialPIN();
    }

    /**
     * Create an instance of {@link ExpireFinancialPINResponse }
     * 
     */
    public ExpireFinancialPINResponse createExpireFinancialPINResponse() {
        return new ExpireFinancialPINResponse();
    }

    /**
     * Create an instance of {@link ExpireFinancialPinOutputParams }
     * 
     */
    public ExpireFinancialPinOutputParams createExpireFinancialPinOutputParams() {
        return new ExpireFinancialPinOutputParams();
    }

    /**
     * Create an instance of {@link GenerateOTP }
     * 
     */
    public GenerateOTP createGenerateOTP() {
        return new GenerateOTP();
    }

    /**
     * Create an instance of {@link GenerateOTPResponse }
     * 
     */
    public GenerateOTPResponse createGenerateOTPResponse() {
        return new GenerateOTPResponse();
    }

    /**
     * Create an instance of {@link GenerateOTPOutputParams }
     * 
     */
    public GenerateOTPOutputParams createGenerateOTPOutputParams() {
        return new GenerateOTPOutputParams();
    }

    /**
     * Create an instance of {@link Registration }
     * 
     */
    public Registration createRegistration() {
        return new Registration();
    }

    /**
     * Create an instance of {@link Registration2 }
     * 
     */
    public Registration2 createRegistration2() {
        return new Registration2();
    }

    /**
     * Create an instance of {@link RegistrationResponse }
     * 
     */
    public RegistrationResponse createRegistrationResponse() {
        return new RegistrationResponse();
    }

    /**
     * Create an instance of {@link RegistrationOutputParams }
     * 
     */
    public RegistrationOutputParams createRegistrationOutputParams() {
        return new RegistrationOutputParams();
    }

    /**
     * Create an instance of {@link GetSecretQuestion }
     * 
     */
    public GetSecretQuestion createGetSecretQuestion() {
        return new GetSecretQuestion();
    }

    /**
     * Create an instance of {@link GetSecretQuestionResponse }
     * 
     */
    public GetSecretQuestionResponse createGetSecretQuestionResponse() {
        return new GetSecretQuestionResponse();
    }

    /**
     * Create an instance of {@link SecretQuestionOutputParams }
     * 
     */
    public SecretQuestionOutputParams createSecretQuestionOutputParams() {
        return new SecretQuestionOutputParams();
    }

    /**
     * Create an instance of {@link SetFirstTimeLogin }
     * 
     */
    public SetFirstTimeLogin createSetFirstTimeLogin() {
        return new SetFirstTimeLogin();
    }

    /**
     * Create an instance of {@link SetFirstTimeLoginResponse }
     * 
     */
    public SetFirstTimeLoginResponse createSetFirstTimeLoginResponse() {
        return new SetFirstTimeLoginResponse();
    }

    /**
     * Create an instance of {@link SetFirstTimeLoginOutputParams }
     * 
     */
    public SetFirstTimeLoginOutputParams createSetFirstTimeLoginOutputParams() {
        return new SetFirstTimeLoginOutputParams();
    }

    /**
     * Create an instance of {@link GetCardStatement }
     * 
     */
    public GetCardStatement createGetCardStatement() {
        return new GetCardStatement();
    }

    /**
     * Create an instance of {@link CardStatementFilter }
     * 
     */
    public CardStatementFilter createCardStatementFilter() {
        return new CardStatementFilter();
    }

    /**
     * Create an instance of {@link GetCardStatementResponse }
     * 
     */
    public GetCardStatementResponse createGetCardStatementResponse() {
        return new GetCardStatementResponse();
    }

    /**
     * Create an instance of {@link CardStatementOutputParams }
     * 
     */
    public CardStatementOutputParams createCardStatementOutputParams() {
        return new CardStatementOutputParams();
    }

    /**
     * Create an instance of {@link GetPurposes }
     * 
     */
    public GetPurposes createGetPurposes() {
        return new GetPurposes();
    }

    /**
     * Create an instance of {@link GetPurposesResponse }
     * 
     */
    public GetPurposesResponse createGetPurposesResponse() {
        return new GetPurposesResponse();
    }

    /**
     * Create an instance of {@link PurposeOutputParams }
     * 
     */
    public PurposeOutputParams createPurposeOutputParams() {
        return new PurposeOutputParams();
    }

    /**
     * Create an instance of {@link AuditLoggging }
     * 
     */
    public AuditLoggging createAuditLoggging() {
        return new AuditLoggging();
    }

    /**
     * Create an instance of {@link AuditLoggingRequest }
     * 
     */
    public AuditLoggingRequest createAuditLoggingRequest() {
        return new AuditLoggingRequest();
    }

    /**
     * Create an instance of {@link AuditLogggingResponse }
     * 
     */
    public AuditLogggingResponse createAuditLogggingResponse() {
        return new AuditLogggingResponse();
    }

    /**
     * Create an instance of {@link AuditLoggingOutputParams }
     * 
     */
    public AuditLoggingOutputParams createAuditLoggingOutputParams() {
        return new AuditLoggingOutputParams();
    }

    /**
     * Create an instance of {@link ForgotLoginID }
     * 
     */
    public ForgotLoginID createForgotLoginID() {
        return new ForgotLoginID();
    }

    /**
     * Create an instance of {@link ForgotLoginIDResponse }
     * 
     */
    public ForgotLoginIDResponse createForgotLoginIDResponse() {
        return new ForgotLoginIDResponse();
    }

    /**
     * Create an instance of {@link ForgotIdOutputParams }
     * 
     */
    public ForgotIdOutputParams createForgotIdOutputParams() {
        return new ForgotIdOutputParams();
    }

    /**
     * Create an instance of {@link GetLoginHistory }
     * 
     */
    public GetLoginHistory createGetLoginHistory() {
        return new GetLoginHistory();
    }

    /**
     * Create an instance of {@link GetLoginHistoryResponse }
     * 
     */
    public GetLoginHistoryResponse createGetLoginHistoryResponse() {
        return new GetLoginHistoryResponse();
    }

    /**
     * Create an instance of {@link LoginHistoryOutputParams }
     * 
     */
    public LoginHistoryOutputParams createLoginHistoryOutputParams() {
        return new LoginHistoryOutputParams();
    }

    /**
     * Create an instance of {@link GetTransactionHistory }
     * 
     */
    public GetTransactionHistory createGetTransactionHistory() {
        return new GetTransactionHistory();
    }

    /**
     * Create an instance of {@link GetTransactionHistoryResponse }
     * 
     */
    public GetTransactionHistoryResponse createGetTransactionHistoryResponse() {
        return new GetTransactionHistoryResponse();
    }

    /**
     * Create an instance of {@link TransactionHistoryOutputParams }
     * 
     */
    public TransactionHistoryOutputParams createTransactionHistoryOutputParams() {
        return new TransactionHistoryOutputParams();
    }

    /**
     * Create an instance of {@link FundTransferSchedule }
     * 
     */
    public FundTransferSchedule createFundTransferSchedule() {
        return new FundTransferSchedule();
    }

    /**
     * Create an instance of {@link CustomerAccount }
     * 
     */
    public CustomerAccount createCustomerAccount() {
        return new CustomerAccount();
    }

    /**
     * Create an instance of {@link PaymentDetailDTO }
     * 
     */
    public PaymentDetailDTO createPaymentDetailDTO() {
        return new PaymentDetailDTO();
    }

    /**
     * Create an instance of {@link FundTransferScheduleResponse }
     * 
     */
    public FundTransferScheduleResponse createFundTransferScheduleResponse() {
        return new FundTransferScheduleResponse();
    }

    /**
     * Create an instance of {@link FundTransferScheduleOutputParams }
     * 
     */
    public FundTransferScheduleOutputParams createFundTransferScheduleOutputParams() {
        return new FundTransferScheduleOutputParams();
    }

    /**
     * Create an instance of {@link BillPaymentSchedule }
     * 
     */
    public BillPaymentSchedule createBillPaymentSchedule() {
        return new BillPaymentSchedule();
    }

    /**
     * Create an instance of {@link BillPaymentScheduleResponse }
     * 
     */
    public BillPaymentScheduleResponse createBillPaymentScheduleResponse() {
        return new BillPaymentScheduleResponse();
    }

    /**
     * Create an instance of {@link BillPaymentScheduleOutputParams }
     * 
     */
    public BillPaymentScheduleOutputParams createBillPaymentScheduleOutputParams() {
        return new BillPaymentScheduleOutputParams();
    }

    /**
     * Create an instance of {@link ChangeEmail }
     * 
     */
    public ChangeEmail createChangeEmail() {
        return new ChangeEmail();
    }

    /**
     * Create an instance of {@link SecretQuestion }
     * 
     */
    public SecretQuestion createSecretQuestion() {
        return new SecretQuestion();
    }

    /**
     * Create an instance of {@link ChangeEmailResponse }
     * 
     */
    public ChangeEmailResponse createChangeEmailResponse() {
        return new ChangeEmailResponse();
    }

    /**
     * Create an instance of {@link ChangeEmailOutputParams }
     * 
     */
    public ChangeEmailOutputParams createChangeEmailOutputParams() {
        return new ChangeEmailOutputParams();
    }

    /**
     * Create an instance of {@link ChangeSecretAnswer }
     * 
     */
    public ChangeSecretAnswer createChangeSecretAnswer() {
        return new ChangeSecretAnswer();
    }

    /**
     * Create an instance of {@link ChangeSecretAnswerResponse }
     * 
     */
    public ChangeSecretAnswerResponse createChangeSecretAnswerResponse() {
        return new ChangeSecretAnswerResponse();
    }

    /**
     * Create an instance of {@link ChangeSecretAnswerOutputParams }
     * 
     */
    public ChangeSecretAnswerOutputParams createChangeSecretAnswerOutputParams() {
        return new ChangeSecretAnswerOutputParams();
    }

    /**
     * Create an instance of {@link ForgotSecretAnswer }
     * 
     */
    public ForgotSecretAnswer createForgotSecretAnswer() {
        return new ForgotSecretAnswer();
    }

    /**
     * Create an instance of {@link ForgotSecretAnswerResponse }
     * 
     */
    public ForgotSecretAnswerResponse createForgotSecretAnswerResponse() {
        return new ForgotSecretAnswerResponse();
    }

    /**
     * Create an instance of {@link ForgotSecretAnswerOutputParams }
     * 
     */
    public ForgotSecretAnswerOutputParams createForgotSecretAnswerOutputParams() {
        return new ForgotSecretAnswerOutputParams();
    }

    /**
     * Create an instance of {@link BlockInternetBanking }
     * 
     */
    public BlockInternetBanking createBlockInternetBanking() {
        return new BlockInternetBanking();
    }

    /**
     * Create an instance of {@link BlockInternetBankingResponse }
     * 
     */
    public BlockInternetBankingResponse createBlockInternetBankingResponse() {
        return new BlockInternetBankingResponse();
    }

    /**
     * Create an instance of {@link BlockInternetBankingOutputParams }
     * 
     */
    public BlockInternetBankingOutputParams createBlockInternetBankingOutputParams() {
        return new BlockInternetBankingOutputParams();
    }

    /**
     * Create an instance of {@link ViewScheduleFundsTransfer }
     * 
     */
    public ViewScheduleFundsTransfer createViewScheduleFundsTransfer() {
        return new ViewScheduleFundsTransfer();
    }

    /**
     * Create an instance of {@link ViewScheduleFundsTransferResponse }
     * 
     */
    public ViewScheduleFundsTransferResponse createViewScheduleFundsTransferResponse() {
        return new ViewScheduleFundsTransferResponse();
    }

    /**
     * Create an instance of {@link ViewScheduleFundsTransferOutputParams }
     * 
     */
    public ViewScheduleFundsTransferOutputParams createViewScheduleFundsTransferOutputParams() {
        return new ViewScheduleFundsTransferOutputParams();
    }

    /**
     * Create an instance of {@link ViewScheduledBillPayment }
     * 
     */
    public ViewScheduledBillPayment createViewScheduledBillPayment() {
        return new ViewScheduledBillPayment();
    }

    /**
     * Create an instance of {@link ViewScheduledBillPaymentResponse }
     * 
     */
    public ViewScheduledBillPaymentResponse createViewScheduledBillPaymentResponse() {
        return new ViewScheduledBillPaymentResponse();
    }

    /**
     * Create an instance of {@link ViewScheduledBillPaymentOutputParams }
     * 
     */
    public ViewScheduledBillPaymentOutputParams createViewScheduledBillPaymentOutputParams() {
        return new ViewScheduledBillPaymentOutputParams();
    }

    /**
     * Create an instance of {@link EditScheduleTransaction }
     * 
     */
    public EditScheduleTransaction createEditScheduleTransaction() {
        return new EditScheduleTransaction();
    }

    /**
     * Create an instance of {@link EditScheduleTransactionResponse }
     * 
     */
    public EditScheduleTransactionResponse createEditScheduleTransactionResponse() {
        return new EditScheduleTransactionResponse();
    }

    /**
     * Create an instance of {@link EditScheduleTransactionOutputParams }
     * 
     */
    public EditScheduleTransactionOutputParams createEditScheduleTransactionOutputParams() {
        return new EditScheduleTransactionOutputParams();
    }

    /**
     * Create an instance of {@link DeleteScheduleTransaction }
     * 
     */
    public DeleteScheduleTransaction createDeleteScheduleTransaction() {
        return new DeleteScheduleTransaction();
    }

    /**
     * Create an instance of {@link DeleteScheduleTransactionResponse }
     * 
     */
    public DeleteScheduleTransactionResponse createDeleteScheduleTransactionResponse() {
        return new DeleteScheduleTransactionResponse();
    }

    /**
     * Create an instance of {@link DeleteScheduleTransactionOutputParams }
     * 
     */
    public DeleteScheduleTransactionOutputParams createDeleteScheduleTransactionOutputParams() {
        return new DeleteScheduleTransactionOutputParams();
    }

    /**
     * Create an instance of {@link GenerateMobilePin }
     * 
     */
    public GenerateMobilePin createGenerateMobilePin() {
        return new GenerateMobilePin();
    }

    /**
     * Create an instance of {@link GenerateMobilePinResponse }
     * 
     */
    public GenerateMobilePinResponse createGenerateMobilePinResponse() {
        return new GenerateMobilePinResponse();
    }

    /**
     * Create an instance of {@link GenerateMobilePinOutputParams }
     * 
     */
    public GenerateMobilePinOutputParams createGenerateMobilePinOutputParams() {
        return new GenerateMobilePinOutputParams();
    }

    /**
     * Create an instance of {@link CustomerDetail }
     * 
     */
    public CustomerDetail createCustomerDetail() {
        return new CustomerDetail();
    }

    /**
     * Create an instance of {@link OutputHeader }
     * 
     */
    public OutputHeader createOutputHeader() {
        return new OutputHeader();
    }

    /**
     * Create an instance of {@link ArrayOfTransactionList }
     * 
     */
    public ArrayOfTransactionList createArrayOfTransactionList() {
        return new ArrayOfTransactionList();
    }

    /**
     * Create an instance of {@link TransactionList }
     * 
     */
    public TransactionList createTransactionList() {
        return new TransactionList();
    }

    /**
     * Create an instance of {@link ArrayOfBankList }
     * 
     */
    public ArrayOfBankList createArrayOfBankList() {
        return new ArrayOfBankList();
    }

    /**
     * Create an instance of {@link BankList }
     * 
     */
    public BankList createBankList() {
        return new BankList();
    }

    /**
     * Create an instance of {@link ArrayOfRelationship }
     * 
     */
    public ArrayOfRelationship createArrayOfRelationship() {
        return new ArrayOfRelationship();
    }

    /**
     * Create an instance of {@link Relationship }
     * 
     */
    public Relationship createRelationship() {
        return new Relationship();
    }

    /**
     * Create an instance of {@link ArrayOfBeneficiaryAccount }
     * 
     */
    public ArrayOfBeneficiaryAccount createArrayOfBeneficiaryAccount() {
        return new ArrayOfBeneficiaryAccount();
    }

    /**
     * Create an instance of {@link ArrayOfBillPayee }
     * 
     */
    public ArrayOfBillPayee createArrayOfBillPayee() {
        return new ArrayOfBillPayee();
    }

    /**
     * Create an instance of {@link ArrayOfBillingCompanyType }
     * 
     */
    public ArrayOfBillingCompanyType createArrayOfBillingCompanyType() {
        return new ArrayOfBillingCompanyType();
    }

    /**
     * Create an instance of {@link BillingCompanyType }
     * 
     */
    public BillingCompanyType createBillingCompanyType() {
        return new BillingCompanyType();
    }

    /**
     * Create an instance of {@link ArrayOfBillingCompany }
     * 
     */
    public ArrayOfBillingCompany createArrayOfBillingCompany() {
        return new ArrayOfBillingCompany();
    }

    /**
     * Create an instance of {@link BillingCompany }
     * 
     */
    public BillingCompany createBillingCompany() {
        return new BillingCompany();
    }

    /**
     * Create an instance of {@link PaymentRule }
     * 
     */
    public PaymentRule createPaymentRule() {
        return new PaymentRule();
    }

    /**
     * Create an instance of {@link ArrayOfSecretQuestion }
     * 
     */
    public ArrayOfSecretQuestion createArrayOfSecretQuestion() {
        return new ArrayOfSecretQuestion();
    }

    /**
     * Create an instance of {@link ArrayOfCardTransactionList }
     * 
     */
    public ArrayOfCardTransactionList createArrayOfCardTransactionList() {
        return new ArrayOfCardTransactionList();
    }

    /**
     * Create an instance of {@link CardTransactionList }
     * 
     */
    public CardTransactionList createCardTransactionList() {
        return new CardTransactionList();
    }

    /**
     * Create an instance of {@link ArrayOfPurpose }
     * 
     */
    public ArrayOfPurpose createArrayOfPurpose() {
        return new ArrayOfPurpose();
    }

    /**
     * Create an instance of {@link Purpose }
     * 
     */
    public Purpose createPurpose() {
        return new Purpose();
    }

    /**
     * Create an instance of {@link ArrayOfLoginHistoryTransactionList }
     * 
     */
    public ArrayOfLoginHistoryTransactionList createArrayOfLoginHistoryTransactionList() {
        return new ArrayOfLoginHistoryTransactionList();
    }

    /**
     * Create an instance of {@link LoginHistoryTransactionList }
     * 
     */
    public LoginHistoryTransactionList createLoginHistoryTransactionList() {
        return new LoginHistoryTransactionList();
    }

    /**
     * Create an instance of {@link ArrayOfTransactionHistoryList }
     * 
     */
    public ArrayOfTransactionHistoryList createArrayOfTransactionHistoryList() {
        return new ArrayOfTransactionHistoryList();
    }

    /**
     * Create an instance of {@link TransactionHistoryList }
     * 
     */
    public TransactionHistoryList createTransactionHistoryList() {
        return new TransactionHistoryList();
    }

    /**
     * Create an instance of {@link ArrayOfFTScheduleTransactionList }
     * 
     */
    public ArrayOfFTScheduleTransactionList createArrayOfFTScheduleTransactionList() {
        return new ArrayOfFTScheduleTransactionList();
    }

    /**
     * Create an instance of {@link FTScheduleTransactionList }
     * 
     */
    public FTScheduleTransactionList createFTScheduleTransactionList() {
        return new FTScheduleTransactionList();
    }

    /**
     * Create an instance of {@link ArrayOfBPScheduleTransactionList }
     * 
     */
    public ArrayOfBPScheduleTransactionList createArrayOfBPScheduleTransactionList() {
        return new ArrayOfBPScheduleTransactionList();
    }

    /**
     * Create an instance of {@link BPScheduleTransactionList }
     * 
     */
    public BPScheduleTransactionList createBPScheduleTransactionList() {
        return new BPScheduleTransactionList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InputHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "InputHeader")
    public JAXBElement<InputHeader> createInputHeader(InputHeader value) {
        return new JAXBElement<InputHeader>(_InputHeader_QNAME, InputHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerRequestOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CustomerRequestOutputParams")
    public JAXBElement<CustomerRequestOutputParams> createCustomerRequestOutputParams(CustomerRequestOutputParams value) {
        return new JAXBElement<CustomerRequestOutputParams>(_CustomerRequestOutputParams_QNAME, CustomerRequestOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerDetail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CustomerDetail")
    public JAXBElement<CustomerDetail> createCustomerDetail(CustomerDetail value) {
        return new JAXBElement<CustomerDetail>(_CustomerDetail_QNAME, CustomerDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutputHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "OutputHeader")
    public JAXBElement<OutputHeader> createOutputHeader(OutputHeader value) {
        return new JAXBElement<OutputHeader>(_OutputHeader_QNAME, OutputHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountStatementFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AccountStatementFilter")
    public JAXBElement<AccountStatementFilter> createAccountStatementFilter(AccountStatementFilter value) {
        return new JAXBElement<AccountStatementFilter>(_AccountStatementFilter_QNAME, AccountStatementFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountStatementOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AccountStatementOutputParams")
    public JAXBElement<AccountStatementOutputParams> createAccountStatementOutputParams(AccountStatementOutputParams value) {
        return new JAXBElement<AccountStatementOutputParams>(_AccountStatementOutputParams_QNAME, AccountStatementOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfTransactionList")
    public JAXBElement<ArrayOfTransactionList> createArrayOfTransactionList(ArrayOfTransactionList value) {
        return new JAXBElement<ArrayOfTransactionList>(_ArrayOfTransactionList_QNAME, ArrayOfTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "TransactionList")
    public JAXBElement<TransactionList> createTransactionList(TransactionList value) {
        return new JAXBElement<TransactionList>(_TransactionList_QNAME, TransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankDetailsOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BankDetailsOutputParams")
    public JAXBElement<BankDetailsOutputParams> createBankDetailsOutputParams(BankDetailsOutputParams value) {
        return new JAXBElement<BankDetailsOutputParams>(_BankDetailsOutputParams_QNAME, BankDetailsOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBankList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBankList")
    public JAXBElement<ArrayOfBankList> createArrayOfBankList(ArrayOfBankList value) {
        return new JAXBElement<ArrayOfBankList>(_ArrayOfBankList_QNAME, ArrayOfBankList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BankList")
    public JAXBElement<BankList> createBankList(BankList value) {
        return new JAXBElement<BankList>(_BankList_QNAME, BankList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeneficiaryRelationshipOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BeneficiaryRelationshipOutputParams")
    public JAXBElement<BeneficiaryRelationshipOutputParams> createBeneficiaryRelationshipOutputParams(BeneficiaryRelationshipOutputParams value) {
        return new JAXBElement<BeneficiaryRelationshipOutputParams>(_BeneficiaryRelationshipOutputParams_QNAME, BeneficiaryRelationshipOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelationship }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfRelationship")
    public JAXBElement<ArrayOfRelationship> createArrayOfRelationship(ArrayOfRelationship value) {
        return new JAXBElement<ArrayOfRelationship>(_ArrayOfRelationship_QNAME, ArrayOfRelationship.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Relationship }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "Relationship")
    public JAXBElement<Relationship> createRelationship(Relationship value) {
        return new JAXBElement<Relationship>(_Relationship_QNAME, Relationship.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeneficiaryAccountOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BeneficiaryAccountOutputParams")
    public JAXBElement<BeneficiaryAccountOutputParams> createBeneficiaryAccountOutputParams(BeneficiaryAccountOutputParams value) {
        return new JAXBElement<BeneficiaryAccountOutputParams>(_BeneficiaryAccountOutputParams_QNAME, BeneficiaryAccountOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBeneficiaryAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBeneficiaryAccount")
    public JAXBElement<ArrayOfBeneficiaryAccount> createArrayOfBeneficiaryAccount(ArrayOfBeneficiaryAccount value) {
        return new JAXBElement<ArrayOfBeneficiaryAccount>(_ArrayOfBeneficiaryAccount_QNAME, ArrayOfBeneficiaryAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeneficiaryAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BeneficiaryAccount")
    public JAXBElement<BeneficiaryAccount> createBeneficiaryAccount(BeneficiaryAccount value) {
        return new JAXBElement<BeneficiaryAccount>(_BeneficiaryAccount_QNAME, BeneficiaryAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OTP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "OTP")
    public JAXBElement<OTP> createOTP(OTP value) {
        return new JAXBElement<OTP>(_OTP_QNAME, OTP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBeneficiaryOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AddBeneficiaryOutputParams")
    public JAXBElement<AddBeneficiaryOutputParams> createAddBeneficiaryOutputParams(AddBeneficiaryOutputParams value) {
        return new JAXBElement<AddBeneficiaryOutputParams>(_AddBeneficiaryOutputParams_QNAME, AddBeneficiaryOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModifyBeneficiaryAccountOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ModifyBeneficiaryAccountOutputParams")
    public JAXBElement<ModifyBeneficiaryAccountOutputParams> createModifyBeneficiaryAccountOutputParams(ModifyBeneficiaryAccountOutputParams value) {
        return new JAXBElement<ModifyBeneficiaryAccountOutputParams>(_ModifyBeneficiaryAccountOutputParams_QNAME, ModifyBeneficiaryAccountOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteBeneficiaryAccountOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "DeleteBeneficiaryAccountOutputParams")
    public JAXBElement<DeleteBeneficiaryAccountOutputParams> createDeleteBeneficiaryAccountOutputParams(DeleteBeneficiaryAccountOutputParams value) {
        return new JAXBElement<DeleteBeneficiaryAccountOutputParams>(_DeleteBeneficiaryAccountOutputParams_QNAME, DeleteBeneficiaryAccountOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillPayeeOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillPayeeOutputParams")
    public JAXBElement<BillPayeeOutputParams> createBillPayeeOutputParams(BillPayeeOutputParams value) {
        return new JAXBElement<BillPayeeOutputParams>(_BillPayeeOutputParams_QNAME, BillPayeeOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBillPayee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBillPayee")
    public JAXBElement<ArrayOfBillPayee> createArrayOfBillPayee(ArrayOfBillPayee value) {
        return new JAXBElement<ArrayOfBillPayee>(_ArrayOfBillPayee_QNAME, ArrayOfBillPayee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillPayee }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillPayee")
    public JAXBElement<BillPayee> createBillPayee(BillPayee value) {
        return new JAXBElement<BillPayee>(_BillPayee_QNAME, BillPayee.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBillPayeeOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AddBillPayeeOutputParams")
    public JAXBElement<AddBillPayeeOutputParams> createAddBillPayeeOutputParams(AddBillPayeeOutputParams value) {
        return new JAXBElement<AddBillPayeeOutputParams>(_AddBillPayeeOutputParams_QNAME, AddBillPayeeOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteBillPayeeOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "DeleteBillPayeeOutputParams")
    public JAXBElement<DeleteBillPayeeOutputParams> createDeleteBillPayeeOutputParams(DeleteBillPayeeOutputParams value) {
        return new JAXBElement<DeleteBillPayeeOutputParams>(_DeleteBillPayeeOutputParams_QNAME, DeleteBillPayeeOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingCompanyTypeOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillingCompanyTypeOutputParams")
    public JAXBElement<BillingCompanyTypeOutputParams> createBillingCompanyTypeOutputParams(BillingCompanyTypeOutputParams value) {
        return new JAXBElement<BillingCompanyTypeOutputParams>(_BillingCompanyTypeOutputParams_QNAME, BillingCompanyTypeOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBillingCompanyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBillingCompanyType")
    public JAXBElement<ArrayOfBillingCompanyType> createArrayOfBillingCompanyType(ArrayOfBillingCompanyType value) {
        return new JAXBElement<ArrayOfBillingCompanyType>(_ArrayOfBillingCompanyType_QNAME, ArrayOfBillingCompanyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingCompanyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillingCompanyType")
    public JAXBElement<BillingCompanyType> createBillingCompanyType(BillingCompanyType value) {
        return new JAXBElement<BillingCompanyType>(_BillingCompanyType_QNAME, BillingCompanyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingCompaniesOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillingCompaniesOutputParams")
    public JAXBElement<BillingCompaniesOutputParams> createBillingCompaniesOutputParams(BillingCompaniesOutputParams value) {
        return new JAXBElement<BillingCompaniesOutputParams>(_BillingCompaniesOutputParams_QNAME, BillingCompaniesOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBillingCompany }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBillingCompany")
    public JAXBElement<ArrayOfBillingCompany> createArrayOfBillingCompany(ArrayOfBillingCompany value) {
        return new JAXBElement<ArrayOfBillingCompany>(_ArrayOfBillingCompany_QNAME, ArrayOfBillingCompany.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillingCompany }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillingCompany")
    public JAXBElement<BillingCompany> createBillingCompany(BillingCompany value) {
        return new JAXBElement<BillingCompany>(_BillingCompany_QNAME, BillingCompany.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentRule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "PaymentRule")
    public JAXBElement<PaymentRule> createPaymentRule(PaymentRule value) {
        return new JAXBElement<PaymentRule>(_PaymentRule_QNAME, PaymentRule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateFinancialPinOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "GenerateFinancialPinOutputParams")
    public JAXBElement<GenerateFinancialPinOutputParams> createGenerateFinancialPinOutputParams(GenerateFinancialPinOutputParams value) {
        return new JAXBElement<GenerateFinancialPinOutputParams>(_GenerateFinancialPinOutputParams_QNAME, GenerateFinancialPinOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyFinancialPinOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "VerifyFinancialPinOutputParams")
    public JAXBElement<VerifyFinancialPinOutputParams> createVerifyFinancialPinOutputParams(VerifyFinancialPinOutputParams value) {
        return new JAXBElement<VerifyFinancialPinOutputParams>(_VerifyFinancialPinOutputParams_QNAME, VerifyFinancialPinOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpireFinancialPinOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ExpireFinancialPinOutputParams")
    public JAXBElement<ExpireFinancialPinOutputParams> createExpireFinancialPinOutputParams(ExpireFinancialPinOutputParams value) {
        return new JAXBElement<ExpireFinancialPinOutputParams>(_ExpireFinancialPinOutputParams_QNAME, ExpireFinancialPinOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateOTPOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "GenerateOTPOutputParams")
    public JAXBElement<GenerateOTPOutputParams> createGenerateOTPOutputParams(GenerateOTPOutputParams value) {
        return new JAXBElement<GenerateOTPOutputParams>(_GenerateOTPOutputParams_QNAME, GenerateOTPOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Registration2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "Registration")
    public JAXBElement<Registration2> createRegistration(Registration2 value) {
        return new JAXBElement<Registration2>(_Registration_QNAME, Registration2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretQuestion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "SecretQuestion")
    public JAXBElement<SecretQuestion> createSecretQuestion(SecretQuestion value) {
        return new JAXBElement<SecretQuestion>(_SecretQuestion_QNAME, SecretQuestion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistrationOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "RegistrationOutputParams")
    public JAXBElement<RegistrationOutputParams> createRegistrationOutputParams(RegistrationOutputParams value) {
        return new JAXBElement<RegistrationOutputParams>(_RegistrationOutputParams_QNAME, RegistrationOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretQuestionOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "SecretQuestionOutputParams")
    public JAXBElement<SecretQuestionOutputParams> createSecretQuestionOutputParams(SecretQuestionOutputParams value) {
        return new JAXBElement<SecretQuestionOutputParams>(_SecretQuestionOutputParams_QNAME, SecretQuestionOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSecretQuestion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfSecretQuestion")
    public JAXBElement<ArrayOfSecretQuestion> createArrayOfSecretQuestion(ArrayOfSecretQuestion value) {
        return new JAXBElement<ArrayOfSecretQuestion>(_ArrayOfSecretQuestion_QNAME, ArrayOfSecretQuestion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetFirstTimeLoginOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "SetFirstTimeLoginOutputParams")
    public JAXBElement<SetFirstTimeLoginOutputParams> createSetFirstTimeLoginOutputParams(SetFirstTimeLoginOutputParams value) {
        return new JAXBElement<SetFirstTimeLoginOutputParams>(_SetFirstTimeLoginOutputParams_QNAME, SetFirstTimeLoginOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardStatementFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CardStatementFilter")
    public JAXBElement<CardStatementFilter> createCardStatementFilter(CardStatementFilter value) {
        return new JAXBElement<CardStatementFilter>(_CardStatementFilter_QNAME, CardStatementFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardStatementOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CardStatementOutputParams")
    public JAXBElement<CardStatementOutputParams> createCardStatementOutputParams(CardStatementOutputParams value) {
        return new JAXBElement<CardStatementOutputParams>(_CardStatementOutputParams_QNAME, CardStatementOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCardTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfCardTransactionList")
    public JAXBElement<ArrayOfCardTransactionList> createArrayOfCardTransactionList(ArrayOfCardTransactionList value) {
        return new JAXBElement<ArrayOfCardTransactionList>(_ArrayOfCardTransactionList_QNAME, ArrayOfCardTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CardTransactionList")
    public JAXBElement<CardTransactionList> createCardTransactionList(CardTransactionList value) {
        return new JAXBElement<CardTransactionList>(_CardTransactionList_QNAME, CardTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PurposeOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "PurposeOutputParams")
    public JAXBElement<PurposeOutputParams> createPurposeOutputParams(PurposeOutputParams value) {
        return new JAXBElement<PurposeOutputParams>(_PurposeOutputParams_QNAME, PurposeOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPurpose }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfPurpose")
    public JAXBElement<ArrayOfPurpose> createArrayOfPurpose(ArrayOfPurpose value) {
        return new JAXBElement<ArrayOfPurpose>(_ArrayOfPurpose_QNAME, ArrayOfPurpose.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Purpose }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "Purpose")
    public JAXBElement<Purpose> createPurpose(Purpose value) {
        return new JAXBElement<Purpose>(_Purpose_QNAME, Purpose.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuditLoggingRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AuditLoggingRequest")
    public JAXBElement<AuditLoggingRequest> createAuditLoggingRequest(AuditLoggingRequest value) {
        return new JAXBElement<AuditLoggingRequest>(_AuditLoggingRequest_QNAME, AuditLoggingRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuditLoggingOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "AuditLoggingOutputParams")
    public JAXBElement<AuditLoggingOutputParams> createAuditLoggingOutputParams(AuditLoggingOutputParams value) {
        return new JAXBElement<AuditLoggingOutputParams>(_AuditLoggingOutputParams_QNAME, AuditLoggingOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForgotIdOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ForgotIdOutputParams")
    public JAXBElement<ForgotIdOutputParams> createForgotIdOutputParams(ForgotIdOutputParams value) {
        return new JAXBElement<ForgotIdOutputParams>(_ForgotIdOutputParams_QNAME, ForgotIdOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginHistoryOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "LoginHistoryOutputParams")
    public JAXBElement<LoginHistoryOutputParams> createLoginHistoryOutputParams(LoginHistoryOutputParams value) {
        return new JAXBElement<LoginHistoryOutputParams>(_LoginHistoryOutputParams_QNAME, LoginHistoryOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLoginHistoryTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfLoginHistoryTransactionList")
    public JAXBElement<ArrayOfLoginHistoryTransactionList> createArrayOfLoginHistoryTransactionList(ArrayOfLoginHistoryTransactionList value) {
        return new JAXBElement<ArrayOfLoginHistoryTransactionList>(_ArrayOfLoginHistoryTransactionList_QNAME, ArrayOfLoginHistoryTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginHistoryTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "LoginHistoryTransactionList")
    public JAXBElement<LoginHistoryTransactionList> createLoginHistoryTransactionList(LoginHistoryTransactionList value) {
        return new JAXBElement<LoginHistoryTransactionList>(_LoginHistoryTransactionList_QNAME, LoginHistoryTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionHistoryOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "TransactionHistoryOutputParams")
    public JAXBElement<TransactionHistoryOutputParams> createTransactionHistoryOutputParams(TransactionHistoryOutputParams value) {
        return new JAXBElement<TransactionHistoryOutputParams>(_TransactionHistoryOutputParams_QNAME, TransactionHistoryOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTransactionHistoryList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfTransactionHistoryList")
    public JAXBElement<ArrayOfTransactionHistoryList> createArrayOfTransactionHistoryList(ArrayOfTransactionHistoryList value) {
        return new JAXBElement<ArrayOfTransactionHistoryList>(_ArrayOfTransactionHistoryList_QNAME, ArrayOfTransactionHistoryList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransactionHistoryList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "TransactionHistoryList")
    public JAXBElement<TransactionHistoryList> createTransactionHistoryList(TransactionHistoryList value) {
        return new JAXBElement<TransactionHistoryList>(_TransactionHistoryList_QNAME, TransactionHistoryList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "CustomerAccount")
    public JAXBElement<CustomerAccount> createCustomerAccount(CustomerAccount value) {
        return new JAXBElement<CustomerAccount>(_CustomerAccount_QNAME, CustomerAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentDetailDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "PaymentDetailDTO")
    public JAXBElement<PaymentDetailDTO> createPaymentDetailDTO(PaymentDetailDTO value) {
        return new JAXBElement<PaymentDetailDTO>(_PaymentDetailDTO_QNAME, PaymentDetailDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FundTransferScheduleOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "FundTransferScheduleOutputParams")
    public JAXBElement<FundTransferScheduleOutputParams> createFundTransferScheduleOutputParams(FundTransferScheduleOutputParams value) {
        return new JAXBElement<FundTransferScheduleOutputParams>(_FundTransferScheduleOutputParams_QNAME, FundTransferScheduleOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BillPaymentScheduleOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BillPaymentScheduleOutputParams")
    public JAXBElement<BillPaymentScheduleOutputParams> createBillPaymentScheduleOutputParams(BillPaymentScheduleOutputParams value) {
        return new JAXBElement<BillPaymentScheduleOutputParams>(_BillPaymentScheduleOutputParams_QNAME, BillPaymentScheduleOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeEmailOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ChangeEmailOutputParams")
    public JAXBElement<ChangeEmailOutputParams> createChangeEmailOutputParams(ChangeEmailOutputParams value) {
        return new JAXBElement<ChangeEmailOutputParams>(_ChangeEmailOutputParams_QNAME, ChangeEmailOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeSecretAnswerOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ChangeSecretAnswerOutputParams")
    public JAXBElement<ChangeSecretAnswerOutputParams> createChangeSecretAnswerOutputParams(ChangeSecretAnswerOutputParams value) {
        return new JAXBElement<ChangeSecretAnswerOutputParams>(_ChangeSecretAnswerOutputParams_QNAME, ChangeSecretAnswerOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForgotSecretAnswerOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ForgotSecretAnswerOutputParams")
    public JAXBElement<ForgotSecretAnswerOutputParams> createForgotSecretAnswerOutputParams(ForgotSecretAnswerOutputParams value) {
        return new JAXBElement<ForgotSecretAnswerOutputParams>(_ForgotSecretAnswerOutputParams_QNAME, ForgotSecretAnswerOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BlockInternetBankingOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BlockInternetBankingOutputParams")
    public JAXBElement<BlockInternetBankingOutputParams> createBlockInternetBankingOutputParams(BlockInternetBankingOutputParams value) {
        return new JAXBElement<BlockInternetBankingOutputParams>(_BlockInternetBankingOutputParams_QNAME, BlockInternetBankingOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewScheduleFundsTransferOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ViewScheduleFundsTransferOutputParams")
    public JAXBElement<ViewScheduleFundsTransferOutputParams> createViewScheduleFundsTransferOutputParams(ViewScheduleFundsTransferOutputParams value) {
        return new JAXBElement<ViewScheduleFundsTransferOutputParams>(_ViewScheduleFundsTransferOutputParams_QNAME, ViewScheduleFundsTransferOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfFTScheduleTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfFTScheduleTransactionList")
    public JAXBElement<ArrayOfFTScheduleTransactionList> createArrayOfFTScheduleTransactionList(ArrayOfFTScheduleTransactionList value) {
        return new JAXBElement<ArrayOfFTScheduleTransactionList>(_ArrayOfFTScheduleTransactionList_QNAME, ArrayOfFTScheduleTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FTScheduleTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "FTScheduleTransactionList")
    public JAXBElement<FTScheduleTransactionList> createFTScheduleTransactionList(FTScheduleTransactionList value) {
        return new JAXBElement<FTScheduleTransactionList>(_FTScheduleTransactionList_QNAME, FTScheduleTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewScheduledBillPaymentOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ViewScheduledBillPaymentOutputParams")
    public JAXBElement<ViewScheduledBillPaymentOutputParams> createViewScheduledBillPaymentOutputParams(ViewScheduledBillPaymentOutputParams value) {
        return new JAXBElement<ViewScheduledBillPaymentOutputParams>(_ViewScheduledBillPaymentOutputParams_QNAME, ViewScheduledBillPaymentOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfBPScheduleTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "ArrayOfBPScheduleTransactionList")
    public JAXBElement<ArrayOfBPScheduleTransactionList> createArrayOfBPScheduleTransactionList(ArrayOfBPScheduleTransactionList value) {
        return new JAXBElement<ArrayOfBPScheduleTransactionList>(_ArrayOfBPScheduleTransactionList_QNAME, ArrayOfBPScheduleTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BPScheduleTransactionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "BPScheduleTransactionList")
    public JAXBElement<BPScheduleTransactionList> createBPScheduleTransactionList(BPScheduleTransactionList value) {
        return new JAXBElement<BPScheduleTransactionList>(_BPScheduleTransactionList_QNAME, BPScheduleTransactionList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EditScheduleTransactionOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "EditScheduleTransactionOutputParams")
    public JAXBElement<EditScheduleTransactionOutputParams> createEditScheduleTransactionOutputParams(EditScheduleTransactionOutputParams value) {
        return new JAXBElement<EditScheduleTransactionOutputParams>(_EditScheduleTransactionOutputParams_QNAME, EditScheduleTransactionOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteScheduleTransactionOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "DeleteScheduleTransactionOutputParams")
    public JAXBElement<DeleteScheduleTransactionOutputParams> createDeleteScheduleTransactionOutputParams(DeleteScheduleTransactionOutputParams value) {
        return new JAXBElement<DeleteScheduleTransactionOutputParams>(_DeleteScheduleTransactionOutputParams_QNAME, DeleteScheduleTransactionOutputParams.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateMobilePinOutputParams }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/SBLServiceModels", name = "GenerateMobilePinOutputParams")
    public JAXBElement<GenerateMobilePinOutputParams> createGenerateMobilePinOutputParams(GenerateMobilePinOutputParams value) {
        return new JAXBElement<GenerateMobilePinOutputParams>(_GenerateMobilePinOutputParams_QNAME, GenerateMobilePinOutputParams.class, null, value);
    }

}
