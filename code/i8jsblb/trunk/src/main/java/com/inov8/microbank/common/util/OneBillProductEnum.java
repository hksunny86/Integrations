package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum OneBillProductEnum {

    //Agent Products
    SILK_BANK_CREDIT_CARD("2511236"),
    MCB_CREDIT_CARD("2511234"),
    ABL_CREDIT_CARD("2511232"),
    AKBL_CREDIT_CARD("2511230"),
    HBL_CREDIT_CARD("2511228"),
    JS_BANK_CREDIT_CARD("2511226"),
    UBL_CREDIT_CARD("2511224"),
    FBL_CREDIT_CARD("2511222"),
    BAFL_CREDIT_CARD("2511220"),

    ABL_ASSESSMENT_MGT("2511170"),
    CDC("2511172"),
    DAEWOO("2511114"),
    DHA_ISB_RWP_DHAI_R(""),
    DP_WORLD("2511120"),
    EFU_CONVENTIONAL("2511184"),
    EFU_ISLAMIC("2511186"),
    ENGRO("2511122"),
    JLIADH("2511124"),
    JLI_IND("2511126"),
    JLI_NIND("2511128"),
    KENC("2511130"),
    LUMS("2511110"),
    MCB_AH_INVESTMENTS(""),
    NAYATEL("2511200"),
    OPTIX("2511132"),
    JAZZ_CASH("2511206"),
    PAK_QATAR_TAKAFUL_FAM1("2511176"),
    PAK_QATAR_TAKAFUL_FAM2("2511178"),
    PAK_QATAR_TAKAFUL_FAM3("2511180"),
    BOP_AGGREGATOR_PESSI("2511182"),
    TRANSWORLD("2511202"),
    INDUS_MOTOR_COMPANY("2511134"),

    ONELOAD("2511148"),
    AKD_INVESTMENT("2511160"),
    ASKARI_INVESTMENT("2511162"),
    CDC_IAS(""),
    FINJA("2511150"),
    HABALL("2511152"),
    IBA("2511108"),
    AL_MEEZAN_INVESTMENT("2511166"),
    CONNECT_PAY("2511154"),
    UBL_FUND("2511168"),
    KEENU("2511156"),
    CITY_SCHOOL("10245237"),
    NTS("10245239"),
    ICT("10245241"),

    //Customer Products
    CUST_SBL_CREDIT_CARD("2511366"),
    CUST_MCB_CREDIT_CARD("2511364"),
    CUST_ABL_CREDIT_CARD("2511362"),
    CUST_AKBL_CREDIT_CARD("2511360"),
    CUST_HBL_CREDIT_CARD("2511358"),
    CUST_JS_BANK_CREDIT_CARD("2511356"),
    CUST_UBL_CREDIT_CARD("2511354"),
    CUST_BANK_ALFALAH_CREDIT_CARD("2511352"),
    CUST_FAYSAL_BANK_CREDIT_CARD("2511350"),
//    CUST_UFONE_SUPERCARD(""),
//    CUST_GOPb(""),
//    CUST_PRA_Govt_of_Punjab(""),
//    CUST_SECP(""),
//    CUST_FBR_PRAL(""),
//    CUST_SRB(""),
    CUST_JAZZ_CASH("2511336"),
//    CUST_WORLD_CALL(""),
    CUST_TRANSWORLD("2511332"),
    CUST_NAYATEL("2511330"),
//    CUST_ASKINV(""),
//    CUST_EFU_Insurance_Takaful(""),
//    CUST_EFU_Insurance_Conventional(""),
//    CUST_Jubilee_Life_Insurance_NONINDEX(""),
//    CUST_Jubilee_Life_Insurance_INDEX(""),
//    CUST_Jubilee_Life_Insurance_ADHOC(""),
    CUST_EFU_ISLAMIC("2511316"),
    CUST__EFU_CONVENTIONAL("2511314"),
    CUST_BOP_AGGREGATOR_PESSI("2511312"),
    CUST_PAK_QATAR_TAKAFUL_FAMILY_3("2511310"),
    CUST_PAK_QATAR_TAKAFUL_FAMILY_2("2511308"),
    CUST_PAK_QATAR_TAKAFUL_FAMILY_1("2511306"),
    CUST_MUSLIM_COMMERCIAL_BANK_ARIF_HABIB_INVESTMENTS("2511304"),
    CUST_CENTRAL_DEPOSITORY_COMPANY_CDC("2511302"),
    CUST_ABL_ASSET_MANAGEMENT("2511300"),
    CUST_UBLFUND("2511298"),
    CUST_AL_MEEZAN_INVESTMENT("2511296"),
    CUST_CENTRAL_DEPOSITORY_COMPANY_INVESTOR_ACCOUNT("2511294"),
    CUST_ASKARI_INVESTMENT("2511292"),
    CUST_AKD_INVESTMENT("2511290"),
//    CUST_ABL_AMC(""),
    CUST_KEENU("2511286"),
    CUST_CONNECTPAY("2511284"),
    CUST_HABALL("2511282"),
    CUST_FINJA("2511280"),
    CUST_ONELOAD("2511278"),
//    CUST_IMC(""),
//    CUST_DHAIR(""),
//    CUST_PAYPRO(""),
//    CUST_Kuickpay(""),
//    CUST_SINDH_WORKER_WELFARE_FUND(""),
//    CUST_BSS(""),
    CUST_INDUS_MOTORS_COMPANY("2511264"),
    CUST_OPTIX("2511262"),
    CUST_KARACHI_ELECTRIC_NEW_CONNECTION_KENC("2511260"),
    CUST_JUBILEE_LIFE_INSURANCE_NIND("2511258"),
    CUST_JUBILEE_LIFE_INSURANCE_IND("2511256"),
    CUST_JLIADH("2511254"),
    CUST_ENGRO("2511252"),
    CUST_DP_WORLD("2511250"),
    CUST_DEFENSE_HOUSING_AUTHORITY_ISLAMABAD_RAWALPIND("2511248"),
//    CUST_SHAHEEN_AIR_INTERNATIONAL(""),
    CUST_DAEWOO("2511244"),
//    CUST_LUMS_PGC(""),
    CUST_LUMS("2511240"),
    CUST_IBA("2511238"),
    LHR_TRAFFIC_CHALLAN("2511368"),
    GUJ_TRAFFIC_CHALLAN("2511370"),
    FSD_TRAFFIC_CHALLAN("2511372"),
    MUL_TRAFFIC_CHALLAN("2511374"),
    PUNAB_TOKEN_TAX("2511376"),
    PUNJAB_VEHICLE_FITNESS("2511378"),
    PUNJAB_ROUTE_PERMIT("2511380"),
    EXCISE_AND_TAXATION("10245216"),
    CUST_NATIONAL_TESTING_SERVICE("10245223"),
    CUST_ISLAMABAD_CAPTIAL_TERITORY("10245225"),
    CUST_CITY_SCHOOL("10245227"),
    CUST_KPK_EXCISE_TAXATION("10245259"),
    KPK_EXCISE_TAXATION("10245261"),
    CUST_PASSPORT_FEE("10245371");

    private static final Map<String, OneBillProductEnum> lookup = new HashMap<String, OneBillProductEnum>();

    static {
        for (OneBillProductEnum oneBillProductEnum : EnumSet.allOf(OneBillProductEnum.class))
            lookup.put(oneBillProductEnum.getValue(), oneBillProductEnum);
    }

    private OneBillProductEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static OneBillProductEnum lookup(String code) {
        return lookup.get(code);
    }

    public static boolean contains(String code) {
        boolean retVal = false;
        for (OneBillProductEnum oneBillProductEnum : OneBillProductEnum.values()) {
            if (oneBillProductEnum.value.equals(code)) {
                retVal = true;
                break;
            }
        }
        return retVal;

    }
}
