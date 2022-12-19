/*     */ package com.inov8.encryption;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HexEncodingUtils
/*     */ {
/*  18 */   private static final char[] HexChars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String toHex(long value, int len, char pad) {
/*  36 */     StringBuffer sb = new StringBuffer(Long.toHexString(value));
/*  37 */     int npad = len - sb.length();
/*  38 */     while (npad-- > 0)
/*  39 */       sb.insert(0, pad); 
/*  40 */     return new String(sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String toHex(byte[] bytes) {
/*  55 */     StringBuffer sb = new StringBuffer();
/*     */     
/*  57 */     for (int i = 0; i < bytes.length; i++) {
/*     */       
/*  59 */       sb.append(HexChars[bytes[i] >> 4 & 0xF]);
/*  60 */       sb.append(HexChars[bytes[i] & 0xF]);
/*     */     } 
/*  62 */     return new String(sb);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] fromHex(String string) {
/*  67 */     byte[] bytes = new byte[string.length() / 2];
/*  68 */     String buf = string.toLowerCase();
/*     */     
/*  70 */     for (int i = 0; i < buf.length(); i += 2) {
/*     */       
/*  72 */       char left = buf.charAt(i);
/*  73 */       char right = buf.charAt(i + 1);
/*  74 */       int index = i / 2;
/*     */       
/*  76 */       if (left < 'a') {
/*     */         
/*  78 */         bytes[index] = (byte)(left - 48 << 4);
/*     */       }
/*     */       else {
/*     */         
/*  82 */         bytes[index] = (byte)(left - 97 + 10 << 4);
/*     */       } 
/*  84 */       if (right < 'a') {
/*     */         
/*  86 */         bytes[index] = (byte)(bytes[index] + (byte)(right - 48));
/*     */       }
/*     */       else {
/*     */         
/*  90 */         bytes[index] = (byte)(bytes[index] + (byte)(right - 97 + 10));
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     return bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] fromHex(byte[] array) {
/*  99 */     byte[] bytes = new byte[array.length / 2];
/*     */     
/* 101 */     fromHex(array, 0, array.length, bytes, 0);
/*     */     
/* 103 */     return bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fromHex(byte[] in, int inOff, int length, byte[] out, int outOff) {
/* 108 */     int halfLength = length / 2;
/*     */     
/* 110 */     for (int i = 0; i < halfLength; i++) {
/*     */       
/* 112 */       byte left = in[inOff + i * 2];
/* 113 */       byte right = in[inOff + i * 2 + 1];
/*     */       
/* 115 */       if (left < 97) {
/*     */         
/* 117 */         out[outOff] = (byte)(left - 48 << 4);
/*     */       }
/*     */       else {
/*     */         
/* 121 */         out[outOff] = (byte)(left - 97 + 10 << 4);
/*     */       } 
/* 123 */       if (right < 97) {
/*     */         
/* 125 */         out[outOff] = (byte)(out[outOff] + (byte)(right - 48));
/*     */       }
/*     */       else {
/*     */         
/* 129 */         out[outOff] = (byte)(out[outOff] + (byte)(right - 97 + 10));
/*     */       } 
/*     */       
/* 132 */       outOff++;
/*     */     } 
/*     */     
/* 135 */     return halfLength;
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\HexEncodingUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */