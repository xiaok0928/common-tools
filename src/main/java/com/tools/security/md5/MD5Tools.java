package com.tools.security.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description : MD5非对称加密
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月14日 上午10:58:05
 */
public class MD5Tools {

	/**
	 * Description : MD5加密返回一个16位的字符串(前后去8)
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午11:00:05 
	 * 
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String MD5EncryptBy16(String values) throws Exception {
		String str = MD5EncryptBy32(values);
		return str.substring(8, str.length() - 8);
	}

	/**
	 * Description : MD5加密返回一个32位的字符串
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月14日 上午10:59:32 
	 * 
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String MD5EncryptBy32(String values) throws Exception {
		// 初始化加密之后的字符串
		String encrypeString = null;

		// 16进制的数组
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		// 字符串的加密过程
		try {
			// 把需要加密的字符串转化为字节数组
			byte[] neededEncrypedByteTemp = values.getBytes();

			// 得到MD5的加密算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 更新算法使用的摘要
			md.update(neededEncrypedByteTemp);

			// 完成算法加密过程
			byte[] middleResult = md.digest();

			// 把加密后的字节数组转化为字符串
			int length = middleResult.length;
			char[] neededEncrypedByte = new char[length * 2];
			int k = 0;
			for (int i = 0; i < length; i++) {
				byte byte0 = middleResult[i];
				neededEncrypedByte[k++] = hexDigits[byte0 >>> 4 & 0xf];
				neededEncrypedByte[k++] = hexDigits[byte0 & 0xf];
			}
			encrypeString = new String(neededEncrypedByte);
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex);
		}

		// 返回加密之后的字符串
		return encrypeString;
	}
}