/*     */ package com.inov8.microbank.common.ecryption;
/*     */ import com.inov8.encryption.DESEncryption;
/*     */ import com.inov8.encryption.EncoderUtils;
/*     */ import com.inov8.encryption.EncryptionUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ public class MainFrame extends JFrame {
/*  24 */   private ImageIcon encryptIcon = null; private static final String ACCOUNT_INF_KEY = "0102030405060708090A0B0C0D0E0F100102030405060708";
/*  25 */   private ImageIcon decryptIcon = null;
/*  26 */   private ImageIcon closeIcon = null;
/*  27 */   private ImageIcon copyIcon = null;
/*  28 */   private ImageIcon resetIcon = null;
/*     */   Image titleImage;
/*  30 */   private JButton encryptButton = null;
/*  31 */   private JButton close = null;
/*  32 */   private JLabel pageLabel = new JLabel();
/*  33 */   private JTextArea outpuTextField = new JTextArea();
/*  34 */   DefaultComboBoxModel pagesComboModel = new DefaultComboBoxModel();
/*  35 */   DefaultComboBoxModel extComboModel = new DefaultComboBoxModel();
/*  36 */   private JLabel pageLabel1 = new JLabel();
/*  37 */   private JLabel pageLabel2 = new JLabel();
/*     */   
/*  39 */   private JTextArea inputTextField = new JTextArea();
/*  40 */   private JButton decryptButton = new JButton();
/*  41 */   private JButton copyButton = new JButton();
/*  42 */   private JButton resetButton = new JButton();
/*  43 */   JComboBox encryptionCombo = new JComboBox();
/*  44 */   private DefaultComboBoxModel procFuncComboModel = new DefaultComboBoxModel();
/*     */   
/*     */   public MainFrame() {
/*     */     try {
/*  48 */       jbInit();
/*  49 */     } catch (Exception e) {
/*  50 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void jbInit() throws Exception {
/*  55 */     this.titleImage = getToolkit().getImage(MainFrame.class.getResource("main.png"));
/*  56 */     setIconImage(this.titleImage);
/*  57 */     getContentPane().setLayout((LayoutManager)null);
/*  58 */     setTitle("Encryption Utility");
/*  59 */     setResizable(false);
/*  60 */     setBackground(new Color(145, 152, 245));
/*  61 */     getContentPane().setBackground(new Color(145, 152, 245));
/*  62 */     URL closeImgURL = getClass().getResource("close.png");
/*  63 */     URL encImgURL = getClass().getResource("encrypted.png");
/*  64 */     URL decImgURL = getClass().getResource("decrypted.png");
/*  65 */     URL copyImgURL = getClass().getResource("copy.png");
/*  66 */     URL resetImgURL = getClass().getResource("reset.png");
/*  67 */     this.encryptIcon = new ImageIcon(encImgURL);
/*  68 */     this.decryptIcon = new ImageIcon(decImgURL);
/*  69 */     this.closeIcon = new ImageIcon(closeImgURL);
/*  70 */     this.copyIcon = new ImageIcon(copyImgURL);
/*  71 */     this.resetIcon = new ImageIcon(resetImgURL);
/*  72 */     this.close = new JButton(this.closeIcon);
/*  73 */     this.resetButton = new JButton(this.resetIcon);
/*  74 */     this.decryptButton = new JButton(this.decryptIcon);
/*  75 */     this.encryptButton = new JButton(this.encryptIcon);
/*  76 */     this.copyButton = new JButton(this.copyIcon);
/*  77 */     this.resetButton = new JButton(this.resetIcon);
/*  78 */     this.encryptButton.setBackground(new Color(145, 152, 245));
/*  79 */     this.decryptButton.setBackground(new Color(145, 152, 245));
/*  80 */     this.copyButton.setBackground(new Color(145, 152, 245));
/*  81 */     this.close.setBackground(new Color(145, 152, 245));
/*  82 */     this.resetButton.setBackground(new Color(145, 152, 245));
/*     */     
/*  84 */     this.encryptButton.setBounds(new Rectangle(35, 285, 45, 40));
/*  85 */     this.encryptButton.setToolTipText("EnCrypt");
/*  86 */     this.encryptButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  88 */             MainFrame.this.encryptButton_actionPerformed(e);
/*     */           }
/*     */         });
/*  91 */     this.decryptButton.setToolTipText("DeCrypt");
/*  92 */     this.resetButton.setToolTipText("Reset");
/*  93 */     this.copyButton.setToolTipText("Copy Text To ClipBoard");
/*  94 */     this.close.setBounds(new Rectangle(235, 285, 45, 40));
/*  95 */     this.close.setToolTipText("Close Encryption Utility");
/*  96 */     this.close.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  98 */             MainFrame.this.close_actionPerformed(e);
/*     */           }
/*     */         });
/* 101 */     this.pageLabel.setText("Input:");
/* 102 */     this.pageLabel.setForeground(Color.white);
/* 103 */     this.pageLabel1.setBounds(new Rectangle(35, 5, 120, 20));
/* 104 */     this.encryptionCombo.setBounds(new Rectangle(35, 25, 245, 25));
/* 105 */     this.pageLabel.setBounds(new Rectangle(35, 55, 120, 20));
/* 106 */     setSize(new Dimension(620, 370));
/* 107 */     this.inputTextField.setBounds(new Rectangle(35, 75, 550, 80));
/* 108 */     this.outpuTextField.setBounds(new Rectangle(35, 190, 550, 80));
/* 109 */     this.procFuncComboModel.addElement("Account Info");
/* 110 */     this.procFuncComboModel.addElement("Account Number");
/* 111 */     this.procFuncComboModel.addElement("Balance");
/* 112 */     this.procFuncComboModel.addElement("OTP");
/* 113 */     this.procFuncComboModel.addElement("User Device Account");
/* 114 */     this.encryptionCombo.setModel(this.procFuncComboModel);
/* 115 */     this.outpuTextField.setEditable(false);
/* 116 */     this.pageLabel1.setText("Encryption:");
/* 117 */     this.pageLabel1.setForeground(Color.white);
/* 118 */     this.pageLabel2.setText("Output:");
/* 119 */     this.pageLabel2.setBounds(new Rectangle(35, 170, 120, 20));
/* 120 */     this.pageLabel2.setForeground(Color.white);
/* 121 */     this.decryptButton.setBounds(new Rectangle(85, 285, 45, 40));
/* 122 */     this.decryptButton.setForeground(Color.white);
/* 123 */     this.decryptButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 125 */             MainFrame.this.decryptButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 128 */     this.copyButton.setBounds(new Rectangle(135, 285, 45, 40));
/* 129 */     this.copyButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 131 */             MainFrame.this.copyButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 134 */     this.encryptionCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 136 */             MainFrame.this.tableCombo_actionPerformed(e);
/*     */           }
/*     */         });
/* 139 */     this.resetButton.setBounds(new Rectangle(185, 285, 45, 40));
/* 140 */     this.resetButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 142 */             MainFrame.this.resetButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 145 */     getContentPane().add(this.resetButton, (Object)null);
/* 146 */     getContentPane().add(this.copyButton, (Object)null);
/* 147 */     getContentPane().add(this.decryptButton, (Object)null);
/* 148 */     getContentPane().add(this.inputTextField, (Object)null);
/* 149 */     getContentPane().add(this.encryptionCombo, (Object)null);
/* 150 */     getContentPane().add(this.pageLabel1, (Object)null);
/* 151 */     getContentPane().add(this.pageLabel2, (Object)null);
/* 152 */     getContentPane().add(this.outpuTextField, (Object)null);
/*     */     
/* 154 */     getContentPane().add(this.pageLabel, (Object)null);
/*     */     
/* 156 */     getContentPane().add(this.close, (Object)null);
/*     */     
/* 158 */     getContentPane().add(this.encryptButton, (Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> getListFormModel(DefaultComboBoxModel<E> comboModel) {
/* 163 */     Map<String, String> map = new HashMap<>();
/* 164 */     for (int index = 0; index < comboModel.getSize(); index++) {
/* 165 */       map.put(comboModel.getElementAt(index).toString(), "");
/*     */     }
/* 167 */     return map;
/*     */   }
/*     */   
/*     */   private void close_actionPerformed(ActionEvent e) {
/* 171 */     int i = JOptionPane.showConfirmDialog(this, "Encryption Utility will be closed, continue?");
/* 172 */     if (i == 0)
/* 173 */       System.exit(1); 
/*     */   }
/*     */   
/*     */   public static byte[] toBinArray(String hexStr) {
/* 177 */     byte[] bArray = new byte[hexStr.length() / 2];
/* 178 */     for (int i = 0; i < bArray.length; i++) {
/* 179 */       bArray[i] = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 2), 16);
/*     */     }
/* 181 */     return bArray;
/*     */   }
/*     */   private void encryptButton_actionPerformed(ActionEvent e) {
/* 184 */     String encryptedString = "";
/* 185 */     String input = this.inputTextField.getText();
/*     */     try {
/* 187 */       if (this.encryptionCombo.getSelectedItem().toString().equals("Balance")) {
/* 188 */         encryptedString = EncryptionUtil.encryptPin(input);
/* 189 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("Account Number")) {
/* 190 */         encryptedString = EncryptionUtil.encryptAccountNo(input);
/* 191 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("Account Info")) {
/* 192 */         DESEncryption desEncryption = DESEncryption.getInstance();
/* 193 */         byte[] keyBytes = toBinArray("0102030405060708090A0B0C0D0E0F100102030405060708");
/* 194 */         encryptedString = desEncryption.encrypt(input, keyBytes, true);
/* 195 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("OTP")) {
/* 196 */         encryptedString = EncoderUtils.encodeToSha(input);
/* 197 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("User Device Account")) {
/* 198 */         encryptedString = EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", input);
/*     */       } 
/* 200 */     } catch (Exception ee) {
/* 201 */       ee.printStackTrace();
/*     */     } 
/* 203 */     this.outpuTextField.setText(encryptedString);
/*     */   }
/*     */   
/*     */   private void decryptButton_actionPerformed(ActionEvent e) {
/* 207 */     String decryptedString = "";
/* 208 */     String input = this.inputTextField.getText();
/*     */     try {
/* 210 */       if (this.encryptionCombo.getSelectedItem().toString().equals("Balance")) {
/* 211 */         decryptedString = EncryptionUtil.decryptPin(input);
/* 212 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("Account Number")) {
/* 213 */         decryptedString = EncryptionUtil.decryptAccountNo(input);
/* 214 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("Account Info")) {
/* 215 */         DESEncryption desEncryption = DESEncryption.getInstance();
/* 216 */         byte[] keyBytes = toBinArray("0102030405060708090A0B0C0D0E0F100102030405060708");
/* 217 */         decryptedString = desEncryption.decrypt(input, keyBytes, true);
/* 218 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("User Device Account")) {
/* 219 */         decryptedString = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", input);
/* 220 */       } else if (this.encryptionCombo.getSelectedItem().toString().equals("OTP")) {
/* 221 */         JOptionPane.showMessageDialog(this, "No Decryption for SHA.");
/*     */       } 
/* 223 */     } catch (Exception ee) {}
/*     */ 
/*     */     
/* 226 */     this.outpuTextField.setText(decryptedString);
/*     */   }
/*     */   
/*     */   private void copyButton_actionPerformed(ActionEvent e) {
/* 230 */     TextTransfer textTransfer = new TextTransfer();
/* 231 */     textTransfer.setClipboardContents(this.outpuTextField.getText());
/*     */   }
/*     */   
/*     */   private void resetButton_actionPerformed(ActionEvent e) {
/* 235 */     this.inputTextField.setText("");
/* 236 */     this.outpuTextField.setText("");
/*     */   }
/*     */   void tableCombo_actionPerformed(ActionEvent e) {
/* 239 */     this.inputTextField.setText("");
/* 240 */     this.outpuTextField.setText("");
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\MainFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */