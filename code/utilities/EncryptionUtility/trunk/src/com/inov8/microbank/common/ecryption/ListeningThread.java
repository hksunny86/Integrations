/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.net.ServerSocket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListeningThread
/*    */   extends Thread
/*    */ {
/* 18 */   public ServerSocket serverSocket = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int port;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ListeningThread(int port) {
/* 31 */     this.port = port;
/*    */     try {
/* 33 */       this.serverSocket = new ServerSocket(port);
/* 34 */       System.out.println("Server is Ready on port : " + port);
/* 35 */     } catch (Exception e) {
/* 36 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public static void main(String[] args) {
/*    */     try {
/* 41 */       int port = 8787;
/* 42 */       if (args != null && args.length > 0) {
/* 43 */         port = Integer.parseInt(args[0]);
/*    */       }
/* 45 */       (new ListeningThread(port)).start();
/* 46 */     } catch (Exception e) {
/* 47 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public void run() {
/*    */     try {
/*    */       while (true)
/* 53 */         (new RequestProcessor(new Request(this.serverSocket.accept()))).start(); 
/* 54 */     } catch (Exception e) {
/* 55 */       e.printStackTrace();
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\ListeningThread.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */