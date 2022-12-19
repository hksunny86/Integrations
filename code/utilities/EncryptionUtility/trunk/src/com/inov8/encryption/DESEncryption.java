/*     */ package com.inov8.encryption;
/*     */ 
/*     */ import com.sun.crypto.provider.SunJCE;
/*     */ import java.security.Security;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.bouncycastle.util.encoders.Hex;
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
/*     */ public class DESEncryption
/*     */ {
/*  25 */   protected Map<String, DESKeySupport> cache = Collections.synchronizedMap(new HashMap<>(5));
/*  26 */   private static final byte[] DESedeKeyBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8 };
/*  27 */   private static final byte[] ivBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
/*     */   
/*  29 */   private BASE64Encoder base64Encoder = new BASE64Encoder();
/*  30 */   private BASE64Decoder base64Decoder = new BASE64Decoder();
/*     */ 
/*     */ 
/*     */   
/*  34 */   private static IvParameterSpec iv = new IvParameterSpec(ivBytes);
/*  35 */   private static DESEncryption object = null;
/*     */ 
/*     */   
/*     */   public static synchronized DESEncryption getInstance() {
/*  39 */     if (object == null) {
/*  40 */       object = new DESEncryption();
/*     */     }
/*     */     
/*  43 */     return object;
/*     */   }
/*     */   
/*     */   private DESEncryption() {
/*  47 */     initDefault();
/*     */   }
/*     */   
/*     */   private synchronized void initDefault() {
/*     */     try {
/*  52 */       Security.addProvider(new SunJCE());
/*     */     
/*     */     }
/*  55 */     catch (Exception var2) {
/*     */       
/*  57 */       var2.printStackTrace();
/*  58 */       throw new RuntimeException("Error initializing Cipher for DES Encryption/Decryption");
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized String encrypt(String string, byte[] keyBytes, boolean isTripleDES) throws Exception {
/*  63 */     DESKeySupport keySupport = getKeySupport(keyBytes, isTripleDES);
/*  64 */     byte[] passwordBytes = string.getBytes();
/*  65 */     byte[] encryptedPasswordBytes = keySupport.getEncryptCipher().doFinal(passwordBytes);
/*     */     
/*  67 */     return isTripleDES ? this.base64Encoder.encode(encryptedPasswordBytes) : new String(Hex.encode(encryptedPasswordBytes));
/*     */   }
/*     */   
/*     */   public synchronized String decrypt(String string, byte[] keyBytes, boolean isTripleDES) throws Exception {
/*  71 */     DESKeySupport keySupport = getKeySupport(keyBytes, isTripleDES);
/*  72 */     byte[] encryptedBytes = isTripleDES ? this.base64Decoder.decodeBuffer(string) : Hex.decode(string);
/*  73 */     byte[] passwordBytes = keySupport.getDecryptCipher().doFinal(encryptedBytes);
/*     */     
/*  75 */     return new String(passwordBytes);
/*     */   }
/*     */   
/*     */   private DESKeySupport getKeySupport(byte[] keyBytes, boolean isTripleDES) throws Exception {
/*  79 */     keyBytes = (keyBytes != null) ? keyBytes : DESedeKeyBytes;
/*  80 */     DESKeySupport keySupport = null;
/*  81 */     String keyStr = new String(keyBytes);
/*     */     
/*  83 */     if (this.cache.containsKey(keyStr)) {
/*  84 */       keySupport = this.cache.get(keyStr);
/*     */     }
/*     */     else {
/*     */       
/*  88 */       keySupport = new DESKeySupport(keyBytes, isTripleDES);
/*  89 */       this.cache.put(keyStr, keySupport);
/*     */     } 
/*     */     
/*  92 */     return keySupport;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  97 */       String e = "1111";
/*  98 */       DESEncryption encryptAgent = new DESEncryption();
/*  99 */       String key = "302F3825382D2D22";
/* 100 */       System.out.println("Password ....................[" + e + "]");
/* 101 */       String encodedEncryptedStr = encryptAgent.encrypt(e, (byte[])null, false);
/* 102 */       System.out.println("Encoded encrypted password ..[" + encodedEncryptedStr + "]");
/* 103 */       String recoveredStr = encryptAgent.decrypt(encodedEncryptedStr, (byte[])null, false);
/* 104 */       System.out.println("Recovered password ..........[" + recoveredStr + "]");
/* 105 */       System.out.println("String matched: " + e.equals(recoveredStr));
/*     */     
/*     */     }
/* 108 */     catch (Exception var7) {
/* 109 */       var7.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class DESKeySupport {
/*     */     private SecretKey secretKey;
/*     */     private Cipher encryptCipher;
/*     */     private Cipher decryptCipher;
/*     */     
/*     */     public DESKeySupport(byte[] keyBytes, boolean isTripleDES) throws Exception {
/* 119 */       if (isTripleDES) {
/* 120 */         this.encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
/* 121 */         this.decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
/* 122 */         this.secretKey = new SecretKeySpec(keyBytes, "DESede");
/* 123 */         this.encryptCipher.init(1, this.secretKey, DESEncryption.iv);
/* 124 */         this.decryptCipher.init(2, this.secretKey, DESEncryption.iv);
/*     */       }
/*     */       else {
/*     */         
/* 128 */         this.encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "SunJCE");
/* 129 */         this.decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "SunJCE");
/* 130 */         this.secretKey = new SecretKeySpec(keyBytes, "DES");
/* 131 */         this.encryptCipher.init(1, this.secretKey);
/* 132 */         this.decryptCipher.init(2, this.secretKey);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Cipher getDecryptCipher() {
/* 137 */       return this.decryptCipher;
/*     */     }
/*     */     
/*     */     public Cipher getEncryptCipher() {
/* 141 */       return this.encryptCipher;
/*     */     }
/*     */     
/*     */     public SecretKey getSecretKey() {
/* 145 */       return this.secretKey;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\DESEncryption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */