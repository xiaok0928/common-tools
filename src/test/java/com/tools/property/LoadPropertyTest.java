package com.tools.property;

import java.util.Map;

public class LoadPropertyTest {
	public static void main(String[] args) {
		Map<String, String> sysMap = LoadProperty.getInstance("config.properties").configMap;
		System.out.println(sysMap);
		System.out.println(LoadProperty.getInstance("config.properties").get("1"));
	}
}
