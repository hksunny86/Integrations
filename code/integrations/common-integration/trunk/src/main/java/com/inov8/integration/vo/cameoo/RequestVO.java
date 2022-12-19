package com.inov8.integration.vo.cameoo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Muhammad Ali on 12/14/2016.
 */
public class RequestVO implements Serializable {


    @Min(value = 1, message = "CardID is required", groups = {CameooValidations.ValidateCardID.class, CameooValidations.ValidateCardRequest.class})
    private int cardID;
    @NotNull(message = "ReferenceID cannot be null", groups = CameooValidations.ValidateReferenceID.class)
    private String referenceID;
    @NotNull(message = "TransactionID cannot be null", groups = CameooValidations.ValidateCardRequest.class)
    private String transactionID;
    @Min(value = 1, message = "Quantity is required", groups = CameooValidations.ValidateCardRequest.class)
    private int quantity;


    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public interface CameooValidations {


        interface ValidateCardID {
            // validation group marker interface
        }

        interface ValidateCardRequest {
            // validation group marker interface
        }

        interface ValidateReferenceID {
            // validation group marker interface
        }
    }
}
