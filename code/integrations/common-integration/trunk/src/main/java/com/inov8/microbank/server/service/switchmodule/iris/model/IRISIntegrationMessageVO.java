/**
 * 
 */
package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.util.List;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;


/**
 * Project Name: Financial-Integration
 * @author Imran Sarwar Creation Date: Nov 5, 2007 Creation Time: 1:25:15 PM Description:
 */
public class IRISIntegrationMessageVO implements IntegrationMessageVO
{

	protected static String STAN_PADDING_CHAR = "0";
	protected static int STAN_LENGTH = 6;
	protected static boolean STAN_LEFT_PAD = true;
	
	protected static String RRN_PADDING_CHAR = "0";
	
	private long timeOutInterval;
	public String getMiniStatementDataLength()
	{
		return miniStatementDataLength;
	}

	public void setMiniStatementDataLength(String miniStatementDataLength)
	{
		this.miniStatementDataLength = miniStatementDataLength;
	}

	public String getMiniStatementData()
	{
		return miniStatementData;
	}

	public void setMiniStatementData(String miniStatementData)
	{
		this.miniStatementData = miniStatementData;
	}

	public long getTimeOutInterval()
	{
		return timeOutInterval;
	}

	public void setTimeOutInterval(long timeOut)
	{
		this.timeOutInterval = timeOut;
	}

	protected static int RRN_LENGTH = 12;
	protected static boolean RRN_LEFT_PAD = true;
	
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -3857122067283739201L;

	private boolean					isNetworkMessage;

	private String					paymentGatewayCode;
	private String					microbankTransactionCode;
	private String					protocol;
	private String					version;
	private String					transmissionDateAndTime;
	private String					reservedHeaderField = "          ";
	private String					messageType;
	private String					channelUserIdentification;
	private String					transactionCode;
	private String					transactionLocalDate;
	private String					transactionLocalTime;
	private String					channelType;
	private String					sourceChannelType;
	private String					destinationChannelType;
	private String					deliveryChannelId;
	private String					deliveryChannelNameAndLocation;
	private String					systemTraceAuditNumber;
	private String					retrievalReferenceNumber;
	private String					secureDataType;
	private String					secureVerificationData;
	private String					authorizationResponseId;
	private String					responseCode;
	private String					reservedField;

	private String					customerId;
	private String					noOfAccounts;
	private String					utilityCompanyId;
	private String					consumerNumber;
	private String					amountPaid;
	private String					transactionCurrency;
	private String					transactionFee;

	private CustomerAccount			fromCustomerAccount;
	private CustomerAccount			toCustomerAccount;
	private String					transactionDescription;
	
	private String				destinationAccountNumber;
	private String				destinationAccountType;
	private String				destinationAccountCurrency;
	private String				destinationAccountBankIMD;
	
	private String 				miniStatementDataLength;
	private String 				miniStatementData;
	
	private String					messageAsEdi;

	private List<CustomerAccount>	accounts;

	public IRISIntegrationMessageVO(boolean isNetworkMessage, String paymentGatewayCode, String microbankTransactionCode)
	{
		super();
		this.isNetworkMessage = isNetworkMessage;
		this.paymentGatewayCode = paymentGatewayCode;
		this.microbankTransactionCode = microbankTransactionCode;
	}

	/**
	 * @return the isNetworkMessage
	 */
	public boolean isNetworkMessage()
	{
		return isNetworkMessage;
	}

	/**
	 * @return the authorizationResponseId
	 */
	public String getAuthorizationResponseId()
	{
		return authorizationResponseId;
	}

	/**
	 * @param authorizationResponseId the authorizationResponseId to set
	 */
	public void setAuthorizationResponseId(String authorizationResponseId)
	{
		this.authorizationResponseId = authorizationResponseId;
	}

	/**
	 * @return the channelUserIdentification
	 */
	public String getChannelUserIdentification()
	{
		return channelUserIdentification;
	}

	/**
	 * @param channelUserIdentification the channelUserIdentification to set
	 */
	public void setChannelUserIdentification(String channelUserIdentification)
	{
		this.channelUserIdentification = channelUserIdentification;
	}

	/**
	 * @return the deliveryChannelId
	 */
	public String getDeliveryChannelId()
	{
		return deliveryChannelId;
	}

	/**
	 * @param deliveryChannelId the deliveryChannelId to set
	 */
	public void setDeliveryChannelId(String deliveryChannelId)
	{
		this.deliveryChannelId = deliveryChannelId;
	}

	/**
	 * @return the deliveryChannelNameAndLocation
	 */
	public String getDeliveryChannelNameAndLocation()
	{
		return deliveryChannelNameAndLocation;
	}

