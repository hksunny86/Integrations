package com.inov8.microbank.common.util;

/**
 * 
 * @author Soofia Faruq
 * @date 02-September-2013
 * 
 */

public class ThreadLocalEncryptionType {
	private static ThreadLocal<Byte> encryptionType = new ThreadLocal<Byte>();

	private ThreadLocalEncryptionType() {
	}

	public static Byte getEncryptionType() {
		return (Byte) ThreadLocalEncryptionType.encryptionType.get();
	}

	public static void setEncryptionType( Byte encType) {
		ThreadLocalEncryptionType.encryptionType.set(encType);
	}
	
	public static void remove() {
		ThreadLocalEncryptionType.encryptionType.remove();
	}
}
