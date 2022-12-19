package com.inov8.export.common;


import com.inov8.export.ExportFileJobController;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;


public class EncryptionUtil
{
    private static Cipher cipher;
//    private static SecureRandom random = new SecureRandom();
//    private static byte[] cipherText;
//    private static byte[] plainText;
    
    private static final String key = "<java.security.KeyPair>"
  +"<privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\">"
    +"<org.bouncycastle.jce.provider.JCERSAPrivateKey>"
    +"<big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int>"
    +"<hashtable/>"
    +"<vector/>"
    +"<big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int>"
    +"</org.bouncycastle.jce.provider.JCERSAPrivateKey>"
    +"<org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"
    +"<default>"
    +"  <crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>"
    +"  <primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>"
    +"  <primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>"
    +"  <primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>"
    +"  <primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>"
    +"  <publicExponent>65537</publicExponent>"
    +"</default>"
    +"</org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"
    +"</privateKey>"
    +"<publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\">"
    +"<modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus>"
    +"<publicExponent>65537</publicExponent>"
    +"</publicKey>"
    +"</java.security.KeyPair>";

    static Logger logger = Logger.getLogger(EncryptionUtil.class);
    
        
    public static String doEncrypt(String key, String input)
    {
      Security.addProvider(new BouncyCastleProvider());
      BASE64Encoder encoder = new BASE64Encoder();
      
      XStream xstream = new XStream();
      KeyPair keypair = (KeyPair) xstream.fromXML(key);
      Key pubKey = keypair.getPublic();
  		
      byte[] cipherText = null;
      SecureRandom random = new SecureRandom();
      
      try
      {
    	  synchronized( Cipher.class )
    	  {
    		  cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
    		  byte[] data = input.getBytes();
    		  cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
    		  cipherText = cipher.doFinal(data);
    	  }
      }
      catch (IllegalBlockSizeException ibse)
      {
        ibse.printStackTrace();
      }
      catch (BadPaddingException bpe)
      {
        bpe.printStackTrace();
      }
      catch (InvalidKeyException ike)
      {
        ike.printStackTrace();
      }
      catch (NoSuchPaddingException nspe)
      {
        nspe.printStackTrace();
      }
      catch (NoSuchProviderException nspe)
      {
        nspe.printStackTrace();
      }
      catch (NoSuchAlgorithmException nsae)
      {
        nsae.printStackTrace();
      }
      
      return encoder.encode(cipherText);

    }

    public static String doDecrypt(String key, String cipherText)
    {

       Security.addProvider(new BouncyCastleProvider());
       BASE64Decoder decoder = new BASE64Decoder();
       
       XStream xstream = new XStream();
       KeyPair keypair = (KeyPair) xstream.fromXML(key);
       Key privKey = keypair.getPrivate();
       byte[] plainText = null;

       try
       {
    	   synchronized( Cipher.class )
    	   {

    		   if( cipher == null )
    			   cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");

    		   byte[] cipher_text = decoder.decodeBuffer(cipherText);
    		   cipher.init(Cipher.DECRYPT_MODE, privKey);
    		   plainText = cipher.doFinal(cipher_text);
    	   }
       }

       catch (IllegalBlockSizeException ibse)
       {
    	   ibse.printStackTrace();
       }
       catch (BadPaddingException bpe)
       {
    	   bpe.printStackTrace();
       }
       catch (InvalidKeyException ike)
       {
    	   ike.printStackTrace();

       }
       catch (IOException ioe)
       {
    	   ioe.printStackTrace();
       }
       catch (NoSuchPaddingException nspe)
       {
    	   nspe.printStackTrace();
       }
       catch (NoSuchProviderException nspe)
       {
    	   nspe.printStackTrace();

       }
       catch (NoSuchAlgorithmException nsae)
       {
    	   nsae.printStackTrace();
       }
       catch (ArrayIndexOutOfBoundsException e){

           logger.error(e.getClass() + " : " + e.getMessage(), e);
           logger.error("Data Could not be Decrypted:  " + cipherText,e);
       }
       finally {
           cipher = null;
       }

        String plaintext;
       if(plainText != null) {
           plaintext = new String(plainText);
           return plaintext;
       }
       else {
           // Encrypted data will be returned in this case...
           return cipherText;
       }
     }
    
    public static String encryptWithDES(String strToEncrypt) throws Exception
    {
        return HexEncodingUtils.toHex(strToEncrypt.getBytes("UTF-8"));
        //return DESEncryption.getInstance().encrypt(strToEncrypt, null);
    }

    public static String encryptAccountNo(String strToEncrypt) throws Exception
    {
        return encryptWithDES(strToEncrypt);
    }

    public static String decryptAccountNo(String strToDecrypt) throws Exception
    {
        return decryptWithDES(strToDecrypt);
    }
    
    public static String decryptWithDES(String strToDecrypt) throws Exception
    {
        return new String(HexEncodingUtils.fromHex(strToDecrypt.getBytes("UTF-8")));
        //return DESEncryption.getInstance().decrypt(strToDecrypt, null);
    }
    
    public static String decryptPin(String pin)
    {
        if(null != pin && null != key && !pin.equals(""))
        {
            pin = replaceSpacesWithPlus(pin);
            pin = EncryptionUtil.doDecrypt(key,pin);
        }
        return pin;
    }
    
    
    public static String encryptPin(String pin)
    {
        if(pin != null && key != null)
        {
            pin = EncryptionUtil.doEncrypt(key, pin);
        }
        return pin;
    }


    private static String replaceSpacesWithPlus(String orginalString)
	{
		return orginalString.replaceAll(" ", "+");
	}

    public static void main(String[] args)
    {
        try {

            String encryptedString = "IBUH99Y96NLPQueYlh+neebrojXD5ntgBcyEuPH6OFVn3aHAi39sFKOchXrWAOWLwBGEgQF3olC4\n" +
                    "KemeAdn8I5ytZrkv9BvZwrR4Y5dHDzFBRIYrJzWSZNjN8rH55N9shOd6s0pkwnDF8oEBnsivkHqZ\n" +
                    "ZXxIcaMHwleAVPZ5TO4=";

            String plainText =  decryptPin(encryptedString);

            System.out.println(plainText);

        } catch (Exception e) {
           logger.error(e.getClass() + " : " + e.getMessage(), e);
        }


    }
}
