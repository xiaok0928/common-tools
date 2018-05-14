package com.tools.http.common;

import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 * Description : 默认安全证书
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午4:18:46
 */
public class DefaultSSLConnectionSocketFactory {
	
	/**
	 * Description : 默认HTTPS安全连接
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午4:19:18 
	 * 
	 * @return
	 */
	public static SSLConnectionSocketFactory defaultSSLConnectionSocketFactory() {
		SSLConnectionSocketFactory defaultSSLConnectionSocketFactory = null;
		try {
			TrustManager[] tm = { new PublicX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, new SecureRandom());
			defaultSSLConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultSSLConnectionSocketFactory;
	}
}