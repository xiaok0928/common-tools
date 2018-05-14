package com.tools.xml;

import java.util.Date;

public class XmlToolsTest {
	public static void main(String[] args) {
		TestBean t1 = new TestBean();
		t1.setName("ccc");
		t1.setAge(23);
		t1.setBirthday(new Date());
		System.out.println(XmlTools.convertToXml(t1));
	}
}
