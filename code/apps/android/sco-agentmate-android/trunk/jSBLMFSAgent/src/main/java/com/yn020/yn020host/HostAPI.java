package com.yn020.yn020host;

public class HostAPI {

	public final byte UART_COMMUNICATION = 0;
	final byte USB_COMMUNICATION = 1;
	final String ttyS0 = "[/dev/ttyS0]";
	final String ttyS1 = "[/dev/ttyS1]";
	final String ttyS2 = "[/dev/ttyS2]";
	final String ttyS3 = "[/dev/ttyS3]";
	final String ttyMT0 = "[/dev/ttyMT0]";
	public final String ttyMT1 = "[/dev/ttyMT1]";
	final String ttyMT2 = "[/dev/ttyMT2]";
	public final String ttyMT3 = "[/dev/ttyMT3]";
	final int baud_rate_9600 = 1;
	final int baud_rate_19200 = 2;
	public final int baud_rate_38400 = 3;
	final int baud_rate_57600 = 4;
	final int baud_rate_115200 = 5;
	final int baud_rate_230400 = 6;
	final int baud_rate_460800 = 7;
	public final int baud_rate_921600 = 8;
	
	public byte Communication_Type = UART_COMMUNICATION;
	public String dev = ttyS2;
	public int baud_rate  = 0;
	public byte[] GetDevSgNum = new byte[1];
	public byte[] Cur_UID = new byte[8];
	public HostAPI(){
		
	}

	public byte CommandProcess(short p_wCmdCode, byte[] p_nParam1, byte[] p_nParam2, byte[] p_nParam3, byte[] p_nParam4){
		
		return JNI_CommandProcess(p_wCmdCode, p_nParam1, p_nParam2, p_nParam3, p_nParam4, Cur_UID);
	}
	public byte Init_Device(){
		
		return JNI_Init_Device(Communication_Type, dev, baud_rate);
	}
	public byte Stop_Device(){
		
		return JNI_Stop_Device();
	}
	public byte GetDevMessage(byte[] DevNumber, byte[] DevUID){
		return JNI_GetDevMessage(DevNumber, DevUID);
	}
	
	public native byte JNI_CommandProcess(short p_wCmdCode, byte[] p_nParam1, byte[] p_nParam2, byte[] p_nParam3, byte[] p_nParam4, byte[] p_nParam5);
	private native byte JNI_Init_Device(byte Communication_Type, String dev, int baud_rate);
	private native byte JNI_Stop_Device();
	private native byte JNI_GetDevMessage(byte[] DevNumber, byte[] DevUID);
	
	static {
		System.loadLibrary("yn020_android");
	}

	final byte TEMPLATE_TYPE_FPTH   =    0;
	final byte TEMPLATE_TYPE_ISO_2005    =    1;
	final byte TEMPLATE_TYPE_ISO_2010    =    2;
	// 0x00 ~ 0x1F : Device Config Code
	public final short		YN020_TEST_CONNECTION		=		0x00;
	final short		YN020_GET_DEV_INFO			=		0x01;
	public final short		YN020_SET_BAUDRATE			=		0x02;
	final short		YN020_SET_SECURITYLEVEL		=		0x03;
	final short		YN020_GET_SECURITYLEVEL		=		0x04;
	final short		YN020_SET_DEV_ID			=		0x05;
	final short		YN020_GET_DEV_ID			=		0x06;
	final short		YN020_SET_DUP_CHECK			=		0x07;
	final short		YN020_GET_DUP_CHECK			=		0x08;
	final short		YN020_SET_AUTO_LEARN		=		0x09;
	final short		YN020_GET_AUTO_LEARN		=		0x0A;
	final short		YN020_SET_DEVPASS			=		0x0B;
	final short		YN020_VERIFY_DEVPASS		=		0x0C;
	public final short		YN020_SLED_CTRL				=		0x0D;
	final short		YN020_ADJUST_SENSOR			=		0x0E;
	public final short		YN020_TOUCH_SENSING			=		0x0F;
	final short		YN020_SLEEP_CONFIG			=		0x10;
	final short		YN020_UPGRADE_FIRMWARE		=		0x11;
	
	// 0x20 ~ 0x3F : Image and Template Code            
	public final short		YN020_GET_FP_IMAGE				=	0x20 ;
	final short		YN020_CONTINUOUS_GET_FP_IMAGE	=	0x21 ;
	public final short		YN020_UP_FP_IMAGE				=	0x22 ;
	public final short		YN020_GENERATE_FP_RAM			=	0x23 ;
	final short		YN020_SYNTHESIZE_FP_RAM			=	0x24 ;
	public final short		YN020_READ_FP_RAM				=	0x25 ;
	final short		YN020_WRITE_FP_RAM				=	0x26 ;
	final short		YN020_READ_FP_DATA				=	0x27 ;
	final short		YN020_WRITE_FP_DATA				=	0x28 ;
	final short		YN020_WRITE_MULTI_FP_DATA     	= 	0x29 ;
	final short		YN020_UPDATE_TMPL				=	0x30 ;
	final short		YN020_CONVERT_TMPL_TO_FMR		=	0x31 ;
	final short		YN020_CONVERT_FMR_TO_TMPL		=	0x32 ;
	final short		YN020_DOWN_FP_IMAGE				=	0x3F ;

