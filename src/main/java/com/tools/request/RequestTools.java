package com.tools.request;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * Description : Request请求工具类
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月11日 下午5:40:32
 */
public class RequestTools {
	
	/**
	 * Description : 获取GET请求的参数并转换成JSON
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:40:15 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JSONObject readGetParamsToJson(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			Iterator<String> paIter = parameterMap.keySet().iterator();
			while (paIter.hasNext()) {
				String key = ((String) paIter.next()).toString();
				String[] values = (String[]) parameterMap.get(key);
				jsonObject.put(key, values[0]);
			}
		} catch (Exception e) {
			throw e;
		}
		return jsonObject;
	}

	/**
	 * Description :  获取POST请求中的流数据并转换成JSON
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:40:50 
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JSONObject readPostStreamToJson(HttpServletRequest request) throws Exception {
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(request.getInputStream(), "UTF-8");
			StringBuffer sb = new StringBuffer();
			int size = 0;
			char[] buf = new char[1024];
			while ((size = isr.read(buf)) != -1) {
				sb.append(buf, 0, size);
			}
			JSONObject jsonObject = JSONObject.parseObject(sb.toString());
			return jsonObject;
		} catch (Exception e) {
			throw e;
		} finally {
			isr.close();
		}
	}

	/**
	 * Description : 获取IP地址
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:42:02 
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static String getIpAddress(HttpServletRequest request) throws Exception {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");

			if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
				ipAddress = request.getRemoteAddr();
				if ((ipAddress.equals("127.0.0.1")) || (ipAddress.equals("0:0:0:0:0:0:0:1"))) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}

			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if ((ipAddress != null) && (ipAddress.length() > 15) && (ipAddress.indexOf(",") > 0))
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		} catch (Exception e) {
			throw e;
		}
		return ipAddress;
	}
}