package com.inov8.jsblconsumer.model;

import java.io.Serializable;

public class TransactionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public String msgType;
    public String apid;
    public String code;
    public String mobNo;
    public String descr;
    public String type;
    public String prod;
    public String bcode;
    public String supp;
    public String pmode;
    public String suppHlpLine;
    public boolean flag = false;
    public String cscd;
    public String agent1CommAmt;
    public String agent1Bal;
    public String swmob;
    public String rwmob;
    public String wcmob;
    public String cnic;
    public String swcnic;
    public String rwcnic;
    public String senderCnic;
    public String bamt;
    private String id;
    private String amob;
    private String ramob;
    private String racnic;
    private String raname;
    private String cmob;
    private String rcmob;
    private String tmob;
    private String txam;
    private String txamf;
    private String tpam;
    private String tpamf;
    private String camt;
    private String camtf;
    private String tamt;
    private String tamtf;
    private String balf;
    private String bbacid;
    private String coreacid;
    private String coreactl;
    private String actitle;
    private String bname;
    private String cpname;
    private String cardno;
    private String cname;
    private String ctname;
    private String crname;
    private String mobn;
    private String accno;
    private String cnumber;
    private String ica;
    private String productName;
    private String consumer;
    private String date;
    private String datef;
    private String timef;

    public TransactionModel() {

    }

    public TransactionModel(String code, String mNo, String cscd, String dt,
                            String type, String prod, String datef, String timef, String amt,
                            String amtf, String bcode, String supp, String pmode,
                            String suppHlpLine, String tpam, String tpamf, String tamt,
                            String tamtf) // , String descr)
    {
        super();
        this.code = code;
        this.mobNo = mNo;
        this.cscd = cscd;
        this.date = dt;
        this.type = type;
        this.prod = prod;
        this.datef = datef;
        this.timef = timef;
        this.txam = amt;
        this.txamf = amtf;
        this.bcode = bcode;
        this.supp = supp;
        this.pmode = pmode;
        this.suppHlpLine = suppHlpLine;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;

        if (tamtf != null && tpamf != null && !tamtf.equals("0.00")
                && !tpamf.equals("0.00"))
            flag = true;
    }

    /**
     * For Domestic Remittance Leg1
     *
     * @param code
     * @param mNo
     * @param cscd
     * @param senderMSISDN
     * @param receiverMSISDN
     * @param senderCNIC
     * @param receiverCNIC
     * @param dt
     * @param type
     * @param prod
     * @param datef
     * @param timef
     * @param amt
     * @param amtf
     * @param bcode
     * @param supp
     * @param pmode
     * @param suppHlpLine
     * @param tpam
     * @param tpamf
     * @param tamt
     * @param tamtf
     */
    public TransactionModel(String code, String mNo, String cscd,
                            String senderMSISDN, String receiverMSISDN, String senderCNIC,
                            String receiverCNIC, String dt, String type, String prod,
                            String datef, String timef, String amt, String amtf, String bcode,
                            String supp, String pmode, String suppHlpLine, String tpam,
                            String tpamf, String tamt, String tamtf) // , String descr)
    {
        super();
        this.code = code;
        this.swmob = senderMSISDN;
        this.rwmob = receiverMSISDN;
        this.swcnic = senderCNIC;
        this.rwcnic = receiverCNIC;
        this.mobNo = mNo;
        this.cscd = cscd;
        this.date = dt;
        this.type = type;
        this.prod = prod;
        this.datef = datef;
        this.timef = timef;
        this.txam = amt;
        this.txamf = amtf;
        this.bcode = bcode;
        this.supp = supp;
        this.pmode = pmode;
        this.suppHlpLine = suppHlpLine;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;

        if (tamtf != null && tpamf != null && !tamtf.equals("0.00")
                && !tpamf.equals("0.00"))
            flag = true;
    }

    /**
     * For Credit Recharge and Transfer
     *
     * @param code
     * @param apid
     * @param dt
     * @param type
     * @param datef
     * @param timef
     * @param amt
     * @param amtf
     */
    public TransactionModel(String code, String apid, String dt, String type,
                            String datef, String timef, String amt, String amtf, String msgType) {
        super();
        this.code = code;
        this.apid = apid;
        this.date = dt;
        this.type = type;
        this.datef = datef;
        this.timef = timef;
        this.txam = amt;
        this.txamf = amtf;
        this.msgType = msgType;
    }

    /**
     * Transaction Sales Summary
     *
     * @param prod
     * @param amtf
     */
    public TransactionModel(String prod, String amt, String amtf) {
        super();
        this.prod = prod;
        this.txam = amtf;
        this.txamf = amt;
    }

    public TransactionModel(String code, String date, String datef, String timef,
                            String amt, String amtf, String agent1Bal) {
        super();
        this.code = code;
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        this.txam = amtf;
        this.txamf = amt;
        this.agent1Bal = agent1Bal;
    }

    /*
     *
     *  mini statement
     */
    public TransactionModel(String date, String descr, String datef, String amt,
                            String amtf) {
        super();
        this.date = date;
        this.descr = descr;
        this.datef = datef;
        this.tamt = amt;
        this.tamtf = amtf;
    }

    /**
     * For Cash Withdrawal Transaction - Leg2
     */
    public TransactionModel(String code, String mNo, String dt, String type,
                            String prod, String datef, String timef, String amt, String amtf,
                            String bcode, String supp, String pmode, String suppHlpLine,
                            String tpam, String tamt, String camt, String bamt, String a1camt,
                            String a1bal) {
        super();
        this.code = code;
        this.mobNo = mNo;
        this.date = dt;
        this.type = type;

        this.prod = prod;
        this.datef = datef;
        this.timef = timef;
        this.txam = amtf;
        this.txamf = amt;
        this.bcode = bcode;
        this.supp = supp;
        this.pmode = pmode;
        this.suppHlpLine = suppHlpLine;

        this.tpam = tpam;
        this.tamt = tamt;
        this.camt = camt;
        this.bamt = bamt;

        this.agent1CommAmt = a1camt;
        this.agent1Bal = a1bal;
    }

    /***
     * Retail payment
     */

    public TransactionModel(String date, String datef, String timef, String tRXID,
                            String aMOB, String pROD, String cAMT, String cAMTF, String tPAM,
                            String tPAMF, String tAMT, String tAMTF, String bALF) {
        super();
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        id = tRXID;
        amob = aMOB;
        prod = pROD;
        camt = cAMT;
        camtf = cAMTF;
        tpam = tPAM;
        tpamf = tPAMF;
        tamt = tAMT;
        tamtf = tAMTF;
        balf = bALF;
    }

    /***
     * funds transfer
     */

    public TransactionModel(String date, String datef, String timef, String tRXID,
                            String aMOB, String bbacid, String coreacid, String pROD, String cAMT, String cAMTF, String tPAM,
                            String tPAMF, String tAMT, String tAMTF, String bALF) {
        super();
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        id = tRXID;
        amob = aMOB;
        this.bbacid = bbacid;
        this.coreacid = coreacid;
        prod = pROD;
        camt = cAMT;
        camtf = cAMTF;
        tpam = tPAM;
        tpamf = tPAMF;
        tamt = tAMT;
        tamtf = tAMTF;

        balf = bALF;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getWcmob() {
        return wcmob;
    }

    public void setWcmob(String wcmob) {
        this.wcmob = wcmob;
    }

    public String getSenderCnic() {
        return senderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        this.senderCnic = senderCnic;
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

    public String getTmob() {
        return tmob;
    }

    public void setTmob(String tmob) {
        this.tmob = tmob;
    }

    /***
     * Bill payment
     */


    public void setMiniLoadTransaction(String id, String prod, String tmob, String amob,
                                       String date, String datef, String timef, String txamf, String camt,
                                       String camtf, String tpam, String tpamf, String tamt, String tamtf,
                                       String balf) {
        this.id = id;
        this.prod = prod;
        this.tmob = tmob;
        this.amob = amob;
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        this.txamf = txamf;
        this.camt = camt;
        this.camtf = camtf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.balf = balf;
    }



    public void setBillPaymentTransaction(String TRXID, String datef, String timef, String tRXID,
                                          String cMOB, String pROD, String cAMT, String cAMTF, String tPAM,
                                          String tPAMF, String tAMT, String tAMTF, String bALF) {
        this.date = TRXID;
        this.datef = datef;
        this.timef = timef;
        this.id = tRXID;
        this.cmob = cMOB;
        this.prod = pROD;
        this.camt = cAMT;
        this.camtf = cAMTF;
        this.tpam = tPAM;
        this.tpamf = tPAMF;
        this.tamt = tAMT;
        this.tamtf = tAMTF;
        this.balf = bALF;
    }

    public void setCollectionPaymentTransaction(String tRXID, String apid, String prod, String consumer,
                                          String txam, String txamf, String tpam, String tpamf, String tamt,
                                          String tamtf, String mobNo, String cnic,String datef,String balf) {
        this.id = tRXID;
        this.apid = apid;
        this.prod = prod;
        this.consumer = consumer;
        this.txam = txam;
        this.txamf = txamf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.mobNo = mobNo;
        this.cnic =cnic ;
        this.datef = datef;
        this.balf = balf;

    }
//	public void setMiniLoadTransaction(String tRXID, String prod, String tmob,
//									   String txam, String txamf, String cAMT, String cAMTF, String tPAM,
//									   String tPAMF, String tAMT, String tAMTF, String date, String datef,
//									   String timef, String bALF) {
//
//		this.id = tRXID;
//		this.prod = prod;
//		this.txam = txam;
//		this.tmob = tmob;
//		this.txamf = txamf;
//		this.camt = cAMT;
//		this.camtf = cAMTF;
//		this.tpam = tPAM;
//		this.tpamf = tPAMF;
//		this.tamt = tAMT;
//		this.tamtf = tAMTF;
//		this.date = date;
//		this.datef = datef;
//		this.timef = timef;
//		this.balf = bALF;
//	}

//    public void setMiniLoadTransaction(String txam, String txamf, String camt, String camtf,
//                                       String tpam, String tpamf, String tamt, String tamtf,
//                                       String prod, String trxid, String date, String datef,
//                                       String timef, String balf, String mNo) {
//
//        this.camt = camt;
//        this.camtf = camtf;
//        this.id = trxid;
//        this.mobNo = mNo;
//        this.date = date;
//        this.datef = datef;
//        this.timef = timef;
//        this.prod = prod;
//        this.txam = txam;
//        this.txamf = txamf;
//        this.tpam = tpam;
//        this.tpamf = tpamf;
//        this.tamt = tamt;
//        this.tamtf = tamtf;
//        this.balf = balf;
//    }

    public void ReceiveMoneySenderRedeem(String trxid, String swcnic, String swmob, String rwcnic,
                                         String rwmob, String date, String datef, String timef, String prod, String tamt, String tamtf, String balf) {
        this.id = trxid;
        this.swcnic = swcnic;
        this.swmob = swmob;
        this.rwcnic = rwcnic;
        this.rwmob = rwmob;
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        this.prod = prod;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.balf = balf;
    }

    public void ReceiveMoneyPendingTrx(String trxid, String swcnic, String swmob, String rwcnic,
                                       String rwmob, String date, String datef, String timef, String prod, String camt, String camtf,
                                       String tpam, String tpamf, String tamt, String tamtf, String txam, String txamf, String balf) {
        this.id = trxid;
        this.swcnic = swcnic;
        this.swmob = swmob;
        this.rwcnic = rwcnic;
        this.rwmob = rwmob;
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        this.prod = prod;
        this.camt = camt;
        this.camtf = camtf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.txam = txam;
        this.txamf = txamf;
        this.balf = balf;
    }

    public void ReceiveMoneyReceiveCash(String trxid, String swcnic, String swmob, String rwcnic,
                                        String rwmob, String date, String datef, String timef, String prod, String camt, String camtf,
                                        String tpam, String tpamf, String tamt, String tamtf, String txam, String txamf, String balf) {
        this.id = trxid;
        this.swcnic = swcnic;
        this.swmob = swmob;
        this.rwcnic = rwcnic;
        this.rwmob = rwmob;
        this.date = date;
        this.datef = datef;
        this.timef = timef;
        this.prod = prod;
        this.camt = camt;
        this.camtf = camtf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.txam = txam;
        this.txamf = txamf;
        this.balf = balf;
    }

    /***
     * Debit Card Issuance Transaction
     */
    public void setDebitCardIssunaceTransaction(String id, String prod,
                                                String acctitle, String bname, String txam, String txamf,
                                                String camt, String camtf, String tpam, String tpamf, String tamt,
                                                String tamtf, String datef, String balf, String ica, String cname,
                                                String ctname, String mobn) {
        this.id = id;
        this.prod = prod;
        this.actitle = acctitle;
        this.bname = bname;
        this.txam = txam;
        this.txamf = txamf;
        this.camt = camt;
        this.camtf = camtf;
        this.tpam = tpam;
        this.tpamf = tpamf;
        this.tamt = tamt;
        this.tamtf = tamtf;
        this.datef = datef;
        this.balf = balf;
        this.ica = ica;
        this.cname = cname;
        this.ctname = ctname;
        this.mobn = mobn;
    }

    /**
     * Debit Card Activation
     */
    public void setDebitCardActivation(String mobn, String cnic, String actitle, String cardno,
                                       String cpname, String datef) {
        this.mobn = mobn;
        this.cnic = cnic;
        this.actitle = actitle;
        this.cardno = cardno;
        this.cpname = cpname;
        this.datef = datef;
    }

    /**
     * For Transaction Log
     *
     * @param creditTransfer
     * @return
     */
    public String[][] transactionSummary(boolean creditTransfer) {
        short sumLength = 9;
        short rowIdx = -1;

        if (creditTransfer)
            sumLength = 4;
        String tranSummary[][] = new String[sumLength][2];

        if (code != null) {
            tranSummary[++rowIdx][0] = "Transaction code:";
            tranSummary[rowIdx][1] = code;
        }
        if (mobNo != null) {
            tranSummary[++rowIdx][0] = "Mobile:";
            tranSummary[rowIdx][1] = mobNo;
        }
        if (datef != null && timef != null) {
            tranSummary[++rowIdx][0] = "Transaction date:";
            tranSummary[rowIdx][1] = datef + " " + timef;
        }
        if (txamf != null) {
            tranSummary[++rowIdx][0] = creditTransfer ? "Credit transferred:"
                    : "Amount paid:";
            tranSummary[rowIdx][1] = "PKR " + txamf;
        }
        if (!creditTransfer) {
            if (supp != null && !"".equals(supp)) {
                tranSummary[++rowIdx][0] = "Supplier:";
                tranSummary[rowIdx][1] = supp;
            }
            if (prod != null && !"".equals(prod)) {
                tranSummary[++rowIdx][0] = "Product:";
                tranSummary[rowIdx][1] = prod;
            }
            if (pmode != null && !"".equals(pmode)) {
                tranSummary[++rowIdx][0] = "Paid using:";
                tranSummary[rowIdx][1] = pmode;
            }
            if (bcode != null && !"".equals(bcode)) {
                tranSummary[++rowIdx][0] = "Auth code:";
                tranSummary[rowIdx][1] = bcode;
            }
            if (suppHlpLine != null && !"".equals(suppHlpLine)) {
                tranSummary[++rowIdx][0] = "For Help:";
                tranSummary[rowIdx][1] = suppHlpLine;
            }
        }
        return tranSummary;
    }

    public String getCnic() {
        return cnic;
    }

    public String getCardNo() {
        return cardno;
    }

    public String getCpname() {
        return cpname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getApid() {
        return apid;
    }

    public void setApid(String apid) {
        this.apid = apid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getSupp() {
        return supp;
    }

    public void setSupp(String supp) {
        this.supp = supp;
    }

    public String getPmode() {
        return pmode;
    }

    public void setPmode(String pmode) {
        this.pmode = pmode;
    }

    public String getSuppHlpLine() {
        return suppHlpLine;
    }

    public void setSuppHlpLine(String suppHlpLine) {
        this.suppHlpLine = suppHlpLine;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getCscd() {
        return cscd;
    }

    public void setCscd(String cscd) {
        this.cscd = cscd;
    }

    public String getAgent1CommAmt() {
        return agent1CommAmt;
    }

    public void setAgent1CommAmt(String agent1CommAmt) {
        this.agent1CommAmt = agent1CommAmt;
    }

    public String getAgent1Bal() {
        return agent1Bal;
    }

    public void setAgent1Bal(String agent1Bal) {
        this.agent1Bal = agent1Bal;
    }

    public String getAmob() {
        return amob;
    }

    public void setAmob(String amob) {
        this.amob = amob;
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

    public String getSwmob() {
        return swmob;
    }

    public void setSwmob(String swmob) {
        this.swmob = swmob;
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

    public String getRwcnic() {
        return rwcnic;
    }

    public void setRwcnic(String rwcnic) {
        this.rwcnic = rwcnic;
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

    public String getBamt() {
        return bamt;
    }

    public void setBamt(String bamt) {
        this.bamt = bamt;
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

    public String getBalf() {
        return balf;
    }

    public void setBalf(String balf) {
        this.balf = balf;
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

    public String getActitle() {
        return actitle;
    }

    public void setActitle(String actitle) {
        this.actitle = actitle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getCnumber() {
        return cnumber;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
    }
}