	// 0x40 ~ 0x5F : Fp Command Code
	public final short		YN020_GET_EMPTY_FP_ID			=	0x40;
	final short		YN020_GET_FP_STATUS				=	0x41;
	final short		YN020_GET_BROKEN_FP_ID			=	0x42;
	final short		YN020_GET_ENROLL_FP_COUNT		=	0x43;
	final short		YN020_FINGER_DETECT				=	0x44;
	public final short		YN020_ENROLL_FP					=	0x45;
	final short		YN020_SET_ENROLL_ID				=	0x46;
	final short		YN020_GET_ENROLL_ID				=	0x47;
	final short		YN020_CLEAR_FP					=	0x48;
	public final short		YN020_CLEAR_ALL_FP				=	0x49;
	public final short		YN020_VERIFY_FP					=	0x4A;
	public final short		YN020_IDENTIFY_FP				=	0x4B;
	final short		YN020_VERIFY_WITH_FP_RAM		=	0x4C;
	final short		YN020_IDENTIFY_WITH_FP_RAM		=	0x4D;
	final short   YN020_VERIFY_WITH_DOWN_TEMPLATE	= 	0x4E;
	final short   YN020_IDENTIFY_WITH_DOWN_TEMPLATE	= 	0x4F;
	final short   YN020_SET_AUTO_IDENTIFY			= 	0x50;
	final short   YN020_SET_FP_TIMEOUT				= 	0x51;
	final short   YN020_GET_FP_TIMEOUT				= 	0x52;
	final short   YN020_FP_CANCEL					= 	0x53;
	
	// 0x60 ~ 0x6F : GPIO Control Code                 
	final short   YN020_CONFIG_WIEGAND_MODE			=	0x60;  
	final short   YN020_GET_WIEGAND_INPUT			=	0x61;  
	final short   YN020_SET_WIEGAND_OUTPUT			=	0x62;  
	final short   YN020_CONFIG_GPIO15				=	0x63;  
	final short   YN020_GET_GPIO15_INPUT			=	0x64;  
	final short   YN020_SET_GPIO15_OUTPUT			=	0x65;  
	final short   YN020_CONFIG_GPIO16				=	0x66;  
	final short   YN020_GET_GPIO16_INPUT			=	0x67;  
	final short   YN020_SET_GPIO16_OUTPUT			=	0x68;  
	
	// 0x70 ~ 0x7F : Analog Control Code             
	final short 	YN020_ADC_POWER_CONTROL			=	0x70;  
	final short 	YN020_GET_ADC_VALUE				=	0x71;  
	final short 	YN020_AUDIO_POWER_CONTROL		=	0x72;  
	final short 	YN020_AUDIO_PLAYBACK			=	0x73;  

	// 0xA0 :                                           
	final short     RCM_INCORRECT_COMMAND		    =	0xA0;
	                                                    
	final short     YN020_RESET_MODULE		    	=   0xA1;
	final short     YN020_GET_IDENTIFY_RESULT   	=   0xA2;
	//-----------------------------------------------------------------------------//
	//  YN020 Return Code                              //
	//-----------------------------------------------------------------------------//
	final short		ERR_FAIL						=	1;
	final short		ERR_SUCCESS						=	0;
	final short		ERR_CONNECTION					=	3;//User Define
	final short		ERR_TIME_OUT					=	4;//User Define
                   
	final short		ERR_VERIFY						=	0x11 ;
	final short		ERR_IDENTIFY					=	0x12 ;
	final short		ERR_TMPL_EMPTY					=	0x13 ;
	final short		ERR_TMPL_NOT_EMPTY				=	0x14 ;
	final short		ERR_ALL_TMPL_EMPTY				=	0x15 ;
	final short		ERR_EMPTY_ID_NOEXIST			=	0x16 ;
	final short		ERR_BROKEN_ID_NOEXIST			=	0x17 ;
	final short		ERR_INVALID_TMPL_DATA			=	0x18 ;
	final short		ERR_DUPLICATION_ID				=	0x19 ;
	final short		ERR_BAD_QUALITY					=	0x1B ;
	final short		ERR_SMALL_LINES					=	0x1C ;
	final short		ERR_GENERALIZE					=	0x1D ;
	final short		ERR_INTERNAL					=	0x1E ;
	final short		ERR_MEMORY						=	0x1F ;
	final short		ERR_EXCEPTION					=	0x20 ;
	final short		ERR_INVALID_TMPL_NO				=	0x21 ;
	final short		ERR_INVALID_PARAM				=	0x25 ;
	final short 	ERR_EMPTY_FP_IMAGE      		=  	0x26;
	final short 	ERR_GEN_COUNT					=	0x27 ;
	final short		ERR_INVALID_BUFFER_ID			=	0x29 ;
	final short		ERR_INVALID_STORE_TYPE			=	0x2A ;
	final short		ERR_NO_DETECT_FINGER			=	0x31 ;
	
	final short	  	ERR_NOT_AUTHORIZED				=	0x50 ;
	final short    	ERR_DEV_PWD_NOT_SET				=  	0x52;
	final short		ERR_SOUND_NOT_EXIST				=	0xA0 ;
	final short		ERR_ADC_POWER_OFF				=	0xA1 ;
	final short		ERR_DAC_POWER_OFF				=	0xA2 ;
	final short		ERR_ILLEGAL_OPERATION			=	0xA3 ;
	final short		ERR_NO_WIEGAND_INPUT			=	0xA4 ;
           
	final short		GD_NO_DETECT_FINGER				=	0x01 ;
	final short		GD_DETECT_FINGER				=	0x00 ;
	final short		GD_TEMPLATE_NOT_EMPTY			=	0x01 ;
	final short		GD_TEMPLATE_EMPTY				=	0x00 ;
	final short     YN020_THREAD         			= 	0xab ;

}