	/**
	 * @param deliveryChannelNameAndLocation the deliveryChannelNameAndLocation to set
	 */
	public void setDeliveryChannelNameAndLocation(String deliveryChannelNameAndLocation)
	{
		this.deliveryChannelNameAndLocation = deliveryChannelNameAndLocation;
	}

	/**
	 * @return the destinationChannelType
	 */
	public String getDestinationChannelType()
	{
		return destinationChannelType;
	}

	/**
	 * @param destinationChannelType the destinationChannelType to set
	 */
	public void setDestinationChannelType(String destinationChannelType)
	{
		this.destinationChannelType = destinationChannelType;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType()
	{
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType)
	{
		this.messageType = messageType;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	/**
	 * @return the reservedField
	 */
	public String getReservedField()
	{
		return reservedField;
	}

	/**
	 * @param reservedField the reservedField to set
	 */
	public void setReservedField(String reservedField)
	{
		this.reservedField = reservedField;
	}

	/**
	 * @return the reservedHeaderField
	 */
	public String getReservedHeaderField()
	{
		return reservedHeaderField;
	}

	/**
	 * @param reservedHeaderField the reservedHeaderField to set
	 */
	public void setReservedHeaderField(String reservedHeaderField)
	{
		this.reservedHeaderField = reservedHeaderField;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode()
	{
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode)
	{
		this.responseCode = responseCode;
	}

	/**
	 * @return the retrievalReferenceNumber
	 */
	public String getRetrievalReferenceNumber()
	{
		return addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
	}

	/**
	 * @param retrievalReferenceNumber the retrievalReferenceNumber to set
	 */
	public void setRetrievalReferenceNumber(String retrievalReferenceNumber)
	{
		this.retrievalReferenceNumber = addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
	}

	/**
	 * @return the secureDataType
	 */
	public String getSecureDataType()
	{
		return secureDataType;
	}

	/**
	 * @param secureDataType the secureDataType to set
	 */
	public void setSecureDataType(String secureDataType)
	{
		this.secureDataType = secureDataType;
	}

	/**
	 * @return the secureVerificationData
	 */
	public String getSecureVerificationData()
	{
		return secureVerificationData;
	}

	/**
	 * @param secureVerificationData the secureVerificationData to set
	 */
	public void setSecureVerificationData(String secureVerificationData)
	{
		this.secureVerificationData = secureVerificationData;
	}

	/**
	 * @return the sourceChannelType
	 */
	public String getSourceChannelType()
	{
		return sourceChannelType;
	}

	/**
	 * @param sourceChannelType the sourceChannelType to set
	 */
	public void setSourceChannelType(String sourceChannelType)
	{
		this.sourceChannelType = sourceChannelType;
	}

	/**
	 * @return the systemTraceAuditNumber
	 */
	public String getSystemTraceAuditNumber()
	{
		return addPadding(STAN_PADDING_CHAR, STAN_LEFT_PAD, STAN_LENGTH, systemTraceAuditNumber);
	}

	/**
	 * @param systemTraceAuditNumber the systemTraceAuditNumber to set
	 */
	public void setSystemTraceAuditNumber(String systemTraceAuditNumber)
	{
		this.systemTraceAuditNumber = addPadding(STAN_PADDING_CHAR, STAN_LEFT_PAD, STAN_LENGTH, systemTraceAuditNumber);
	}

	/**
	 * @return the transactionCode
	 */
	public String getTransactionCode()
	{
		return transactionCode;
	}

	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode)
	{
		this.transactionCode = transactionCode;
	}

	/**
	 * @return the transactionLocalDate
	 */
	public String getTransactionLocalDate()
	{
		return transactionLocalDate;
	}

	/**
	 * @param transactionLocalDate the transactionLocalDate to set
	 */
	public void setTransactionLocalDate(String transactionLocalDate)
	{
		this.transactionLocalDate = transactionLocalDate;
	}

	/**
	 * @return the transactionLocalTime
	 */
	public String getTransactionLocalTime()
	{
		return transactionLocalTime;
	}

	/**
	 * @param transactionLocalTime the transactionLocalTime to set
	 */
	public void setTransactionLocalTime(String transactionLocalTime)
	{
		this.transactionLocalTime = transactionLocalTime;
	}

	/**
	 * @return the transmissionDateAndTime
	 */
	public String getTransmissionDateAndTime()
	{
		return transmissionDateAndTime;
	}

	/**
	 * @param transmissionDateAndTime the transmissionDateAndTime to set
	 */
	public void setTransmissionDateAndTime(String transmissionDateAndTime)
	{
		this.transmissionDateAndTime = transmissionDateAndTime;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	public void addAccount(CustomerAccount customerAccount)
	{
		this.accounts.add(customerAccount);
	}

	/**
	 * @return the amountPaid
	 */
	public String getAmountPaid()
	{
		return amountPaid;
	}

	/**
	 * @param amountPaid the amountPaid to set
	 */
	public void setAmountPaid(String amountPaid)
	{
		this.amountPaid = amountPaid;
	}

	/**
	 * @return the consumerNumber
	 */
	public String getConsumerNumber()
	{
		return consumerNumber;
	}

	/**
	 * @param consumerNumber the consumerNumber to set
	 */
	public void setConsumerNumber(String consumerNumber)
	{
		this.consumerNumber = consumerNumber;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * @return the fromCustomerAccount
	 */
	public CustomerAccount getFromCustomerAccount()
	{
		return fromCustomerAccount;
	}

	/**
	 * @param fromCustomerAccount the fromCustomerAccount to set
	 */
	public void setFromCustomerAccount(CustomerAccount fromCustomerAccount)
	{
		this.fromCustomerAccount = fromCustomerAccount;
	}

	/**
	 * @return the noOfAccounts
	 */
	public String getNoOfAccounts()
	{
		return noOfAccounts;
	}

	/**
	 * @param noOfAccounts the noOfAccounts to set
	 */
	public void setNoOfAccounts(String noOfAccounts)
	{
		this.noOfAccounts = noOfAccounts;
	}

	/**
	 * @return the transactionDescription
	 */
	public String getTransactionDescription()
	{
		return transactionDescription;
	}

	/**
	 * @param transactionDescription the transactionDescription to set
	 */
	public void setTransactionDescription(String transactionDescription)
	{
		this.transactionDescription = transactionDescription;
	}

	/**
	 * @return the transactionFee
	 */
	public String getTransactionFee()
	{
		return transactionFee;
	}

	/**
	 * @param transactionFee the transactionFee to set
	 */
	public void setTransactionFee(String transactionFee)
	{
		this.transactionFee = transactionFee;
	}

	/**
	 * @return the utilityCompanyId
	 */
	public String getUtilityCompanyId()
	{
		return utilityCompanyId;
	}

	/**
	 * @param utilityCompanyId the utilityCompanyId to set
	 */
	public void setUtilityCompanyId(String utilityCompanyId)
	{
		this.utilityCompanyId = utilityCompanyId;
	}

	/**
	 * @return the paymentGatewayCode
	 */
	public String getPaymentGatewayCode()
	{
		return paymentGatewayCode;
	}
	
	/**
	 * @param paymentGatewayCode the paymentGatewayCode to set
	 */
	public void setPaymentGatewayCode(String paymentGatewayCode)
	{
		this.paymentGatewayCode = paymentGatewayCode;
	}

	/**
	 * @return the microbankTransactionCode
	 */
	public String getMicrobankTransactionCode()
	{
		return microbankTransactionCode;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setCustomerAccounts(List<CustomerAccount> accounts)
	{
		this.accounts = accounts;
	}

	/**
	 * @return the accounts
	 */
	public List<CustomerAccount> getCustomerAccounts()
	{
		return accounts;
	}

	/**
	 * @return the messageAsEdi
	 */
	public String getMessageAsEdi()
	{
		return messageAsEdi;
	}

	/**
	 * @param messageAsEdi the messageAsEdi to set
	 */
	public void setMessageAsEdi(String messageAsEdi)
	{
		this.messageAsEdi = messageAsEdi;
	}

	public void setNetworkMessage(boolean isNetworkMessage)
	{
		this.isNetworkMessage = isNetworkMessage;
	}

	public static String addPadding(String padCharacter, boolean leftPad, int requiredLength, String value)
	{
		String input = defaultString(value).trim();
		if(leftPad)
			return leftPad(input, requiredLength , padCharacter);
		else
			return rightPad(input, requiredLength , padCharacter);
	}

	 public static String rightPad(String str, int size, String padStr) {
	        if (str == null) {
	            return null;
	        }
	        if (null==padStr||"".equals(str)) {
	            padStr = " ";
	        }
	        int padLen = padStr.length();
	        int strLen = str.length();
	        int pads = size - strLen;
	        if (pads <= 0) {
	            return str; // returns original String when possible
	        }
	        if (padLen == 1 && pads <= 8192) {
	            return rightPad(str, size, padStr.charAt(0));
	        }

	        if (pads == padLen) {
	            return str.concat(padStr);
	        } else if (pads < padLen) {
	            return str.concat(padStr.substring(0, pads));
	        } else {
	            char[] padding = new char[pads];
	            char[] padChars = padStr.toCharArray();
	            for (int i = 0; i < pads; i++) {
	                padding[i] = padChars[i % padLen];
	            }
	            return str.concat(new String(padding));
	        }
	    }
	 public static String rightPad(String str, int size, char padChar) {
	        if (str == null) {
	            return null;
	        }
	        int pads = size - str.length();
	        if (pads <= 0) {
	            return str; // returns original String when possible
	        }
	        if (pads > 8192) {
	            return rightPad(str, size, String.valueOf(padChar));
	        }
	        return str.concat(padding(pads, padChar));
	    }
	 
	    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
	        if (repeat < 0) {
	            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
	        }
	        final char[] buf = new char[repeat];
	        for (int i = 0; i < buf.length; i++) {
	            buf[i] = padChar;
	        }
	        return new String(buf);
	    }	 
	    
	    public static String leftPad(String str, int size, char padChar) {
	        if (str == null) {
	            return null;
	        }
	        int pads = size - str.length();
	        if (pads <= 0) {
	            return str; // returns original String when possible
	        }
	        if (pads > 8192) {
	            return leftPad(str, size, String.valueOf(padChar));
	        }
	        return padding(pads, padChar).concat(str);
	    }
	    
	    public static String leftPad(String str, int size, String padStr) {
	        if (str == null) {
	            return null;
	        }
	        if (null==padStr||"".equals(str)) {
	            padStr = " ";
	        }
	        int padLen = padStr.length();
	        int strLen = str.length();
	        int pads = size - strLen;
	        if (pads <= 0) {
	            return str; // returns original String when possible
	        }
	        if (padLen == 1 && pads <= 8192) {
	            return leftPad(str, size, padStr.charAt(0));
	        }

	        if (pads == padLen) {
	            return padStr.concat(str);
	        } else if (pads < padLen) {
	            return padStr.substring(0, pads).concat(str);
	        } else {
	            char[] padding = new char[pads];
	            char[] padChars = padStr.toCharArray();
	            for (int i = 0; i < pads; i++) {
	                padding[i] = padChars[i % padLen];
	            }
	            return new String(padding).concat(str);
	        }
	    }
	    
	    public static String defaultString(String str) {
	        return str == null ? "" : str;
	    }
	    
	    
	    public static void main(String[] args)
		{
			IRISIntegrationMessageVO vo = new IRISIntegrationMessageVO(false, "", "");
			vo.setSystemTraceAuditNumber("520");
			System.out.println(">>"+vo.getSystemTraceAuditNumber());
		}

		public void setMicrobankTransactionCode(String microbankTransactionCode)
		{
			this.microbankTransactionCode = microbankTransactionCode;
		}
		
		public String getIvrChannelStatus()
		{
			return "" ;
		}
		
		public String getMobileChannelStatus()
		{
			return "" ;
		}

		public String getChannelType()
		{
			return channelType;
		}

		public void setChannelType(String channelType)
		{
			this.channelType = channelType;
		}

		public CustomerAccount getToCustomerAccount()
		{
			return toCustomerAccount;
		}

		public void setToCustomerAccount(CustomerAccount toCustomerAccount)
		{
			this.toCustomerAccount = toCustomerAccount;
		}

		public String getTransactionCurrency()
		{
			return transactionCurrency;
		}

		public void setTransactionCurrency(String transactionCurrency)
		{
			this.transactionCurrency = transactionCurrency;
		}

		public String getDestinationAccountNumber()
		{
			return destinationAccountNumber;
		}

		public void setDestinationAccountNumber(String destinationAccountNumber)
		{
			this.destinationAccountNumber = destinationAccountNumber;
		}

		public String getDestinationAccountType()
		{
			return destinationAccountType;
		}

		public void setDestinationAccountType(String destinationAccountType)
		{
			this.destinationAccountType = destinationAccountType;
		}

		public String getDestinationAccountCurrency()
		{
			return destinationAccountCurrency;
		}

		public void setDestinationAccountCurrency(String destinationAccountCurrency)
		{
			this.destinationAccountCurrency = destinationAccountCurrency;
		}

		public String getDestinationAccountBankIMD()
		{
			return destinationAccountBankIMD;
		}

		public void setDestinationAccountBankIMD(String destinationAccountBankIMD)
		{
			this.destinationAccountBankIMD = destinationAccountBankIMD;
		}
}
