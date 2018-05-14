package com.tools.excel.read;

import java.util.Map;

/**
 * Description : Read Excel的相关配置
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 上午9:29:54
 */
public class ExcelReadConfig<T> {
	//读取的Sheet索引
	private Integer sheetIndex;
	//开始行索引
	private Integer startRowIndex;
	//结束行索引
	private Integer endRowIndex;
	//固定选择读取的行
	private Integer[] readRowIndexArray;
	//列索引:Excel列转换后的索引, 属性名
	private Map<Integer, String> columnIndex;
	//返回的实体类
	private Class<T> targetClass;

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

	public Integer[] getReadRowIndexArray() {
		return this.readRowIndexArray;
	}

	public void setReadRowIndexArray(Integer[] readRowIndexArray) {
		int length = readRowIndexArray.length;
		this.readRowIndexArray = new Integer[length];
		for (int i = 0; i < length; i++) {
			this.readRowIndexArray[i] = readRowIndexArray[i] - 1;
		}
	}

	public Map<Integer, String> getColumnIndex() {
		return this.columnIndex;
	}

	public void setColumnIndex(Map<Integer, String> columnIndex) {
		this.columnIndex = columnIndex;
	}

	public Class<T> getTargetClass() {
		return this.targetClass;
	}

	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
}