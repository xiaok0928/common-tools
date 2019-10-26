package com.tools.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.tools.common.Validate;
import com.tools.http.common.ConnectConfig;
import com.tools.http.common.UrlConfig;

/**
 * Description : HTTP/HTTPS请求工具类 <br/>
 * Created By : xiaok0928@hotmail.com <br/>
 * Creation Time : 2018年5月11日 下午4:24:51
 */
public class HttpTools {
	private static CloseableHttpClient httpClient;
	private static HttpTools sendForHttp = null;

	/**
	 * Description : 获取实例 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:25:07
	 * 
	 * @return
	 */
	public static synchronized HttpTools getInstance() {
		if (null == sendForHttp) {
			sendForHttp = new HttpTools(new ConnectConfig());
		}
		return sendForHttp;
	}

	/**
	 * Description : 获取带参数的实例 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:25:17
	 * 
	 * @param connectConfig
	 * @return
	 */
	public static synchronized HttpTools getInstance(ConnectConfig connectConfig) {
		if (null == sendForHttp) {
			sendForHttp = new HttpTools(connectConfig);
		}
		return sendForHttp;
	}

	/**
	 * Description : 设置请求配置 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:29:09
	 * 
	 * @param connectConfig
	 */
	private HttpTools(ConnectConfig connectConfig) {
		// 請求配置
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(connectConfig.getConnectTimeout())
				.setSocketTimeout(connectConfig.getSocketTimeout())
				.setConnectionRequestTimeout(connectConfig.getConnectionRequestTimeout())
				.setRedirectsEnabled(connectConfig.isRedirectsEnabled())
				.setRelativeRedirectsAllowed(connectConfig.isRelativeRedirectsAllowed())
				.setMaxRedirects(connectConfig.getMaxRedirects())
				.setExpectContinueEnabled(connectConfig.isExpectContinueEnabled())
				.setCircularRedirectsAllowed(connectConfig.isCircularRedirectsAllowed())
				.setAuthenticationEnabled(connectConfig.isAuthenticationEnabled())
				.setContentCompressionEnabled(connectConfig.isContentCompressionEnabled())
				.build();

		// 实例化连接管理器
		PoolingHttpClientConnectionManager connectManager = new PoolingHttpClientConnectionManager();

		// 设置最大连接数
		connectManager.setMaxTotal(connectConfig.getMaxConnect());

		// 设置最大路由数
		connectManager.setDefaultMaxPerRoute(connectConfig.getMaxPerRoute());

		// 实例化HttpClient
		HttpClientBuilder httpBuilder = HttpClients.custom().setConnectionManager(connectManager)
				.setDefaultRequestConfig(config);
		// 判断是否存在HTTPS安全认证
		if (null != connectConfig.getSslConnectionSocketFactory()) {
			httpBuilder.setSSLSocketFactory(connectConfig.getSslConnectionSocketFactory());
		}
		httpClient = httpBuilder.build();
	}

	/**
	 * Description : 发送HTTP/HTTPS GET请求 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:30:57
	 * 
	 * @param urlConfig
	 * @return
	 * @throws Exception
	 */
	public String sendForGet(UrlConfig urlConfig) throws Exception {
		return doGet(urlConfig);
	}

	/**
	 * Description : 发送HTTP/HTTPS POST请求 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:31:15
	 * 
	 * @param urlConfig
	 * @return
	 * @throws Exception
	 */
	public String sendForPost(UrlConfig urlConfig) throws Exception {
		return doPost(urlConfig);
	}

