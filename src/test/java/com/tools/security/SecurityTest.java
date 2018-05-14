package com.tools.security;

import com.tools.security.aes.AESTools;
import com.tools.security.base64.Base64;
import com.tools.security.blowfish.BCrypt;
import com.tools.security.des.DESTools;
import com.tools.security.md5.MD5Tools;
import com.tools.security.other.OtherEncryptTools;

public class SecurityTest {
	public static void main(String[] args) throws Exception {
//		aes();
//		base64();
//		blowfish();
//		des();
//		md5();
//		otherEncrypt();
	}
	
	public static void aes() throws Exception {
		String aesKey = "123456";
		byte[] se = AESTools.encrypt("Test Aes()", aesKey);
		System.out.println(AESTools.decrypt(se, aesKey));
		String s = Base64.encodeByteToString(se);
		System.out.println(s);
		System.out.println(AESTools.decrypt(Base64.decodeStringToByte(s), aesKey));
		
	}
	
	public static void base64() throws Exception {
		String a = "test base64";
		String encode = Base64.encodeStringToString(a);
		System.out.println(encode);
		System.out.println(Base64.decodeStringToString(encode));
	}
	
	public static void blowfish() throws Exception {
		String hash = BCrypt.hashPassword("testblowfish", BCrypt.genSalt());
		System.out.println(hash);
		// 校验加密
		System.out.println(BCrypt.checkPassword("testblowfish", hash));
		System.out.println(BCrypt.checkPassword("blowfish", hash));
	}
	
	public static void des() throws Exception {
		String key = "qazwsxedcrfvtgby";
		String s = DESTools.encrypt("test des()", key);
		System.out.println(s);
		System.out.println(DESTools.decrypt(s, key));
	}
	
	public static void md5() throws Exception {
		String value = "test MD5";
		System.out.println(MD5Tools.MD5EncryptBy32(value));
		System.out.println(MD5Tools.MD5EncryptBy16(value));
	}
	
	public static void otherEncrypt() {
		String key = "AWEFGYHJUREVCDJU";
		String a = "test OtherEncrypt()";
		String v = OtherEncryptTools.encode(a, key);
		System.out.println(v);
		System.out.println(OtherEncryptTools.decode(v, key));
	}
}
