/*     */ package com.inov8.microbank.common.ecryption;
/*     */ import com.inov8.encryption.bulk.EncryptionProcess;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.net.URL;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import oracle.jdbc.driver.OracleDriver;
/*     */ import oracle.jdbc.pool.OracleDataSource;
/*     */ 
/*     */ public class MainFrameBulk extends JFrame {
/*  29 */   private ImageIcon encryptIcon = null; private static final String ACCOUNT_INF_KEY = "0102030405060708090A0B0C0D0E0F100102030405060708";
/*  30 */   private ImageIcon decryptIcon = null;
/*  31 */   private ImageIcon closeIcon = null;
/*  32 */   private ImageIcon copyIcon = null;
/*  33 */   private ImageIcon resetIcon = null;
/*     */   Image titleImage;
/*  35 */   private JButton encryptButton = null;
/*  36 */   private JButton close = null;
/*  37 */   private JLabel pageLabel = new JLabel();
/*  38 */   private JTextArea outpuTextField = new JTextArea();
/*  39 */   DefaultComboBoxModel pagesComboModel = new DefaultComboBoxModel();
/*  40 */   DefaultComboBoxModel extComboModel = new DefaultComboBoxModel();
/*  41 */   private JLabel encryptionTitle = new JLabel();
/*  42 */   private JLabel urlTitle = new JLabel();
/*     */   
/*  44 */   private JLabel pageLabel2 = new JLabel();
/*  45 */   private JTextField urlTextField = new JTextField();
/*     */   
/*  47 */   private JTextArea inputTextField = new JTextArea();
/*  48 */   private JButton decryptButton = new JButton();
/*  49 */   private JButton copyButton = new JButton();
/*  50 */   private JButton resetButton = new JButton();
/*  51 */   JComboBox encryptionCombo = new JComboBox();
/*  52 */   private DefaultComboBoxModel procFuncComboModel = new DefaultComboBoxModel();
/*     */   
/*     */   public MainFrameBulk() {
/*     */     try {
/*  56 */       jbInit();
/*  57 */     } catch (Exception e) {
/*  58 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void jbInit() throws Exception {
/*  63 */     this.titleImage = getToolkit().getImage(MainFrame.class.getResource("main.png"));
/*  64 */     setIconImage(this.titleImage);
/*  65 */     getContentPane().setLayout((LayoutManager)null);
/*  66 */     setTitle("Encryption Utility Bulk");
/*  67 */     setResizable(false);
/*  68 */     setBackground(new Color(145, 152, 245));
/*  69 */     getContentPane().setBackground(new Color(145, 152, 245));
/*  70 */     URL closeImgURL = getClass().getResource("close.png");
/*  71 */     URL encImgURL = getClass().getResource("encrypted.png");
/*  72 */     URL decImgURL = getClass().getResource("decrypted.png");
/*  73 */     URL copyImgURL = getClass().getResource("copy.png");
/*  74 */     URL resetImgURL = getClass().getResource("reset.png");
/*  75 */     this.encryptIcon = new ImageIcon(encImgURL);
/*  76 */     this.decryptIcon = new ImageIcon(decImgURL);
/*  77 */     this.closeIcon = new ImageIcon(closeImgURL);
/*  78 */     this.copyIcon = new ImageIcon(copyImgURL);
/*  79 */     this.resetIcon = new ImageIcon(resetImgURL);
/*  80 */     this.close = new JButton(this.closeIcon);
/*  81 */     this.resetButton = new JButton(this.resetIcon);
/*  82 */     this.decryptButton = new JButton(this.decryptIcon);
/*  83 */     this.encryptButton = new JButton(this.encryptIcon);
/*  84 */     this.copyButton = new JButton(this.copyIcon);
/*  85 */     this.resetButton = new JButton(this.resetIcon);
/*  86 */     this.encryptButton.setBackground(new Color(145, 152, 245));
/*  87 */     this.decryptButton.setBackground(new Color(145, 152, 245));
/*  88 */     this.copyButton.setBackground(new Color(145, 152, 245));
/*  89 */     this.close.setBackground(new Color(145, 152, 245));
/*  90 */     this.resetButton.setBackground(new Color(145, 152, 245));
/*     */     
/*  92 */     this.encryptButton.setBounds(new Rectangle(35, 285, 45, 40));
/*  93 */     this.encryptButton.setToolTipText("EnCrypt");
/*  94 */     this.encryptButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  96 */             MainFrameBulk.this.encryptButton_actionPerformed(e);
/*     */           }
/*     */         });
/*  99 */     this.decryptButton.setToolTipText("DeCrypt");
/* 100 */     this.resetButton.setToolTipText("Reset");
/* 101 */     this.copyButton.setToolTipText("Copy Text To ClipBoard");
/* 102 */     this.close.setBounds(new Rectangle(235, 285, 45, 40));
/* 103 */     this.close.setToolTipText("Close Encryption Utility");
/* 104 */     this.close.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 106 */             MainFrameBulk.this.close_actionPerformed(e);
/*     */           }
/*     */         });
/* 109 */     this.pageLabel.setText("Select Query:");
/* 110 */     this.pageLabel.setForeground(Color.white);
/* 111 */     this.encryptionTitle.setBounds(new Rectangle(35, 5, 120, 20));
/* 112 */     this.encryptionCombo.setBounds(new Rectangle(35, 25, 120, 25));
/* 113 */     this.urlTitle.setBounds(new Rectangle(160, 5, 120, 20));
/* 114 */     this.urlTextField.setBounds(new Rectangle(160, 25, 440, 25));
/* 115 */     this.urlTextField.setText("<DB_USER>/<DB_PASSWORD>@jdbc:oracle:thin:@<DB_HOST>:<DB_PORT>:<SID>");
/* 116 */     this.pageLabel.setBounds(new Rectangle(35, 55, 120, 20));
/* 117 */     setSize(new Dimension(620, 370));
/* 118 */     this.inputTextField.setBounds(new Rectangle(35, 75, 550, 80));
/* 119 */     this.outpuTextField.setBounds(new Rectangle(35, 190, 550, 80));
/* 120 */     this.procFuncComboModel.addElement("Account Info");
/*     */     
/* 122 */     this.procFuncComboModel.addElement("Balance");
/*     */ 
/*     */     
/* 125 */     this.encryptionCombo.setModel(this.procFuncComboModel);
/*     */     
/* 127 */     this.encryptionTitle.setText("Encryption:");
/* 128 */     this.encryptionTitle.setForeground(Color.white);
/* 129 */     this.urlTitle.setText("URL:");
/* 130 */     this.urlTitle.setForeground(Color.white);
/* 131 */     this.pageLabel2.setText("Update Query:");
/* 132 */     this.pageLabel2.setBounds(new Rectangle(35, 170, 120, 20));
/* 133 */     this.pageLabel2.setForeground(Color.white);
/* 134 */     this.decryptButton.setBounds(new Rectangle(85, 285, 45, 40));
/* 135 */     this.decryptButton.setForeground(Color.white);
/* 136 */     this.decryptButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 138 */             MainFrameBulk.this.decryptButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 141 */     this.copyButton.setBounds(new Rectangle(135, 285, 45, 40));
/* 142 */     this.copyButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 144 */             MainFrameBulk.this.copyButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 147 */     this.encryptionCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 149 */             MainFrameBulk.this.tableCombo_actionPerformed(e);
/*     */           }
/*     */         });
/* 152 */     this.resetButton.setBounds(new Rectangle(185, 285, 45, 40));
/* 153 */     this.resetButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/* 155 */             MainFrameBulk.this.resetButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 158 */     getContentPane().add(this.resetButton, (Object)null);
/* 159 */     getContentPane().add(this.copyButton, (Object)null);
/* 160 */     getContentPane().add(this.decryptButton, (Object)null);
/* 161 */     getContentPane().add(this.inputTextField, (Object)null);
/* 162 */     getContentPane().add(this.encryptionCombo, (Object)null);
/* 163 */     getContentPane().add(this.urlTextField, (Object)null);
/* 164 */     getContentPane().add(this.encryptionTitle, (Object)null);
/* 165 */     getContentPane().add(this.urlTitle, (Object)null);
/* 166 */     getContentPane().add(this.pageLabel2, (Object)null);
/* 167 */     getContentPane().add(this.outpuTextField, (Object)null);
/*     */     
/* 169 */     getContentPane().add(this.pageLabel, (Object)null);
/*     */     
/* 171 */     getContentPane().add(this.close, (Object)null);
/*     */     
/* 173 */     getContentPane().add(this.encryptButton, (Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> getListFormModel(DefaultComboBoxModel<E> comboModel) {
/* 178 */     Map<String, String> map = new HashMap<>();
/* 179 */     for (int index = 0; index < comboModel.getSize(); index++) {
/* 180 */       map.put(comboModel.getElementAt(index).toString(), "");
/*     */     }
/* 182 */     return map;
/*     */   }
/*     */   
/*     */   private void close_actionPerformed(ActionEvent e) {
/* 186 */     int i = JOptionPane.showConfirmDialog(this, "Encryption Utility will be closed, continue?");
/* 187 */     if (i == 0)
/* 188 */       System.exit(1); 
/*     */   }
/*     */   
/*     */   public static byte[] toBinArray(String hexStr) {
/* 192 */     byte[] bArray = new byte[hexStr.length() / 2];
/* 193 */     for (int i = 0; i < bArray.length; i++) {
/* 194 */       bArray[i] = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 2), 16);
/*     */     }
/* 196 */     return bArray;
/*     */   }
/*     */   private void encryptButton_actionPerformed(ActionEvent e) {
/* 199 */     String encryptedString = "";
/* 200 */     String input = this.inputTextField.getText();
/*     */     
/*     */     try {
/* 203 */       (new EncryptionProcess(getDBConnection(this.urlTextField.getText()), 1, 10000, this.inputTextField.getText().toUpperCase(), this.outpuTextField.getText().toUpperCase(), "enc")).run();
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
/*     */     }
/* 222 */     catch (Exception ee) {
/* 223 */       ee.printStackTrace();
/*     */     } 
/* 225 */     this.outpuTextField.setText(encryptedString);
/*     */   }
/*     */   
/*     */   private void decryptButton_actionPerformed(ActionEvent e) {
/* 229 */     String decryptedString = "";
/* 230 */     String input = this.inputTextField.getText();
/*     */     try {
/* 232 */       EncryptionProcess encryptionProcess = new EncryptionProcess(getDBConnection(this.urlTextField.getText()), 1, 10000, this.inputTextField.getText().toUpperCase(), this.outpuTextField.getText().toUpperCase(), "dec");
/* 233 */       encryptionProcess.run();
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
/*     */     }
/* 248 */     catch (Exception ee) {
/* 249 */       ee.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyButton_actionPerformed(ActionEvent e) {
/* 255 */     TextTransfer textTransfer = new TextTransfer();
/* 256 */     textTransfer.setClipboardContents(this.outpuTextField.getText());
/*     */   }
/*     */   
/*     */   private void resetButton_actionPerformed(ActionEvent e) {
/* 260 */     this.inputTextField.setText("");
/* 261 */     this.outpuTextField.setText("");
/*     */   }
/*     */   void tableCombo_actionPerformed(ActionEvent e) {
/* 264 */     this.inputTextField.setText("");
/* 265 */     this.outpuTextField.setText("");
/*     */   }
/*     */   private Connection getDBConnection(String url) {
/* 268 */     Connection connection = null;
/*     */     try {
/* 270 */       String user = url.substring(0, url.indexOf("/")).trim();
/* 271 */       String password = url.substring(url.indexOf("/") + 1, url.indexOf("@")).trim();
/* 272 */       url = url.substring(url.indexOf("@") + 1).trim();
/* 273 */       Properties prop = new Properties();
/* 274 */       prop.put("user", user);
/* 275 */       prop.put("password", password);
/* 276 */       DriverManager.registerDriver((Driver)new OracleDriver());
/* 277 */       OracleDataSource ods = new OracleDataSource();
/* 278 */       ods.setConnectionProperties(prop);
/* 279 */       ods.setURL(url);
/* 280 */       System.out.println("Getting DB Connection from Config File..");
/* 281 */       connection = ods.getConnection();
/* 282 */     } catch (Exception e) {
/* 283 */       e.printStackTrace();
/*     */     } 
/* 285 */     return connection;
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\microbank\common\ecryption\MainFrameBulk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */