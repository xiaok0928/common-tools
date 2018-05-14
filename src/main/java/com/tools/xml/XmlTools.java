package com.tools.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Description : JavaBean与XML互相转换 <br>
 * 如果要将整个实体类转换成xml
 * 需要在类名上添加注解:@XmlRootElement, 标记该类的Root节点为类名, 如果需要更换Root节点名称则需要指定该注解的name属性:@XmlRootElement(name = "Test")
 * 如果需要将其中某个字段更换成其他节点名称,则需要在类的属性的公有方法(get,set)方法上添加注解:@XmlElement(name = "userName")
 * Created By : xiaok0928@hotmail.com <br>
 * Creation Time : 2018年5月11日 下午5:44:35
 */
public class XmlTools {

	/**
	 * Description : JavaBean转XML
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:45:00 
	 * 
	 * @param obj
	 * @return
	 */
	public static String convertToXml(Object obj) {
		return convertToXml(obj, "UTF-8");
	}

	/**
	 * Description : JavaBean转XML
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:58:38 
	 * 
	 * @param obj
	 * @param encoding
	 * @return
	 */
	public static String convertToXml(Object obj, String encoding) {
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Description : xml转换成JavaBean
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:59:27 
	 * 
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(String xml, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	/**
	 * Description : xml转换成JavaBean
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:59:39 
	 * 
	 * @param xmlFile
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(File xmlFile, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * Description : xml转换成JavaBean
	 * <br>Created By : xiaok0928@hotmail.com 
	 * <br>Creation Time : 2018年5月11日 下午5:59:55 
	 * 
	 * @param input
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(InputStream input, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
}