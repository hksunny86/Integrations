package com.inov8.microbank.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Dec 7, 2013 9:55:07 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */

@Entity
@Table(name = "AGENT_DAILY_TX_STATS_VIEW")
public class AgentDailyTxStatsViewModel extends BasePersistableModel implements Serializable
{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1225374872068841274L;

    private String userId;
	private Long appUserId;
	private String mobileNo;
	private Double totalTx;
	private Double p2pSent;
	private Double p2pRecieved;
	private Double p2pSentAmount;
	private Double p2pRecAmount;
	private Double p2pSentCom;
	private Double p2pRecCom;
	private Double billPayment;
	private Double billPaymentAmount;
	private Double billPaymentCom;
	private Double others;
	private Double othersAmount;
	private Double othersCom;

    /** default constructor */
    public AgentDailyTxStatsViewModel()
    {
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getAppUserId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "appUserId";
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&appUserId="+getAppUserId();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setAppUserId( primaryKey );
    }


    @Column( name = "USER_ID" )
    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    @Id
    @Column( name = "APP_USER_ID" )
    public Long getAppUserId()
    {
        return this.appUserId;
    }

    public void setAppUserId( Long appUserId )
    {
        this.appUserId = appUserId;
    }

    @Column( name = "MOBILE_NO" )
    public String getMobileNo()
    {
        return this.mobileNo;
    }

    public void setMobileNo( String mobileNo )
    {
        this.mobileNo = mobileNo;
    }

    @Column( name = "TOTAL_TX" )
    public Double getTotalTx()
    {
        return this.totalTx;
    }

    public void setTotalTx( Double totalTx )
    {
        this.totalTx = totalTx;
    }

    @Column( name = "P2P_SENT" )
    public Double getP2pSent()
    {
        return this.p2pSent;
    }

    public void setP2pSent( Double p2pSent )
    {
        this.p2pSent = p2pSent;
    }

    @Column( name = "P2P_RECIEVED" )
    public Double getP2pRecieved()
    {
        return this.p2pRecieved;
    }

    public void setP2pRecieved( Double p2pRecieved )
    {
        this.p2pRecieved = p2pRecieved;
    }

    @Column( name = "P2P_SENT_AMOUNT" )
    public Double getP2pSentAmount()
    {
        return this.p2pSentAmount;
    }

    public void setP2pSentAmount( Double p2pSentAmount )
    {
        this.p2pSentAmount = p2pSentAmount;
    }

    @Column( name = "P2P_REC_AMOUNT" )
    public Double getP2pRecAmount()
    {
        return this.p2pRecAmount;
    }

    public void setP2pRecAmount( Double p2pRecAmount )
    {
        this.p2pRecAmount = p2pRecAmount;
    }

    @Column( name = "P2P_SENT_COM" )
    public Double getP2pSentCom()
    {
        return this.p2pSentCom;
    }

    public void setP2pSentCom( Double p2pSentCom )
    {
        this.p2pSentCom = p2pSentCom;
    }

    @Column( name = "P2P_REC_COM" )
    public Double getP2pRecCom()
    {
        return this.p2pRecCom;
    }

    public void setP2pRecCom( Double p2pRecCom )
    {
        this.p2pRecCom = p2pRecCom;
    }

    @Column( name = "BILL_PAYMENT" )
    public Double getBillPayment()
    {
        return this.billPayment;
    }

    public void setBillPayment( Double billPayment )
    {
        this.billPayment = billPayment;
    }

    @Column( name = "BILL_PAYMENT_AMOUNT" )
    public Double getBillPaymentAmount()
    {
        return this.billPaymentAmount;
    }

    public void setBillPaymentAmount( Double billPaymentAmount )
    {
        this.billPaymentAmount = billPaymentAmount;
    }

    @Column( name = "BILL_PAYMENT_COM" )
    public Double getBillPaymentCom()
    {
        return this.billPaymentCom;
    }

    public void setBillPaymentCom( Double billPaymentCom )
    {
        this.billPaymentCom = billPaymentCom;
    }

    @Column( name = "OTHERS" )
    public Double getOthers()
    {
        return this.others;
    }

    public void setOthers( Double others )
    {
        this.others = others;
    }

    @Column( name = "OTHERS_AMOUNT" )
    public Double getOthersAmount()
    {
        return this.othersAmount;
    }

    public void setOthersAmount( Double othersAmount )
    {
        this.othersAmount = othersAmount;
    }

    @Column( name = "OTHERS_COM" )
    public Double getOthersCom()
    {
        return this.othersCom;
    }

    public void setOthersCom( Double othersCom )
    {
        this.othersCom = othersCom;
    }

}