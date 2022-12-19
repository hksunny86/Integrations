package com.inov8.microbank.common.model;



/**
 * @author Rizwan Munir
 */

public class P2PDetailRefDataModel  {

	private String transactionCode;
    private String senderCNIC;
    private String senderMobile;
    private String recipientCNIC;
    private String recipientMobile;

    public P2PDetailRefDataModel() {
    }

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		if (transactionCode != null) {
			this.transactionCode = transactionCode;
		}
	}

	public String getSenderCNIC() {
		return senderCNIC;
	}

	public void setSenderCNIC(String senderCNIC) {
		if (senderCNIC != null) {
			this.senderCNIC = senderCNIC;
		}
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		if (senderMobile != null) {
			this.senderMobile = senderMobile;
		}
	}

	public String getRecipientCNIC() {
		return recipientCNIC;
	}

	public void setRecipientCNIC(String recipientCNIC) {
		if (recipientCNIC != null) {
			this.recipientCNIC = recipientCNIC;
		}
	}

	public String getRecipientMobile() {
		return recipientMobile;
	}

	public void setRecipientMobile(String recipientMobile) {
		if (recipientMobile != null) {
			this.recipientMobile = recipientMobile;
		}
	}
    

}