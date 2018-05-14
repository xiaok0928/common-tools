package com.tools.excel.write;

import java.util.HashMap;
import java.util.Map;

import com.tools.common.Validate;

/**
 * Description : 写入Excel的字段及头部
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 上午11:49:08
 */
public class ExcelWriteFieldMapping {
	//Map<Excel列导航, Map<字段名称, 标题名称>>
	private Map<String, Map<String, ExcelWriteFieldAttribute>> fieldMapping = new HashMap<String, Map<String, ExcelWriteFieldAttribute>>();

	/**
	 * Description : 设置映射字段及Head标题
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 下午1:04:15 
	 * 
	 * @param columnIndex
	 * @param fieldName
	 * @return
	 */
	public ExcelWriteFieldAttribute put(String columnIndex, String fieldName) {
		Validate.isNotNull(columnIndex, "索引列不能为空!");
		Validate.isNotNull(fieldName, "字段不能为空!");
		//存放字段名称及标题名称
		Map<String, ExcelWriteFieldAttribute> attrMap = this.fieldMapping.get(columnIndex);
		if ((null == attrMap) || (attrMap.size() < 1)) {
			attrMap = new HashMap<String, ExcelWriteFieldAttribute>();
			this.fieldMapping.put(columnIndex, attrMap);
		}
		//相信很多人看不懂这里. 我在这里稍微的解释一下
		//声明后的变量在内存中的地址是不会变的.当我们初始化好一个Map时, 他在内存中的地址是已经固定的.
		//当第二个Map声明出来后, 他在内存中的地址也是已经固定了.
		//当我们吧第二个Map放入第一个Map的时候实际是第一个Map记录了第二个Map的内存地址
		//因此我们在给第二个Map赋值的时候, 放入第一个Map里的Map是不用做任何更改的.
		ExcelWriteFieldAttribute attr = new ExcelWriteFieldAttribute();
		attrMap.put(fieldName, attr);
		return attr;
	}

	public Map<String, Map<String, ExcelWriteFieldAttribute>> getFieldMapping() {
		return this.fieldMapping;
	}

	/**
	 * Description : Head名称
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月11日 上午11:48:24
	 */
	public class ExcelWriteFieldAttribute {
		private String headName;

		public String getHeadName() {
			return this.headName;
		}

		public void setHeadName(String headName) {
			this.headName = headName;
		}
	}
}