package com.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.tools.common.Validate;
import com.tools.excel.common.ExcelType;
import com.tools.excel.common.ExcelUtil;
import com.tools.excel.read.ExcelReadValue;
import com.tools.excel.read.ExcelUserReadConfig;
import com.tools.excel.write.ExcelWriteConfig;
import com.tools.excel.write.ExcelWriteValue;

/**
 * Description : Excel读写工具类
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月14日 上午11:35:38
 */
public class ExcelTools {
	
	/**
	 * Description : Excel读取
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:35:51 
	 * 
	 * @param file
	 * @param readConfig
	 * @return
	 */
	public static <T> List<T> readExcel(File file, ExcelUserReadConfig<T> readConfig) {
		System.out.println("---------------------------------------read excel start---------------------------------------");
		long startTime = System.currentTimeMillis();
		boolean isSuccess = true;
		List<T> dataList = null;
		try {
			InputStream fileStream = new FileInputStream(file);
			dataList = ExcelReadValue.readExcel(fileStream, readConfig);
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		System.out.println("read excel is success: " + isSuccess);
		System.out.println("read excel use time: " + (System.currentTimeMillis() - startTime) + "ms");
		System.out.println("---------------------------------------read excel end---------------------------------------");
		return dataList;
	}

	/**
	 * Description : Excel写入(生成文件)
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:38:25 
	 * 
	 * @param excelType
	 * @param filePath
	 * @param fileName
	 * @param excelWriteConfig
	 */
	public static <T> void writeExcel(ExcelType excelType, String filePath, String fileName, ExcelWriteConfig<T> excelWriteConfig) {
		System.out.println("---------------------------------------write excel start---------------------------------------");
		long startTime = Long.valueOf(System.currentTimeMillis());
		boolean isSuccess = true;
		try {
			Validate.isNotNull(excelType, "文件类型不能为空!");
			Validate.isNotNull(filePath, "文件存储地址不能为空!");
			Validate.isNotNull(fileName, "文件名称不能为空!");
			//校验配置信息
			ExcelUtil.validateWriteConfig(false, excelWriteConfig);
			
			fileName = fileName + "." + excelType.name().toLowerCase();
			File excelFile = new File(filePath, fileName);
			Validate.isNotNull(fileName, "未读取到文件!");
			excelFile.createNewFile();
			System.out.println("write excel location: " + excelFile.getAbsolutePath());
			OutputStream outputStream = new FileOutputStream(excelFile);
			ExcelWriteValue.writeExcel(excelType, outputStream, excelWriteConfig);
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		System.out.println("write excel is success: " + isSuccess);
		System.out.println("write excel use time: " + (System.currentTimeMillis() - startTime) + "ms");
		System.out.println("---------------------------------------write excel end---------------------------------------");
	}

	/**
	 * Description : Excel写入(使用模板)
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:37:26 
	 * 
	 * @param templateFile
	 * @param filePath
	 * @param fileName
	 * @param excelWriteConfig
	 */
	public static <T> void writeExcel(File templateFile, String filePath, String fileName, ExcelWriteConfig<T> excelWriteConfig) {
		System.out.println("---------------------------------------write excel start---------------------------------------");
		long startTime = System.currentTimeMillis();
		boolean isSuccess = true;
		try {
			Validate.isNotNull(templateFile, "模板文件不能为空!");
			Validate.isNotNull(filePath, "文件存储地址不能为空!");
			Validate.isNotNull(fileName, "文件名称不能为空!");
			//校验配置信息
			ExcelUtil.validateWriteConfig(true, excelWriteConfig);
			
			InputStream excelTemplate = new FileInputStream(templateFile);
			fileName = fileName + templateFile.getName().substring(templateFile.getName().lastIndexOf("."), templateFile.getName().length()).toLowerCase();
			File excelFile = new File(filePath, fileName);
			excelFile.createNewFile();
			System.out.println("write excel location: " + excelFile.getAbsolutePath());
			OutputStream outputStream = new FileOutputStream(excelFile);
			ExcelWriteValue.writeExcel(excelTemplate, outputStream, excelWriteConfig);
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		}
		System.out.println("write excel is success: " + isSuccess);
		System.out.println("write excel use time: " + (System.currentTimeMillis() - startTime) + "ms");
		System.out.println("---------------------------------------write excel end---------------------------------------");
	}
}