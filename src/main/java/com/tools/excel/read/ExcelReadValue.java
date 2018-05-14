package com.tools.excel.read;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.util.TypeUtils;
import com.tools.common.Validate;
import com.tools.excel.common.ExcelException;
import com.tools.excel.common.ExcelExceptionEnum;
import com.tools.excel.common.ExcelUtil;

/**
 * Description : 读取Excel相关方法
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 上午9:32:37
 */
public class ExcelReadValue {
	
	/**
	 * Description : 读取Excel
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午9:34:12 
	 * 
	 * @param file
	 * @param readConfig
	 * @return
	 * @throws Exception 
	 */
	public static <T> List<T> readExcel(InputStream file, ExcelUserReadConfig<T> readConfig) throws Exception {
		//判断文件是否存在
		Validate.isTrue(null == file, "未读取到文件!");

		//获取工作区
		Workbook workbook = getExcelWorkBook(file);

		//转换用户输入的Config
		ExcelReadConfig<T> excelConfig = ExcelUtil.converConfig(readConfig, workbook);

		//读取Sheet
		Sheet sheet = getExcelSheet(workbook, excelConfig.getSheetIndex().intValue());

		//返回实体
		return readExcelValue(sheet, excelConfig);
	}

	/**
	 * Description : 获取Excel的工作区
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午9:36:17 
	 * 
	 * @param workbookStream
	 * @return
	 * @throws ExcelException
	 */
	private static Workbook getExcelWorkBook(InputStream workbookStream) throws ExcelException {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(workbookStream);
		} catch (Exception e) {
			throw new ExcelException(ExcelExceptionEnum.ER00002, "创建WorkbookFactory失败!");
		}
		Validate.isNotNull(null == workbook, "未读取到工作区!");
		return workbook;
	}

	/**
	 * Description : 获取Sheet
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午9:36:47 
	 * 
	 * @param workbook
	 * @param sheetIndex
	 * @return
	 */
	private static Sheet getExcelSheet(Workbook workbook, int sheetIndex) {
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		Validate.isNotNull(null == sheet, "未读取到Sheet!");
		return sheet;
	}

	/**
	 * Description : 读取Excel的数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午9:37:28 
	 * 
	 * @param sheet
	 * @param excelConfig
	 * @return
	 * @throws Exception 
	 */
	private static <T> List<T> readExcelValue(Sheet sheet, ExcelReadConfig<T> excelConfig) throws Exception {
		List<T> dataList = new ArrayList<T>();
		Row row = null;
		//判断起始行及结束行是否有值
		if (null != excelConfig.getStartRowIndex() && null != excelConfig.getEndRowIndex()) {
			// 循环要读取的行
			for (int i = excelConfig.getStartRowIndex(); i <= excelConfig.getEndRowIndex(); i++) {
				Map<String, Cell> fieldMapping = new HashMap<String, Cell>();
				//获取行
				row = sheet.getRow(i);
				if (null != row) {
					//循环每一列
					for (Map.Entry<Integer, String> columnField : excelConfig.getColumnIndex().entrySet()) {
						//获取Cell的值
						Cell cell = getCellValue(row, columnField.getKey());
						fieldMapping.put(columnField.getValue(), cell);
					}
					//转换为实体数据
					T tValue = convertJavaBeanValue(excelConfig.getTargetClass(), fieldMapping);
					//检验数据的有效性
					if (valueIsEffect(tValue, fieldMapping)) {
						dataList.add(tValue);
					}
				}
			}
		} else {
			//如果没有值的话说明用户需要读取固定的行数
			//循环要读取的固定行数
			for (Integer index : excelConfig.getReadRowIndexArray()) {
				Map<String, Cell> fieldMapping = new HashMap<String, Cell>();
				//获取行
				row = sheet.getRow(index);
				if (null != row) {
					//循环每一列的值
					for (Map.Entry<Integer, String> columnField : excelConfig.getColumnIndex().entrySet()) {
						//读取Cell值
						Cell cell = getCellValue(row, (Integer) columnField.getKey());
						fieldMapping.put(columnField.getValue(), cell);
					}
					//转换为实体数据
					T tValue = convertJavaBeanValue(excelConfig.getTargetClass(), fieldMapping);
					//检验数据的有效性
					if (valueIsEffect(tValue, fieldMapping)) {
						dataList.add(tValue);
					}
				}
			}
		}
		return dataList;
	}

	/**
	 * Description : 获取Cell的值
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午11:09:24 
	 * 
	 * @param row
	 * @param columnIndex
	 * @return
	 */
	private static Cell getCellValue(Row row, Integer columnIndex) {
		Validate.isTrue(null == row, "读取的Row为空!");
		Validate.isTrue(null == columnIndex, "索引列无效:" + columnIndex);
		
		//判断获取的Cell值是否为空
		if (null == row.getCell(columnIndex)) {
			return null;
		}

		return row.getCell(columnIndex);
	}

	/**
	 * Description : 将数据转换为实体对象数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午11:09:54 
	 * 
	 * @param targetClazz
	 * @param excelValue
	 * @return
	 * @throws ExcelException
	 */
	private static <T> T convertJavaBeanValue(Class<T> targetClazz, Map<String, Cell> excelValue) throws ExcelException {
		T tClass = null;
		try {
			tClass = targetClazz.newInstance();
		} catch (Exception e) {
			throw new ExcelException(ExcelExceptionEnum.ER00006, "目标对象反射实例化过程出错!");
		}

		//循环映射该实体对象的数据
		for (Map.Entry<String, Cell> column : excelValue.entrySet()) {
			//获取属性
			PropertyDescriptor property = ExcelUtil.getPropertyDescriptor(targetClazz, column.getKey());
			//获取参数类型
			Class<?> paramType = property.getWriteMethod().getParameterTypes()[0];
			//转换并返回
			Object value = TypeUtils.cast(ExcelUtil.convertCell(column.getValue()), paramType, null);
			try {
				//写入
				property.getWriteMethod().invoke(tClass, value);
			} catch (Exception e) {
				throw new ExcelException(ExcelExceptionEnum.ER00007, "目标对象属性反射赋值过程出错!");
			}
		}
		return tClass;
	}
	
	/**
	 * Description : 判断数据是否有效
	 * 既判断不为空的对象其属性也不包含不为空的数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午3:23:46 
	 * 
	 * @param valueClass
	 * @param excelValue
	 * @return
	 * @throws Exception
	 */
	private static <T> boolean valueIsEffect(T valueClass, Map<String, Cell> excelValue) throws Exception {
		//获取该数据实体类的类
		Class<?> clazz = valueClass.getClass();
		//标识数据是否有效, 有效的依据为该实体中只要存在一个不为空的属性既有效
		boolean isEffect = false;
		//循环要判断的属性
		for (Map.Entry<String, Cell> column : excelValue.entrySet()) {
			PropertyDescriptor property = ExcelUtil.getPropertyDescriptor(clazz, column.getKey());
			try {
				//获取该属性的值
				Object obj = property.getReadMethod().invoke(valueClass);
				//判断该值是否为空
				if (!StringUtils.isEmpty(obj)) {
					isEffect = true;
					return isEffect;
				}
			} catch (Exception e) {
				throw new ExcelException(ExcelExceptionEnum.ER00007, "目标对象属性反射赋值过程出错!");
			}
		}
		return isEffect;
	}
}