package com.inov8.ola.util;


import com.inov8.microbank.common.util.CryptographyType;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.WordUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.util.List;


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
    
    
    
    public String encoder(Key pubKey, String input) 
    {
        byte[] data = input.getBytes();
        byte[] cipherText = null;
        SecureRandom random = new SecureRandom();
        
        try 
        {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
            cipherText = cipher.doFinal(data);
        } 
        catch (IllegalBlockSizeException ibse) 
        {
            ibse.printStackTrace();
            System.out.println("Illegal Block Size" + ibse.toString());
        } 
        catch (BadPaddingException bpe) 
        {
            bpe.printStackTrace();
            System.out.println("Bad Padding Exception." + bpe.toString());
        } 
        catch (InvalidKeyException ike) 
        {
            ike.printStackTrace();
            System.out.println("Invalid Key." + ike.toString());
        }

        return Base64.encodeBytes(cipherText);
  } 
    
        
    
    private static String decoder(Key privKey,String data) 
    {
    	byte[] plainText = null;

        try
        {
            byte[] cipher_text=Base64.decode(data);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            plainText = cipher.doFinal(cipher_text);
        }
        catch(IllegalBlockSizeException ibse)
        {
            ibse.printStackTrace();
            System.out.println("Illegal Block Size"+ibse.toString());
        }
        catch(BadPaddingException bpe)
        {
            bpe.printStackTrace();
            System.out.println("Bad Padding Exception."+bpe.toString());
        }
        catch(InvalidKeyException ike)
        {
            ike.printStackTrace();
            System.out.println("Invalid Key."+ike.toString());
        }
        
        String plaintext= new String(plainText);
        return plaintext;
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

       String plaintext = new String(plainText);
       return plaintext;

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
    
//    public static String decryptPin(String pin)
//    {
//        return pin;
        /*if(null != pin && null != key && !pin.equals(""))
        {
            pin = StringUtil.replaceSpacesWithPlus(pin);
            pin = EncryptionUtil.doDecrypt(key,pin);
        }
        return pin;*/
//    }
    
    
//    public static String encryptPin(String pin)
//    {
//        return pin;
       /* if(pin != null && key != null)
        {
            pin = EncryptionUtil.doEncrypt(key, pin);
        }
        return pin;*/
//    }

    
    public static void main(String[] args)
    {
        StringBuilder strBuilder = new StringBuilder();
        String str= "AQEELLLL";
        Integer lengt=str.length();
        for(int i=6; i>=1; i--){
            strBuilder.append(str.charAt(lengt-i));
        }
        /*strBuilder.append(str.charAt(lengt-6));
        strBuilder.append(str.charAt(lengt-5));
        strBuilder.append(str.charAt(lengt-4));
        strBuilder.append(str.charAt(lengt-3));
        strBuilder.append(str.charAt(lengt-2));
        strBuilder.append(str.charAt(lengt-1));*/
        System.out.println("Name:: "+strBuilder);



        String test = "0000000438";
        try {
            String e = encryptWithDES(test);
            String b= encryptWithDES(test);
            if(b.equals(e))
            {
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$ TRUE $$$$$$$$$$$$$$$$$$$$$$$$$$="+e);
            }
//          System.out.println(e);
//          String d = decryptWithDES(e);
//          System.out.println(d);
//          if()
            
//          String encrypted = HexEncodingUtils.toHex(encryptWithDES(test).getBytes("UTF-8"));
//          System.out.println(encrypted);
//          String decrypted = decryptWithDES(new String(HexEncodingUtils.fromHex(encrypted.getBytes("UTF-8"))));
//          System.out.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
//      String key = "<java.security.KeyPair>"+
//        "<privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\">"+
//        "<org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
//        "<big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int>"+
//        "<hashtable/>"+
//        "<vector/>"+
//        "<big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int>"+
//        "</org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
//        "<org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
//        "<default>"+
//        "<crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>"+
//        "<primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>"+
//        "<primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>"+
//        "<primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>"+
//        "<primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>"+
//        "<publicExponent>65537</publicExponent>"+
//        "</default>"+
//        "</org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
//    "</privateKey>"+
//    "<publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\">"+
//      "<modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus>"+
//      "<publicExponent>65537</publicExponent>"+
//    "</publicKey>"+
//  "</java.security.KeyPair>";
//  
//      /*
//      
//      String encryptedText = "TOUFl4C9Bt3ajgP784m3qLT7FSTSaeM1OwhQTICasTlKTOlZLDIM9vhVRkiA0Ib1fnyiGeqmK9Iu"+
//      "uZoMktFNKFmXW9q/2lDyaU2WwHCag1mwMDITHRhIcF8FlIC0Wv3ub2tO5OoLpMS62LYAW0nXSw+Y"+
//      "vZVqMdAA76gNAPKME6Y="; 
//  
//  
//  String deKey = doDecrypt(key, encryptedText);
//  
//  System.out.println("De key : "+deKey);*/
//  
//      String encryptedKey = doEncrypt(key, "1111");
//      
//      System.out.println("Encrypted Key : "+encryptedKey);
//      
//      
//      
    }
    
    /**
     * @param object The object which has crypted fields
     * @param type The cryptography type
     * @param fieldNames The fieldNames which are to be crypted
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    public static void docryptFields( CryptographyType type, Object object, String... fieldNames) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Class<?> clazz = object.getClass();
        for( String fieldName : fieldNames )
        {
            String capitalizedFieldName = WordUtils.capitalize( fieldName );
            String getterName = "get" + capitalizedFieldName;
            String setterName = "set" + capitalizedFieldName;
            Method method = clazz.getDeclaredMethod( getterName );
            Object fieldValue = method.invoke( object );
            if( fieldValue != null )
            {
                String cryptedValue = fieldValue.toString();
                /*if( CryptographyType.ENCRYPT == type  )
                {
                    cryptedValue = encryptPin( fieldValue.toString() );
                }
                else
                {
                    cryptedValue = decryptPin( fieldValue.toString() );
                }*/
                Method setter = clazz.getDeclaredMethod( setterName, String.class );
                setter.invoke( object, cryptedValue );
            }
        }
    }

    /**
     * @param type The cryptography type
     *
     * @param fieldNames The fieldNames which are to be crypted
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void docryptFields( CryptographyType type, List<?> list, String... fieldNames) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        if( null != list && !list.isEmpty() )
        {
            for( Object object : list )
            {
                docryptFields( type, object, fieldNames );
            }
        }
    }

}
