package com.tools.security.aes;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Description : AES 对称加密 <br>
 * 
 * @author xiaok
 * Created By : xiaok0928@hotmail.com <br>
 * Creation Time : 2018年5月14日 上午10:26:47
 */
public class AESTools {

	/**
	 * Description : AES加密<br>
	 * Created By : xiaok0928@hotmail.com <br>
	 * Creation Time : 2018年5月14日 上午10:27:46
	 * 
	 * @param password
	 * @param aesKey
	 * @return
	 */
	public static byte[] encrypt(String password, String aesKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(aesKey.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = password.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description : AES解密 <br>
	 * Created By : xiaok0928@hotmail.com <br>
	 * Creation Time : 2018年5月14日 上午10:28:14
	 * 
	 * @param content
	 * @param aesKey
	 * @return
	 */
	public static String decrypt(byte[] content, String aesKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(aesKey.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return new String(result, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}