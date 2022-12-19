/*     */ package com.inov8.encryption.bulk;
/*     */ 
/*     */ import com.inov8.encryption.EncryptionUtil;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EncryptionProcess
/*     */ {
/*  15 */   Connection connection = null;
/*  16 */   private int startRange = 0;
/*  17 */   private int endRange = 0;
/*  18 */   private String selectQuery = null;
/*  19 */   private String updateQuery = null;
/*  20 */   private String encDec = "enc";
/*     */   public EncryptionProcess(Connection connection, int startRange, int endRange, String selectQuery, String updateQuery, String encDec) {
/*  22 */     this.connection = connection;
/*  23 */     this.startRange = startRange;
/*  24 */     this.endRange = endRange;
/*  25 */     this.selectQuery = selectQuery;
/*  26 */     this.updateQuery = updateQuery;
/*  27 */     this.encDec = encDec;
/*     */   }
/*     */   
/*     */   public void processEncryption() throws Exception {
/*     */     try {
/*  32 */       System.out.println("Inside Process..");
/*  33 */       EncyptionDAO encyptionDAO = new EncyptionDAO(this.connection, this.updateQuery);
/*  34 */       encyptionDAO.initializeBatch();
/*  35 */       ResultSet resultSet = encyptionDAO.loadData(this.startRange, this.endRange, this.selectQuery);
/*     */ 
/*     */ 
/*     */       
/*  39 */       int indexOfSet = this.updateQuery.indexOf(" SET ") + 5;
/*  40 */       int indexOfWhere = this.updateQuery.indexOf(" WHERE ");
/*  41 */       String updateColumns = this.updateQuery.substring(indexOfSet, indexOfWhere);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       int indexOfSelect = this.selectQuery.indexOf("SELECT") + 6;
/*  50 */       int indexOfFrom = this.selectQuery.indexOf("FROM");
/*  51 */       String columns = this.selectQuery.substring(indexOfSelect, indexOfFrom);
/*  52 */       String[] columnsArr = columns.split(",");
/*  53 */       String[] params = new String[columns.length()];
/*  54 */       String key = "";
/*  55 */       while (resultSet.next()) {
/*  56 */         int j = 0;
/*  57 */         for (int i = 0; i < columns.length(); i++) {
/*     */           
/*  59 */           params[j] = resultSet.getString(columnsArr[i].trim());
/*  60 */           if (this.encDec.equals("enc")) {
/*  61 */             params[j] = EncryptionUtil.encryptPin(params[i]);
/*     */           } else {
/*  63 */             params[j] = EncryptionUtil.decryptPin(params[i]);
/*     */           } 
/*  65 */           j++;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*  70 */         params[j] = key;
/*  71 */         encyptionDAO.addToBatch(params);
/*     */       } 
/*  73 */     } catch (Exception e) {
/*  74 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*  80 */       System.out.println("Process Started");
/*  81 */       processEncryption();
/*  82 */       System.out.println("Process Completed");
/*     */     }
/*  84 */     catch (Exception e) {
/*  85 */       System.err.println(e);
/*  86 */       e.printStackTrace();
/*     */       try {
/*  88 */         this.connection.rollback();
/*  89 */       } catch (Exception ee) {
/*  90 */         ee.printStackTrace();
/*     */       } 
/*     */     } finally {
/*     */       
/*  94 */       try { this.connection.commit();
/*  95 */         this.connection.close(); }
/*  96 */       catch (Exception e) { e.printStackTrace(); }
/*     */     
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 123 */       String updateQuery = "UPDATE ACCOUNT SET BALANCE_ENC = ?, CNIC_ENC = ? WHERE ACCOUNT_ID = ? AND BALANCE_ENC IS NOT NULL";
/* 124 */       int indexOfSet = updateQuery.indexOf(" SET ") + 5;
/* 125 */       int indexOfWhere = updateQuery.indexOf(" WHERE ");
/* 126 */       String updateColumns = updateQuery.substring(indexOfSet, indexOfWhere);
/* 127 */       System.out.println(updateColumns);
/* 128 */       String[] updateColumnsArr = updateColumns.split(",");
/* 129 */       for (int i = 0; i < updateColumnsArr.length; i++) {
/* 130 */         updateColumnsArr[i] = updateColumnsArr[i].trim();
/* 131 */         updateColumnsArr[i] = updateColumnsArr[i].split("=")[0].trim();
/*     */       } 
/* 133 */       System.out.println(updateColumnsArr);
/* 134 */     } catch (Exception e) {
/* 135 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\bulk\EncryptionProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */