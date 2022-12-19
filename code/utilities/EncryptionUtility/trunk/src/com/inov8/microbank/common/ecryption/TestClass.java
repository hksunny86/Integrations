/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class TestClass
/*    */ {
/*    */   public void Method1() throws Exception {
/*  8 */     System.out.println("2");
/*  9 */     (new Thread1()).start();
/* 10 */     System.out.println("5");
/*    */   }
/*    */   public void Method2() {
/* 13 */     String s = "";
/* 14 */     System.out.println("3");
/* 15 */     Integer.parseInt(s);
/* 16 */     System.out.println("4");
/*    */   }
/*    */   public static void main(String[] args) {
/*    */     try {
/* 20 */       System.out.println((new Date()).getTime());
/* 21 */       System.out.println(System.currentTimeMillis());
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 26 */     catch (Exception e) {
/* 27 */       System.out.println("...");
/* 28 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public static synchronized long getTransactionCode() {
/* 32 */     return System.currentTimeMillis();
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\TestClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */