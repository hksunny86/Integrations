package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum OtherThanOneBillProductEnum {

    //Agent Products
    UFONE_SUPERCARD("2511218"),
    GOPb("2511216"),
    PRA_GOVT_OF_PUNJAB("2511214"),
    SECP("2511212"),
    FBR_PRAL("2511210"),
    SRB("2511208"),
    WORLD_CALL("2511204"),
    ASKINV("2511198"),
    EFU_INSURANCE_TAKAFUL("2511196"),
    EFU_INSURANCE_CONVENTIONAL("2511194"),
    JLI_NONINDEX("2511192"),
    JLI_INDEX("2511190"),
    JLI_ADH("2511188"),
    ABL_AMC("2511158"),
    IMC("2511146"),
    DHAIR("2511144"),
    PAYPRO("2511142"),
    KUICKPAY("2511140"),
    SINDH_WORKER_WELFARE_FUND("2511138"),
    BSS("2511136"),
    OPTIX("2511132"),
    SHAHEEN_AIR_INTERNATIONAL("2511116"),
    LUMS_PGC("2511112"),

    //Customer Products
    CUST_UFONE_SUPERCARD("2511348"),
    CUST_GOPb("2511346"),
    CUST_PRA_GOVT_OF_PUNJAB("2511344"),
    CUST_SECP("2511342"),
    CUST_FBR_PRAL("2511340"),
    CUST_SRB("2511338"),
    CUST_WORLD_CALL("2511334"),
    CUST_ASKINV("2511328"),
    CUST_EFU_INSURANCE_TAKAFUL("2511326"),
    CUST_EFU_INSURANCE_CONVENTIONAL("2511324"),
    CUST_JLI_NONINDEX("2511322"),
    CUST_JLI_INDEX("2511320"),
    CUST_JLI_ADH("2511318"),
    CUST_ABL_AMC("2511288"),
    CUST_IMC("2511276"),
    CUST_DHAIR("2511274"),
    CUST_PAYPRO("2511272"),
    CUST_KUICKPAY("2511270"),
    CUST_SINDH_WORKER_WELFARE_FUND("2511268"),
    CUST_BSS("2511266"),
    CUST_OPTIX("2511262"),
    CUST_SHAHEEN_AIR_INTERNATIONAL("2511246"),
    CUST_LUMS_PGC("2511242"),
    CAREEM("2511382");
    private static final Map<String, OtherThanOneBillProductEnum> lookup = new HashMap<String, OtherThanOneBillProductEnum>();

    static {
        for (OtherThanOneBillProductEnum otherThanOneBillProductEnum : EnumSet.allOf(OtherThanOneBillProductEnum.class))
            lookup.put(otherThanOneBillProductEnum.getValue(), otherThanOneBillProductEnum);
    }

    private OtherThanOneBillProductEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static OtherThanOneBillProductEnum lookup(String code) {
        return lookup.get(code);
    }

    public static boolean contains(String code) {
        boolean retVal = false;
        for (OtherThanOneBillProductEnum otherThanOneBillProductEnum : OtherThanOneBillProductEnum.values()) {
            if (otherThanOneBillProductEnum.value.equals(code)) {
                retVal = true;
                break;
            }
        }
        return retVal;

    }
}
