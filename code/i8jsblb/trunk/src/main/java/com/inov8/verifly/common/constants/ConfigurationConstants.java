package com.inov8.verifly.common.constants;

public interface ConfigurationConstants {
	public static long ENCODER_CLASS = 1;

	public static long ENCRYPTOR_CLASS = 2;

	public static long MIN_PIN_LENGTH = 3;

	public static long MAX_PIN_LENGTH = 4;

	public static long KEY_PAIR = 5;
	
	public static long PIN_EXPIRY_TIME = 7 ;
	public static long PIN_RETRY_COUNT = 8 ;	
	
	/* Added by Soofia Faruq
    * AES Encryption Support
    */
	public static long AES_ENCRYPTOR_CLASS = 9 ;
	public static long AES_KEY = 10 ;
	public static Long AUTHENTICATION_URL = 11L;
	/**********************************************************************************/
	
    public static String ENCRYPT_KEY_NAME = "DES";
    public static String DEFAULT_ENCRYPT_CLASS = "com.inov8.verifly.common.encoder.NullEncryption" ;
    public static int FIRST_INDEX = 0 ;
    public static String DELETED_STRING = "Deleted" ;
	public static final String REPLACE_COLUMNS_LIST[] = {"com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountNo","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/cardNo","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/cardExpiryDate","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/newPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/oldPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/generatedPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/pin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountInfoIdLogModelList/owner/cardNo","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountInfoIdLogModelList/owner/cardExpiryDate","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountInfoIdLogModelList/owner/pin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountInfoIdLogModelList/owner/accountNo","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/accountInfoIdLogModelList/owner/generatedPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/newPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/confirmNewPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/oldPin","com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl/accountInfoModel/generatedPin"};


}
