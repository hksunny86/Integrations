package com.inov8.microbank.common.util;

import org.apache.commons.validator.GenericValidator;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 13, 2013 11:09:03 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public final class ProductUtils
{
    public static boolean isBillPaymentByCustomer(Long supplierId, String agent1Id)
    {
        boolean isBillPaymentByCustomer = false;

        if( supplierId != null && SupplierConstants.TransReportPhonixCSRViewSupplierID == supplierId.longValue() && GenericValidator.isBlankOrNull( agent1Id ) )
        {
            isBillPaymentByCustomer = true;
        }
        return isBillPaymentByCustomer;
    }

    public static boolean isBillPaymentByAgent(Long supplierId, String agent1Id)
    {
        boolean isBillPaymentByAgent = false;

        if( supplierId != null && SupplierConstants.TransReportPhonixCSRViewSupplierID == supplierId.longValue()
                && !GenericValidator.isBlankOrNull( agent1Id ) )
        {
            isBillPaymentByAgent = true;
        }
        return isBillPaymentByAgent;
    }
}
