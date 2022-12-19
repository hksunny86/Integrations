/*      */ package com.inov8.encryption;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Base64
/*      */ {
/*      */   public static final int NO_OPTIONS = 0;
/*      */   public static final int ENCODE = 1;
/*      */   public static final int DECODE = 0;
/*      */   public static final int GZIP = 2;
/*      */   public static final int DONT_BREAK_LINES = 8;
/*      */   private static final int MAX_LINE_LENGTH = 76;
/*      */   private static final byte EQUALS_SIGN = 61;
/*      */   private static final byte NEW_LINE = 10;
/*      */   private static final String PREFERRED_ENCODING = "UTF-8";
/*      */   private static final byte[] ALPHABET;
/*      */   
/*      */   static {
/*      */     byte[] arrayOfByte;
/*      */   }
/*      */   
/*   95 */   private static final byte[] _NATIVE_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] DECODABET;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte WHITE_SPACE_ENC = -5;
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte EQUALS_SIGN_ENC = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  113 */       arrayOfByte = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes("UTF-8");
/*      */     
/*      */     }
/*  116 */     catch (UnsupportedEncodingException use) {
/*  117 */       arrayOfByte = _NATIVE_ALPHABET;
/*      */     } 
/*  119 */     ALPHABET = arrayOfByte;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  127 */     DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes) {
/*  199 */     encode3to4(threeBytes, 0, numSigBytes, b4, 0);
/*  200 */     return b4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
/*  239 */     int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  244 */     switch (numSigBytes) {
/*      */       case 3:
/*  246 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  247 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  248 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  249 */         destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
/*  250 */         return destination;
/*      */       
/*      */       case 2:
/*  253 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  254 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  255 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  256 */         destination[destOffset + 3] = 61;
/*  257 */         return destination;
/*      */       
/*      */       case 1:
/*  260 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  261 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  262 */         destination[destOffset + 2] = 61;
/*  263 */         destination[destOffset + 3] = 61;
/*  264 */         return destination;
/*      */     } 
/*      */     
/*  267 */     return destination;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeObject(Serializable serializableObject) {
/*  284 */     return encodeObject(serializableObject, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeObject(Serializable serializableObject, int options) {
/*  314 */     ByteArrayOutputStream baos = null;
/*  315 */     java.io.OutputStream b64os = null;
/*  316 */     ObjectOutputStream oos = null;
/*  317 */     GZIPOutputStream gzos = null;
/*      */ 
/*      */     
/*  320 */     int gzip = options & 0x2;
/*  321 */     int dontBreakLines = options & 0x8;
/*      */ 
/*      */     
/*      */     try {
/*  325 */       baos = new ByteArrayOutputStream();
/*  326 */       b64os = new OutputStream(baos, 0x1 | dontBreakLines);
/*      */ 
/*      */       
/*  329 */       if (gzip == 2) {
/*  330 */         gzos = new GZIPOutputStream(b64os);
/*  331 */         oos = new ObjectOutputStream(gzos);
/*      */       } else {
/*      */         
/*  334 */         oos = new ObjectOutputStream(b64os);
/*      */       } 
/*  336 */       oos.writeObject(serializableObject);
/*      */     }
/*  338 */     catch (IOException e) {
/*  339 */       e.printStackTrace();
/*  340 */       return null;
/*      */     } finally {
/*      */       
/*      */       try {
/*  344 */         oos.close();
/*  345 */       } catch (Exception e) {}
/*      */       
/*      */       try {
/*  348 */         gzos.close();
/*  349 */       } catch (Exception e) {}
/*      */       
/*      */       try {
/*  352 */         b64os.close();
/*  353 */       } catch (Exception e) {}
/*      */       
/*      */       try {
/*  356 */         baos.close();
/*  357 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  363 */       return new String(baos.toByteArray(), "UTF-8");
/*      */     }
/*  365 */     catch (UnsupportedEncodingException uue) {
/*  366 */       return new String(baos.toByteArray());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source) {
/*  380 */     return encodeBytes(source, 0, source.length, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int options) {
/*  405 */     return encodeBytes(source, 0, source.length, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int off, int len) {
/*  419 */     return encodeBytes(source, off, len, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBytes(byte[] source, int off, int len, int options) {
/*  448 */     int dontBreakLines = options & 0x8;
/*  449 */     int gzip = options & 0x2;
/*      */ 
/*      */     
/*  452 */     if (gzip == 2) {
/*  453 */       ByteArrayOutputStream baos = null;
/*  454 */       GZIPOutputStream gzos = null;
/*  455 */       OutputStream b64os = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  460 */         baos = new ByteArrayOutputStream();
/*  461 */         b64os = new OutputStream(baos, 0x1 | dontBreakLines);
/*  462 */         gzos = new GZIPOutputStream(b64os);
/*      */         
/*  464 */         gzos.write(source, off, len);
/*  465 */         gzos.close();
/*      */       }
/*  467 */       catch (IOException iOException) {
/*  468 */         iOException.printStackTrace();
/*  469 */         return null;
/*      */       } finally {
/*      */         
/*      */         try {
/*  473 */           gzos.close();
/*  474 */         } catch (Exception exception) {}
/*      */         
/*      */         try {
/*  477 */           b64os.close();
/*  478 */         } catch (Exception exception) {}
/*      */         
/*      */         try {
/*  481 */           baos.close();
/*  482 */         } catch (Exception exception) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  488 */         return new String(baos.toByteArray(), "UTF-8");
/*      */       }
/*  490 */       catch (UnsupportedEncodingException uue) {
/*  491 */         return new String(baos.toByteArray());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  498 */     boolean breakLines = (dontBreakLines == 0);
/*      */     
/*  500 */     int len43 = len * 4 / 3;
/*  501 */     byte[] outBuff = new byte[len43 + ((len % 3 > 0) ? 4 : 0) + (breakLines ? (len43 / 76) : 0)];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  506 */     int d = 0;
/*  507 */     int e = 0;
/*  508 */     int len2 = len - 2;
/*  509 */     int lineLength = 0;
/*  510 */     for (; d < len2; d += 3, e += 4) {
/*  511 */       encode3to4(source, d + off, 3, outBuff, e);
/*      */       
/*  513 */       lineLength += 4;
/*  514 */       if (breakLines && lineLength == 76) {
/*  515 */         outBuff[e + 4] = 10;
/*  516 */         e++;
/*  517 */         lineLength = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  521 */     if (d < len) {
/*  522 */       encode3to4(source, d + off, len - d, outBuff, e);
/*  523 */       e += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  529 */       return new String(outBuff, 0, e, "UTF-8");
/*      */     }
/*  531 */     catch (UnsupportedEncodingException uue) {
/*  532 */       return new String(outBuff, 0, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
/*  568 */     if (source[srcOffset + 2] == 61) {
/*      */ 
/*      */ 
/*      */       
/*  572 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  577 */       destination[destOffset] = (byte)(outBuff >>> 16);
/*  578 */       return 1;
/*      */     } 
/*      */ 
/*      */     
/*  582 */     if (source[srcOffset + 3] == 61) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  587 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  594 */       destination[destOffset] = (byte)(outBuff >>> 16);
/*  595 */       destination[destOffset + 1] = (byte)(outBuff >>> 8);
/*  596 */       return 2;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  607 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | DECODABET[source[srcOffset + 3]] & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  616 */       destination[destOffset] = (byte)(outBuff >> 16);
/*  617 */       destination[destOffset + 1] = (byte)(outBuff >> 8);
/*  618 */       destination[destOffset + 2] = (byte)outBuff;
/*      */       
/*  620 */       return 3;
/*  621 */     } catch (Exception e) {
/*  622 */       System.out.println("" + source[srcOffset] + ": " + DECODABET[source[srcOffset]]);
/*      */       
/*  624 */       System.out.println("" + source[srcOffset + 1] + ": " + DECODABET[source[srcOffset + 1]]);
/*      */       
/*  626 */       System.out.println("" + source[srcOffset + 2] + ": " + DECODABET[source[srcOffset + 2]]);
/*      */       
/*  628 */       System.out.println("" + source[srcOffset + 3] + ": " + DECODABET[source[srcOffset + 3]]);
/*      */       
/*  630 */       return -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(byte[] source, int off, int len) {
/*  648 */     int len34 = len * 3 / 4;
/*  649 */     byte[] outBuff = new byte[len34];
/*  650 */     int outBuffPosn = 0;
/*      */     
/*  652 */     byte[] b4 = new byte[4];
/*  653 */     int b4Posn = 0;
/*  654 */     int i = 0;
/*  655 */     byte sbiCrop = 0;
/*  656 */     byte sbiDecode = 0;
/*  657 */     for (i = off; i < off + len; i++) {
/*  658 */       sbiCrop = (byte)(source[i] & Byte.MAX_VALUE);
/*  659 */       sbiDecode = DECODABET[sbiCrop];
/*      */       
/*  661 */       if (sbiDecode >= -5) {
/*      */ 
/*      */         
/*  664 */         if (sbiDecode >= -1) {
/*  665 */           b4[b4Posn++] = sbiCrop;
/*  666 */           if (b4Posn > 3) {
/*  667 */             outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
/*  668 */             b4Posn = 0;
/*      */ 
/*      */             
/*  671 */             if (sbiCrop == 61) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  679 */         System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
/*      */         
/*  681 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/*  685 */     byte[] out = new byte[outBuffPosn];
/*  686 */     System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
/*  687 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decode(String s) {
/*      */     try {
/*  702 */       arrayOfByte = s.getBytes("UTF-8");
/*      */     }
/*  704 */     catch (UnsupportedEncodingException uee) {
/*  705 */       arrayOfByte = s.getBytes();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  710 */     byte[] arrayOfByte = decode(arrayOfByte, 0, arrayOfByte.length);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  715 */     if (arrayOfByte != null && arrayOfByte.length >= 4) {
/*      */       
/*  717 */       int head = arrayOfByte[0] & 0xFF | arrayOfByte[1] << 8 & 0xFF00;
/*  718 */       if (35615 == head) {
/*  719 */         ByteArrayInputStream bais = null;
/*  720 */         GZIPInputStream gzis = null;
/*  721 */         ByteArrayOutputStream baos = null;
/*  722 */         byte[] buffer = new byte[2048];
/*  723 */         int length = 0;
/*      */         
/*      */         try {
/*  726 */           baos = new ByteArrayOutputStream();
/*  727 */           bais = new ByteArrayInputStream(arrayOfByte);
/*  728 */           gzis = new GZIPInputStream(bais);
/*      */           
/*  730 */           while ((length = gzis.read(buffer)) >= 0) {
/*  731 */             baos.write(buffer, 0, length);
/*      */           }
/*      */ 
/*      */           
/*  735 */           arrayOfByte = baos.toByteArray();
/*      */         
/*      */         }
/*  738 */         catch (IOException e) {
/*      */ 
/*      */         
/*      */         } finally {
/*      */           try {
/*  743 */             baos.close();
/*  744 */           } catch (Exception e) {}
/*      */           
/*      */           try {
/*  747 */             gzis.close();
/*  748 */           } catch (Exception e) {}
/*      */           
/*      */           try {
/*  751 */             bais.close();
/*  752 */           } catch (Exception e) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  759 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object decodeToObject(String encodedObject) {
/*  773 */     byte[] objBytes = decode(encodedObject);
/*      */     
/*  775 */     ByteArrayInputStream bais = null;
/*  776 */     ObjectInputStream ois = null;
/*  777 */     Object obj = null;
/*      */     
/*      */     try {
/*  780 */       bais = new ByteArrayInputStream(objBytes);
/*  781 */       ois = new ObjectInputStream(bais);
/*      */       
/*  783 */       obj = ois.readObject();
/*      */     }
/*  785 */     catch (IOException e) {
/*  786 */       e.printStackTrace();
/*  787 */       obj = null;
/*      */     }
/*  789 */     catch (ClassNotFoundException e) {
/*  790 */       e.printStackTrace();
/*  791 */       obj = null;
/*      */     } finally {
/*      */       
/*      */       try {
/*  795 */         bais.close();
/*  796 */       } catch (Exception e) {}
/*      */       
/*      */       try {
/*  799 */         ois.close();
/*  800 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*  804 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean encodeToFile(byte[] dataToEncode, String filename) {
/*  818 */     boolean success = false;
/*  819 */     OutputStream bos = null;
/*      */     try {
/*  821 */       bos = new OutputStream(new FileOutputStream(filename), 1);
/*      */       
/*  823 */       bos.write(dataToEncode);
/*  824 */       success = true;
/*      */     }
/*  826 */     catch (IOException e) {
/*      */       
/*  828 */       success = false;
/*      */     } finally {
/*      */       
/*      */       try {
/*  832 */         bos.close();
/*  833 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*  837 */     return success;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean decodeToFile(String dataToDecode, String filename) {
/*  851 */     boolean success = false;
/*  852 */     OutputStream bos = null;
/*      */     try {
/*  854 */       bos = new OutputStream(new FileOutputStream(filename), 0);
/*      */       
/*  856 */       bos.write(dataToDecode.getBytes("UTF-8"));
/*  857 */       success = true;
/*      */     }
/*  859 */     catch (IOException e) {
/*  860 */       success = false;
/*      */     } finally {
/*      */       
/*      */       try {
/*  864 */         bos.close();
/*  865 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*  869 */     return success;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decodeFromFile(String filename) {
/*  883 */     byte[] decodedData = null;
/*  884 */     InputStream bis = null;
/*      */     
/*      */     try {
/*  887 */       File file = new File(filename);
/*  888 */       byte[] buffer = null;
/*  889 */       int length = 0;
/*  890 */       int numBytes = 0;
/*      */ 
/*      */       
/*  893 */       if (file.length() > 2147483647L) {
/*  894 */         System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");
/*      */         
/*  896 */         return null;
/*      */       } 
/*  898 */       buffer = new byte[(int)file.length()];
/*      */ 
/*      */       
/*  901 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  906 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/*  907 */         length += numBytes;
/*      */       }
/*      */       
/*  910 */       decodedData = new byte[length];
/*  911 */       System.arraycopy(buffer, 0, decodedData, 0, length);
/*      */     
/*      */     }
/*  914 */     catch (IOException e) {
/*  915 */       System.err.println("Error decoding from file " + filename);
/*      */     } finally {
/*      */       
/*      */       try {
/*  919 */         bis.close();
/*  920 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*  924 */     return decodedData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeFromFile(String filename) {
/*  938 */     String encodedData = null;
/*  939 */     InputStream bis = null;
/*      */     
/*      */     try {
/*  942 */       File file = new File(filename);
/*  943 */       byte[] buffer = new byte[(int)(file.length() * 1.4D)];
/*  944 */       int length = 0;
/*  945 */       int numBytes = 0;
/*      */ 
/*      */       
/*  948 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  953 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/*  954 */         length += numBytes;
/*      */       }
/*      */       
/*  957 */       encodedData = new String(buffer, 0, length, "UTF-8");
/*      */ 
/*      */     
/*      */     }
/*  961 */     catch (IOException e) {
/*  962 */       System.err.println("Error encoding from file " + filename);
/*      */     } finally {
/*      */       
/*      */       try {
/*  966 */         bis.close();
/*  967 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/*  971 */     return encodedData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InputStream
/*      */     extends FilterInputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int numSigBytes;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */ 
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in) {
/* 1003 */       this(in, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in, int options) {
/* 1029 */       super(in);
/* 1030 */       this.breakLines = ((options & 0x8) != 8);
/* 1031 */       this.encode = ((options & 0x1) == 1);
/* 1032 */       this.bufferLength = this.encode ? 4 : 3;
/* 1033 */       this.buffer = new byte[this.bufferLength];
/* 1034 */       this.position = -1;
/* 1035 */       this.lineLength = 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/* 1047 */       if (this.position < 0) {
/* 1048 */         if (this.encode) {
/* 1049 */           byte[] b3 = new byte[3];
/* 1050 */           int numBinaryBytes = 0;
/* 1051 */           for (int i = 0; i < 3; i++) {
/*      */             try {
/* 1053 */               int b = this.in.read();
/*      */ 
/*      */               
/* 1056 */               if (b >= 0) {
/* 1057 */                 b3[i] = (byte)b;
/* 1058 */                 numBinaryBytes++;
/*      */               }
/*      */             
/*      */             }
/* 1062 */             catch (IOException e) {
/*      */               
/* 1064 */               if (i == 0) {
/* 1065 */                 throw e;
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/* 1070 */           if (numBinaryBytes > 0) {
/* 1071 */             Base64.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0);
/* 1072 */             this.position = 0;
/* 1073 */             this.numSigBytes = 4;
/*      */           } else {
/*      */             
/* 1076 */             return -1;
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1082 */           byte[] b4 = new byte[4];
/* 1083 */           int i = 0;
/* 1084 */           for (i = 0; i < 4; i++) {
/*      */             
/* 1086 */             int b = 0;
/*      */             do {
/* 1088 */               b = this.in.read();
/* 1089 */             } while (b >= 0 && Base64.DECODABET[b & 0x7F] <= -5);
/*      */ 
/*      */             
/* 1092 */             if (b < 0) {
/*      */               break;
/*      */             }
/* 1095 */             b4[i] = (byte)b;
/*      */           } 
/*      */           
/* 1098 */           if (i == 4) {
/* 1099 */             this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0);
/* 1100 */             this.position = 0;
/*      */           } else {
/* 1102 */             if (i == 0) {
/* 1103 */               return -1;
/*      */             }
/*      */ 
/*      */             
/* 1107 */             throw new IOException("Improperly padded Base64 input.");
/*      */           } 
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1114 */       if (this.position >= 0) {
/*      */         
/* 1116 */         if (this.position >= this.numSigBytes) {
/* 1117 */           return -1;
/*      */         }
/* 1119 */         if (this.encode && this.breakLines && this.lineLength >= 76) {
/* 1120 */           this.lineLength = 0;
/* 1121 */           return 10;
/*      */         } 
/*      */         
/* 1124 */         this.lineLength++;
/*      */ 
/*      */ 
/*      */         
/* 1128 */         int b = this.buffer[this.position++];
/*      */         
/* 1130 */         if (this.position >= this.bufferLength) {
/* 1131 */           this.position = -1;
/*      */         }
/* 1133 */         return b & 0xFF;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1141 */       throw new IOException("Error in Base64 code reading stream.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read(byte[] dest, int off, int len) throws IOException {
/*      */       int i;
/* 1162 */       for (i = 0; i < len; i++) {
/* 1163 */         int b = read();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1168 */         if (b >= 0)
/* 1169 */         { dest[off + i] = (byte)b; }
/* 1170 */         else { if (i == 0)
/* 1171 */             return -1; 
/*      */           break; }
/*      */       
/*      */       } 
/* 1175 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class OutputStream
/*      */     extends FilterOutputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */ 
/*      */     
/*      */     private byte[] b4;
/*      */ 
/*      */     
/*      */     private boolean suspendEncoding;
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out) {
/* 1209 */       this(out, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out, int options) {
/* 1234 */       super(out);
/* 1235 */       this.breakLines = ((options & 0x8) != 8);
/* 1236 */       this.encode = ((options & 0x1) == 1);
/* 1237 */       this.bufferLength = this.encode ? 3 : 4;
/* 1238 */       this.buffer = new byte[this.bufferLength];
/* 1239 */       this.position = 0;
/* 1240 */       this.lineLength = 0;
/* 1241 */       this.suspendEncoding = false;
/* 1242 */       this.b4 = new byte[4];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(int theByte) throws IOException {
/* 1260 */       if (this.suspendEncoding) {
/* 1261 */         this.out.write(theByte);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1266 */       if (this.encode) {
/* 1267 */         this.buffer[this.position++] = (byte)theByte;
/* 1268 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1270 */           this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength));
/*      */           
/* 1272 */           this.lineLength += 4;
/* 1273 */           if (this.breakLines && this.lineLength >= 76) {
/* 1274 */             this.out.write(10);
/* 1275 */             this.lineLength = 0;
/*      */           } 
/*      */           
/* 1278 */           this.position = 0;
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1285 */       else if (Base64.DECODABET[theByte & 0x7F] > -5) {
/* 1286 */         this.buffer[this.position++] = (byte)theByte;
/* 1287 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1289 */           int len = Base64.decode4to3(this.buffer, 0, this.b4, 0);
/* 1290 */           this.out.write(this.b4, 0, len);
/*      */           
/* 1292 */           this.position = 0;
/*      */         }
/*      */       
/* 1295 */       } else if (Base64.DECODABET[theByte & 0x7F] != -5) {
/* 1296 */         throw new IOException("Invalid character in Base64 data.");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(byte[] theBytes, int off, int len) throws IOException {
/* 1314 */       if (this.suspendEncoding) {
/* 1315 */         this.out.write(theBytes, off, len);
/*      */         
/*      */         return;
/*      */       } 
/* 1319 */       for (int i = 0; i < len; i++) {
/* 1320 */         write(theBytes[off + i]);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void flushBase64() throws IOException {
/* 1331 */       if (this.position > 0) {
/* 1332 */         if (this.encode) {
/* 1333 */           this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position));
/* 1334 */           this.position = 0;
/*      */         } else {
/*      */           
/* 1337 */           throw new IOException("Base64 input not properly padded.");
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 1351 */       flushBase64();
/*      */ 
/*      */ 
/*      */       
/* 1355 */       super.close();
/*      */       
/* 1357 */       this.buffer = null;
/* 1358 */       this.out = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void suspendEncoding() throws IOException {
/* 1370 */       flushBase64();
/* 1371 */       this.suspendEncoding = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void resumeEncoding() {
/* 1383 */       this.suspendEncoding = false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\jsbank\Encryption_Utility\EncryptionUtility.jar!\com\inov8\encryption\Base64.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */