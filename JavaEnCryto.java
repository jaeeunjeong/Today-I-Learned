
import javax.crypto.*;
import javax.crypto.spec.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class JavaEnCryto {
	public static void main(String[] args) {
		try {
			String originalText = "spam@test.com";
			String key = "!@#$%jiran123456";
			byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
			//String en = Encrypt( originalText, key);
			String iv = "!@#$%jiraniv1234";
			String en = Encrypt( originalText, key, iv);
			//String de = Decrypt( en, key);
			System.out.println( "Original Text is " + originalText);
			System.out.println( "Encrypted Text is " + en );
			System.out.println( "Exagtl Text is 4384065e2f1ba8e11e9d3480fb366429");
			//System.out.println( "Decrypted Text is " + de );
			} catch (Exception ex) {
				System.out.println("오류");
			}
	}
    /**
     * 복호화 : 미완성상태
     *
     * @param text
     * @param key
     * @return
     */
	public static String Decrypt(String text, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
		BASE64Decoder decoder = new BASE64Decoder();
		byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
		
		return new String(results,"UTF-8");
		}
	
    /**
     * 암호화
     *
     * @param text
     * @param key
     * @param iv
     * @return
     */
	public static String Encrypt(String text, String key, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		
		cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder(); //Base64 RETURN
		//return encoder.encode(results);
		return byteArrayToHex(results);//HEX RETURN
		}
    /**
     * 암호화
     *
     * @param text
     * @param key
     * @return
     */
	public static String Encrypt(String text, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		
		cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(results);
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
	
}
