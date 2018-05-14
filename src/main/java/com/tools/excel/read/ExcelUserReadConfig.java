package com.tools.excel.read;

import java.util.Map;

/**
 * Description : 读取Excel用户输入的相关配置<br/>
 * 优先级顺序如下:<br/>
 * SheetIndex > SheetName<br/>
 * endRowIndex > readRowNumber && readRowIndexArray<br/>
 * null == endRowIndex -> [readRowNumber > readRowIndexArray]<br/>
 * null == endRowIndex && null == readRowNumber && null == readRowIndexArray -> [startRowIndex = 0 && endRowIndex = data.lastRow]<br/>
 * 
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 上午9:53:11
 */
public class ExcelUserReadConfig<T> {
	//Sheet名称
	private String sheetName;
	//Sheet索引
	private Integer sheetIndex = 0;
	//开始行索引
	private Integer startRowIndex = 0;
	//结束行索引
	private Integer endRowIndex;
	//读取行数
	private Integer readRowNumber;
	//读取的固定行
	private Integer[] readRowIndexArray;
	//列:Excel列, 属性名; 例:Map<"A", "name">
	private Map<String, String> columnIndex;
	//选择接收返回数据的实体类
	private Class<T> targetClass;

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getSheetIndex() {
		return this.sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public Integer getStartRowIndex() {
		return this.startRowIndex;
	}

	public void setStartRowIndex(Integer startRowIndex) {
		this.startRowIndex = startRowIndex;
	}

	public Integer getEndRowIndex() {
		return this.endRowIndex;
	}

	public void setEndRowIndex(Integer endRowIndex) {
		this.endRowIndex = endRowIndex;
	}

	public Integer getReadRowNumber() {
		return this.readRowNumber;
	}

	public void setReadRowNumber(Integer readRowNumber) {
		this.readRowNumber = readRowNumber;
	}

	public Integer[] getReadRowIndexArray() {
		return this.readRowIndexArray;
	}

	public void setReadRowIndexArray(Integer[] readRowIndexArray) {
		this.readRowIndexArray = readRowIndexArray;
	}

	public Map<String, String> getColumnIndex() {
		return this.columnIndex;
	}

	public void setColumn(Map<String, String> columnIndex) {
		this.columnIndex = columnIndex;
	}

	public Class<T> getTargetClass() {
		return this.targetClass;
	}

	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
}