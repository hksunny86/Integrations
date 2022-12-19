/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Transliteration
/*    */ {
/* 13 */   static Map<String, String> uMap = new HashMap<>();
/*    */   
/*    */   Transliteration() {
/* 16 */     uMap.put("ا", "A");
/* 17 */     uMap.put("ا", "A");
/* 18 */     uMap.put("ٵ", "A");
/* 19 */     uMap.put("ٳ", "A");
/* 20 */     uMap.put("ذ", "A");
/* 21 */     uMap.put("آ", "AA");
/* 22 */     uMap.put("ب", "B");
/* 23 */     uMap.put("پ", "P");
/* 24 */     uMap.put("ت", "T");
/* 25 */     uMap.put("ط", "T");
/* 26 */     uMap.put("ٹ", "T");
/* 27 */     uMap.put("ج", "J");
/* 28 */     uMap.put("س", "S");
/* 29 */     uMap.put("ث", "S");
/* 30 */     uMap.put("ص", "S");
/* 31 */     uMap.put("چ", "CH");
/* 32 */     uMap.put("ح", "H");
/* 33 */     uMap.put("ه", "H");
/* 34 */     uMap.put("ة", "H");
/* 35 */     uMap.put("۟", "H");
/* 36 */     uMap.put("خ", "KH");
/* 37 */     uMap.put("د", "D");
/* 38 */     uMap.put("ڈ", "D");
/* 39 */     uMap.put("ذ", "Z");
/* 40 */     uMap.put("ز", "Z");
/* 41 */     uMap.put("ض", "Z");
/* 42 */     uMap.put("ظ", "Z");
/* 43 */     uMap.put("ڎ", "Z");
/* 44 */     uMap.put("ر", "R");
/* 45 */     uMap.put("ڑ", "R");
/* 46 */     uMap.put("ش", "SH");
/* 47 */     uMap.put("غ", "GH");
/* 48 */     uMap.put("ف", "F");
/* 49 */     uMap.put("ک", "K");
/* 50 */     uMap.put("ق", "K");
/* 51 */     uMap.put("گ", "G");
/* 52 */     uMap.put("ل", "L");
/* 53 */     uMap.put("م", "M");
/* 54 */     uMap.put("ن", "N");
/* 55 */     uMap.put("ں", "N");
/* 56 */     uMap.put("و", "O");
/* 57 */     uMap.put("ى", "Y");
/* 58 */     uMap.put("ئ", "Y");
/* 59 */     uMap.put("ی", "Y");
/* 60 */     uMap.put("ے", "E");
/* 61 */     uMap.put("ہ", "H");
/* 62 */     uMap.put("ي", "E");
/* 63 */     uMap.put("ۂ", "AH");
/* 64 */     uMap.put("ھ", "H");
/* 65 */     uMap.put("ع", "A");
/* 66 */     uMap.put("ك", "K");
/* 67 */     uMap.put("ء", "A");
/* 68 */     uMap.put("ؤ", "O");
/* 69 */     uMap.put("،", "");
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/*    */     try {
/* 74 */       Charset charset = StandardCharsets.UTF_8;
/* 75 */       String str = new String("???? ??????? ???? ????? ??? ???".getBytes(), "UTF-8");
/* 76 */       String uString = new String("???? ????".getBytes(), "UTF-8");
/* 77 */       String a = "?????";
/* 78 */       System.out.printf(a, new Object[0]);
/* 79 */       System.out.printf(str, new Object[0]);
/* 80 */       System.out.printf(uString, new Object[0]);
/* 81 */       char[] chars = uString.toCharArray();
/* 82 */       String eString = "";
/* 83 */       for (char ch : chars) {
/* 84 */         System.out.println(ch);
/* 85 */         System.out.println("\\u" + Integer.toHexString(ch | 0x10000).substring(1));
/* 86 */         String unicode = "\\u" + Integer.toHexString(ch | 0x10000).substring(1);
/* 87 */         eString = eString + (String)uMap.get(unicode);
/*    */       } 
/* 89 */       System.out.println(eString);
/*    */     }
/* 91 */     catch (Exception e) {}
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\Transliteration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */