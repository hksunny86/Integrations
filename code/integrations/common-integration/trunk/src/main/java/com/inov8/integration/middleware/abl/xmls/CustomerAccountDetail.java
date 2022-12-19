package com.inov8.integration.middleware.abl.xmls;


import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",propOrder = {

})
@XmlRootElement(name = "AccountList")
public class CustomerAccountDetail {

    @XmlElement(name = "MINIBALANCE")
    private String miniBalance;
    @XmlElement(name = "BOOKBALANCE")
    private String bookBalance;
    @XmlElement(name = "OPENINGDATE")
    private String openingGate;
    @XmlElement(name = "CUSTNAME")
    private String customerName;
    @XmlElement(name = "HOLDAMOUNT")
    private String holdAmount;
    @XmlElement(name = "DAILYWITHDRAWAL")
    private String dailyWithdrawal;
    @XmlElement(name = "FUNDSADVANCELIMIT")
    private String fundsAdvanceLimit;
    @XmlElement(name = "OVERDRAFTUSAGE")
    private String overDraftUsage;
    @XmlElement(name = "AVAILABLEBALANCE")
    private String availableBalance;
    @XmlElement(name = "NETBALANCE")
    private String netBalance;
    @XmlElement(name = "CUSTNO")
    private String customerNumber;
    @XmlElement(name = "ACCNO")
    private String accountNumber;
    @XmlElement(name = "CODBRANCH")
    private String branchCode;
    @XmlElement(name = "NAMBRANCH")
    private String branchName;
    @XmlElement(name = "BALANCE")
    private String balance;
    @XmlElement(name = "ACCTTYPE")
    private String accountType;
    @XmlElement(name = "DESCACCTTYPE")
    private String accountTypeDesc;
    @XmlElement(name = "CCYDESC")
    private String ccyDesc;
    @XmlElement(name = "STATUS")
    private String status;
    @XmlElement(name = "RELATION")
    private String relation;
    @XmlElement(name = "BALAVAIL")
    private String balAvail;
    @XmlElement(name = "HASCHEQUE")
    private String hasCheque;
    @XmlElement(name = "HASSIFACILITY")
    private String hasFacility;
    @XmlElement(name = "HASOVERDRAFT")
    private String hasOverDraft;
    @XmlElement(name = "UNCLEARFUND")
    private String unClearFund;
    @XmlElement(name = "PRODUCTNAME")
    private String productName;
    @XmlElement(name = "IBAN")
    private String iban;
    @XmlElement(name = "MODEOFOPERATION")
    private String modeOfOperation;
    @XmlElement(name = "PRODCODE")
    private String prodCode;
    @XmlElement(name = "PRODUCTTYPE")
    private String productType;
    @XmlElement(name = "ISDEBITCARDAVAILAB")
    private String isDebitCardAvailable;

    public String getMiniBalance() {
        return miniBalance;
    }

    public void setMiniBalance(String miniBalance) {
        this.miniBalance = miniBalance;
    }

    public String getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(String bookBalance) {
        this.bookBalance = bookBalance;
    }

    public String getOpeningGate() {
        return openingGate;
    }

    public void setOpeningGate(String openingGate) {
        this.openingGate = openingGate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(String holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getDailyWithdrawal() {
        return dailyWithdrawal;
    }

    public void setDailyWithdrawal(String dailyWithdrawal) {
        this.dailyWithdrawal = dailyWithdrawal;
    }

    public String getFundsAdvanceLimit() {
        return fundsAdvanceLimit;
    }

    public void setFundsAdvanceLimit(String fundsAdvanceLimit) {
        this.fundsAdvanceLimit = fundsAdvanceLimit;
    }

    public String getOverDraftUsage() {
        return overDraftUsage;
    }

    public void setOverDraftUsage(String overDraftUsage) {
        this.overDraftUsage = overDraftUsage;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(String netBalance) {
        this.netBalance = netBalance;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeDesc() {
        return accountTypeDesc;
    }

    public void setAccountTypeDesc(String accountTypeDesc) {
        this.accountTypeDesc = accountTypeDesc;
    }

    public String getCcyDesc() {
        return ccyDesc;
    }

    public void setCcyDesc(String ccyDesc) {
        this.ccyDesc = ccyDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getBalAvail() {
        return balAvail;
    }

    public void setBalAvail(String balAvail) {
        this.balAvail = balAvail;
    }

    public String getHasCheque() {
        return hasCheque;
    }

    public void setHasCheque(String hasCheque) {
        this.hasCheque = hasCheque;
    }

    public String getHasFacility() {
        return hasFacility;
    }

    public void setHasFacility(String hasFacility) {
        this.hasFacility = hasFacility;
    }

    public String getHasOverDraft() {
        return hasOverDraft;
    }

    public void setHasOverDraft(String hasOverDraft) {
        this.hasOverDraft = hasOverDraft;
    }

    public String getUnClearFund() {
        return unClearFund;
    }

    public void setUnClearFund(String unClearFund) {
        this.unClearFund = unClearFund;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getModeOfOperation() {
        return modeOfOperation;
    }

    public void setModeOfOperation(String modeOfOperation) {
        this.modeOfOperation = modeOfOperation;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIsDebitCardAvailable() {
        return isDebitCardAvailable;
    }

    public void setIsDebitCardAvailable(String isDebitCardAvailable) {
        this.isDebitCardAvailable = isDebitCardAvailable;
    }
}
