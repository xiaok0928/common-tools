package com.tools.excel.write;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.tools.common.Validate;
import com.tools.excel.common.ExcelUtil;
import com.tools.excel.write.ExcelWriteFieldMapping.ExcelWriteFieldAttribute;

/**
 * Description : Excel 写入配置
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 上午11:21:24
 */
public class ExcelWriteConfig<T> {
	//Sheet索引
	private Integer sheetIndex;
	//Sheet名称
	private String sheetName;
	//开始行
	private Integer startRow;
	//头部:转换后的索引, 标题名称
	private Map<Integer, String> headsMap = new HashMap<Integer, String>();
	//字段:转换后的索引, 字段名
	private Map<Integer, String> fieldMap = new HashMap<Integer, String>();
	//数据
	private List<T> dataList;

	public Integer getSheetIndex() {
		return this.sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		sheetIndex = sheetIndex - 1;
		Validate.isTrue(sheetIndex.intValue() < 0, "Sheet索引值不能小于1");
		this.sheetIndex = sheetIndex;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getStartRow() {
		return this.startRow;
	}

	public void setStartRow(Integer startRow) {
		startRow = startRow - 1;
		Validate.isTrue(startRow.intValue() < 0, "开始行数值不能小于1");
		this.startRow = startRow;
	}

	public Map<Integer, String> getHeadsMap() {
		return this.headsMap;
	}

	public Map<Integer, String> getFieldMap() {
		return this.fieldMap;
	}

	public List<T> getDataList() {
		return this.dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public void setExcelWriteFieldMapping(ExcelWriteFieldMapping excelWriteFieldMapping) {
		Validate.isTrue(null == excelWriteFieldMapping, "字段映射不可为空!");
		Map<String, Map<String, ExcelWriteFieldAttribute>> userInputFieldMapping = excelWriteFieldMapping.getFieldMapping();
		//解析Head及列
		for (Map.Entry<String, Map<String, ExcelWriteFieldAttribute>> inputMap : userInputFieldMapping.entrySet()) {
			Map<String, ExcelWriteFieldAttribute> fieldMapping = inputMap.getValue();
			for (Map.Entry<String, ExcelWriteFieldAttribute> fieldMap : fieldMapping.entrySet()) {
				String headValue = fieldMap.getValue().getHeadName();
				if (!StringUtils.isEmpty(headValue)) {
					this.headsMap.put(ExcelUtil.convertNavToIndex(inputMap.getKey()), headValue);
				}
				this.fieldMap.put(ExcelUtil.convertNavToIndex(inputMap.getKey()),fieldMap.getKey());
			}
		}
	}
}