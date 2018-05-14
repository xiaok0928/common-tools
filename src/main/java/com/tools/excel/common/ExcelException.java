package com.tools.excel.common;

import org.springframework.util.StringUtils;

/**
 * Description : Excel异常类
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月10日 下午6:52:58
 */
public class ExcelException extends RuntimeException {
	private static final long serialVersionUID = 8834681166062539688L;
	private String sheetName;
	private Integer sheetIndex;
	private Integer rowIndex;
	private String columnIndex;
	private ExcelExceptionEnum errorCode;
	private String errorMessage;

	public ExcelException() {
	}

	public ExcelException(ExcelExceptionEnum errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

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

	public Integer getRowIndex() {
		return this.rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getColumnIndex() {
		return this.columnIndex;
	}

	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex;
	}

	public ExcelExceptionEnum getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(ExcelExceptionEnum errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Excel解析错误类型:");
		buffer.append(ExcelExceptionEnum.codeToMessage(this.errorCode));
		buffer.append(";");
		buffer.append("详细描述:");
		buffer.append(this.errorMessage);

		if (!StringUtils.isEmpty(this.sheetName)) {
			buffer.append("Sheet名称:");
			buffer.append(this.sheetName);
			buffer.append(";");
		}

		if (!StringUtils.isEmpty(this.rowIndex)) {
			buffer.append(this.rowIndex);
			buffer.append("行;");
		}

		if (!StringUtils.isEmpty(this.columnIndex)) {
			buffer.append(this.columnIndex);
			buffer.append("列;");
		}

		return buffer.toString();
	}
}