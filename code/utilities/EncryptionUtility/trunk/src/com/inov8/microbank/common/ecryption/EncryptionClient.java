/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncryptionClient
/*    */ {
/* 15 */   private static String sessionIdStatic = "";
/* 16 */   private static Map<String, String> encryptionMap = new HashMap<>();
/* 17 */   private static Map<String, String> decryptionMap = new HashMap<>();
/*    */   
/*    */   public static void main(String[] args) {
/*    */     try {
/* 21 */       String output = doDecrypt("", "Uy4MRmlpgzOUNuv7PwZ9LQ==", "UserDeviceAccount", "localhost", 8787);
/*    */ 
/*    */       
/* 24 */       output = doEncrypt("", "2009", "UserDeviceAccount", "localhost", 8787);
/*    */ 
/*    */       
/* 27 */       System.out.println(output);
/* 28 */     } catch (Exception e) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static synchronized String doDecrypt(String sessionId, String value, String type, String ipAddress, int port) throws Exception {
/* 33 */     String response = "";
/* 34 */     if (!sessionId.equalsIgnoreCase(sessionIdStatic)) {
/* 35 */       decryptionMap.clear();
/* 36 */       sessionIdStatic = sessionId;
/*    */     } 
/* 38 */     if (decryptionMap.containsKey(value)) {
/* 39 */       response = decryptionMap.get(value);
/*    */     } else {
/* 41 */       response = sendRequestActual("DEC-" + type + "|" + value, ipAddress, port);
/* 42 */       if (response.indexOf(" ") != -1 && response.indexOf("java") != -1)
/* 43 */         decryptionMap.put(value, response); 
/*    */     } 
/* 45 */     return response;
/*    */   }
/*    */   public static synchronized String doEncrypt(String sessionId, String value, String type, String ipAddress, int port) throws Exception {
/* 48 */     String response = "";
/* 49 */     if (!sessionId.equalsIgnoreCase(sessionIdStatic)) {
/* 50 */       encryptionMap.clear();
/* 51 */       sessionIdStatic = sessionId;
/*    */     } 
/* 53 */     if (encryptionMap.containsKey(value)) {
/* 54 */       response = encryptionMap.get(value);
/*    */     } else {
/* 56 */       response = sendRequestActual("ENC-" + type + "|" + value, ipAddress, port);
/* 57 */       if (response.indexOf(" ") != -1 && response.indexOf("java") != -1)
/* 58 */         encryptionMap.put(value, response); 
/*    */     } 
/* 60 */     return response;
/*    */   }
/*    */   
/*    */   public static synchronized String sendRequestActual(String data, String ipAddress, int port) throws Exception {
/* 64 */     Socket socket = null;
/* 65 */     ObjectOutputStream objectOutputStream = null;
/* 66 */     ObjectInputStream objectInputStream = null;
/* 67 */     String processedData = "";
/*    */     try {
/* 69 */       data.getBytes("UTF-8");
/* 70 */       socket = new Socket();
/* 71 */       SocketAddress serverAddress = new InetSocketAddress(ipAddress, port);
/* 72 */       socket.setReuseAddress(true);
/* 73 */       socket.connect(serverAddress);
/* 74 */       objectInputStream = new ObjectInputStream(socket.getInputStream());
/* 75 */       objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
/* 76 */       objectOutputStream.writeObject(new String(data));
/* 77 */       processedData = "";
/* 78 */       processedData = (String)objectInputStream.readObject();
/* 79 */       System.out.println(processedData);
/* 80 */     } catch (Exception ex) {
/* 81 */       System.out.println("---Exception occurred @ Client");
/* 82 */       ex.printStackTrace();
/* 83 */       throw ex;
/*    */     } finally {
/* 85 */       if (socket != null && !socket.isClosed()) {
/* 86 */         socket.setReuseAddress(true);
/* 87 */         socket.close();
/*    */       } 
/* 89 */       if (objectInputStream != null) {
/* 90 */         objectInputStream.close();
/*    */       }
/* 92 */       if (objectOutputStream != null) {
/* 93 */         objectOutputStream.close();
/*    */       }
/*    */     } 
/* 96 */     return processedData;
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\EncryptionClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */