package com.mobigen.util;

import javax.crypto.*;
import javax.crypto.spec.*;

import sun.misc.BASE64Encoder;

public class AES {
	public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0)  return null;
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0)  return null;
        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }
   
    public static String makeKey(String keyStr) {
     StringBuffer sb = new StringBuffer();
     
     for(int i=0; i<16; i++) {
      if(keyStr.length() > i) sb.append(keyStr.substring(i,i+1));
      else     sb.append(" ");
     }
     return sb.toString();
    }
   
    public static String encrypt(String message, String keyStr) throws Exception {
    	String key    = makeKey(keyStr);
    	SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    	Cipher cipher    = Cipher.getInstance("AES/ECB/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    	byte[] encrypted   = cipher.doFinal(message.getBytes());
    
    	
    	
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(encrypted);
 
        return new String(str);
    	//	return byteArrayToHex(encrypted);
    }

    public static String decrypt(String encrypted, String keyStr) {
    	String key   = makeKey(keyStr);
    	String originalString;
    	String str="";
    	try {
    		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    		Cipher cipher;
    		cipher    =Cipher.getInstance("AES/ECB/PKCS5Padding");
    		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    		byte[] original = cipher.doFinal(hexToByteArray(encrypted));
    		originalString  = new String(original);
    	} catch (Exception e) {
    		originalString = "ERR";
    	}
    	return originalString;
    }



	public static void main(String[] args) throws Exception {
        String key = "1234567890123456";
		//String enc = encAES("test001"); 
        String enc = encrypt("test001",key); 
        System.out.println(enc); 
				
		//System.out.println(decAES(enc)); 
	}
}

