package com.inov8.microbank.server.service.zong;

import com.inov8.microbank.server.service.PaymentServiceVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;


/**
 * Project Name: Commons-Integration
 * @author Jawwad Farooq Creation Date: July 7, 2009 
 */

public class ZongIntegrationMessageVO extends PaymentServiceVO
{

	protected static String STAN_PADDING_CHAR = "0";
	protected static int STAN_LENGTH = 6;
	protected static boolean STAN_LEFT_PAD = true;
	
	protected static String RRN_PADDING_CHAR = "0";
	protected static int RRN_LENGTH = 12;
	protected static boolean RRN_LEFT_PAD = true;
	
	private long timeOutInterval;
	public long getTimeOutInterval()
	{
		return timeOutInterval;
	}

	public void setTimeOutInterval(long timeOut)
	{
		this.timeOutInterval = timeOut;
	}
	
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -3857122069685739201L;

	private boolean					isNetworkMessage;

	private String					paymentGatewayCode;
	private String					microbankTransactionCode;
	
	private String 					username;
	private String 					password;
	
	private String 					messageString;
	
	private String					protocol;
	private String					version;
	private String					fieldInError;
	private String					messageType;
	private String					transmissionDateAndTime;	
	private String					deliveryChannelType = IntegrationConstants.PhoenixChannelTypes.MOBILE_BANKING.getChannelType();
	private String					deliveryChannelId;
	private String					customerIdentification;
	private String					transactionCode;
	private String					transactionDate;
	private String					transactionTime;
	private String					retrievalReferenceNumber;
	private String					customerPINData;
	private String					channelSpecificDataField;
	private String					channelPrivateData;
	private String					authorizationResponseId;
	private String					responseCode;
	
	private String					destinationChannelType;
	private String					agentID;
	
	private String					dealerNumber;
	private String					dealerPassword;
	private String					customerMobileNumber;
	
	
	/**
	 * Not used just introduced to implement to handle base class abstract methods
	 */
	private String					systemTraceAuditNumber;
	private String					secureVerificationData;
	
	
	/** This field (accountDetail) is a cumulative field consist of either of below one set 
	 * 1) Account Name, Type and Currency
	 * 2) Card Number, Card Expiry and Reserved
	 * 
	 */
//	private String					accountDetail;
//
//	private String					customerId;
//	private String					noOfAccounts;
//	private String					utilityCompanyId;
//	private String					consumerNumber;
	private String					amountPaid;
	
//	private String					cardRequestResponse;
//	private String					miniStatementType;
//	private String					additionalData;
//	private String					transactionLength;
//	private String					transactionBlock;
//	private String					channelPassword;
//	private String					newPIN;
//	private String					PAN;
//	private String					DCType = IntegrationConstants.PhoenixChannelTypes.MOBILE_BANKING.getChannelType();
//	private String					reqChannelStatus;
//	private String					cardStatusCode;
	
	// Customer Account related fields
//	public static final String	ACC_STATUS_ACTIVE	= "00";
//	public static final String	ACC_STATUS_INACTIVE	= "01";

//	private String				accountNumber;
//	private String				accountType;
//	private String				accountCurrency;
//	private String				accountStatus;
	private String				availableBalance;
	private String				actualBalance;
//	private String				withdrawalLimit;
//	private String				availableWithdrawalLimit;
//	private String				draftLimit;
//	private String				limitExpiryDate;
//	private String				currencyName;
//	private String				currencyMnemonic;
//	private String				currencyDecimalPoints;
	
//	private String				openingBalance;
	private String				transactionAmount;
//	private String				transactionCurrency;
//	private String				titleOfTheAccount;
//	private String				accountBankIMD = "600648";
	
	//Customer Profile related fields
//	private String				NIC;
//	private String				newNIC;
//	private String				customerID;
//	private String				title;
//	private String				fullName;
//	private String				dateOfBirth;
//	private String				motherMaidenName;
//	private String				address;
//	private String				postalCode;
//	private String				telephoneNumber;
//	private String				mobileNumber;
//	private String				email;
//	private String				company;
//	private String				CompanyAddress;
//	private String				companyPostalCode;
//	private String				companyTelephone;
//	private String				anniversaryDate;
//	private String				correspondenceFlag;
//	private String				ivrChannelStatus;
//	private String				mobileChannelStatus;
//	private String				callCenterChannelStatus;
//	private String				internetBankingChannelStatus;
//	private String				reserved;
	
