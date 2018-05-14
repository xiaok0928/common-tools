package com.tools.excel.common;

/**
 * Description : Excel错误枚举类
 * <br>Created By : xiaok0928@hotmail.com 
 * <br>Creation Time : 2018年5月14日 上午11:23:12
 */
public enum ExcelExceptionEnum {
	//Excel 读取错误代码
	ER00001("文件读取错误"),
	ER00002("工作区读取错误"),
	ER00003("Sheet读取错误"),
	ER00004("行读取错误"),
	ER00005("单元格读取错误"),
	ER00006("实例化对象错误"),
	ER00007("对象属性赋值错误"),

	//Excel 写入错误代码
	EW00001("数据类型转换写入错误!"),
	EW00002("创建工作区错误!"),
	EW00003("数据类型转换错误!"),

	//Excel通用错误代码
	EC00001("参数不匹配");

	private String message;

	private ExcelExceptionEnum() {
	}

	private ExcelExceptionEnum(String message) {
		this.message = message;
	}

	public String message() {
		return this.message;
	}

	/**
	 * Description : 错误代码转错误信息
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:55:30 
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String codeToMessage(ExcelExceptionEnum errorCode) {
		for (ExcelExceptionEnum tag : values()) {
			if (errorCode.name().equals(tag.name())) {
				return tag.message;
			}
		}
		return "";
	}
}