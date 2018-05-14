package com.tools.common;

import org.springframework.util.StringUtils;

/**
 * Description : 基础校验
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月10日 下午6:11:20
 */
public class Validate {
	
	/**
	 * Description : 校验:数据不为空
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:11:30 
	 * 
	 * @param object
	 * @param message
	 * @throws IllegalArgumentException
	 */
	public static void isNotNull(Object object, String message) throws IllegalArgumentException {
		if (StringUtils.isEmpty(object))
			throw new IllegalArgumentException(message);
	}

	/**
	 * Description : 校验表达式为真
	 * <br/>Created By : xiaok0928@hotmail.com 
	 * <br/>Creation Time : 2018年5月10日 下午6:11:44 
	 * 
	 * @param express
	 * @param message
	 * @throws IllegalArgumentException
	 */
	public static void isTrue(boolean express, String message) throws IllegalArgumentException {
		if (express)
			throw new IllegalArgumentException(message);
	}
}