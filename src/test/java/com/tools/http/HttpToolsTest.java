package com.tools.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.tools.http.common.ConnectConfig;
import com.tools.http.common.UrlConfig;

public class HttpToolsTest {
	public static void main(String[] args) throws Exception {
		ConnectConfig connect = new ConnectConfig();
		connect.setMaxConnect(200);
		UrlConfig config = new UrlConfig();
		config.setUrl("https://httpbin.org/post");
		Map<String, String> param = new HashMap<String, String>();
		param.put("username", "test");
		param.put("pwd", "test");
		config.setKvParams(param);
		config.setFiles(new File[] {new File("D:/template.xlsx")});
		System.out.println(HttpTools.getInstance(connect).sendForPost(config));
	}
}
