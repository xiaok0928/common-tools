package com.tools.http.common;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 * Description : HTTP连接默认配置
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午4:17:32
 */
public class ConnectConfig {
	//连接超时
	private int connectTimeout = 30000;
	//接收返回数据超时
	private int socketTimeout = 15000;
	private int connectionRequestTimeout = -1;
	private boolean redirectsEnabled = true;
	private boolean relativeRedirectsAllowed = true;
	private int maxRedirects = 50;
	private boolean expectContinueEnabled = false;
	private boolean circularRedirectsAllowed = false;
	private boolean authenticationEnabled = true;
	private boolean contentCompressionEnabled = true;
	//最大连接数
	private int maxConnect = 500;
	//最大路由
	private int maxPerRoute = 200;
	//HTTPS安装证书
	private SSLConnectionSocketFactory sslConnectionSocketFactory;
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	public boolean isRedirectsEnabled() {
		return redirectsEnabled;
	}
	public void setRedirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
	}
	public boolean isRelativeRedirectsAllowed() {
		return relativeRedirectsAllowed;
	}
	public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
		this.relativeRedirectsAllowed = relativeRedirectsAllowed;
	}
	public int getMaxRedirects() {
		return maxRedirects;
	}
	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}
	public boolean isExpectContinueEnabled() {
		return expectContinueEnabled;
	}
	public void setExpectContinueEnabled(boolean expectContinueEnabled) {
		this.expectContinueEnabled = expectContinueEnabled;
	}
	public boolean isCircularRedirectsAllowed() {
		return circularRedirectsAllowed;
	}
	public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
		this.circularRedirectsAllowed = circularRedirectsAllowed;
	}
	public boolean isAuthenticationEnabled() {
		return authenticationEnabled;
	}
	public void setAuthenticationEnabled(boolean authenticationEnabled) {
		this.authenticationEnabled = authenticationEnabled;
	}
	public boolean isContentCompressionEnabled() {
		return contentCompressionEnabled;
	}
	public void setContentCompressionEnabled(boolean contentCompressionEnabled) {
		this.contentCompressionEnabled = contentCompressionEnabled;
	}
	public int getMaxConnect() {
		return maxConnect;
	}
	public void setMaxConnect(int maxConnect) {
		this.maxConnect = maxConnect;
	}
	public int getMaxPerRoute() {
		return maxPerRoute;
	}
	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}
	public SSLConnectionSocketFactory getSslConnectionSocketFactory() {
		return sslConnectionSocketFactory;
	}
	public void setSslConnectionSocketFactory(SSLConnectionSocketFactory sslConnectionSocketFactory) {
		this.sslConnectionSocketFactory = sslConnectionSocketFactory;
	}
}