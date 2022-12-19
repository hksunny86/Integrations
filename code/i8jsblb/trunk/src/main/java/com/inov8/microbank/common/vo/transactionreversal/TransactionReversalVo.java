package com.inov8.microbank.common.vo.transactionreversal;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Date;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 6, 2012 9:25:18 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@XmlRootElement(name="transactionReversalVo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class TransactionReversalVo extends BasePersistableModel implements Serializable
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5464442367325819451L;
	
	private Long transactionId;
    private String transactionCode;
    private Long transactionCodeId;
    private String btnName;
    private String comments;
    private String updatedBy;
    private Date updatedOn;
    private Integer redemptionType;
    private Long productId;
    private Boolean isFullReversal;
    //BISP Data
    private String baflTransactionNumber;
    private String baflSessionId;
    private String nadraSessionId;
    private String baflWalletId;

    public TransactionReversalVo()
    {
    }

    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }

    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId( Long transactionId )
    {
        this.transactionId = transactionId;
    }

    public String getTransactionCode()
    {
        return transactionCode;
    }

    public void setTransactionCode( String transactionCode )
    {
        this.transactionCode = transactionCode;
    }

    public Long getTransactionCodeId()
    {
        return transactionCodeId;
    }

    public void setTransactionCodeId( Long transactionCodeId )
    {
        this.transactionCodeId = transactionCodeId;
    }

    public String getBtnName()
    {
        return btnName;
    }

    public void setBtnName( String btnName )
    {
        this.btnName = btnName;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }

    public void setUpdatedBy( String updatedBy )
    {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public void setUpdatedOn( Date updatedOn )
    {
        this.updatedOn = updatedOn;
    }

	public Integer getRedemptionType() {
		return redemptionType;
	}

	public void setRedemptionType(Integer redemptionType) {
		this.redemptionType = redemptionType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Boolean getIsFullReversal() {
		return isFullReversal;
	}

	public void setIsFullReversal(Boolean isFullReversal) {
		this.isFullReversal = isFullReversal;
	}


    public String getBaflTransactionNumber() {
        return baflTransactionNumber;
    }

    public void setBaflTransactionNumber(String baflTransactionNumber) {
        this.baflTransactionNumber = baflTransactionNumber;
    }

    public String getBaflSessionId() {
        return baflSessionId;
    }

    public void setBaflSessionId(String baflSessionId) {
        this.baflSessionId = baflSessionId;
    }

    public String getNadraSessionId() {
        return nadraSessionId;
    }

    public void setNadraSessionId(String nadraSessionId) {
        this.nadraSessionId = nadraSessionId;
    }

    public String getBaflWalletId() {
        return baflWalletId;
    }

    public void setBaflWalletId(String baflWalletId) {
        this.baflWalletId = baflWalletId;
    }
}
