package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum BankEnum {
	ADVANCE_MICROFINANCE_BANK(50589500000L , "Advans Pakistan Microfinance Ltd."),
	BANK_OF_KHYBER(62761800000L, "Bank of Khyber"),
	BURJ_BANK(60478600000L , "Burj Bank Limited"),
	CITI_BANK(50811700000L , "CitiBank"),
	NATIONAL_BANK(95860000000L , "National Bank of Pakistan"),
	NRSP_MICROFINANCE(58601000000L , "NRSP Microfinance Bank Ltd."),
	ICBC_BANK(62146400000L, "ICBC"),
	MCB_ISLAMIC_BANK(50764200000L," MCB Islamic Banking"),
	SAMBA_BANK(60610100000L , "SAMBA"),
	SME_BANK(60488900000L, "SME Bank Limited"),
	FIRST_WOMEN_BANK(62813800000L , "First Women Bank Limited"),
	AL_BARAKA(63953000000L,"Al Baraka Bank Ltd"),
	ABL(58943000000L,"Allied Bank Ltd"),
	ASKARI(60301100000L,"Askari Bank Ltd"),
	AL_HABIB(62719700000L,"Bank AL Habib Ltd"),
	ALFALAH(62710000000L,"Bank Alfalah Ltd"),
	FAYSAL(60137300000L,"Faysal Bank Ltd"),
	HABIB(60064800000L,"Habib Bank Ltd"),
	MEEZAN(62787300000L,"Meezan Bank Ltd"),
	SONERI(78611000000L,"Soneri Bank Ltd"),
	SCB(62727100000L,"SCB"),
	UBL(58897400000L,"United Bank Ltd"),
	SAMBA(60610100000L,"SAMBA"),
	DUBAI_ISLAMIC(42827300000L,"Dubai Islamic Bank"),
	BANK_ISLAMI(63935700000L,"Bank Islami"),
	NIB(99910000000L,"NIB Bank Ltd"),
	BOP(62397700000L,"Bank of Punjab"),
	BURJ(60478600000L,"Burj Bank Ltd"),
	TAMEER(63939000000L,"Tameer Bank"),
	SILK(62754400000L,"Silk Bank"),
	SUMMIT(60478100000L,"Summit Bank"),
	HABIB_METRO(62740800000L,"Habib Metro Bank Ltd"),
	SINDH(50543900000L,"Sindh Bank"),
	CITI(50811700000L,"Citi Bank"),
	APNA_MICRO(58186200000L,"Apna Microfinance"),
	U_BANK(58188600000L,"Ubank"),
	ICBC(62146400000L,"ICBC"),
	MCB(58938800000L,"MCB Bank Ltd");
	
	private static final Map<Long, String> lookup = new HashMap<Long, String>();
	
	static {
	    for(BankEnum bankEnum : EnumSet.allOf(BankEnum.class)) {
	         lookup.put(bankEnum.bankCode, bankEnum.bankName);
	    }
	}
	
	public static String getBankName(Long bankCode) {
		return lookup.get(bankCode);
	}
	
	private BankEnum(Long bankCode, String bankName) {
		this.bankCode = bankCode;
		this.bankName = bankName;
	}

	private Long bankCode;
	private String bankName;

	public Long getBankCode() {
		return bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	
}
