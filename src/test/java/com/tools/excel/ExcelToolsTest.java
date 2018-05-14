package com.tools.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tools.excel.common.ExcelType;
import com.tools.excel.read.ExcelUserReadConfig;
import com.tools.excel.write.ExcelWriteConfig;
import com.tools.excel.write.ExcelWriteFieldMapping;

public class ExcelToolsTest {

	public static void main(String[] args) {
//		write1();
//		write2();
		read();
	}
	
	public static void write1() {
		ExcelWriteConfig<TestBean> config = new ExcelWriteConfig<TestBean>();
		config.setDataList(getDatas());
		config.setSheetIndex(1);
		config.setStartRow(2);
		
		ExcelWriteFieldMapping fieldMap = new ExcelWriteFieldMapping();
		fieldMap.put("A", "name").setHeadName("名称");
		fieldMap.put("B", "age").setHeadName("年龄");
		fieldMap.put("c", "birthday").setHeadName("出生日期");
		config.setExcelWriteFieldMapping(fieldMap);
		ExcelTools.writeExcel(new File("D:/template.xlsx"), "D:/", "test1", config);
	}
	
	public static void write2() {
		ExcelWriteConfig<TestBean> config = new ExcelWriteConfig<TestBean>();
		config.setDataList(new ArrayList<TestBean>());
		config.setSheetIndex(1);
		config.setStartRow(1);
		
		ExcelWriteFieldMapping fieldMap = new ExcelWriteFieldMapping();
		fieldMap.put("A", "name").setHeadName("名称");
		fieldMap.put("B", "age").setHeadName("年龄");
		fieldMap.put("c", "birthday").setHeadName("出生日期");
		config.setExcelWriteFieldMapping(fieldMap);
		ExcelTools.writeExcel(ExcelType.XLSX, "D:/", "test2", config);
	}
	
	public static void read() {
		ExcelUserReadConfig<TestBean> config = new ExcelUserReadConfig<TestBean>();
		config.setSheetIndex(1);
//		config.setStartRowIndex(3);
//		config.setReadRowNumber(1);
		config.setReadRowIndexArray(new Integer[] {4, 6});
		config.setTargetClass(TestBean.class);
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("A", "name");
		fieldMap.put("B", "age");
		fieldMap.put("c", "birthday");
		config.setColumn(fieldMap);
		List<TestBean> dataList = ExcelTools.readExcel(new File("D:/test1.xlsx"), config);
		for (TestBean temp : dataList) {
			System.out.println("name:" + temp.getName() + "---age:" + temp.getAge() + "---birthday:" + temp.getBirthday());
		}
	}
	
	//data
	public static List<TestBean> getDatas() {
		List<TestBean> datas = new ArrayList<TestBean>();
		TestBean t1 = new TestBean();
		t1.setName("ccc");
		t1.setAge(111);
		t1.setBirthday(new Date());
		datas.add(t1);
		
		TestBean t2 = new TestBean();
		t2.setName("aaa");
		t2.setAge(222);
		t2.setBirthday(new Date());
		datas.add(t2);
		
		TestBean t3 = new TestBean();
		t3.setName("qqq");
		t3.setAge(333);
		t3.setBirthday(new Date());
		datas.add(t3);
		
		TestBean t4 = new TestBean();
		t4.setName("eee");
		t4.setAge(444);
		t4.setBirthday(new Date());
		datas.add(t4);
		
		return datas;
	}
}
