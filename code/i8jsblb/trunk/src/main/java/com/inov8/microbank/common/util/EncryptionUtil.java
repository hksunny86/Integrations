package com.inov8.microbank.common.util;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Date;
import java.util.HashMap;

public class EncryptionUtil {
	static Cipher cipher;

	static SecureRandom random = new SecureRandom();
	// private static byte[] cipherText;
	// private static byte[] plainText;

	public static final byte ENCRYPTION_TYPE_RSA = 0;
	public static final byte ENCRYPTION_TYPE_AES = 1;

	private static final Log logger = LogFactory.getLog(EncryptionUtil.class);

	public static String encryptWithAES(String key, String strToEncrypt) {
		/*Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
				0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = strToEncrypt.trim().getBytes("UTF8");

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, skey);

				byte[] cipherText = new byte[cipher.getBlockSize()];

				int ctLength = cipher.update(input, 0, input.length,
						cipherText, 0);
				ctLength += cipher.doFinal(cipherText, ctLength);

				String encryptedString = new String(
						org.bouncycastle.util.encoders.Base64
								.encode(cipherText));
				return encryptedString.trim();
			}
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		return null;*/
		logger.info("EncryptionUtil.encryptWithAES() Started at :: " + new Date());
		Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
				0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = strToEncrypt.getBytes("UTF8");

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, skey);

//    byte[] cipherText = new byte[cipher.getBlockSize()];
				byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
				int ctLength = cipher.update(input, 0, input.length,
						cipherText, 0);
				ctLength += cipher.doFinal(cipherText, ctLength);
				String encryptedString = new String(
						org.bouncycastle.util.encoders.Base64
								.encode(cipherText));
				return encryptedString;
			}
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		finally {
			logger.info("EncryptionUtil.encryptWithAES() Ended at :: " + new Date());
		}
		return null;
	}

	public static String decryptWithAES(String key, String strToDecrypt) {
		/*Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
				0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = org.bouncycastle.util.encoders.Base64
					.decode(strToDecrypt.trim().getBytes("UTF8"));

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, skey);

				byte[] plainText = new byte[cipher.getBlockSize()];
				int ptLength = cipher.update(input, 0, cipher.getBlockSize(),
						plainText, 0);
				ptLength += cipher.doFinal(plainText, ptLength);
				String decryptedString = new String(plainText);
				return decryptedString.trim();
			}
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		return null;*/
		logger.info("EncryptionUtil.decryptWithAES() Started at :: " + new Date());
		Security.addProvider(new BouncyCastleProvider());
		byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
				0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
				0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

		try {
			keyBytes = key.getBytes("UTF8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			byte[] input = org.bouncycastle.util.encoders.Base64
					.decode(strToDecrypt.trim().getBytes("UTF8"));

			synchronized (Cipher.class) {
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, skey);

				//byte[] plainText = new byte[cipher.getBlockSize()];
				//int ptLength = cipher.update(input, 0, cipher.getBlockSize(), plainText, 0);
				byte[] plainText = new byte[cipher.getOutputSize(input.length)];
//                int ptLength = cipher.update(input, 0, cipher.getOutputSize(input.length), plainText, 0);
				int ptLength = cipher.update(input, 0, input.length, plainText, 0);
				ptLength += cipher.doFinal(plainText, ptLength);
				String decryptedString = new String(plainText);
				return decryptedString.trim();
			}
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		}
		finally {
			logger.info("EncryptionUtil.decryptWithAES() Ended at :: " + new Date());
		}
		return null;
	}

	public static String doEncrypt(String key, String input) {
		Security.addProvider(new BouncyCastleProvider());
		BASE64Encoder encoder = new BASE64Encoder();

		XStream xstream = new XStream();
		KeyPair keypair = (KeyPair) xstream.fromXML(key);
		Key pubKey = keypair.getPublic();

		try {
			synchronized (Cipher.class) {
				cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
				byte[] data = input.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
				byte[] cipherText = cipher.doFinal(data);
				return encoder.encode(cipherText);
			}
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchProviderException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return null;

	}

	public String encoder(Key pubKey, String input) {
		byte[] data = input.getBytes();

		try {
			synchronized (Cipher.class) {
				cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
				byte[] cipherText = cipher.doFinal(data);
				return Base64.encodeBytes(cipherText);
			}
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
			System.out.println("Illegal Block Size" + ibse.toString());
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
			System.out.println("Bad Padding Exception." + bpe.toString());
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
			System.out.println("Invalid Key." + ike.toString());
		}

		return null;
	}

	private static String decoder(Key privKey, String data) {
		try {
			byte[] cipher_text = Base64.decode(data);
			synchronized (Cipher.class) {
				cipher.init(Cipher.DECRYPT_MODE, privKey);
				byte[] plainText = cipher.doFinal(cipher_text);
				return new String(plainText);
			}
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
			System.out.println("Illegal Block Size" + ibse.toString());
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
			System.out.println("Bad Padding Exception." + bpe.toString());
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
			System.out.println("Invalid Key." + ike.toString());
		}

		return null;
	}

	public static String doDecrypt(String key, String cipherText) {

		Security.addProvider(new BouncyCastleProvider());
		BASE64Decoder decoder = new BASE64Decoder();

		XStream xstream = new XStream();
		KeyPair keypair = (KeyPair) xstream.fromXML(key);
		Key privKey = keypair.getPrivate();

		try {
			synchronized (Cipher.class) {
				if (cipher == null)
					cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");

				byte[] cipher_text = decoder.decodeBuffer(cipherText);
				cipher.init(Cipher.DECRYPT_MODE, privKey);
				byte[] plainText = cipher.doFinal(cipher_text);
				return new String(plainText);
			}
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (NoSuchProviderException nspe) {
			nspe.printStackTrace();

		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}

		return null;
	}

	public static String encryptWithDES(String strToEncrypt) throws Exception {
		return HexEncodingUtils.toHex(strToEncrypt.getBytes("UTF-8"));
		// return DESEncryption.getInstance().encrypt(strToEncrypt, null);
	}

	public static String decryptWithDES(String strToDecrypt) throws Exception {
		String id=new String(HexEncodingUtils.fromHex(strToDecrypt
				.getBytes("UTF-8")));
		Long decId;
		try {
			decId=new Long(id);
		}
		catch (Exception e){
			id=strToDecrypt;
		}

		return id;
		// return DESEncryption.getInstance().decrypt(strToDecrypt, null);
	}

	public static Long decryptForAppUserId(String strToDecrypt) throws Exception {
		Long appUserId;
		try{
			appUserId= Long.valueOf( new String(HexEncodingUtils.fromHex(strToDecrypt
					.getBytes("UTF-8"))));
		}
		catch (Exception e){
			appUserId=new Long( strToDecrypt);
		}
		return appUserId;
		// return DESEncryption.getInstance().decrypt(strToDecrypt, null);
	}

	public static void main (String[] args) {
		HashMap<String, String> map = new HashMap<String, String>();
/*		map.put("6tBH5Et3C3b9p7Xzr1YVIQ==", "1111");
		map.put("ndFIlSe9hvdJvk+tH0a8MA==", "2460");
		map.put("XJerXP0BAfwFw6f/l11XdQ==", "se6f");
		map.put("knkMAySjuEnkoMCQ/5M4yQ==", "rest");
		map.put("R+5cOIQmc8KUIUS8NdZuFA==", "24569825");
		map.put("bDPvW6pqkjJpG7wq6enodA==", "abc123yz");
		map.put("jMnEtFkJEUIoCSmYKDRQrw==", "meezanmb");
		map.put("EnDaiCPN2UYEkG0SzouELw==", "testing520");
		map.put("KoM51/x29tNaFW6iZBm8vQ==", "0300425098");
		map.put("vJ5736fUdbwbiKwQUbPHAw==", "inov8testing");
		map.put("UN+5wuQ7svfe/YYV+BiTlQ==", "445632400876");
		map.put("RNvYovEWkTq4TQmrC9KD4A==", "testingencrypti");
		map.put("NjCpGQhvQyfnzyfdOfsu+w==", "234566669856444");
		map.put("VX51hZmUHLVJy+XZqOtobg==", "abc1234refd9873");*/

		
		
		String key = new String("682ede816988e58fb6d057d9d85605e0");
		String decrypted = null;
		try {
			decrypted = decryptWithAES(key,"uJDlGng8vcfWlCNltTAbng==");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Decryption: " + decrypted);
		
		/*try {
			Iterator<String> myIterator = map.keySet().iterator();

			while (myIterator.hasNext()) {
				String encryptedStr = myIterator.next();
				System.out.println("Original key: " + encryptedStr
						+ " | Original value: " + map.get(encryptedStr));

				String encryption = encryptWithAES(key, map.get(encryptedStr));
				System.out.println("Encryption: " + encryption);

				String decrypted = decryptWithAES(key, encryption);
				System.out.println("Decryption: " + decrypted);

				encryption = encryptWithAES(key, decrypted);
				System.out.println("Encryption: " + encryption);

				System.out.println("-----------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		// String key = "<java.security.KeyPair>"+
		// "<privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\">"+
		// "<org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
		// "<big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int>"+
		// "<hashtable/>"+
		// "<vector/>"+
		// "<big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int>"+
		// "</org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
		// "<org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
		// "<default>"+
		// "<crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>"+
		// "<primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>"+
		// "<primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>"+
		// "<primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>"+
		// "<primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>"+
		// "<publicExponent>65537</publicExponent>"+
		// "</default>"+
		// "</org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
		// "</privateKey>"+
		// "<publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\">"+
		// "<modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus>"+
		// "<publicExponent>65537</publicExponent>"+
		// "</publicKey>"+
		// "</java.security.KeyPair>";
		//
		// /*
		//
		// String encryptedText =
		// "TOUFl4C9Bt3ajgP784m3qLT7FSTSaeM1OwhQTICasTlKTOlZLDIM9vhVRkiA0Ib1fnyiGeqmK9Iu"+
		// "uZoMktFNKFmXW9q/2lDyaU2WwHCag1mwMDITHRhIcF8FlIC0Wv3ub2tO5OoLpMS62LYAW0nXSw+Y"+
		// "vZVqMdAA76gNAPKME6Y=";
		//
		//
		// String deKey = doDecrypt(key, encryptedText);
		//
		// System.out.println("De key : "+deKey);*/
		//
		// String encryptedKey = doEncrypt(key, "1111");
		//
		// System.out.println("Encrypted Key : "+encryptedKey);
		//
		//
		//
	}
}
