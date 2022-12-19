/*    */ package com.inov8.encryption.bulk;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncyptionDAO
/*    */ {
/*    */   private Connection connection;
/*    */   private PreparedStatement preparedStatement;
/*    */   private int batchCounter;
/*    */   private boolean executed;
/*    */   private boolean initialized;
/* 17 */   private String updateQuery = null;
/*    */   public EncyptionDAO(Connection connection, String updateQuery) {
/* 19 */     this.connection = connection;
/* 20 */     this.updateQuery = updateQuery;
/*    */   }
/*    */   
/*    */   public ResultSet loadData(int startRange, int endRange, String query) throws Exception, Exception {
/* 24 */     int indexOfSelect = query.indexOf("SELECT") + 6;
/* 25 */     int indexOfFrom = query.indexOf("FROM");
/* 26 */     String columns = query.substring(indexOfSelect, indexOfFrom);
/* 27 */     query = query.replace(columns, " rownum rn, " + columns);
/* 28 */     query = "select rn, " + columns + " from ( " + query + ") where rn >=" + startRange + " and rn<=" + endRange;
/* 29 */     System.out.println(query);
/* 30 */     PreparedStatement preparedStatement = this.connection.prepareStatement(query);
/* 31 */     preparedStatement.setFetchSize(5000);
/* 32 */     ResultSet resultSet = preparedStatement.executeQuery();
/* 33 */     return resultSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initializeBatch() throws Exception {
/* 38 */     StringBuilder query = new StringBuilder();
/* 39 */     query.append(this.updateQuery);
/* 40 */     System.out.println(query.toString());
/* 41 */     this.preparedStatement = this.connection.prepareStatement(query.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void closeBatch() throws Exception {
/* 47 */     if (!this.executed)
/*    */     {
/* 49 */       executeBatch();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void executeBatch() throws Exception {
/* 56 */     this.preparedStatement.executeBatch();
/* 57 */     this.preparedStatement.clearBatch();
/* 58 */     this.preparedStatement.close();
/* 59 */     this.connection.commit();
/* 60 */     this.preparedStatement = null;
/* 61 */     this.executed = true;
/* 62 */     this.batchCounter = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToBatch(String[] params) throws Exception {
/* 68 */     for (int i = 0; i < params.length; i++) {
/* 69 */       this.preparedStatement.setString(i + 1, params[i]);
/*    */     }
/* 71 */     this.preparedStatement.addBatch();
/* 72 */     this.executed = false;
/* 73 */     this.batchCounter++;
/* 74 */     if (this.batchCounter > 1000) {
/* 75 */       executeBatch();
/* 76 */       initializeBatch();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\bulk\EncyptionDAO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */