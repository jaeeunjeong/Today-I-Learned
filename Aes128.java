
import javax.crypto.*;
import javax.crypto.spec.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Aes128 {
	 /**
     * 암호화
     *
     * @param input
     * @param key
     * @return
     */
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch(Exception e) {
        }
 
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(crypted);
 
        return new String(str);

    }
 
    /**
     * 복호화
     *
     * @param input
     * @param key
     * @return
     */
    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
 
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
 
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decodeBuffer(input));
 
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        
       
        return new String(output);
    }
    
    public static void main(String[] args) {
        Aes128 aes = new Aes128();
        String key = "1234567890123456";
        String str = "test001";
        
        String encode = aes.encrypt(str, key);     
        String decode = aes.decrypt(encode, key);
        
        System.out.println("encode--->" + encode);
        System.out.println("decode--->" + decode);



	}
}

