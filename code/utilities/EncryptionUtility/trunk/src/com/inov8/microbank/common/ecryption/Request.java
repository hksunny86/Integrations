/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Request
/*    */ {
/*  9 */   private Socket socket = null;
/*    */   public Request(Socket socket) {
/* 11 */     setSocket(socket);
/*    */   }
/*    */   
/*    */   public Socket getSocket() {
/* 15 */     return this.socket;
/*    */   }
/*    */   
/*    */   public void setSocket(Socket socket) {
/* 19 */     this.socket = socket;
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\Request.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */