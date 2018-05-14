package com.tools.http.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * Description : 重写的安全校验. 既不校验任何东西
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午4:19:57
 */
public class PublicX509TrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}