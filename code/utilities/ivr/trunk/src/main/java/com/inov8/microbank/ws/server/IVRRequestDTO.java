package com.inov8.microbank.ws.server;

public class IVRRequestDTO {
	
	private String customerMobileNo;
	private String transactionId;
	private String agentMobileNo;
	private String handlerMobileNo;
	private Double amount;
	private Double charges;
	private Integer retryCount;
	private String agentId;
	private Long productId;
	private Boolean isPinAuthenticated;
	private Boolean isCredentialsExpired;
	private String pin;
	private String recipientCnic;
	private String recipientMobile;
	private String coreAccountNo;
	private String consumerNo;
	private String nfcTagNo;
	private String nfcTagIdentifier;
	private String ivrId;
	
	public String getCustomerMobileNo() {
		return customerMobileNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public String getAgentMobileNo() {
		return agentMobileNo;
	}
	public Double getAmount() {
		return amount;
	}
	public Double getCharges() {
		return charges;
	}
	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setCharges(Double charges) {
		this.charges = charges;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public Boolean getIsPinAuthenticated() {
		return isPinAuthenticated;
	}
	public Boolean getIsCredentialsExpired() {
		return isCredentialsExpired;
	}
	public void setIsPinAuthenticated(Boolean isPinAuthenticated) {
		this.isPinAuthenticated = isPinAuthenticated;
	}
	public void setIsCredentialsExpired(Boolean isCredentialsExpired) {
		this.isCredentialsExpired = isCredentialsExpired;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getRecipientCnic()
	{
		return recipientCnic;
	}
	public void setRecipientCnic(String recipientCnic)
	{
		this.recipientCnic = recipientCnic;
	}
	public String getRecipientMobile()
	{
		return recipientMobile;
	}
	public void setRecipientMobile(String recipientMobile)
	{
		this.recipientMobile = recipientMobile;
	}
	public String getCoreAccountNo()
	{
		return coreAccountNo;
	}
	public void setCoreAccountNo(String coreAccountNo)
	{
		this.coreAccountNo = coreAccountNo;
	}
	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @return - Returns the consumerNo
	 * </pre>
	 */
	public String getConsumerNo()
	{
		return consumerNo;
	}
	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 *
	 * @param consumerNo - the consumerNo to set
	 * </pre>
	 */
	public void setConsumerNo(String consumerNo)
	{
		this.consumerNo = consumerNo;
	}


	public String getNfcTagNo()
	{
		return nfcTagNo;
	}
	public void setNfcTagNo(String nfcTagNo)
	{
		this.nfcTagNo = nfcTagNo;
	}
	public String getNfcTagIdentifier()
	{
		return nfcTagIdentifier;
	}
	public void setNfcTagIdentifier(String nfcTagIdentifier)
	{
		this.nfcTagIdentifier = nfcTagIdentifier;
	}

	public String getIvrId()
	{
		return ivrId;
	}
	public void setIvrId(String ivrId)
	{
		this.ivrId = ivrId;
	}

	@Override
	public String toString()
	{
		return "IVRRequestDTO [customerMobileNo=" + customerMobileNo
				+ ", transactionId=" + transactionId + ", agentMobileNo="
				+ agentMobileNo + ", handlerMobileNo="
						+ handlerMobileNo + ", amount=" + amount + ", charges=" + charges
				+ ", retryCount=" + retryCount + ", agentId=" + agentId
				+ ", productId=" + productId + ", isPinAuthenticated="
				+ isPinAuthenticated + ", isCredentialsExpired="
				+ isCredentialsExpired + ", recipientCnic="
				+ recipientCnic + ", recipientMobile=" + recipientMobile
				+ ", coreAccountNo=" + coreAccountNo + ", consumerNo="
				+ consumerNo + ", nfcTagNo=" + nfcTagNo + ", nfcTagIdentifier="
				+ nfcTagIdentifier + ", ivrId=" + ivrId + "]";
	}
	public String getHandlerMobileNo() {
		return handlerMobileNo;
	}
	public void setHandlerMobileNo(String handlerMobileNo) {
		this.handlerMobileNo = handlerMobileNo;
	}

}
