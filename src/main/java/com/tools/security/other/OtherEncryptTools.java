package com.tools.security.other;

import java.io.ByteArrayOutputStream;

import org.springframework.util.StringUtils;

import com.tools.common.Validate;

/**
 * Description : 其他加密方式<br>
 * 原理:给定的需要加解密的内容转换成二进制,再由二进制内容在给定的HexString中做偏移,偏移后的结果组成一个字符串及加密后的字符串.<br>
 * 解密的原理为反向推理.<br>
 * 需要注意的是:该种加密方式传入的HexString尽量保持为[a-z][A-Z][0-9]组成.否则解密结果可能会存在偏差<br>
 * <br>
 * Created By : xiaok0928@hotmail.com <br>
 * Creation Time : 2018年5月14日 上午11:10:30
 */
public class OtherEncryptTools {

	/**
	 * Description : 加密 <br>
	 * Created By : xiaok0928@hotmail.com <br>
	 * Creation Time : 2018年5月14日 上午11:17:09
	 * 
	 * @param str
	 * @param hexString
	 * @return
	 */
	public static String encode(String str, String hexString) {
		Validate.isTrue(StringUtils.isEmpty(hexString) && hexString.length() < 16, "给定的Hex值不能为空且不能小于16位");
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xF0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0xF) >> 0));
		}
		return sb.toString();
	}

	/**
	 * Description : 解密 <br>
	 * Created By : xiaok0928@hotmail.com <br>
	 * Creation Time : 2018年5月14日 上午11:17:16
	 * 
	 * @param bytes
	 * @param hexString
	 * @return
	 */
	public static String decode(String bytes, String hexString) {
		Validate.isTrue(StringUtils.isEmpty(hexString) && hexString.length() < 16, "给定的Hex值不能为空且不能小于16位");
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);

		for (int i = 0; i < bytes.length(); i += 2)
			baos.write(hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1)));
		return new String(baos.toByteArray());
	}
}