/*    */ package com.inov8.microbank.common.ecryption;
/*    */ 
/*    */ import com.jtattoo.plaf.smart.SmartLookAndFeel;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Toolkit;
/*    */ import java.util.Properties;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncryptionController
/*    */ {
/*    */   private void setTheme() {
/*    */     try {
/* 17 */       Properties props = new Properties();
/*    */       
/* 19 */       props.put("logoString", "my company");
/* 20 */       props.put("licenseKey", "INSERT YOUR LICENSE KEY HERE");
/*    */       
/* 22 */       props.put("selectionBackgroundColor", "180 140 197");
/* 23 */       props.put("menuSelectionBackgroundColor", "180 240 197");
/*    */       
/* 25 */       props.put("controlColor", "218 254 230");
/* 26 */       props.put("controlColorLight", "218 254 230");
/* 27 */       props.put("controlColorDark", "180 240 197");
/*    */       
/* 29 */       props.put("buttonColor", "118 130 154");
/* 30 */       props.put("buttonColorLight", "255 255 255");
/* 31 */       props.put("buttonColorDark", "244 242 232");
/*    */       
/* 33 */       props.put("rolloverColor", "218 254 230");
/* 34 */       props.put("rolloverColorLight", "218 254 230");
/* 35 */       props.put("rolloverColorDark", "180 240 197");
/*    */       
/* 37 */       props.put("windowTitleForegroundColor", "0 0 0");
/* 38 */       props.put("windowTitleBackgroundColor", "180 240 197");
/* 39 */       props.put("windowTitleColorLight", "218 254 230");
/* 40 */       props.put("windowTitleColorDark", "180 240 197");
/* 41 */       props.put("windowBorderColor", "218 254 230");
/*    */ 
/*    */       
/* 44 */       SmartLookAndFeel.setCurrentTheme(props);
/*    */       
/* 46 */       UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
/* 47 */     } catch (Exception e) {
/* 48 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EncryptionController(String mode) {
/*    */     try {
/* 56 */       setTheme();
/* 57 */     } catch (Exception e) {
/* 58 */       e.printStackTrace();
/*    */     } 
/* 60 */     JFrame frame = null;
/* 61 */     if (mode.equals("B")) {
/* 62 */       frame = new MainFrameBulk();
/*    */     } else {
/* 64 */       frame = new MainFrame();
/*    */     } 
/* 66 */     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/* 67 */     Dimension frameSize = frame.getSize();
/* 68 */     if (frameSize.height > screenSize.height) {
/* 69 */       frameSize.height = screenSize.height;
/*    */     }
/* 71 */     if (frameSize.width > screenSize.width) {
/* 72 */       frameSize.width = screenSize.width;
/*    */     }
/* 74 */     frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
/* 75 */     frame.setDefaultCloseOperation(3);
/* 76 */     frame.setVisible(true);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {}
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\EncryptionController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */