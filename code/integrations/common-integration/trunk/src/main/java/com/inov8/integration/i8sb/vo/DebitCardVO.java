package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.joda.time.DateTime;
import java.io.Serializable;

@XStreamAlias("DebitCard")
public class DebitCardVO implements Serializable
{
    private String relationShipNo;
    private String accountNo;
    private String cardEmborsingName;
    private String cardTypeCode;
    private String cardProdCode;
    private String cardBranchCode;
    private String primaryCNIC;
    private String issuedDate;
    private String requestType;

    public String getRelationShipNo() {
        return relationShipNo;
    }

    public void setRelationShipNo(String relationShipNo) {
        this.relationShipNo = relationShipNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCardEmborsingName() {
        return cardEmborsingName;
    }

    public void setCardEmborsingName(String cardEmborsingName) {
        this.cardEmborsingName = cardEmborsingName;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardProdCode() {
        return cardProdCode;
    }

    public void setCardProdCode(String cardProdCode) {
        this.cardProdCode = cardProdCode;
    }

    public String getCardBranchCode() {
        return cardBranchCode;
    }

    public void setCardBranchCode(String cardBranchCode) {
        this.cardBranchCode = cardBranchCode;
    }

    public String getPrimaryCNIC() {
        return primaryCNIC;
    }

    public void setPrimaryCNIC(String primaryCNIC) {
        this.primaryCNIC = primaryCNIC;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

}
