/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import com.inov8.encryption.DESEncryption;
/*    */ import com.inov8.encryption.EncryptionUtil;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestProcessor
/*    */   extends Thread
/*    */ {
/* 15 */   private Request request = null;
/*    */   RequestProcessor(Request request) {
/* 17 */     this.request = request;
/*    */   }
/*    */   
/*    */   public void run() {
/* 21 */     Socket socket = this.request.getSocket();
/* 22 */     ObjectOutputStream objectOutputStream = null;
/* 23 */     ObjectInputStream objectInputStream = null;
/* 24 */     String response = "";
/* 25 */     String data = "";
/*    */     try {
/* 27 */       objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
/*    */       
/* 29 */       objectInputStream = new ObjectInputStream(socket.getInputStream());
/*    */       
/* 31 */       data = (String)objectInputStream.readObject();
/* 32 */       String encModeType = data.substring(0, data.indexOf("|"));
/* 33 */       String input = data.substring(data.indexOf("|") + 1);
/* 34 */       String[] encModeTypeArr = encModeType.split("-");
/* 35 */       String encryptionMode = encModeTypeArr[0];
/* 36 */       String encryptionType = encModeTypeArr[1];
/* 37 */       if (encryptionType.equalsIgnoreCase("Balance")) {
/* 38 */         if (encryptionMode.equalsIgnoreCase("E") || encryptionMode.equalsIgnoreCase("ENC")) {
/* 39 */           response = EncryptionUtil.encryptPin(input);
/*    */         } else {
/* 41 */           response = EncryptionUtil.decryptPin(input);
/*    */         } 
/* 43 */       } else if (encryptionType.equalsIgnoreCase("AccountNumber")) {
/* 44 */         if (encryptionMode.equalsIgnoreCase("E") || encryptionMode.equalsIgnoreCase("ENC")) {
/* 45 */           response = EncryptionUtil.encryptAccountNo(input);
/*    */         } else {
/* 47 */           response = EncryptionUtil.decryptAccountNo(input);
/*    */         } 
/* 49 */       } else if (encryptionType.equalsIgnoreCase("AccountInfo")) {
/* 50 */         DESEncryption desEncryption = DESEncryption.getInstance();
/* 51 */         byte[] keyBytes = toBinArray("0102030405060708090A0B0C0D0E0F100102030405060708");
/* 52 */         if (encryptionMode.equalsIgnoreCase("E") || encryptionMode.equalsIgnoreCase("ENC")) {
/* 53 */           response = desEncryption.encrypt(input, keyBytes, true);
/*    */         } else {
/* 55 */           response = desEncryption.decrypt(input, keyBytes, true);
/*    */         } 
/* 57 */       } else if (encryptionType.equalsIgnoreCase("UserDeviceAccount")) {
/* 58 */         if (encryptionMode.equalsIgnoreCase("E") || encryptionMode.equalsIgnoreCase("ENC")) {
/* 59 */           response = EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", input);
/*    */         } else {
/* 61 */           response = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", input);
/*    */         } 
/* 63 */       } else if (encryptionType.equalsIgnoreCase("OTP")) {
/*    */       
/*    */       } 
/* 66 */       response.getBytes("UTF-8");
/* 67 */       System.out.println("Response Sent..");
/* 68 */     } catch (Exception e) {
/* 69 */       e.printStackTrace();
/* 70 */       response = "0.0";
/*    */     } finally {
/*    */       try {
/* 73 */         objectOutputStream.writeObject(new String(response));
/* 74 */         System.out.println(response);
/* 75 */         objectOutputStream.flush();
/* 76 */         objectOutputStream.close();
/* 77 */         objectInputStream.close();
/* 78 */         socket.close();
/* 79 */       } catch (Exception e) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] toBinArray(String hexStr) {
/* 85 */     byte[] bArray = new byte[hexStr.length() / 2];
/* 86 */     for (int i = 0; i < bArray.length; i++) {
/* 87 */       bArray[i] = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 2), 16);
/*    */     }
/* 89 */     return bArray;
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\RequestProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */