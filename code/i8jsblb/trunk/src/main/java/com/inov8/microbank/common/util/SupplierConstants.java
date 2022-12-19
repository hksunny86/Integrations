package com.inov8.microbank.common.util;

public interface SupplierConstants
{
  // --- Suppliers
  public static final String SUPPLIER_LESCO = "Lesco";

  // --- Lesco Services
  public static final String LESCO_GET_BILL_INFO = "GetBillInfo";
  public static final String LESCO_GET_BILL_INFO_RETURNEDVAL =
      "GetBillInfoReturnedVal";
  public static final String LESCO_BILL_PAYMENT = "BillPayment";
  public static final String LESCO_BILL_PAYMENT_RETURNEDVAL =
      "BillPaymentReturnedVal";

  // --- Invoice Type
  public static final String DEBIT_INVOICE = "Debit";
  public static final String DEBIT_CREDIT_INVOICE = "DebitCredit";

  // --- Payment Type
  public static final String PAYMENT_TYPE_DEBIT = "Debit";
  public static final String PAYMENT_TYPE_CREDIT = "Credit";
  public static final String PAYMENT_TYPE_CASH = "Cash";
  
  // --- TransReportPhonix CSR Supplier  
  public static final long TransReportPhonixCSRViewSupplierID = 50063L  ;

  public static final Long APOTHECARE_SUPPLIER_ID = 50274L;
  public static final Long DAWAT_E_ISLAMI_SUPPLIER_ID = 50275L;
  public static final Long TELCO_SUPPLIER_ID = 50067L;

  Long BRANCHLESS_BANKING_SUPPLIER = 50065L;
  String BRANCHLESS_BANKING_SUPPLIER_NAME = "Branchless Banking Supplier";
}