	/**
	 * Description : GET 请求 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:31:34
	 * 
	 * @param urlConfig
	 * @return
	 * @throws Exception
	 */
	private String doGet(UrlConfig urlConfig) throws Exception {
		Validate.isNotNull(urlConfig, "请求配置不可为空!");
		Validate.isNotNull(urlConfig.getUrl(), "请求的URL不能为空!");
		// 记录请求信息
		urlConfig.outRequestInfoStart(System.currentTimeMillis());
		boolean isSuccess = true;
		try {
			String url = urlConfig.getUrl();

			// 判断KV参数是否为空
			if ((null != urlConfig.getKvParams()) && (!urlConfig.getKvParams().isEmpty())) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(urlConfig.getKvParams().size());
				// 转化参数集合
				for (Map.Entry<String, String> entry : urlConfig.getKvParams().entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair((String) entry.getKey(), value));
					}
				}
				// 拼装URL
				url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, urlConfig.getCharset()));
			}

			HttpGet httpGet = new HttpGet(url);

			// 判断是否存在头部参数. 如果存在, 则添加请求头部参数
			if ((null != urlConfig.getHeads()) && (!urlConfig.getHeads().isEmpty())) {
				for (Map.Entry<String, String> e : urlConfig.getHeads().entrySet()) {
					httpGet.addHeader(((String) e.getKey()).toString(), ((String) e.getValue()).toString());
				}
			}

			// 执行请求
			CloseableHttpResponse response = httpClient.execute(httpGet);
			// 获取请求状态代码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				isSuccess = false;
				httpGet.abort();
			}
			response.getAllHeaders();
			HttpEntity entity = response.getEntity();
			String result = null;
			if (null != entity) {
				result = EntityUtils.toString(entity, urlConfig.getCharset());
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 记录请求信息
			urlConfig.outRequestInfoEnd(Long.valueOf(System.currentTimeMillis()), isSuccess);
		}
	}

	/**
	 * Description : POST 请求 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午4:38:38
	 * 
	 * @param urlConfig
	 * @return
	 * @throws Exception
	 */
	private String doPost(UrlConfig urlConfig) throws Exception {
		Validate.isNotNull(urlConfig, "请求配置不可为空!");
		Validate.isNotNull(urlConfig.getUrl(), "请求的URL不能为空!");
		urlConfig.outRequestInfoStart(Long.valueOf(System.currentTimeMillis()));
		boolean isSuccess = true;
		try {
			HttpPost httpPost = new HttpPost(urlConfig.getUrl());

			// 添加头部信息
			if ((null != urlConfig.getHeads()) && (!urlConfig.getHeads().isEmpty())) {
				for (Entry<String, String> e : urlConfig.getHeads().entrySet()) {
					httpPost.addHeader(e.getKey(), e.getValue());
				}
			}

			// 校验是否存在两种参数类型
			Validate.isTrue((!StringUtils.isEmpty(urlConfig.getJsonParams())) && (null != urlConfig.getKvParams()), "POST请求携带的参数只能使用一种形式:K-V 或 JSON!");

			// 添加JSON类型的参数
			if (!StringUtils.isEmpty(urlConfig.getJsonParams())) {
				StringEntity params = new StringEntity(urlConfig.getJsonParams(), urlConfig.getContentType());
				httpPost.setEntity(params);
			} else {
				//判断是否存在文件参数
				if (null != urlConfig.getFiles() && urlConfig.getFiles().length > 0) {
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					//设置编码格式
					builder.setCharset(urlConfig.getCharset());
					//设置模式:浏览器兼容模式
					builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

					//循环添加文件
					for (File file : urlConfig.getFiles()) {
						FileBody fileBody = new FileBody(file);
						builder.addPart(urlConfig.getRequestFileName(), fileBody);
					}

					//判断上传文件的同时是否存在KV参数
					if (null != urlConfig.getKvParams() && !urlConfig.getKvParams().isEmpty()) {
						for (Map.Entry<String, String> entry : urlConfig.getKvParams().entrySet()) {
							if (!StringUtils.isEmpty(entry.getValue())) {
								StringBody stringBody = new StringBody(entry.getValue(), urlConfig.getContentType());
								builder.addPart(entry.getKey(), stringBody);
							}
						}
					}

					HttpEntity fileParam = builder.build();
					httpPost.setEntity(fileParam);
				} else if (null != urlConfig.getKvParams() && !urlConfig.getKvParams().isEmpty()) {
					// POST FORM提交KV参数
					List<NameValuePair> pairs = new ArrayList<NameValuePair>(urlConfig.getKvParams().size());
					for (Map.Entry<String, String> entry : urlConfig.getKvParams().entrySet()) {
						String value = (String) entry.getValue();
						if (value != null) {
							pairs.add(new BasicNameValuePair((String) entry.getKey(), value));
						}
					}
					httpPost.setEntity(new UrlEncodedFormEntity(pairs, urlConfig.getCharset()));
				}
			}
			
			//执行请求
			CloseableHttpResponse response = httpClient.execute(httpPost);
			//获取网络请求编码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				isSuccess = false;
				httpPost.abort();
			}
			
			//获取返回的数据
			HttpEntity entity = response.getEntity();
			String result = null;
			if (null != entity) {
				result = EntityUtils.toString(entity, urlConfig.getCharset());
			}
			
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			//记录信息
			urlConfig.outRequestInfoEnd(System.currentTimeMillis(), isSuccess);
		}
	}
}
