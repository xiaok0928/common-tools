package com.tools.excel.common;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.tools.common.Validate;
import com.tools.excel.read.ExcelReadConfig;
import com.tools.excel.read.ExcelUserReadConfig;
import com.tools.excel.write.ExcelWriteConfig;

/**
 * Description : Excel辅助通用类
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月10日 下午6:56:03
 */
public class ExcelUtil {
	//时间格式化
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Description : Excel导航转索引
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:56:31 
	 * 
	 * @param columnNav
	 * @return
	 */
	public static int convertNavToIndex(String columnNav) {
		columnNav = columnNav.toUpperCase();
		int result = 0;
		int valLength = columnNav.length();
		for (int i = 0; i < valLength; i++) {
			int num = 0;
			char ch = columnNav.charAt(valLength - i - 1);
			Validate.isTrue((ch < 'A') || (ch > 'Z'), new StringBuilder().append("非法值:").append(columnNav).toString());
			num = ch - 'A' + 1;
			num = (int) (num * Math.pow(26.0D, i));
			result += num;
		}
		return result - 1;
	}

	/**
	 * Description : Excel索引转导航
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:56:52 
	 * 
	 * @param index
	 * @return
	 */
	public static String convertIndexToNav(int index) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; index >= 0; i++) {
			Character c = null;
			if (i != 0)
				c = Character.valueOf((char) (index % 26 + 65));
			else {
				c = Character.valueOf((char) (index % 26 + 65 - 1));
			}
			buffer.insert(0, c);
			index = index / 26 - 1;
		}
		return buffer.toString();
	}

	/**
	 * Description : Excel用户输入的配置文件转程序可读配置文件
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:57:18 
	 * 
	 * @param userConfig
	 * @param workbook
	 * @return
	 */
	public static <T> ExcelReadConfig<T> converConfig(ExcelUserReadConfig<T> userConfig, Workbook workbook) {
		ExcelReadConfig<T> config = new ExcelReadConfig<T>();

		//校验
		Validate.isTrue(userConfig.getSheetIndex().intValue() < 0, "Sheet值不能小于或等于0!");
		
		//判断用户输入的Sheet索引并转化程序可读的Sheet索引值
		if (userConfig.getSheetIndex().intValue() != 0) {
			//如果输入的Sheet索引值不为0的话,则由输入的值-1得到程序可读下标
			config.setSheetIndex(userConfig.getSheetIndex().intValue() - 1);
		} else if (!StringUtils.isEmpty(userConfig.getSheetName())) {
			//如果用户没有针对Sheet索引输入的话则根据用户输入的Sheet名称获取Sheet索引
			int sheetIndex = workbook.getSheetIndex(userConfig.getSheetName());
			if (sheetIndex < 0) {
				ExcelException e = new ExcelException();
				e.setSheetName(userConfig.getSheetName());
				e.setErrorCode(ExcelExceptionEnum.ER00003);
				e.setErrorMessage("该Sheet名称不存在!");
				throw e;
			}
			config.setSheetIndex(sheetIndex);
		} else {
			config.setSheetIndex(userConfig.getSheetIndex());
		}

		//判断是否有输入起始行
		boolean hasStartRowIndex = false;
		//校验用户输入的起始行数, 并转化为程序可读的起始行
		Validate.isTrue(userConfig.getStartRowIndex().intValue() < 0, "读取Excel的开始行的值不能小于或等于0!");
		if (userConfig.getStartRowIndex().intValue() != 0) {
			//如果输入的起始行索引值不为0的话,则由输入的值-1得到程序可读下标
			config.setStartRowIndex(userConfig.getStartRowIndex().intValue() - 1);
			hasStartRowIndex = true;
		}

		//标识是否存在要读取的结束行
		boolean hasEndRowIndex = false;
		//校验用户输入要读取的行数, 并转化为程序可读取的行数
		if (!StringUtils.isEmpty(userConfig.getEndRowIndex())) {
			Validate.isTrue(userConfig.getEndRowIndex().intValue() < 0, "读取Excel的结束行的值不能小于0!");
			Validate.isTrue(userConfig.getEndRowIndex().intValue() - 1 < config.getStartRowIndex().intValue(), "读取Excel的结束行值不能小于开始行值!");
			config.setEndRowIndex(userConfig.getEndRowIndex().intValue() - 1);
			hasEndRowIndex = true;
		}
		
		//如果读取行数的参数不为空,则设置结束行 = 其实行 + 读取的行数
		if (!StringUtils.isEmpty(userConfig.getReadRowNumber()) && userConfig.getReadRowNumber().intValue() > 0) {
			config.setEndRowIndex((userConfig.getStartRowIndex() - 1) + (userConfig.getReadRowNumber() - 1));
			hasEndRowIndex = true;
		}
		
		//标识用户是否输入了要读取的固定行数
		boolean hasReadRowArray = false;
		if (null != userConfig.getReadRowIndexArray() && userConfig.getReadRowIndexArray().length > 0) {
			config.setReadRowIndexArray(userConfig.getReadRowIndexArray());
			hasReadRowArray = true;
		}
		
		//判断用户输入的没有起始行
		if (!hasStartRowIndex) {
			//判断没有结束行
			if (!hasEndRowIndex) {
				//判断用户也没有输入要读取的固定行
				if (!hasReadRowArray) {
					//取默认值
					config.setStartRowIndex(userConfig.getStartRowIndex());
					config.setEndRowIndex(workbook.getSheetAt(config.getSheetIndex().intValue()).getLastRowNum());
				}
			} else {
				//取默认值
				config.setStartRowIndex(userConfig.getStartRowIndex());
			}
		} else {
			//如果没有结束行的话,则取结束行的默认值
			if (!hasEndRowIndex) {
				//取默认值
				config.setEndRowIndex(workbook.getSheetAt(config.getSheetIndex().intValue()).getLastRowNum());
			}
		}

		//判断用户读取的列,并转化为程序可识别的参数
		Validate.isTrue((null == userConfig.getColumnIndex()) || (userConfig.getColumnIndex().isEmpty()), "读取Excel的列不能为空!");
		Map<Integer, String> columnIndex = new HashMap<Integer, String>();
		for (Map.Entry<String, String> columnKey : userConfig.getColumnIndex().entrySet()) {
			Validate.isNotNull(columnKey.getKey(), "列值不能为空!");
			columnIndex.put(convertNavToIndex((String) columnKey.getKey()), columnKey.getValue());
		}
		config.setColumnIndex(columnIndex);

		//判断接收实体是否为空
		Validate.isTrue(null == userConfig.getTargetClass(), "对象实体类不能为空!");
		config.setTargetClass(userConfig.getTargetClass());

		return config;
	}

	/**
	 * Description : 转化Cell值为Object
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午7:08:25 
	 * 
	 * @param cell
	 * @return
	 */
	public static Object convertCell(Cell cell) {
		if (cell == null) {
			return null;
		}
		int cellType = cell.getCellType();
		Object value = null;
		switch (cellType) {
			case Cell.CELL_TYPE_NUMERIC:
				Object inputValue = null;
				double doubleVal = cell.getNumericCellValue();
				if (DateUtil.isCellDateFormatted(cell)) {
					inputValue = DateUtil.getJavaDate(doubleVal);
				} else {
					long longVal = Math.round(cell.getNumericCellValue());
					if (Double.parseDouble(longVal + ".0") == doubleVal) {
						inputValue = longVal;
					} else {
						inputValue = doubleVal;
					}
				}
				value = inputValue;
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_BLANK:
				value = null;
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				boolean bool = cell.getBooleanCellValue();
				value = Boolean.valueOf(bool);
				break;
			case Cell.CELL_TYPE_ERROR:
				ExcelException e = new ExcelException();
				e.setSheetName(cell.getSheet().getSheetName());
				e.setSheetIndex(cell.getSheet().getWorkbook().getSheetIndex(cell.getSheet()));
				e.setRowIndex(cell.getRowIndex());
				e.setColumnIndex(convertIndexToNav(cell.getColumnIndex()));
				e.setErrorCode(ExcelExceptionEnum.ER00005);
				e.setErrorMessage("单元格类型错误!无法识别的单元格类型!");
				throw e;
			default:
				throw new ExcelException(ExcelExceptionEnum.ER00001, "不支持的Excel类型:" + cellType);
			}
		return value;
	}

	/**
	 * Description : 验证Excel写入配置文件的有效性
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午7:17:21 
	 * 
	 * @param useExcelTemplate
	 * @param excelWriteConfig
	 */
	public static <T> void validateWriteConfig(boolean useExcelTemplate, ExcelWriteConfig<T> excelWriteConfig) {
		Validate.isNotNull(excelWriteConfig, "未配置Excel写入信息!");
		if (useExcelTemplate) {
			Validate.isTrue(StringUtils.isEmpty(excelWriteConfig.getSheetIndex()) && (StringUtils.isEmpty(excelWriteConfig.getSheetName())), "有使用Excel模板,则Sheet索引或Sheet名称不能为空!");
		}
		Validate.isNotNull(excelWriteConfig.getStartRow(), "开始行索引不能为空!");
		Validate.isTrue(excelWriteConfig.getFieldMap().size() < 1, "字段映射不能为空!");
		Validate.isTrue((null == excelWriteConfig.getDataList()) || (excelWriteConfig.getDataList().isEmpty()), "写入Excel的数据不能为空!");
	}

	/**
	 * Description : 反射赋值属性
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午7:20:09 
	 * 
	 * @param clazz
	 * @param propertyName
	 * @return
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
		return BeanUtils.getPropertyDescriptor(clazz, propertyName);
	}
}