package com.inov8.microbank.common.util;

import java.util.HashMap;
import java.util.Map;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 17, 2013 2:57:29 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public enum ResendSmsButtonLabelEnum
{
    CASH_DEPOSIT(50002L, ResendSmsButtonLabelConstants.LABEL_AGENT, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    CASH_WITHDRAWAL(50006L, ResendSmsButtonLabelConstants.LABEL_AGENT, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    SALARY_DISBURSEMENT(2510733L, null, ResendSmsButtonLabelConstants.LABEL_CUSTOMER ),
    BULK_PAYMENT(2510801L, ResendSmsButtonLabelConstants.LABEL_RECIPIENT_AGENT, ResendSmsButtonLabelConstants.LABEL_CUSTOMER ),
    ACT_TO_ACT(50000L, ResendSmsButtonLabelConstants.LABEL_SENDER_CUSTOMER, ResendSmsButtonLabelConstants.LABEL_RECIPIENT_CUSTOMER),
    ACCOUNT_TO_CASH(50010L, ResendSmsButtonLabelConstants.LABEL_AGENT, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    CASH_TRANSFER(50011L, ResendSmsButtonLabelConstants.LABEL_AGENT, null),
    ZONG_TOPUP(2510727L, null, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    CUSTOMER_RETAIL_PAYMENT(50018L, ResendSmsButtonLabelConstants.LABEL_CUSTOMER, ResendSmsButtonLabelConstants.LABEL_AGENT),
    APOTHECARE_PAYMENT(2510793L, null, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    DAWAT_E_ISLAMI_ZAKAT_PAYMENT(2510794L, null, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),
    DAWAT_E_ISLAMI_SADQA_PAYMENT(2510796L, null, ResendSmsButtonLabelConstants.LABEL_CUSTOMER),    
    APOTHECARE(2510791L, ResendSmsButtonLabelConstants.LABEL_AGENT, null),
    DAWAT_E_ISLAMI_ZAKAT(2510795L, ResendSmsButtonLabelConstants.LABEL_AGENT, null),
    DAWAT_E_ISLAMI_SADQA(2510797L, ResendSmsButtonLabelConstants.LABEL_AGENT, null),
    AGENT_TO_AGENT_TRANSFER(50013L, ResendSmsButtonLabelConstants.LABEL_SENDER_AGENT, ResendSmsButtonLabelConstants.LABEL_RECIPIENT_AGENT),
    RSO_TO_AGENT_TRANSFER(50014L, ResendSmsButtonLabelConstants.LABEL_SENDER_AGENT, ResendSmsButtonLabelConstants.LABEL_RECIPIENT_AGENT),
    ML_TRANSFER_TO_RETAILER(2510798L, ResendSmsButtonLabelConstants.LABEL_BB_AGENT, ResendSmsButtonLabelConstants.LABEL_RETAILER),
    ML_TRANSFER_TO_CUSTOMER(2510800L, ResendSmsButtonLabelConstants.LABEL_BB_AGENT, ResendSmsButtonLabelConstants.LABEL_CUSTOMER );

    private ResendSmsButtonLabelEnum( Long productId, String initiatorLabel, String recipientLabel )
    {
        this.productId = productId;
        this.initiatorLabel = initiatorLabel;
        this.recipientLabel = recipientLabel;
    }

    private Long   productId;
    private String initiatorLabel;
    private String recipientLabel;

    private static Map<Long, ResendSmsButtonLabelEnum> productIdsAndLabelsMap;

    static
    {
        productIdsAndLabelsMap = new HashMap<>( values().length );
        for( ResendSmsButtonLabelEnum resendSmsButtonLabelEnum : values() )
        {
            productIdsAndLabelsMap.put( resendSmsButtonLabelEnum.getProductId(), resendSmsButtonLabelEnum );
        }
    }

    public static ResendSmsButtonLabelEnum getEnumByProductId(Long productId)
    {
        return productIdsAndLabelsMap.get( productId );
    }

    public Long getProductId()
    {
        return productId;
    }

    public String getInitiatorLabel()
    {
        return initiatorLabel;
    }

    public String getRecipientLabel()
    {
        return recipientLabel;
    }

}
