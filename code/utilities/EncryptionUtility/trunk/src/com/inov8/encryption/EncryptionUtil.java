/*     */ package com.inov8.encryption;
/*     */ 
/*     */ import com.thoughtworks.xstream.XStream;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyPair;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Security;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.ShortBufferException;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ import org.bouncycastle.util.encoders.Base64;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
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
/*     */ public class EncryptionUtil
/*     */ {
/*     */   private static Cipher encryptionCipher;
/*     */   private static Cipher decryptionCipher;
/*     */   private static final String ACCOUNT_INF_KEY = "0102030405060708090A0B0C0D0E0F100102030405060708";
/*     */   private static final String key = "<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>";
/*     */   
/*     */   public static String doEncrypt(String key, String input) {
/*  50 */     BASE64Encoder encoder = null;
/*  51 */     byte[] cipherText = null;
/*     */     try {
/*  53 */       Security.addProvider((Provider)new BouncyCastleProvider());
/*  54 */       encoder = new BASE64Encoder();
/*     */       
/*  56 */       XStream xstream = new XStream();
/*  57 */       KeyPair keypair = (KeyPair)xstream.fromXML(key);
/*  58 */       Key pubKey = keypair.getPublic();
/*     */ 
/*     */       
/*  61 */       synchronized (encryptionCipher) {
/*  62 */         if (encryptionCipher == null) {
/*  63 */           SecureRandom random = new SecureRandom();
/*  64 */           encryptionCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
/*  65 */           encryptionCipher.init(1, pubKey, random);
/*     */         } 
/*  67 */         byte[] data = input.getBytes();
/*  68 */         cipherText = encryptionCipher.doFinal(data);
/*  69 */         if (cipherText == null) {
/*  70 */           throw new Exception("Invalid input");
/*     */         }
/*     */       } 
/*  73 */     } catch (IllegalBlockSizeException ibse) {
/*  74 */       ibse.printStackTrace();
/*  75 */     } catch (BadPaddingException bpe) {
/*  76 */       bpe.printStackTrace();
/*  77 */     } catch (InvalidKeyException ike) {
/*  78 */       ike.printStackTrace();
/*  79 */     } catch (NoSuchPaddingException nspe) {
/*  80 */       nspe.printStackTrace();
/*  81 */     } catch (NoSuchProviderException nspe) {
/*  82 */       nspe.printStackTrace();
/*  83 */     } catch (NoSuchAlgorithmException nsae) {
/*  84 */       nsae.printStackTrace();
/*  85 */     } catch (Exception e) {
/*  86 */       encryptionCipher = null;
/*  87 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  90 */     return encoder.encode(cipherText);
/*     */   }
/*     */ 
/*     */   
/*     */   public String encoder(Key pubKey, String input) {
/*  95 */     byte[] data = input.getBytes();
/*  96 */     byte[] cipherText = null;
/*  97 */     SecureRandom random = new SecureRandom();
/*     */     
/*     */     try {
/* 100 */       encryptionCipher.init(1, pubKey, random);
/* 101 */       cipherText = encryptionCipher.doFinal(data);
/* 102 */     } catch (IllegalBlockSizeException ibse) {
/* 103 */       ibse.printStackTrace();
/* 104 */       System.out.println("Illegal Block Size" + ibse.toString());
/* 105 */     } catch (BadPaddingException bpe) {
/* 106 */       bpe.printStackTrace();
/* 107 */       System.out.println("Bad Padding Exception." + bpe.toString());
/* 108 */     } catch (InvalidKeyException ike) {
/* 109 */       ike.printStackTrace();
/* 110 */       System.out.println("Invalid Key." + ike.toString());
/*     */     } 
/*     */     
/* 113 */     return Base64.encodeBytes(cipherText);
/*     */   }
/*     */   
/*     */   private static String decoder(Key privKey, String data) {
/* 117 */     byte[] plainText = null;
/*     */     
/*     */     try {
/* 120 */       byte[] cipher_text = Base64.decode(data);
/* 121 */       decryptionCipher.init(2, privKey);
/* 122 */       plainText = decryptionCipher.doFinal(cipher_text);
/* 123 */     } catch (IllegalBlockSizeException ibse) {
/* 124 */       ibse.printStackTrace();
/* 125 */       System.out.println("Illegal Block Size" + ibse.toString());
/* 126 */     } catch (BadPaddingException bpe) {
/* 127 */       bpe.printStackTrace();
/* 128 */       System.out.println("Bad Padding Exception." + bpe.toString());
/* 129 */     } catch (InvalidKeyException ike) {
/* 130 */       ike.printStackTrace();
/* 131 */       System.out.println("Invalid Key." + ike.toString());
/*     */     } 
/*     */     
/* 134 */     return new String(plainText);
/*     */   }
/*     */   static {
/*     */     try {
/* 138 */       Security.addProvider((Provider)new BouncyCastleProvider());
/* 139 */       BASE64Decoder decoder = new BASE64Decoder();
/* 140 */       XStream xstream = new XStream();
/* 141 */       KeyPair keypair = (KeyPair)xstream.fromXML("<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>");
/* 142 */       Key privKey = keypair.getPrivate();
/* 143 */       decryptionCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
/* 144 */       decryptionCipher.init(2, privKey);
/* 145 */     } catch (Exception e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     BASE64Encoder encoder = null;
/* 151 */     byte[] cipherText = null;
/*     */     try {
/* 153 */       Security.addProvider((Provider)new BouncyCastleProvider());
/* 154 */       encoder = new BASE64Encoder();
/*     */       
/* 156 */       XStream xstream = new XStream();
/* 157 */       KeyPair keypair = (KeyPair)xstream.fromXML("<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>");
/* 158 */       Key pubKey = keypair.getPublic();
/*     */       
/* 160 */       SecureRandom random = new SecureRandom();
/* 161 */       encryptionCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
/* 162 */       encryptionCipher.init(1, pubKey, random);
/* 163 */     } catch (Exception e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String doDecrypt(String key, String cipherText) {
/* 170 */     Security.addProvider((Provider)new BouncyCastleProvider());
/* 171 */     BASE64Decoder decoder = new BASE64Decoder();
/*     */     
/* 173 */     XStream xstream = new XStream();
/* 174 */     KeyPair keypair = (KeyPair)xstream.fromXML(key);
/* 175 */     Key privKey = keypair.getPrivate();
/* 176 */     byte[] plainText = null;
/*     */     
/*     */     try {
/* 179 */       synchronized (decryptionCipher) {
/* 180 */         if (decryptionCipher == null) {
/* 181 */           decryptionCipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
/* 182 */           decryptionCipher.init(2, privKey);
/*     */         } 
/* 184 */         byte[] cipher_text = decoder.decodeBuffer(cipherText);
/* 185 */         plainText = decryptionCipher.doFinal(cipher_text);
/*     */       } 
/* 187 */     } catch (IllegalBlockSizeException ibse) {
/* 188 */       ibse.printStackTrace();
/* 189 */     } catch (BadPaddingException bpe) {
/* 190 */       bpe.printStackTrace();
/* 191 */     } catch (InvalidKeyException ike) {
/* 192 */       ike.printStackTrace();
/*     */     }
/* 194 */     catch (IOException ioe) {
/* 195 */       ioe.printStackTrace();
/* 196 */     } catch (NoSuchPaddingException nspe) {
/* 197 */       nspe.printStackTrace();
/* 198 */     } catch (NoSuchProviderException nspe) {
/* 199 */       nspe.printStackTrace();
/*     */     }
/* 201 */     catch (NoSuchAlgorithmException nsae) {
/* 202 */       nsae.printStackTrace();
/* 203 */     } catch (ArrayIndexOutOfBoundsException aioobe) {
/* 204 */       decryptionCipher = null;
/* 205 */       aioobe.printStackTrace();
/*     */     } 
/*     */     
/* 208 */     String plaintext = new String(plainText);
/* 209 */     return plaintext;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String encryptWithDES(String strToEncrypt) throws Exception {
/* 214 */     return HexEncodingUtils.toHex(strToEncrypt.getBytes("UTF-8"));
/*     */   }
/*     */   
/*     */   public static String encryptAccountNo(String strToEncrypt) throws Exception {
/* 218 */     return encryptWithDES(strToEncrypt);
/*     */   }
/*     */   
/*     */   public static String decryptAccountNo(String strToDecrypt) throws Exception {
/* 222 */     return decryptWithDES(strToDecrypt);
/*     */   }
/*     */   
/*     */   private static String decryptWithDES(String strToDecrypt) throws Exception {
/* 226 */     return new String(HexEncodingUtils.fromHex(strToDecrypt.getBytes("UTF-8")));
/*     */   }
/*     */   
/*     */   public static String decryptPin(String pin) {
/*     */     try {
/* 231 */       if (null != pin && null != "<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>" && !pin.equals("")) {
/* 232 */         pin = replaceSpacesWithPlus(pin);
/* 233 */         pin = doDecrypt("<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>", pin);
/*     */       } 
/* 235 */     } catch (Exception e) {
/* 236 */       e.printStackTrace();
/*     */     } 
/* 238 */     return pin;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String encryptPin(String pin) {
/* 243 */     if (pin != null && "<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>" != null) {
/* 244 */       pin = doEncrypt("<java.security.KeyPair><privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\"><org.bouncycastle.jce.provider.JCERSAPrivateKey><big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int><hashtable/><vector/><big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int></org.bouncycastle.jce.provider.JCERSAPrivateKey><org.bouncycastle.jce.provider.JCERSAPrivateCrtKey><default>  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>  <publicExponent>65537</publicExponent></default></org.bouncycastle.jce.provider.JCERSAPrivateCrtKey></privateKey><publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\"><modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus><publicExponent>65537</publicExponent></publicKey></java.security.KeyPair>", pin);
/*     */     }
/* 246 */     return pin;
/*     */   }
/*     */   
/*     */   public static String replaceSpacesWithPlus(String orginalString) {
/* 250 */     return orginalString.replaceAll(" ", "+");
/*     */   }
/*     */   
/*     */   public static byte[] toBinArray(String hexStr) {
/* 254 */     byte[] bArray = new byte[hexStr.length() / 2];
/* 255 */     for (int i = 0; i < bArray.length; i++) {
/* 256 */       bArray[i] = Byte.parseByte(hexStr.substring(2 * i, 2 * i + 2), 16);
/*     */     }
/* 258 */     return bArray;
/*     */   }
/*     */   
/*     */   public static String encryptWithAES(String key, String strToEncrypt) {
/* 262 */     Security.addProvider((Provider)new BouncyCastleProvider());
/* 263 */     byte[] keyBytes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 268 */       keyBytes = key.getBytes("UTF8");
/* 269 */       SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
/* 270 */       byte[] input = strToEncrypt.trim().getBytes("UTF8");
/*     */       
/* 272 */       synchronized (Cipher.class) {
/* 273 */         System.out.println("***********");
/* 274 */         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
/* 275 */         cipher.init(1, skey);
/*     */ 
/*     */         
/* 278 */         byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
/*     */         
/* 280 */         int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
/*     */         
/* 282 */         ctLength += cipher.doFinal(cipherText, ctLength);
/*     */         
/* 284 */         String encryptedString = new String(Base64.encode(cipherText));
/*     */ 
/*     */         
/* 287 */         return encryptedString.trim();
/*     */       } 
/* 289 */     } catch (UnsupportedEncodingException uee) {
/* 290 */       uee.printStackTrace();
/* 291 */     } catch (IllegalBlockSizeException ibse) {
/* 292 */       ibse.printStackTrace();
/* 293 */     } catch (BadPaddingException bpe) {
/* 294 */       bpe.printStackTrace();
/* 295 */     } catch (InvalidKeyException ike) {
/* 296 */       ike.printStackTrace();
/* 297 */     } catch (NoSuchPaddingException nspe) {
/* 298 */       nspe.printStackTrace();
/* 299 */     } catch (NoSuchAlgorithmException nsae) {
/* 300 */       nsae.printStackTrace();
/* 301 */     } catch (ShortBufferException e) {
/* 302 */       e.printStackTrace();
/*     */     } 
/* 304 */     return null;
/*     */   }
/*     */   
/*     */   public static String decryptWithAES(String key, String strToDecrypt) {
/* 308 */     Security.addProvider((Provider)new BouncyCastleProvider());
/* 309 */     byte[] keyBytes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 314 */       keyBytes = key.getBytes("UTF8");
/* 315 */       SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
/* 316 */       byte[] input = Base64.decode(strToDecrypt.trim().getBytes("UTF8"));
/*     */ 
/*     */       
/* 319 */       synchronized (Cipher.class) {
/* 320 */         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
/* 321 */         cipher.init(2, skey);
/*     */ 
/*     */ 
/*     */         
/* 325 */         byte[] plainText = new byte[cipher.getOutputSize(input.length)];
/* 326 */         int ptLength = cipher.update(input, 0, cipher.getOutputSize(input.length), plainText, 0);
/* 327 */         ptLength += cipher.doFinal(plainText, ptLength);
/* 328 */         String decryptedString = new String(plainText);
/* 329 */         return decryptedString.trim();
/*     */       } 
/* 331 */     } catch (UnsupportedEncodingException uee) {
/* 332 */       uee.printStackTrace();
/* 333 */     } catch (IllegalBlockSizeException ibse) {
/* 334 */       ibse.printStackTrace();
/* 335 */     } catch (BadPaddingException bpe) {
/* 336 */       bpe.printStackTrace();
/* 337 */     } catch (InvalidKeyException ike) {
/* 338 */       ike.printStackTrace();
/* 339 */     } catch (NoSuchPaddingException nspe) {
/* 340 */       nspe.printStackTrace();
/* 341 */     } catch (NoSuchAlgorithmException nsae) {
/* 342 */       nsae.printStackTrace();
/* 343 */     } catch (ShortBufferException e) {
/* 344 */       e.printStackTrace();
/*     */     } 
/* 346 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 375 */       String pin = "1254";
/* 376 */       System.out.println(EncoderUtils.encodeToSha(pin));
/*     */     }
/* 378 */     catch (Exception e) {
/* 379 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\EncryptionUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */