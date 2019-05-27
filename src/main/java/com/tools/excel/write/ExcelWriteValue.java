package com.tools.excel.write;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;

import com.tools.common.Validate;
import com.tools.excel.common.ExcelException;
import com.tools.excel.common.ExcelExceptionEnum;
import com.tools.excel.common.ExcelType;
import com.tools.excel.common.ExcelUtil;

/**
 * Description : Excel写入
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午1:04:47
 */
public class ExcelWriteValue {
	
	/**
	 * Description : 创建Excel并写入数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:05:04 
	 * 
	 * @param excelType
	 * @param outputStream
	 * @param excelWriteConfig
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws ExcelException
	 */
	public static <T> void writeExcel(ExcelType excelType, OutputStream outputStream, ExcelWriteConfig<T> excelWriteConfig) throws ExcelException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Validate.isNotNull(excelType, "文件类型不能为空!");
		Validate.isNotNull(outputStream, "文件路径参数无效!");
		
		//根据用户输入的文件类型创建不同类型的工作区
		Workbook workbook = null;
		if (excelType == ExcelType.XLS) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new SXSSFWorkbook();
		}

		//写入Excel的Sheet
		writeExcelSheet(false, workbook, excelWriteConfig);
		
		//写入文件
		workbook.write(outputStream);
		//刷新缓冲区
		outputStream.flush();
		outputStream.close();
		//停止读写
		workbook.close();
	}

	/**
	 * Description : 根据用户提供的模板信息填写Excel数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:08:04 
	 * 
	 * @param excelTemplate
	 * @param outputStream
	 * @param excelWriteConfig
	 * @throws ExcelException
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static <T> void writeExcel(InputStream excelTemplate, OutputStream outputStream, ExcelWriteConfig<T> excelWriteConfig) throws ExcelException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Validate.isNotNull(excelTemplate, "模板文件无效!");
		Validate.isNotNull(outputStream, "文件路径参数无效!");
		Workbook workbook = null;
		try {
			//根据用户提供的模板数据创建工作空间
			workbook = WorkbookFactory.create(excelTemplate);
		} catch (Exception e) {
			throw new ExcelException(ExcelExceptionEnum.EW00002, "使用Excel模板创建Excel工作空间失败:" + e.getMessage());
		}

		//写入Excel的Sheet
		writeExcelSheet(true, workbook, excelWriteConfig);
	}

	/**
	 * Description : 写入Excel的Sheet
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:09:15 
	 * 
	 * @param useExcelTemplate
	 * @param workbook
	 * @param outputStream
	 * @param excelWriteConfig
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws ExcelException
	 */
	private static <T> void writeExcelSheet(boolean useExcelTemplate, Workbook workbook, ExcelWriteConfig<T> excelWriteConfig) throws ExcelException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Sheet sheet = null;

		//如果使用的是Excel模板的话,则根据Sheet名称获取Sheet
		if (useExcelTemplate) {
			if (null == excelWriteConfig.getSheetIndex()) {
				Validate.isTrue(StringUtils.isEmpty(excelWriteConfig.getSheetName()), "使用Excel模板时,Sheet索引或Sheet名称不能为空!");
				sheet = workbook.getSheet(excelWriteConfig.getSheetName());
			} else {
				sheet = workbook.getSheetAt(excelWriteConfig.getSheetIndex());
			}
		}

		//如果Sheet为空,则创建一个
		if (null == sheet) {
			String sheetName = StringUtils.isEmpty(excelWriteConfig.getSheetName()) ? "未命名" : excelWriteConfig.getSheetName();
			sheet = workbook.createSheet(sheetName);
		}

		//获取开始行的索引值
		Integer startRow = excelWriteConfig.getStartRow();
		if (excelWriteConfig.getHeadsMap().size() > 0) {
			//如果用户填入的有头部标题,则根据用户提供的索引值判断该行是否存在,如果存在则直接写入数据,不存在则创建一行, 并返回下一行的索引值
			Row headerRow = sheet.getRow(excelWriteConfig.getStartRow());
			if (null == headerRow) {
				headerRow = sheet.createRow(excelWriteConfig.getStartRow());
			}
			Integer rowIndex = writeExcelHead(headerRow, excelWriteConfig.getHeadsMap());
			startRow = rowIndex + 1;
		}

		//写入Excel数据
		writeExcelValue(sheet, excelWriteConfig.getFieldMap(), excelWriteConfig.getDataList(), startRow);
	}

	/**
	 * Description : 写入头部标题
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:13:24 
	 * 
	 * @param row
	 * @param headerMapping
	 * @return
	 */
	private static Integer writeExcelHead(Row row, Map<Integer, String> headerMapping) {
		Cell cell = null;
		//循环头部标题,并写入对应的单元格
		for (Map.Entry<Integer, String> headerValue : headerMapping.entrySet()) {
			//判断获取的单元格是否存在, 如果不存在则创建一个. 存在则直接写入数据
			cell = row.getCell(headerValue.getKey());
			if (null == cell) {
				cell = row.createCell(headerValue.getKey());
			}
			//单元格写入数据
			writeExcelCellValue(cell, headerValue.getValue());
		}
		//返回当前所在的行索引
		return cell.getRowIndex();
	}

	/**
	 * Description : Excel数据写入
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:19:34 
	 * 
	 * @param sheet
	 * @param fieldMapping
	 * @param datas
	 * @param dataStartRow
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws ExcelException
	 */
	private static <T> void writeExcelValue(Sheet sheet, Map<Integer, String> fieldMapping, List<T> datas, Integer dataStartRow) throws ExcelException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Validate.isNotNull(dataStartRow, "起始行计算错误!");
		Validate.isNotNull(sheet, "Sheet创建错误!");
		//循环写入数据
		for (int i = 0; i < datas.size(); i++) {
			Row valueRow = sheet.getRow(dataStartRow + i);
			if (null == valueRow) {
				valueRow = sheet.createRow(dataStartRow + i);
			}
			Object t = datas.get(i);
			writeExcelRowValue(valueRow, fieldMapping, t);
		}
	}

	/**
	 * Description : Excel行数据写入
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:21:32 
	 * 
	 * @param row
	 * @param fieldMapping
	 * @param tclass
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws ExcelException
	 */
	private static <T> void writeExcelRowValue(Row row, Map<Integer, String> fieldMapping, T tclass) throws ExcelException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Validate.isNotNull(row, "创建写入行出错!");
		Class<?> clazz = tclass.getClass();
		//循环写入每一列的值
		for (Map.Entry<Integer, String> fieldMap : fieldMapping.entrySet()) {
			Cell cell = row.getCell(fieldMap.getKey());
			if (null == cell) {
				cell = row.createCell(fieldMap.getKey());
			}
			//利用反射获取该类的数据
			PropertyDescriptor property = ExcelUtil.getPropertyDescriptor(clazz, fieldMap.getValue());
			//通过反射调用该公共方法的数据并写入单元格
			writeExcelCellValue(cell, property.getReadMethod().invoke(tclass));
		}
	}

	/**
	 * Description : 根据数据类型写入单元格数据
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:23:27 
	 * 
	 * @param cell
	 * @param value
	 */
	private static void writeExcelCellValue(Cell cell, Object value) {
		if (value instanceof String) {
			cell.setCellValue(value.toString());
		} else if (value instanceof Boolean) {
			cell.setCellValue(((Boolean) value).booleanValue());
		} else if ((value instanceof Double)) {
			cell.setCellValue(((Double) value).doubleValue());
		} else if ((value instanceof Float)) {
			cell.setCellValue(((Float) value).floatValue());
		} else if ((value instanceof Long)) {
			cell.setCellValue(((Long) value).longValue());
		} else if ((value instanceof Integer)) {
			cell.setCellValue(((Integer) value).intValue());
		} else if ((value instanceof Date)) {
			cell.setCellValue(ExcelUtil.dateFormat.format((Date) value));
		} else if ((value instanceof Short)) {
			cell.setCellValue(((Short) value).shortValue());
		} else if ((value instanceof Byte)) {
			cell.setCellValue(((Byte) value).byteValue());
		} else if (value == null) {
			cell.setCellValue("");
		} else {
			throw new ExcelException(ExcelExceptionEnum.EW00003, "写入的值类型转换错误!坐标:" + (cell.getRowIndex() + 1) + "," + ExcelUtil.convertIndexToNav(cell.getColumnIndex() + 1) + "value:" + value);
		}
	}
}