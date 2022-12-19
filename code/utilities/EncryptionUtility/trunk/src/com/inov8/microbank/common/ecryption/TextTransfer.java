/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.ClipboardOwner;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.awt.datatransfer.Transferable;
/*    */ import java.awt.datatransfer.UnsupportedFlavorException;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public final class TextTransfer
/*    */   implements ClipboardOwner {
/*    */   public static void main(String... aArguments) {
/* 15 */     TextTransfer textTransfer = new TextTransfer();
/*    */ 
/*    */     
/* 18 */     System.out.println("Clipboard contains:" + textTransfer.getClipboardContents());
/*    */ 
/*    */     
/* 21 */     textTransfer.setClipboardContents("blah, blah, blah");
/* 22 */     System.out.println("Clipboard contains:" + textTransfer.getClipboardContents());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void lostOwnership(Clipboard aClipboard, Transferable aContents) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClipboardContents(String aString) {
/* 37 */     StringSelection stringSelection = new StringSelection(aString);
/* 38 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 39 */     clipboard.setContents(stringSelection, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClipboardContents() {
/* 49 */     String result = "";
/* 50 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/*    */     
/* 52 */     Transferable contents = clipboard.getContents(null);
/* 53 */     boolean hasTransferableText = (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor));
/*    */ 
/*    */ 
/*    */     
/* 57 */     if (hasTransferableText) {
/*    */       try {
/* 59 */         result = (String)contents.getTransferData(DataFlavor.stringFlavor);
/*    */       }
/* 61 */       catch (UnsupportedFlavorException ex) {
/*    */         
/* 63 */         System.out.println(ex);
/* 64 */         ex.printStackTrace();
/*    */       }
/* 66 */       catch (IOException ex) {
/* 67 */         System.out.println(ex);
/* 68 */         ex.printStackTrace();
/*    */       } 
/*    */     }
/* 71 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\TextTransfer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */