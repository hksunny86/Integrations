/*    */ package com.intellij.uiDesigner.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.swing.JComponent;
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
/*    */ public final class SupportCode
/*    */ {
/*    */   public static TextWithMnemonic parseText(String textWithMnemonic) {
/* 30 */     if (textWithMnemonic == null) {
/* 31 */       throw new IllegalArgumentException("textWithMnemonic cannot be null");
/*    */     }
/*    */     
/* 34 */     int index = -1;
/* 35 */     StringBuffer plainText = new StringBuffer();
/* 36 */     for (int i = 0; i < textWithMnemonic.length(); i++) {
/* 37 */       char ch = textWithMnemonic.charAt(i);
/* 38 */       if (ch == '&') {
/* 39 */         i++;
/* 40 */         if (i >= textWithMnemonic.length()) {
/*    */           break;
/*    */         }
/* 43 */         ch = textWithMnemonic.charAt(i);
/* 44 */         if (ch != '&') {
/* 45 */           index = plainText.length();
/*    */         }
/*    */       } 
/* 48 */       plainText.append(ch);
/*    */     } 
/*    */     
/* 51 */     return new TextWithMnemonic(plainText.toString(), index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class TextWithMnemonic
/*    */   {
/*    */     public final String myText;
/*    */ 
/*    */     
/*    */     public final int myMnemonicIndex;
/*    */ 
/*    */     
/*    */     private TextWithMnemonic(String text, int index) {
/* 65 */       if (text == null) {
/* 66 */         throw new IllegalArgumentException("text cannot be null");
/*    */       }
/* 68 */       if (index != -1 && (index < 0 || index >= text.length())) {
/* 69 */         throw new IllegalArgumentException("wrong index: " + index + "; text = '" + text + "'");
/*    */       }
/* 71 */       this.myText = text;
/* 72 */       this.myMnemonicIndex = index;
/*    */     }
/*    */     
/*    */     public char getMnemonicChar() {
/* 76 */       if (this.myMnemonicIndex == -1) {
/* 77 */         throw new IllegalStateException("text doesn't contain mnemonic");
/*    */       }
/* 79 */       return Character.toUpperCase(this.myText.charAt(this.myMnemonicIndex));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setDisplayedMnemonicIndex(JComponent component, int index) {
/*    */     try {
/* 89 */       Method method = component.getClass().getMethod("setDisplayedMnemonicIndex", new Class[] { int.class });
/* 90 */       method.setAccessible(true);
/* 91 */       method.invoke(component, new Object[] { new Integer(index) });
/*    */     }
/* 93 */     catch (Exception e) {}
/*    */   }
/*    */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\intelli\\uiDesigner\core\SupportCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */