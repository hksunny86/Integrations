package com.inov8.jsblconsumer.model;

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
	private String cnic;
	private String rwcnic;
	private String rwmob;
	private String swcnic;
	private String swmob;
	private String ramob;
	private String type;
	private String prod;
	private String expiry;
	private String bankName;
	private String branchName;
	private String iban;
	private String crDr;


	public String getProd() {
		return prod;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public String getTmob() {
		return tmob;
	}

	public void setTmob(String tmob) {
		this.tmob = tmob;
	}

	private String tmob;
	private String racnic;
	private String raname;
	private String isoverdue;
	private String trxid;
	private String datef;
	private String timef;
	private String isreg;
	private String cname;
	private String cid;
	private String ctname;
	private String ctid;
	private String crname;
	private String crid;
	private String catname;
	private String catid;
	private String mobn;
	private String actitle;
	private String bcode;
	private String bname;
	private String cnumber;
	private String date;

	public  void setProd(String prod){this.prod = prod;}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	private static TransactionInfoModel transactionInfo = null;

	private TransactionInfoModel() {
	}

	public static TransactionInfoModel getInstance() {
		if (transactionInfo == null) {
			transactionInfo = new TransactionInfoModel();
		}
		return transactionInfo;
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
	 * Cash In & Out for Consumer
	 */
	public void CashInOutConsumer(String txam,
						  String txamf, String tpam, String tpamf, String camt, String camtf,
						  String tamt, String tamtf) {
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
	 * Bill Inquiry
	 */

	public void MiniLoadInfo(String prod, String pname, String tmob,
							String txam, String txamf, String tpam, String tpamf,
							String tamt, String tamtf, String camt, String camtf) {
		this.prod = prod;
		this.pname = pname;
		this.tmob = tmob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.camt = camt;
		this.camtf = camtf;
	}

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
	public void BookmePaymentInquiry(String cmob, String pname,
							String bamt, String bamtf, String tpam, String camt,
							String tamt) {
		this.cmob = cmob;
		this.pname = pname;
		this.bamt = bamt;
		this.bamtf = bamtf;
		this.tpam = tpam;
		this.camt = camt;
		this.tamt = tamt;
	}


	public void CollectionInquiry(String pid, String prod, String consumer,
							 String txam, String txamf,String tpam, String tpamf, String tamt,
							String tamtf, String cmob, String cnic, String duedate) {
		this.pid = pid;
		this.prod = prod;
		this.consumer = consumer;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.cmob = cmob;
		this.cnic = cnic;
		this.duedate = duedate;
	}
	// Mini Load Info

	public void setTopUpInfo(String prod, String mob, String tXAM, String tXAMf,
							 String cAMT, String cAMTF, String tPAM, String tPAMF, String tAMT, String tAMTF) {

		this.prod = prod;
		mobn = mob;
		txam = tXAM;
		txamf = tXAMf;
		camt = cAMT;
		camtf = cAMTF;
		tpam = tPAM;
		tpamf = tPAMF;
		tamt = tAMT;
		tamtf = tAMTF;
	}

	public void setCashWithdrawalInfo(String date, String datef, String timef, String trxid, String expiry) {

		this.date = date;
		this.datef = datef;
		this.timef = timef;
		this.trxid = trxid;
		this.expiry = expiry;
	}

	/**
	 * Retail Payment
	 */
	public void RetailPayment(String amob, String txam, String txamf,
			String tpam, String tpamf, String camt, String camtf, String tamt,
			String tamtf, String raname) {
		this.amob = amob;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = camtf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.raname = raname;
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

	/**
	 * Funds Transfer BLB2BLB Info
	 */
	public void FundsTransferBlb2Blb(String rcmob, String txam,
			String txamf, String tpam, String tpamf, String camt, String camtf,
			String tamt, String tamtf, String pid) {
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
	public void FundsTransferBlb2Cnic(String rwmob, String rwcnic,
			String txam, String txamf, String tpam, String tpamf, String camt,
			String camtf, String tamt, String tamtf, String pid) {
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

	public void setPayCashBulkInfo(String tPAM, String tPAMF, String tAMT,
								   String tAMTF, String tXAM, String tXAMf, String cNAME, String cNIC,
								   String cMob, String txid, String date, String datef, String timef,
								   String type) {
		tpam = tPAM;
		tpamf = tPAMF;
		tamt = tAMT;
		tamtf = tAMTF;
		txam = tXAM;
		txamf = tXAMf;
		cname = cNAME;
		cnic = cNIC;
		cmob = cMob;
		this.trxid = txid;
		this.setDate(date);
		this.setDatef(datef);
		this.setTimef(timef);
		this.setType(type);
	}


	/**
	 * Funds Transfer BLB2Core Info
	 */
	public void FundsTransferBlb2Core(String rcmob,
			String coreacid, String coreactl, String txam, String txamf,
			String tpam, String tpamf, String camt, String camtf, String tamt,
			String tamtf, String pid) {
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

	public void FundsTransferBlb2Ibft(String rcmob, String coreacid, String coreactl, String txam,
									   String txamf, String tpam, String tpamf, String camt, String camtf,
									   String tamt, String tamtf, String pid, String prod,
									  String bankName,String branchName,String iban,String crDr) {
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
		this.prod = prod;
		this.bankName = bankName;
		this.branchName = branchName;
		this.iban = iban;
		this.crDr = crDr;
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

	public void setDebitCardActivationInfo(String cname, String ctname,
										   String crname, String mobn, String actitle, String cnumber,
										   String trxid) {
		this.cname = cname;
		this.ctname = ctname;
		this.crname = crname;
		this.mobn = mobn;
		this.actitle = actitle;
		this.cnumber = cnumber;
		this.trxid = trxid;
	}

	public void setDebitCardIssuanceInfo(String cname, String cid,
										 String ctname, String ctid, String crname, String crid,
										 String catname, String catid, String mobn, String actitle,
										 String cnumber, String txam, String txamf, String tpam,
										 String tpamf, String camt, String cmatf, String tamt, String tamtf,
										 String bcode, String bname, String trxid) {
		this.cname = cname;
		this.cid = cid;
		this.ctname = ctname;
		this.ctid = ctid;
		this.crname = crname;
		this.crid = crid;
		this.catname = catname;
		this.catid = catid;
		this.mobn = mobn;
		this.actitle = actitle;
		this.cnumber = cnumber;
		this.txam = txam;
		this.txamf = txamf;
		this.tpam = tpam;
		this.tpamf = tpamf;
		this.camt = camt;
		this.camtf = cmatf;
		this.tamt = tamt;
		this.tamtf = tamtf;
		this.bcode = bcode;
		this.bname = bname;
		this.trxid = trxid;
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

	public String getCtname() {
		return ctname;
	}

	public void setCtname(String ctname) {
		this.ctname = ctname;
	}

	public String getCrname() {
		return crname;
	}

	public void setCrname(String crname) {
		this.crname = crname;
	}

	public String getMobn() {
		return mobn;
	}

	public void setMobn(String mobn) {
		this.mobn = mobn;
	}

	public String getActitle() {
		return actitle;
	}

	public void setActitle(String actitle) {
		this.actitle = actitle;
	}

	public String getCnumber() {
		return cnumber;
	}

	public void setCnumber(String cnumber) {
		this.cnumber = cnumber;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getCrid() {
		return crid;
	}

	public void setCrid(String crid) {
		this.crid = crid;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getBcode() {
		return bcode;
	}

	public void setBcode(String bcode) {
		this.bcode = bcode;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public String getExpiry() {
		return expiry;
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
}