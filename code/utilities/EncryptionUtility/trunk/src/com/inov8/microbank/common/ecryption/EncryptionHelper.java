/*     */ package com.inov8.microbank.common.ecryption;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import javax.crypto.spec.PBEParameterSpec;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EncryptionHelper
/*     */ {
/*  23 */   private static EncryptionHelper encrypter = new EncryptionHelper();
/*     */ 
/*     */   
/*  26 */   private static byte[] salt = new byte[] { -57, 115, 33, -116, 126, -56, -18, -103 };
/*     */ 
/*     */   
/*     */   private static String _decrypt(String source) {
/*  30 */     source = source.replace("\r\n", "");
/*  31 */     source = source.replace("\r", "");
/*  32 */     source = source.replace("\n", "");
/*     */     
/*  34 */     String decodedStr = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  41 */       PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
/*     */ 
/*     */       
/*  44 */       PBEKeySpec pbeKeySpec = new PBEKeySpec((new String("abcdeFGHIJ")).toCharArray());
/*  45 */       SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEwithMD5andDES");
/*  46 */       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
/*     */ 
/*     */       
/*  49 */       Cipher pbeCipher = Cipher.getInstance("PBEwithMD5andDES");
/*  50 */       pbeCipher.init(2, pbeKey, pbeParamSpec);
/*     */       
/*  52 */       byte[] decoded = (new BASE64Decoder()).decodeBuffer(source);
/*  53 */       byte[] original = pbeCipher.doFinal(decoded);
/*     */       
/*  55 */       decodedStr = new String(original);
/*  56 */     } catch (Exception e) {
/*  57 */       e.printStackTrace();
/*     */     } 
/*  59 */     return decodedStr;
/*     */   }
/*     */   
/*     */   private static String _encrypt(String source) {
/*  63 */     source = source.replace("\r\n", "");
/*  64 */     source = source.replace("\r", "");
/*  65 */     source = source.replace("\n", "");
/*     */     
/*  67 */     String encodedStr = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  74 */       PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
/*     */ 
/*     */       
/*  77 */       PBEKeySpec pbeKeySpec = new PBEKeySpec((new String("abcdeFGHIJ")).toCharArray());
/*  78 */       SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEwithMD5andDES");
/*  79 */       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
/*     */ 
/*     */       
/*  82 */       Cipher pbeCipher = Cipher.getInstance("PBEwithMD5andDES");
/*  83 */       pbeCipher.init(1, pbeKey, pbeParamSpec);
/*     */ 
/*     */       
/*  86 */       byte[] ciphertext = pbeCipher.doFinal(source.getBytes());
/*  87 */       encodedStr = (new BASE64Encoder()).encode(ciphertext);
/*     */       
/*  89 */       encodedStr = encodedStr.replace("\r\n", "");
/*  90 */       encodedStr = encodedStr.replace("\r", "");
/*  91 */       encodedStr = encodedStr.replace("\n", "");
/*  92 */     } catch (Exception e) {
/*  93 */       e.printStackTrace();
/*     */     } 
/*  95 */     return encodedStr;
/*     */   }
/*     */   
/*     */   public static String decrypt(String source) {
/*  99 */     getEncrypter(); return _decrypt(source);
/*     */   }
/*     */   
/*     */   public static String encrypt(String source) {
/* 103 */     getEncrypter(); return _encrypt(source);
/*     */   }
/*     */ 
/*     */   
/*     */   private static EncryptionHelper getEncrypter() {
/* 108 */     if (encrypter == null) {
/* 109 */       encrypter = new EncryptionHelper();
/*     */     }
/*     */     
/* 112 */     return encrypter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getKeyedDigest(byte[] buffer, byte[] key) {
/*     */     try {
/* 122 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 123 */       md5.update(buffer);
/* 124 */       byte[] messageDigest = md5.digest(key);
/* 125 */       StringBuffer hexString = new StringBuffer();
/* 126 */       for (int i = 0; i < messageDigest.length; i++) {
/* 127 */         hexString.append(Integer.toHexString(messageDigest[i] & 0xFF | 0x100).substring(1, 3));
/*     */       }
/* 129 */       return hexString.toString();
/* 130 */     } catch (NoSuchAlgorithmException e) {
/*     */       
/* 132 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\EncryptionHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */