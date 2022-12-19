package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BankIMDEnum {

	ADVANCE_MICROFINANCE_BANK(505895 , "Advans Pakistan Microfinance Ltd."),
	AL_BARAKA(639530 , "Al Baraka Bank Limited"),
	ALLIED_BANK(589430, "Allied Bank Limited"),
	APNA_MICROFINANCE_BANK(581862, "Apna Microfinance Bank"),
	ASKARI_BANK(603011, "Askari Commercial Bank Limited"),
	BANK_AL_HABIB(627197, "Bank AL Habib"),
	BANK_ALFALAH(627100, "Bank Alfalah Limited"),
	BANK_ISLAMI(639357,"BankIslami Pakistan Limited"),
	BANK_OF_PUNJAB(623977, "Bank of Punjab"),
	BANK_OF_KHYBER(627618, "Bank of Khyber"),
	BURJ_BANK(604786 , "Burj Bank Limited"),
	CITI_BANK(508117 , "CitiBank"),
	DUBAI_ISLAMIC_BANK(428273 , "Dubai Islamic Bank Pakistan Limited"),
	NATIONAL_BANK(958600 , "National Bank of Pakistan"),
	NRSP_MICROFINANCE(586010 , "NRSP Microfinance Bank Ltd."),
	HABIB_BANK(600648, "Habib Bank Limited"),
	HABIB_METRO_BANK(627408,"Habib Metropolitan Bank Limited"),
	FAYSAL_BANK(601373 , "Faysal Bank Limited"),
	ICBC_BANK(621464, "ICBC"),
	JS_BANK(603733, "JS Bank"),
	//KASB_BANK(628999 , "KASB Bank Limited"),
	MCB_BANK(589388,"MCB Bank Limited"),
	MCB_ISLAMIC_BANK(507642," MCB Islamic Banking"),
	MEEZAN_BANK(627873 , "Meezan Bank Limited"),
	NIB_BANK(999100, "NIB Bank Limtied"),
	SAMBA_BANK(606101 , "SAMBA"),
	SINDH_BANK(505439,"Sindh Bank"),
	SILK_BANK(627544, "Silk Bank"),
	SME_BANK(604889, "SME Bank Limited"),
	STANDARD_CHARTERED(627271, "Standard Chartered Bank"),
	SONERI_BANK(786110 , "Soneri Bank Limited"),
	SUMMIT_BANK(604781, "Summit Bank"),
	TAMEER_BANK(639390, "Tameer Bank"),
	UBL_BANK(588974 , "United Bank Limited"),
	U_BANK(581886, "Ubank"),
	FIRST_WOMEN_BANK(628138 , "First Women Bank Limited"),
	FINCA_Bank(502841L, "FINCA Microfinance Bank"),
	Mobilink_Bank(585953L, "Mobilink Microfinance Bank"),
	MODEL_BANK(100196,"Model Bank" )
	;


	private static final Map<Long, String> lookup = new HashMap<>();

	static {
		for(BankIMDEnum bankImdEnum : EnumSet.allOf(BankIMDEnum.class))
			lookup.put(bankImdEnum.getId(), bankImdEnum.getName());
	}

	public static String lookup(Long id) {
		return lookup.get(id.longValue());
	}

	private Long id;
	private String name;

	private BankIMDEnum(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