	// Credit Card related fields
	
//	private String				cardNumber;
//	private String				cardExpiry;
	private String				reserved1;
//	private String				closingBalance;
//	private String				dueDate;
//	private String				minPaymentDue;
//	private String				currentBalance;
//	private String				rewardPointsEarned;
//	private String				reserved2;
//	private String				availableCreditLimit;
//	private String				customerName;
//	private String				reserved3;
	
//	private String				messageAsEdi;
	
	public ZongIntegrationMessageVO(boolean isNetworkMessage, String paymentGatewayCode, String microbankTransactionCode)
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
//		return addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
		return retrievalReferenceNumber;
	}

	/**
	 * @param retrievalReferenceNumber the retrievalReferenceNumber to set
	 */
	public void setRetrievalReferenceNumber(String retrievalReferenceNumber)
	{
//		this.retrievalReferenceNumber = addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
		this.retrievalReferenceNumber = retrievalReferenceNumber;
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
	public String getTransactionDate()
	{
		return transactionDate;
	}

	/**
	 * @param transactionLocalDate the transactionLocalDate to set
	 */
	public void setTransactionDate(String transactionDate)
	{
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionLocalTime
	 */
	public String getTransactionTime()
	{
		return transactionTime;
	}

	/**
	 * @param transactionLocalTime the transactionLocalTime to set
	 */
	public void setTransactionTime(String transactionTime)
	{
		this.transactionTime = transactionTime;
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

//	/**
//	 * @return the consumerNumber
//	 */
//	public String getConsumerNumber()
//	{
//		return consumerNumber;
//	}

//	/**
//	 * @param consumerNumber the consumerNumber to set
//	 */
//	public void setConsumerNumber(String consumerNumber)
//	{
//		this.consumerNumber = consumerNumber;
//	}
//
//	/**
//	 * @return the customerId
//	 */
//	public String getCustomerId()
//	{
//		return customerId;
//	}
//
//	/**
//	 * @param customerId the customerId to set
//	 */
//	public void setCustomerId(String customerId)
//	{
//		this.customerId = customerId;
//	}
//
//	/**
//	 * @return the noOfAccounts
//	 */
//	public String getNoOfAccounts()
//	{
//		return noOfAccounts;
//	}
//
//	/**
//	 * @param noOfAccounts the noOfAccounts to set
//	 */
//	public void setNoOfAccounts(String noOfAccounts)
//	{
//		this.noOfAccounts = noOfAccounts;
//	}
//
//	/**
//	 * @return the utilityCompanyId
//	 */
//	public String getUtilityCompanyId()
//	{
//		return utilityCompanyId;
//	}
//
//	/**
//	 * @param utilityCompanyId the utilityCompanyId to set
//	 */
//	public void setUtilityCompanyId(String utilityCompanyId)
//	{
//		this.utilityCompanyId = utilityCompanyId;
//	}

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

//	/**
//	 * @param accounts the accounts to set
//	 */
//	public void setCustomerAccounts(List<CustomerAccount> accounts)
//	{
//		this.accounts = accounts;
//	}
//
//	/**
//	 * @return the accounts
//	 */
//	public List<CustomerAccount> getCustomerAccounts()
//	{
//		return accounts;
//	}
//
//	/**
//	 * @return the messageAsEdi
//	 */
//	public String getMessageAsEdi()
//	{
//		return messageAsEdi;
//	}

//	/**
//	 * @param messageAsEdi the messageAsEdi to set
//	 */
//	public void setMessageAsEdi(String messageAsEdi)
//	{
//		this.messageAsEdi = messageAsEdi;
//	}

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
	    
	    
//	    public static void main(String[] args)
//		{
//			ZongIntegrationMessageVO vo = new ZongIntegrationMessageVO(false, "", "");
//			//vo.setSystemTraceAuditNumber("520");
//			System.out.println(">>"+vo.getRetrievalReferenceNumber());
//		}

		public void setMicrobankTransactionCode(String microbankTransactionCode)
		{
			this.microbankTransactionCode = microbankTransactionCode;
		}

		public String getFieldInError()
		{
			return fieldInError;
		}

		public void setFieldInError(String fieldInError)
		{
			this.fieldInError = fieldInError;
		}

		public String getCustomerPINData()
		{
			return customerPINData;
		}

		public void setCustomerPINData(String customerPINData)
		{
			this.customerPINData = customerPINData;
		}

		public String getAgentID()
		{
			return agentID;
		}

		public void setAgentID(String agentID)
		{
			this.agentID = agentID;
		}

		public String getChannelSpecificDataField()
		{
			return channelSpecificDataField;
		}

		public void setChannelSpecificDataField(String channelSpecificDataField)
		{
			this.channelSpecificDataField = channelSpecificDataField;
		}

		public String getChannelPrivateData()
		{
			return channelPrivateData;
		}

		public void setChannelPrivateData(String channelPrivateData)
		{
			this.channelPrivateData = channelPrivateData;
		}

//		public String getCardNumber()
//		{
//			return cardNumber;
//		}
//
//		public void setCardNumber(String cardNumber)
//		{
//			this.cardNumber = cardNumber;
//		}
//
//		public String getCardRequestResponse()
//		{
//			return cardRequestResponse;
//		}
//
//		public void setCardRequestResponse(String cardRequestResponse)
//		{
//			this.cardRequestResponse = cardRequestResponse;
//		}
//
//		public String getMiniStatementType()
//		{
//			return miniStatementType;
//		}
//
//		public void setMiniStatementType(String miniStatementType)
//		{
//			this.miniStatementType = miniStatementType;
//		}
//
//		
//
//		public String getAdditionalData()
//		{
//			return additionalData;
//		}
//
//		public void setAdditionalData(String additionalData)
//		{
//			this.additionalData = additionalData;
//		}
//
//		public String getTransactionLength()
//		{
//			return transactionLength;
//		}
//
//		public void setTransactionLength(String transactionLength)
//		{
//			this.transactionLength = transactionLength;
//		}
//
//		public String getTransactionBlock()
//		{
//			return transactionBlock;
//		}
//
//		public void setTransactionBlock(String transactionBlock)
//		{
//			this.transactionBlock = transactionBlock;
//		}
//
//		public String getChannelPassword()
//		{
//			return channelPassword;
//		}
//
//		public void setChannelPassword(String channelPassword)
//		{
//			this.channelPassword = channelPassword;
//		}
//
//		public String getNewPIN()
//		{
//			return newPIN;
//		}
//
//		public void setNewPIN(String newPIN)
//		{
//			this.newPIN = newPIN;
//		}
//
//		public String getPAN()
//		{
//			return PAN;
//		}
//
//		public void setPAN(String PAN)
//		{
//			this.PAN = PAN;
//		}
//
//		public String getReqChannelStatus()
//		{
//			return reqChannelStatus;
//		}
//
//		public void setReqChannelStatus(String reqChannelStatus)
//		{
//			this.reqChannelStatus = reqChannelStatus;
//		}
//
//		public String getCardStatusCode()
//		{
//			return cardStatusCode;
//		}
//
//		public void setCardStatusCode(String cardStatusCode)
//		{
//			this.cardStatusCode = cardStatusCode;
//		}
//
//		public String getAccountNumber()
//		{
//			return accountNumber;
//		}
//
//		public void setAccountNumber(String accountNumber)
//		{
//			this.accountNumber = accountNumber;
//		}
//
//		public String getAccountType()
//		{
//			return accountType;
//		}
//
//		public void setAccountType(String accountType)
//		{
//			this.accountType = accountType;
//		}
//
//		public String getAccountCurrency()
//		{
//			return accountCurrency;
//		}
//
//		public void setAccountCurrency(String accountCurrency)
//		{
//			this.accountCurrency = accountCurrency;
//		}
//
//		public String getAccountStatus()
//		{
//			return accountStatus;
//		}
//
//		public void setAccountStatus(String accountStatus)
//		{
//			this.accountStatus = accountStatus;
//		}

		public String getAvailableBalance()
		{
			return availableBalance;
		}

		public void setAvailableBalance(String availableBalance)
		{
			this.availableBalance = availableBalance;
		}

		public String getActualBalance()
		{
			return actualBalance;
		}

		public void setActualBalance(String actualBalance)
		{
			this.actualBalance = actualBalance;
		}

//		public String getDraftLimit()
//		{
//			return draftLimit;
//		}
//
//		public void setDraftLimit(String draftLimit)
//		{
//			this.draftLimit = draftLimit;
//		}
//
//		public String getLimitExpiryDate()
//		{
//			return limitExpiryDate;
//		}
//
//		public void setLimitExpiryDate(String limitExpiryDate)
//		{
//			this.limitExpiryDate = limitExpiryDate;
//		}
//
//		public String getCurrencyName()
//		{
//			return currencyName;
//		}
//
//		public void setCurrencyName(String currencyName)
//		{
//			this.currencyName = currencyName;
//		}
//
//		public String getCurrencyMnemonic()
//		{
//			return currencyMnemonic;
//		}
//
//		public void setCurrencyMnemonic(String currencyMnemonic)
//		{
//			this.currencyMnemonic = currencyMnemonic;
//		}
//
//		public String getCurrencyDecimalPoints()
//		{
//			return currencyDecimalPoints;
//		}
//
//		public void setCurrencyDecimalPoints(String currencyDecimalPoints)
//		{
//			this.currencyDecimalPoints = currencyDecimalPoints;
//		}
//
//		public String getOpeningBalance()
//		{
//			return openingBalance;
//		}
//
//		public void setOpeningBalance(String openingBalance)
//		{
//			this.openingBalance = openingBalance;
//		}
//
//		public String getTransactionAmount()
//		{
//			return transactionAmount;
//		}
//
//		public void setTransactionAmount(String transactionAmount)
//		{
//			this.transactionAmount = transactionAmount;
//		}
//
//		public String getTitleOfTheAccount()
//		{
//			return titleOfTheAccount;
//		}
//
//		public void setTitleOfTheAccount(String titleOfTheAccount)
//		{
//			this.titleOfTheAccount = titleOfTheAccount;
//		}
//
//		public String getAccountBankIMD()
//		{
//			return accountBankIMD;
//		}
//
//		public void setAccountBankIMD(String accountBankIMD)
//		{
//			this.accountBankIMD = accountBankIMD;
//		}
//
//		
//		public String getNIC()
//		{
//			return NIC;
//		}
//
//		public void setNIC(String nic)
//		{
//			NIC = nic;
//		}
//
//		public String getNewNIC()
//		{
//			return newNIC;
//		}
//
//		public void setNewNIC(String newNIC)
//		{
//			this.newNIC = newNIC;
//		}
//
//		public String getCustomerID()
//		{
//			return customerID;
//		}
//
//		public void setCustomerID(String customerID)
//		{
//			this.customerID = customerID;
//		}
//		
//		public String getFullName()
//		{
//			return fullName;
//		}
//
//		public void setFullName(String fullName)
//		{
//			this.fullName = fullName;
//		}
//
//		public String getDateOfBirth()
//		{
//			return dateOfBirth;
//		}
//
//		public void setDateOfBirth(String dateOfBirth)
//		{
//			this.dateOfBirth = dateOfBirth;
//		}
//
//		public String getMotherMaidenName()
//		{
//			return motherMaidenName;
//		}
//
//		public void setMotherMaidenName(String motherMaidenName)
//		{
//			this.motherMaidenName = motherMaidenName;
//		}
//
//		public String getPostalCode()
//		{
//			return postalCode;
//		}
//
//		public void setPostalCode(String postalCode)
//		{
//			this.postalCode = postalCode;
//		}
//
//		public String getTelephoneNumber()
//		{
//			return telephoneNumber;
//		}
//
//		public void setTelephoneNumber(String telephoneNumber)
//		{
//			this.telephoneNumber = telephoneNumber;
//		}
//
//		public String getMobileNumber()
//		{
//			return mobileNumber;
//		}
//
//		public void setMobileNumber(String mobileNumber)
//		{
//			this.mobileNumber = mobileNumber;
//		}
//
//		public String getCompanyAddress()
//		{
//			return CompanyAddress;
//		}
//
//		public void setCompanyAddress(String companyAddress)
//		{
//			CompanyAddress = companyAddress;
//		}
//
//		public String getCompanyPostalCode()
//		{
//			return companyPostalCode;
//		}
//
//		public void setCompanyPostalCode(String companyPostalCode)
//		{
//			this.companyPostalCode = companyPostalCode;
//		}
//
//		public String getCompanyTelephone()
//		{
//			return companyTelephone;
//		}
//
//		public void setCompanyTelephone(String companyTelephone)
//		{
//			this.companyTelephone = companyTelephone;
//		}
//
//		public String getAnniversaryDate()
//		{
//			return anniversaryDate;
//		}
//
//		public void setAnniversaryDate(String anniversaryDate)
//		{
//			this.anniversaryDate = anniversaryDate;
//		}
//
//		public String getCorrespondenceFlag()
//		{
//			return correspondenceFlag;
//		}
//
//		public void setCorrespondenceFlag(String correspondenceFlag)
//		{
//			this.correspondenceFlag = correspondenceFlag;
//		}
//
//		public String getIvrChannelStatus()
//		{
//			return ivrChannelStatus;
//		}
//
//		public void setIvrChannelStatus(String ivrChannelStatus)
//		{
//			this.ivrChannelStatus = ivrChannelStatus;
//		}
//
//		public String getMobileChannelStatus()
//		{
//			return mobileChannelStatus;
//		}
//
//		public void setMobileChannelStatus(String mobileChannelStatus)
//		{
//			this.mobileChannelStatus = mobileChannelStatus;
//		}
//
//		public String getCallCenterChannelStatus()
//		{
//			return callCenterChannelStatus;
//		}
//
//		public void setCallCenterChannelStatus(String callCenterChannelStatus)
//		{
//			this.callCenterChannelStatus = callCenterChannelStatus;
//		}
//
//		public String getInternetBankingChannelStatus()
//		{
//			return internetBankingChannelStatus;
//		}
//
//		public void setInternetBankingChannelStatus(String internetBankingChannelStatus)
//		{
//			this.internetBankingChannelStatus = internetBankingChannelStatus;
//		}
//
//		public String getCardExpiry()
//		{
//			return cardExpiry;
//		}
//
//		public void setCardExpiry(String cardExpiry)
//		{
//			this.cardExpiry = cardExpiry;
//		}
//
//		
//		public String getClosingBalance()
//		{
//			return closingBalance;
//		}
//
//		public void setClosingBalance(String closingBalance)
//		{
//			this.closingBalance = closingBalance;
//		}
//
//		public String getDueDate()
//		{
//			return dueDate;
//		}
//
//		public void setDueDate(String dueDate)
//		{
//			this.dueDate = dueDate;
//		}
//
//		public String getMinPaymentDue()
//		{
//			return minPaymentDue;
//		}
//
//		public void setMinPaymentDue(String minPaymentDue)
//		{
//			this.minPaymentDue = minPaymentDue;
//		}
//
//		public String getCurrentBalance()
//		{
//			return currentBalance;
//		}
//
//		public void setCurrentBalance(String currentBalance)
//		{
//			this.currentBalance = currentBalance;
//		}
//
//		public String getRewardPointsEarned()
//		{
//			return rewardPointsEarned;
//		}
//
//		public void setRewardPointsEarned(String rewardPointsEarned)
//		{
//			this.rewardPointsEarned = rewardPointsEarned;
//		}
//		
//		public String getAvailableCreditLimit()
//		{
//			return availableCreditLimit;
//		}
//
//		public void setAvailableCreditLimit(String availableCreditLimit)
//		{
//			this.availableCreditLimit = availableCreditLimit;
//		}

//		public String getCustomerName()
//		{
//			return customerName;
//		}
//
//		public void setCustomerName(String customerName)
//		{
//			this.customerName = customerName;
//		}
//
//		public String getCompany()
//		{
//			return company;
//		}
//
//		public void setCompany(String company)
//		{
//			this.company = company;
//		}
//
//		public String getAccountDetail()
//		{
//			return accountDetail;
//		}
//
//		public void setAccountDetail(String accountDetail)
//		{
//			this.accountDetail = accountDetail;
//		}

		public String getDeliveryChannelType()
		{
			return deliveryChannelType;
		}

		public void setDeliveryChannelType(String deliveryChannelType)
		{
			this.deliveryChannelType = deliveryChannelType;
		}

		public String getCustomerIdentification()
		{
			return customerIdentification;
		}

		public void setCustomerIdentification(String customerIdentification)
		{
			this.customerIdentification = customerIdentification;
		}

//		public String getDCType()
//		{
//			return DCType;
//		}
//
//		public void setDCType(String type)
//		{
//			DCType = type;
//		}
//
//		public String getTransactionCurrency()
//		{
//			return transactionCurrency;
//		}
//
//		public void setTransactionCurrency(String transactionCurrency)
//		{
//			this.transactionCurrency = transactionCurrency;
//		}
//
//		public String getWithdrawalLimit()
//		{
//			return withdrawalLimit;
//		}
//
//		public void setWithdrawalLimit(String withdrawalLimit)
//		{
//			this.withdrawalLimit = withdrawalLimit;
//		}
//
//		public String getAvailableWithdrawalLimit()
//		{
//			return availableWithdrawalLimit;
//		}
//
//		public void setAvailableWithdrawalLimit(String availableWithdrawalLimit)
//		{
//			this.availableWithdrawalLimit = availableWithdrawalLimit;
//		}
//
//		public String getTitle()
//		{
//			return title;
//		}
//
//		public void setTitle(String title)
//		{
//			this.title = title;
//		}
//
//		public String getAddress()
//		{
//			return address;
//		}
//
//		public void setAddress(String address)
//		{
//			this.address = address;
//		}
//
//		public String getEmail()
//		{
//			return email;
//		}

//		public void setEmail(String email)
//		{
//			this.email = email;
//		}
//
//		public String getReserved()
//		{
//			return reserved;
//		}
//
//		public void setReserved(String reserved)
//		{
//			this.reserved = reserved;
//		}

		public String getReserved1()
		{
			return reserved1;
		}

		public void setReserved1(String reserved1)
		{
			this.reserved1 = reserved1;
		}

		public String getUsername()
		{
			return username;
		}

		public void setUsername(String username)
		{
			this.username = username;
		}

		public String getPassword()
		{
			return password;
		}

		public void setPassword(String password)
		{
			this.password = password;
		}

		public String getMessageString()
		{
			return messageString;
		}

		public void setMessageString(String messageString)
		{
			this.messageString = messageString;
		}

		public String getDealerNumber()
		{
			return dealerNumber;
		}

		public void setDealerNumber(String dealerNumber)
		{
			this.dealerNumber = dealerNumber;
		}

		public String getDealerPassword()
		{
			return dealerPassword;
		}

		public void setDealerPassword(String dealerPassword)
		{
			this.dealerPassword = dealerPassword;
		}

		public String getCustomerMobileNumber()
		{
			return customerMobileNumber;
		}

		public void setCustomerMobileNumber(String customerMobileNumber)
		{
			this.customerMobileNumber = customerMobileNumber;
		}

		public String getTransactionAmount()
		{
			return transactionAmount;
		}

		public void setTransactionAmount(String transactionAmount)
		{
			this.transactionAmount = transactionAmount;
		}

//		public String getReserved2()
//		{
//			return reserved2;
//		}
//
//		public void setReserved2(String reserved2)
//		{
//			this.reserved2 = reserved2;
//		}
//
//		public String getReserved3()
//		{
//			return reserved3;
//		}
//
//		public void setReserved3(String reserved3)
//		{
//			this.reserved3 = reserved3;
//		}
}
