package com.inov8.agentmate.model;

import java.io.Serializable;

public class TransactionInfoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cmob;
	private String rcmob;
	private String amob;
	private String bbacid;
	private String coreacid;
	private String coreactl;
	private String camt;
	private String camtf;
	private String tpam;
	private String tpamf;
	private String tamt;
	private String tamtf;
	private String txam;
	private String txamf;
	private String pname;
	private String pid;
	private String consumer;
	private String duedate;
	private String duedatef;
	private String bamt;
	private String bamtf;
	private String lbamt;
	private String lbamtf;
	private String bpaid;
	private String cname;
	private String cnic;
	private String rwcnic;
	private String rwmob;
	private String swcnic;
	private String swmob;
	private String ramob;
	private String racnic;
	private String raname;
	private String isoverdue;
	private String trxid;
	private String datef;
	private String timef;
	private String isreg;
    private String isBvsReq;
    private String isOtpReq;
    private String status;
    private String thirdPartSessionId;
    private String senderCity;
    private String isWalletExist;
    private String walletNumber;
    private String walletBal;
    private String paymentPurpose;
    private String toBankIMD;

    private String bankName;
    private String branchName;
    private String iban;
    private String crDr;
    private String fingerIndex;


    public String getThirdPartSessionId() {
        return thirdPartSessionId;
    }

    public void setThirdPartSessionId(String thirdPartSessionId) {
        this.thirdPartSessionId = thirdPartSessionId;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

	private static TransactionInfoModel transactionInfo = null;
	private TransactionInfoModel() {}

	public static TransactionInfoModel getInstance() {
		if (transactionInfo == null) {
			transactionInfo = new TransactionInfoModel();
		}
		return transactionInfo;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsOtpRequired() {
        return isOtpReq;
    }

    public void setIsOtpRequired(String isOtpReq) {
        this.isOtpReq = isOtpReq;
    }


    /**
	 * Cash In & Out
	 */
	public void CashInOut(String cmob, String cname, String cnic, String txam,
			String txamf, String tpam, String tpamf, String camt, String camtf,
			String tamt, String tamtf) {
		this.cmob = cmob;
		this.cname = cname;
		this.cnic = cnic;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}

    /**
     * 3rd Party Cash Out
     */
    public void thirdPartyCashOut(String cmob, String accNo, String accTitle, String isBvsReq,
                                  String camt, String camtf, String tpam, String tpamf,
                          String tamt, String tamtf, String txam, String txamf,
                                  String thirdPartySessionId, String cnic) {
        this.cmob = cmob;
        this.coreacid = accNo;
        this.coreactl = accTitle;
        this.isBvsReq = isBvsReq;
        this.camt = camt;
        this.camtf = camtf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.txam = txam;
        this.txamf = txamf;
        this.cnic = cnic;
        this.thirdPartSessionId = thirdPartySessionId;
    }
	public void thirdPartyCashOut(String cmob, String accNo, String accTitle, String isBvsReq,
								  String camt, String camtf, String tpam, String tpamf,
								  String tamt, String tamtf, String txam, String txamf,
								  String thirdPartySessionId, String cnic, String customerCnic) {
		this.cmob = cmob;
		this.coreacid = accNo;
		this.coreactl = accTitle;
		this.isBvsReq = isBvsReq;
		this.camt = camt;
		this.camtf = camtf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.txam = txam;
		this.txamf = txamf;
		this.cnic = cnic;
		this.rwcnic = customerCnic;
		this.thirdPartSessionId = thirdPartySessionId;
	}

    public void CashOutByTrxId(String cmob, String cname, String cnic, String txam,
                          String txamf, String tpam, String tpamf, String camt, String camtf,
                          String tamt, String tamtf, String trxId) {
        this.cmob = cmob;
        this.cname = cname;
        this.cnic = cnic;
        this.txam = txam;
        this.txamf = txamf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.camt = camt;
        this.camtf = camtf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.trxid = trxId;
    }

	/**
	 * Bill Inquiry
	 */
	public void BillInquiry(String cmob, String cnic, String pname,
			String consumer, String duedate, String duedatef, String bamt,
			String bamtf, String lbamt, String lbamtf, String isoverdue,
			String bpaid) {
		this.cmob = cmob;
		this.cnic = cnic;
		this.pname = pname;
		this.consumer = consumer;
		this.duedate = duedate;
		this.duedatef = duedatef;
		this.bamt = bamt;
		this.bamtf = bamtf;
		this.lbamt = lbamt;
		this.lbamtf = lbamtf;
		this.isoverdue = isoverdue;
		this.bpaid = bpaid;
	}

	/**
	 * Retail Payment
	 */
	public void RetailPayment(String cmob, String txam, String txamf,
			String tpam, String tpamf, String camt, String camtf, String tamt,
			String tamtf) {
		this.cmob = cmob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}

	public void DebitCard(String cmob, String txam, String txamf,
							  String tpam, String tpamf, String camt, String camtf, String tamt,
							  String tamtf) {
		this.cmob = cmob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}
	/**
	 * Transfer In
	 */
	public void TransferIn(String bbacid, String coreacid, String txam,
			String txamf, String tpam, String tpamf, String camt, String camtf,
			String tamt, String tamtf) {
		this.bbacid = bbacid;
		this.coreacid = coreacid;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}

	/**
	 * Transfer Out
	 */
	public void TransferOut(String bbacid, String coreacid, String coreactl,
			String txam, String txamf, String tpam, String tpamf, String camt,
			String camtf, String tamt, String tamtf) {
		this.bbacid = bbacid;
		this.coreacid = coreacid;
		this.coreactl = coreactl;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}

	/**
	 * Agent To Agent
	 */
	public void AgentTransfer(String ramob, String racnic, String raname,
			String txam, String txamf, String tpam, String tpamf, String camt,
			String camtf, String tamt, String tamtf) {
		this.ramob = ramob;
		this.racnic = racnic;
		this.raname = raname;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
	}

	public void AgentIBFT(String coreacid, String coreactl, String bbacid,
							  String txam, String txamf, String tpam, String tpamf, String camt,
							  String camtf, String tamt, String tamtf, String toBankIMD, String paymentPurpose,
						  String bankName,String branchName,String iban,String crDr) {
		this.coreacid = coreacid;
		this.coreactl = coreactl;
		this.bbacid = bbacid;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.toBankIMD = toBankIMD;
		this.paymentPurpose = paymentPurpose;
		this.bankName = bankName;
		this.branchName = branchName;
		this.iban = iban;
		this.crDr = crDr;

	}

	/**
	 * Funds Transfer BLB2BLB Info
	 */
	public void FundsTransferBlb2Blb(String cmob, String rcmob, String txam,
			String txamf, String tpam, String tpamf, String camt, String camtf,
			String tamt, String tamtf, String pid) {
		this.cmob = cmob;
		this.rcmob = rcmob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Funds Transfer BLB2CNIC Info
	 */
	public void FundsTransferBlb2Cnic(String cmob, String rwmob, String rwcnic,
			String txam, String txamf, String tpam, String tpamf, String camt,
			String camtf, String tamt, String tamtf, String pid) {
		this.cmob = cmob;
		this.rwmob = rwmob;
		this.rwcnic = rwcnic;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Funds Transfer CNIC2BLB Info
	 */
	public void FundsTransferCnic2Blb(String swmob, String swcnic,
			String rcmob, String txam, String txamf, String tpam, String tpamf,
			String camt, String camtf, String tamt, String tamtf, String pid) {
		this.swmob = swmob;
		this.swcnic = swcnic;
		this.rcmob = rcmob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Funds Transfer CNIC2CNIC Info
	 */
	public void FundsTransferCnic2Cnic(String swmob, String swcnic,
			String rwmob, String rwcnic, String txam, String txamf,
			String tpam, String tpamf, String camt, String camtf, String tamt,
			String tamtf, String pid) {
		this.swmob = swmob;
		this.swcnic = swcnic;
		this.rwmob = rwmob;
		this.rwcnic = rwcnic;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Funds Transfer BLB2Core Info
	 */
	public void FundsTransferBlb2Core(String cmob, String rcmob,
			String coreacid, String coreactl, String txam, String txamf,
			String tpam, String tpamf, String camt, String camtf, String tamt,
			String tamtf, String pid) {
		this.cmob = cmob;
		this.rcmob = rcmob;
		this.coreacid = coreacid;
		this.coreactl = coreactl;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Funds Transfer CNIC2Core Info
	 */
	public void FundsTransferCnic2Core(String swmob, String swcnic,
			String rcmob, String coreacid, String coreactl, String txam,
			String txamf, String tpam, String tpamf, String camt, String camtf,
			String tamt, String tamtf, String pid) {
		this.swmob = swmob;
		this.swcnic = swcnic;
		this.rcmob = rcmob;
		this.coreacid = coreacid;
		this.coreactl = coreactl;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}

	/**
	 * Receive Money - Sender Redeem Info
	 */
	public void ReceiveMoneySenderRedeem(String swmob, String swcnic,
			String rwmob, String rwcnic, String trxid, String datef,
			String timef, String tamt, String tamtf, String pid) {
		this.swmob = swmob;
		this.swcnic = swcnic;
		this.rwmob = rwmob;
		this.rwcnic = rwcnic;
		this.trxid = trxid;
		this.datef = datef;
		this.timef = timef;
//		this.txam = txam;
//		this.txamf = txamf;
//		this.tpam = tpam;
//		this.tpamf = tpamf;
//		this.camt = camt;
//		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
	}
	
	/**
	 * Receive Money - Receive Cash Info
	 */
	public void ReceiveMoneyReceiveCash(String swmob, String swcnic,
			String rwmob, String rwcnic, String trxid, String datef,
			String timef, String txam, String txamf, String tpam, String tpamf,
			String camt, String camtf, String tamt, String tamtf, String pid, String isreg) {
		this.swmob = swmob;
		this.swcnic = swcnic;
		this.rwmob = rwmob;
		this.rwcnic = rwcnic;
		this.trxid = trxid;
		this.datef = datef;
		this.timef = timef;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.pid = pid;
		this.isreg = isreg;
	}

	public String getCmob() {
		return cmob;
	}

	public void setCmob(String cmob) {
		this.cmob = cmob;
	}

	public String getRcmob() {
		return rcmob;
	}

	public void setRcmob(String rcmob) {
		this.rcmob = rcmob;
	}

	public String getAmob() {
		return amob;
	}

	public void setAmob(String amob) {
		this.amob = amob;
	}

	public String getBbacid() {
		return bbacid;
	}

	public void setBbacid(String bbacid) {
		this.bbacid = bbacid;
	}

	public String getCoreacid() {
		return coreacid;
	}

	public void setCoreacid(String coreacid) {
		this.coreacid = coreacid;
	}

	public String getCoreactl() {
		return coreactl;
	}

	public void setCoreactl(String coreactl) {
		this.coreactl = coreactl;
	}

	public String getCamt() {
		return camt;
	}

	public void setCamt(String camt) {
		this.camt = camt;
	}

	public String getCamtf() {
		return camtf;
	}

	public void setCamtf(String camtf) {
		this.camtf = camtf;
	}

	public String getTpam() {
		return tpam;
	}

	public void setTpam(String tpam) {
		this.tpam = tpam;
	}

	public String getTpamf() {
		return tpamf;
	}

	public void setTpamf(String tpamf) {
		this.tpamf = tpamf;
	}

	public String getTamt() {
		return tamt;
	}

	public void setTamt(String tamt) {
		this.tamt = tamt;
	}

	public String getTamtf() {
		return tamtf;
	}

	public void setTamtf(String tamtf) {
		this.tamtf = tamtf;
	}

	public String getTxam() {
		return txam;
	}

	public void setTxam(String txam) {
		this.txam = txam;
	}

	public String getTxamf() {
		return txamf;
	}

	public void setTxamf(String txamf) {
		this.txamf = txamf;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getDuedate() {
		return duedate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getDuedatef() {
		return duedatef;
	}

	public void setDuedatef(String duedatef) {
		this.duedatef = duedatef;
	}

	public String getBamt() {
		return bamt;
	}

	public void setBamt(String bamt) {
		this.bamt = bamt;
	}

	public String getBamtf() {
		return bamtf;
	}

	public void setBamtf(String bamtf) {
		this.bamtf = bamtf;
	}

	public String getLbamt() {
		return lbamt;
	}

	public void setLbamt(String lbamt) {
		this.lbamt = lbamt;
	}

	public String getLbamtf() {
		return lbamtf;
	}

	public void setLbamtf(String lbamtf) {
		this.lbamtf = lbamtf;
	}

	public String getBpaid() {
		return bpaid;
	}

	public void setBpaid(String bpaid) {
		this.bpaid = bpaid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	public String getRwcnic() {
		return rwcnic;
	}

	public void setRwcnic(String rwcnic) {
		this.rwcnic = rwcnic;
	}

	public String getRwmob() {
		return rwmob;
	}

	public void setRwmob(String rwmob) {
		this.rwmob = rwmob;
	}

	public String getSwcnic() {
		return swcnic;
	}

	public void setSwcnic(String swcnic) {
		this.swcnic = swcnic;
	}

	public String getSwmob() {
		return swmob;
	}

	public void setSwmob(String swmob) {
		this.swmob = swmob;
	}

	public String getRamob() {
		return ramob;
	}

	public void setRamob(String ramob) {
		this.ramob = ramob;
	}

	public String getRacnic() {
		return racnic;
	}

	public void setRacnic(String racnic) {
		this.racnic = racnic;
	}

	public String getRaname() {
		return raname;
	}

	public void setRaname(String raname) {
		this.raname = raname;
	}

	public String getIsoverdue() {
		return isoverdue;
	}

	public void setIsoverdue(String isoverdue) {
		this.isoverdue = isoverdue;
	}

	public String getTrxid() {
		return trxid;
	}

	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}

	public String getDatef() {
		return datef;
	}

	public void setDatef(String datef) {
		this.datef = datef;
	}

	public String getTimef() {
		return timef;
	}

	public void setTimef(String timef) {
		this.timef = timef;
	}

	public String getIsreg() {
		return isreg;
	}

	public void setIsreg(String isreg) {
		this.isreg = isreg;
	}

    public String getIsBvsReq() { return isBvsReq; }

    public void setIsBvsReq(String isBvsReq) {
        this.isBvsReq = isBvsReq;
    }

    public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsWalletExist() {
		return isWalletExist;
	}

	public void setIsWalletExist(String isWalletExist) {
		this.isWalletExist = isWalletExist;
	}

	public String getWalletNumber() {
		return walletNumber;
	}

	public void setWalletNumber(String walletNumber) {
		this.walletNumber = walletNumber;
	}

	public String getWalletBal() {
		return walletBal;
	}

	public void setWalletBal(String walletBal) {
		this.walletBal = walletBal;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getToBankIMD() {
		return toBankIMD;
	}

	public void setToBankIMD(String toBankIMD) {
		this.toBankIMD = toBankIMD;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCrDr() {
		return crDr;
	}

	public void setCrDr(String crDr) {
		this.crDr = crDr;
	}

	public String getFingerIndex() {
		return fingerIndex;
	}

	public void setFingerIndex(String fingerIndex) {
		this.fingerIndex = fingerIndex;
	}
}