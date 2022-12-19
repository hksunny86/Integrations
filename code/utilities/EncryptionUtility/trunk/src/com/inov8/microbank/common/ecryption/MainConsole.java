/*     */ package com.inov8.microbank.common.ecryption;
/*     */ 
/*     */ import com.inov8.encryption.EncryptionUtil;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Scanner;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MainConsole
/*     */ {
/*     */   public static void main(String[] args) {
/*     */     try {
/*  21 */       System.out.println(EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", "OCBqdSCtGPAd9HBC8OhrlQ=="));
/*  22 */     } catch (Exception e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     String param = "I";
/*  31 */     if (args != null && args.length > 0) {
/*  32 */       param = args[0].toUpperCase();
/*     */     }
/*     */     try {
/*  35 */       Security.addProvider((Provider)new BouncyCastleProvider());
/*  36 */     } catch (Exception e) {
/*  37 */       e.printStackTrace();
/*     */     } 
/*  39 */     System.out.println("::: Starting Utility..");
/*  40 */     if (param.equals("C")) {
/*  41 */       MainConsole mainConsole = new MainConsole();
/*  42 */       mainConsole.console(args);
/*     */     } else {
/*  44 */       new EncryptionController(param);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void console(String[] args) {
/*     */     try {
/*  50 */       System.out.println("===========================Installing Driver===========================");
/*  51 */       Security.addProvider((Provider)new BouncyCastleProvider());
/*     */       
/*  53 */       byte[] mKeyData = new byte[16];
/*  54 */       byte[] mIv = new byte[8];
/*     */       
/*  56 */       SecretKeySpec KS = new SecretKeySpec(mKeyData, "Blowfish");
/*     */       
/*  58 */       Cipher cipher = Cipher.getInstance("Blowfish/CBC/ZeroBytePadding");
/*  59 */       cipher.init(1, KS, new IvParameterSpec(mIv));
/*  60 */       System.out.println("===========================Installing Driver===========================");
/*  61 */     } catch (Exception e) {
/*  62 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/*  66 */     Scanner in = new Scanner(System.in);
/*     */ 
/*     */     
/*  69 */     System.out.println("\n\n\n\t\t:==================================================:");
/*  70 */     System.out.println("\t\t:                                                  :");
/*  71 */     System.out.println("\t\t:              Microbank Encryption Manager             :");
/*  72 */     System.out.println("\t\t:       Please follow the below instructions       :");
/*  73 */     System.out.println("\t\t:                                                  :");
/*  74 */     System.out.println("\t\t:==================================================:");
/*  75 */     System.out.print("\n\n\t\tProvide 1 to Encrypt String and 2 to Decrypt: ");
/*  76 */     String option = in.nextLine();
/*  77 */     String optionStr = "Encrypt";
/*  78 */     if (!option.equals("1")) {
/*  79 */       optionStr = "Decrypt";
/*     */     }
/*  81 */     System.out.print("\t\tProvide the String to " + optionStr + " : ");
/*  82 */     String input = in.nextLine();
/*  83 */     String output = "";
/*  84 */     if (option.equals("1")) {
/*  85 */       output = EncryptionHelper.encrypt(input);
/*  86 */       System.out.println("\t\tEncrypted string is: " + output);
/*     */     } else {
/*  88 */       output = EncryptionHelper.decrypt(input);
/*  89 */       System.out.println("\t\tDecrypted string is: " + output);
/*     */     } 
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
/* 101 */     in.close();
/* 102 */     if (args != null && args.length > 1) {
/* 103 */       String fileName = "";
/*     */       try {
/* 105 */         fileName = args[1] + "/encryption_results.txt";
/* 106 */         PrintStream stream = new PrintStream(new File(fileName));
/* 107 */         stream.print(output);
/* 108 */         stream.close();
/* 109 */         System.out.println("\n***Text has also been written to file " + fileName + " ***");
/* 110 */       } catch (Exception ex) {
/* 111 */         System.out.println("\nWritting to file " + fileName + " failed.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\MainConsole.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */