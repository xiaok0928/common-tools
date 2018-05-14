package com.tools.http.common;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;

/**
 * Description : HTTP/HTTPS请求配置
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午4:20:22
 */
public class UrlConfig {
	//url连接
	private String url;
	//K-V参数
	private Map<String, String> kvParams;
	//JSON 参数
	private String jsonParams;
	//请求头部信息
	private Map<String, String> heads;
	//文件列表
	private File[] files;
	//请求发送文件对方接收文件的参数名称
	private String requestFileName = "files";
	//请求类型:默认text/plain, utf-8
	private ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
	//编码格式:默认utf-8
	private Charset charset = Consts.UTF_8;
	//请求开始时间
	private long startTime;
	//请求结束时间
	private long endTime;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getKvParams() {
		return this.kvParams;
	}

	public void setKvParams(Map<String, String> kvParams) {
		this.kvParams = kvParams;
	}

	public String getJsonParams() {
		return this.jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}

	public Map<String, String> getHeads() {
		return this.heads;
	}

	public void setHeads(Map<String, String> heads) {
		this.heads = heads;
	}

	public File[] getFiles() {
		return this.files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String getRequestFileName() {
		return this.requestFileName;
	}

	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}

	public ContentType getContentType() {
		return this.contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Charset getCharset() {
		return this.charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Description : 开始请求详细信息
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午4:23:52 
	 * 
	 * @param startTime
	 */
	public void outRequestInfoStart(long startTime) {
		this.startTime = startTime;
		System.out.println("---------------------------------------request url start---------------------------------------");
		System.out.println("url:" + this.url);
		if (!StringUtils.isEmpty(this.jsonParams)) {
			System.out.println("jsonParams:" + this.jsonParams);
		}

		if ((null != this.kvParams) && (!this.kvParams.isEmpty())) {
			System.out.println("kvParams:" + this.kvParams);
		}

		if ((null != this.heads) && (!this.heads.isEmpty())) {
			System.out.println("heads:" + this.heads);
		}

		if ((null != this.files) && (this.files.length > 0)) {
			System.out.println("files Num:" + this.files.length);
			System.out.println("requestFileParamName:" + this.requestFileName);
		}

		System.out.println("contentType:" + this.contentType);
		System.out.println("charset:" + this.charset);
	}

	/**
	 * Description : 请求结束后的信息
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午4:24:10 
	 * 
	 * @param endTime
	 * @param isSuccess
	 */
	public void outRequestInfoEnd(long endTime, boolean isSuccess) {
		this.endTime = endTime;
		System.out.println("request url use time:" + (this.endTime - this.startTime) + "ms");
		System.out.println("request url is success:" + isSuccess);
		System.out.println("---------------------------------------request url end---------------------------------------");
	}
}