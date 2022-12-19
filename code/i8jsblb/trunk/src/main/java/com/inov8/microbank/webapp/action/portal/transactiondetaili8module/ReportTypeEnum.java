package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 4, 2012 5:50:59 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public enum ReportTypeEnum
{
    ACCOUNT_TO_ACCOUNT( "A/C to A/C Transfer Report", "p_marketingaccounttoaccount", 50000L ),
    ACCOUNT_TO_CNIC( "A/C to CNIC Transfer Report", "p_marketingaccounttocnic", 50010L ),
    CNIC_TO_CNIC( "CNIC to CNIC Transfer Report", "p_marketingcnictocnic", 50011L ),
    P_TO_P( "P2P Receiving Status Report", "p_p2ptransactiondetail", 50011L ),
    BALANCE_TOP_UP( "Balance Top Up Report", "p_marketingbalancetopup", 2510727L ),
    CASH_DEPOSIT( "Cash IN Report", "p_marketingcashdeposit", 50002L ),
    CASH_WITHDRAWAL( "Cash OUT Report", "p_marketingcashwithdrawal", 50006L ),
    TRANSACTIONAL_REVENUE( "Transactional Revenue Report", "p_marketingtransactionalrevenue", null ),
    CUSTOMER_RETAIL_PAYMENT("Customer Retail Payment Report","p_marketingcustomerretailpayment",50031L);// Added By Hassan Javaid on 

    ReportTypeEnum( String title, String view, Long productId )
    {
        this.title = title;
        this.view = view;
        this.productId = productId;
    }

    private String title;

    private String view;

    private Long productId;

    public String getTitle()
    {
        return title;
    }

    public String getView()
    {
        return view;
    }

    public Long getProductId()
    {
        return productId;
    }

}
