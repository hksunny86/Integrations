package com.inov8.integration.i8sb.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum I8SBKeysOfCollectionEnum {
    MiniStatement("MiniStatement"),
    UserAccounts("UserAccounts"),
    CheckingAccountSummary("CheckingAccountSummary"),
    LoanAccountSummary("LoanAccountSummary"),
    TDRAccountSummary("TDRAccountSummary"),
    Card("Card"),
    LinkedAcc("LinkedAcc"),
    Transaction("Transaction"),
    BeneficiaryAcct("BeneficiaryAcct"),
    IBFTBenAcc("IBFTBenAcc"),
    ConsumerData("ConsumerData"),
    Instrument("Instrument"),
    StopCheque("StopCheque"),
    CreditCard("CreditCard"),
    PrepaidCard("PrepaidCard"),
    CCMiniStmt("CCMiniStmt"),
    LoanAccountTransaction("LoanAccountTransaction"),
    TDRAccountTransaction("TDRAccountTransaction"),
    SIFUNDSTRANFER("SIFUNDSTRANFER"),
    SIBILLPAYMENT("SIBILLPAYMENT"),
    SIIBFT("SIIBFT"),
    Branch("Branch"),
    Bank("Bank"),
    UBPSCompany("UBPSCompany"),
    Customer("Customer"),
    DebitCard("DebitCard"),
    Account("Account"),
    CustomerAccount("CustomerAccount"),
    DebitCardCharges("DebitCardCharges"),
    GetManualTransactionWindow("GetManualTransactionWindow"),
    SIView("SIView"),
    SIExecution("SIExecution"),
    DebitCardStatus("DebitCardStatus"),
    DebitCardEmpowerment("DebitCardEmpowerment"),
    BeneficiaryHistory("TransactionHistory"),
    VisionDebitCard("VisionDebitCard");

    /**
     * A Map to hold all Enum values used for reverse lookup.
     */
    private static final Map<String, I8SBKeysOfCollectionEnum> lookup = new HashMap<String, I8SBKeysOfCollectionEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet, 
     * that "probably" (according to the javadocs) has better performance than java.util.HashSet. 
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of 
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for (I8SBKeysOfCollectionEnum keysOfCollectionEnum : EnumSet.allOf(I8SBKeysOfCollectionEnum.class))
            lookup.put(keysOfCollectionEnum.getValue(), keysOfCollectionEnum);
    }

    private I8SBKeysOfCollectionEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    /**
     * The static get(int) method here provides the reverse lookup by simply
     * getting the value from the Map,
     *
     * @return ResponseCodeEnum
     */
    public static I8SBKeysOfCollectionEnum lookup(String code) {
        return lookup.get(code);
    }

}
