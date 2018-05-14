package com.tools.random;

/**
 * Description : 随机数
 * <br/>Created By : xiaok0928@hotmail.com 
 * <br/>Creation Time : 2018年5月11日 下午5:31:59
 */
public class RandomTools {
	
	/**
	 * Description : 获取一个5位不重复随机数<br>
	 * 计算方式:毫秒+5位随机数
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:32:18 
	 * 
	 * @return
	 */
	public static String randomNum() {
		//获取当前秒数
		String timeMillis = String.valueOf(System.currentTimeMillis() / 1000L);

		String newString = null;
		
		// 得到0.0到1.0之间的数字,并扩大100000倍
		double doubleP = Math.random() * 100000.0D;

		// 如果数据等于100000,则减少1
		if (doubleP >= 100000.0D) {
			doubleP = 99999.0D;
		}

		// 然后把这个数字转化为不包含小数点的整数
		int tempString = (int) Math.ceil(doubleP);

		// 转化为字符串
		newString = "" + tempString;
		
		// 把得到的数增加为固定长度,为5位
		while (newString.length() < 5) {
			newString = "0" + newString;
		}
		return timeMillis + newString;
	}
}