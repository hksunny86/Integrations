package com.inov8.integration.middleware.util;

import org.jpos.iso.ISOUtil;

public class PinBlockUtil {
	
	private static byte[] zpk = null;

	
	public static void main(String[] args) {
		System.out.println(getPinBlock("4466", "2205450050000666"));
	}
	
	public static String getPinBlock(String pin, String pan){
		//loadKeys();
		zpk=ISOUtil.hex2byte("b098bfc1e0adc7c832f770d3e01c79ba");

		byte[] pinBlock = createPinBlock(pin, pan);

		System.out.println(ISOUtil.byte2hex(pinBlock)+"=pinBlock of pin and pan");
		System.out.println(ISOUtil.byte2hex(zpk)+"=zpk");


		byte[] encryptedPinBlock = null;
		try {
			encryptedPinBlock = TripleDesBouncyCastle.encode(pinBlock, zpk);
			System.out.println(ISOUtil.byte2hex(encryptedPinBlock)+"=Encrypted Pin Block");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ISOUtil.byte2hex(encryptedPinBlock);
	}
	
	private static void loadKeys(){
		
		if(zpk == null){
			/*
			String comp1 ="4C31 B004 AD4C D954 E658 6794 8C4C 7529".replace(" ", "");
	        String comp2 ="F1C8 4C64 155B 29E0 D3A8 3475 B092 EF61".replace(" ", "");
	        String comp3 ="CE83 8051 F49D B5EF 3B5E C449 68F4 2570".replace(" ", "");

	        String encryptedZPK = "626B AD4C F05F D691 07F9 5454 96D4 CC30".replace(" ", "");
	        */
			
			String comp1 ="5208 E5EA F458 B91A C2E6 A23E 5BAD 6280".replace(" ", "");
	        String comp2 ="76E0 F170 DA45 38FE 4607 8C6E E546 57F4".replace(" ", "");
	        String comp3 ="08C2 B629 E08A 5B8A 9123 07A1 8915 9EA7".replace(" ", "");

	        String encryptedZPK = "4082 B1D8 D5A9 EFF1 C286 2BB6 AE2B F140".replace(" ", "");
	        

	        byte[] xorComp1AndComp2 = ISOUtil.xor(ISOUtil.hex2byte(comp1), ISOUtil.hex2byte(comp2));

	        byte[] plainZMK = ISOUtil.xor(xorComp1AndComp2, ISOUtil.hex2byte(comp3));
	        
	        System.out.println(ISOUtil.byte2hex(plainZMK));
	        
	        try {
				zpk = TripleDesBouncyCastle.decode(ISOUtil.hex2byte(encryptedZPK), plainZMK);
				System.out.println(ISOUtil.byte2hex(zpk));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static byte[] createPinBlock(String pin, String accountNumber){
        // Block 1
        byte[] block1 = ISOUtil.hex2byte(new String(formatPINBlock(pin, 0x0)));

        // Block 2
        byte[] block2 = ISOUtil.hex2byte("0000" + formatAccountBlock(accountNumber));
        // pinBlock
        byte[] pinBlock = ISOUtil.xor(block2, block1);
        return pinBlock;
    }
	
	/**
     * a 64-bit block of ones used when calculating pin blocks
     */
    private static final byte[] fPaddingBlock = ISOUtil.hex2byte("FFFFFFFFFFFFFFFF");
    
    private static char[] formatPINBlock(String pin, int checkDigit){
        char[] block = ISOUtil.hexString(fPaddingBlock).toCharArray();
        char[] pinLenHex = String.format("%02X", pin.length()).toCharArray();
        pinLenHex[0] = (char)('0' + checkDigit);

        // pin length then pad with 'F'
        System.arraycopy(pinLenHex, 0, block, 0, pinLenHex.length);
        System.arraycopy(pin.toCharArray(), 0 ,block, pinLenHex.length, pin.length());
		System.out.println(block);
        return block;
    }
	
	private static String formatAccountBlock(String accountNo){
        String accountBlock = null;
        try {
//            accountBlock = ISOUtil.takeLastN(accountNo, 13);
        	accountBlock = "0000" + accountNo.substring(3, accountNo.length()-1);
        	System.out.println(accountBlock);
            return accountBlock;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return accountBlock;
    }
}
