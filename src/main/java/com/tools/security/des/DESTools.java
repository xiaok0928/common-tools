package com.tools.security.des;

import com.tools.common.Validate;
import com.tools.security.base64.Base64;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Description : DES对称加密<br>
 * DES对称加密需要注意的是秘钥不能小于16位
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月14日 上午10:51:12
 */
public class DESTools {
	
	/**
	 * Description : 生成秘钥Key
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:52:01 
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static SecretKey keyGenerator(String key) throws Exception {
		Validate.isNotNull(key, "秘钥Key不能为空!");
		Validate.isTrue(key.length() < 16, "秘钥Key的值长度不能小于16位!");
		byte[] input = HexString2Bytes(key);
		DESKeySpec desKey = new DESKeySpec(input);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		return securekey;
	}

	/**
	 * Description : 字符解析成数字
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:53:31 
	 * 
	 * @param c
	 * @return
	 */
	private static int parse(char c) {
		if (c >= 'a')
			return c - 'a' + 10 & 0xF;
		if (c >= 'A')
			return c - 'A' + 10 & 0xF;
		return c - '0' & 0xF;
	}

	/**
	 * Description : 16进制字符串转byte数组
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:53:41 
	 * 
	 * @param hexstr
	 * @return
	 */
	private static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) (parse(c0) << 4 | parse(c1));
		}
		return b;
	}

	/**
	 * Description : DES加密
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:54:10 
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		Key deskey = keyGenerator(key);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		SecureRandom random = new SecureRandom();
		cipher.init(1, deskey, random);
		return Base64.encodeByteToString(cipher.doFinal(data.getBytes("UTF-8")));
	}

	/**
	 * Description : DES解密
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:54:21 
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		Key deskey = keyGenerator(key);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(2, deskey);
		return new String(cipher.doFinal(Base64.decodeStringToByte(data)), "UTF-8");
	}
}