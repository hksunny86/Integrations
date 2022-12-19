/*    */ package com.inov8.encryption;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import org.bouncycastle.util.encoders.Base64;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncoderUtils
/*    */ {
/*    */   public static String encodeBase64(String stringToEncode) {
/* 15 */     if (stringToEncode != null && !"".equals(stringToEncode)) {
/* 16 */       byte[] data = stringToEncode.getBytes();
/* 17 */       return new String(encodeBase64(data));
/*    */     } 
/* 19 */     throw new IllegalArgumentException("String must not be null or empty");
/*    */   }
/*    */ 
/*    */   
/*    */   public static String decodeBase64(String stringToDecode) {
/* 24 */     if (stringToDecode != null && !"".equals(stringToDecode)) {
/* 25 */       byte[] data = stringToDecode.getBytes();
/* 26 */       return new String(decodeBase64(data));
/*    */     } 
/* 28 */     throw new IllegalArgumentException("String must not be null or empty");
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] encodeBase64(byte[] data) {
/* 33 */     return Base64.encode(data);
/*    */   }
/*    */   
/*    */   public static byte[] decodeBase64(byte[] data) {
/* 37 */     return Base64.decode(data);
/*    */   }
/*    */   
/*    */   public static String encodeToSha(String stringToEncode) {
/* 41 */     byte[] unencodedData = stringToEncode.getBytes();
/* 42 */     MessageDigest md = null;
/*    */     
/*    */     try {
/* 45 */       md = MessageDigest.getInstance("SHA");
/* 46 */     } catch (Exception var9) {
/* 47 */       return stringToEncode;
/*    */     } 
/*    */     
/* 50 */     md.reset();
/* 51 */     md.update(unencodedData);
/* 52 */     byte[] encodedData = md.digest();
/* 53 */     StringBuffer buf = new StringBuffer();
/* 54 */     byte[] var8 = encodedData;
/* 55 */     int var7 = encodedData.length;
/*    */     
/* 57 */     for (int var6 = 0; var6 < var7; var6++) {
/* 58 */       byte anEncodedPassword = var8[var6];
/* 59 */       if ((anEncodedPassword & 0xFF) < 16) {
/* 60 */         buf.append("0");
/*    */       }
/*    */       
/* 63 */       buf.append(Long.toString((anEncodedPassword & 0xFF), 16));
/*    */     } 
/*    */     
/* 66 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\EncoderUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */