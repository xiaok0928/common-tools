package com.tools.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Description : 读取本地配置文件 <br/>
 * Created By : xiaok0928@hotmail.com <br/>
 * Creation Time : 2018年5月11日 下午5:25:50
 */
public class LoadProperty {
	
	private static final LoadProperty loadProperty = null;
	
	public Map<String, String> configMap;

	@Deprecated
	private LoadProperty() {
	}

	/**
	 * Description : 带参构造函数 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午5:26:08
	 * 
	 * @param configFile
	 */
	private LoadProperty(String configFile) {
		if (this.configMap == null)
			try {
				this.configMap = loadProperty(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Description : 获取实例 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午5:26:24
	 * 
	 * @param configFile
	 * @return
	 */
	public static synchronized LoadProperty getInstance(String configFile) {
		if (null == loadProperty) {
			return new LoadProperty(configFile);
		}
		return loadProperty;
	}

	/**
	 * Description : 根据Key获取对应的值 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午5:26:59
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return (String) loadProperty.configMap.get(key);
	}

	/**
	 * Description : 加载配置文件 <br/>
	 * Created By : xiaok0928@hotmail.com <br/>
	 * Creation Time : 2018年5月11日 下午5:27:14
	 * 
	 * @param config
	 * @return
	 */
	private static Map<String, String> loadProperty(String config) {
		InputStream is = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
			Properties pro = new Properties();
			pro.load(is);
			Iterator<Object> localIterator = pro.keySet().iterator();
			while (localIterator.hasNext()) {
				Object key = localIterator.next();
				map.put(key.toString(), pro.get(key).toString());
			}
		} catch (Exception e) {
			System.out.println("配置文件:" + config + "加载出错!");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}