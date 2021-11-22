package com.ecdata.cmp.iaas.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获取properties文件中配置信息的辅助类
 * @author lish
 *
 */
public class PropertiesUtil {
//	private static Logger logger = Logger.getLogger(PropertiesUtil.class);
	public static Map<String, Properties> sysProperties = new HashMap<String, Properties>();
	private static final String SYSTEM_PROPERTIES = "application.properties";

	public static String get(String key) {
		return getProperties(key, SYSTEM_PROPERTIES);
	}
	public static String getProperties(String key, String fileName) {
		Properties props = null;
		try {
			props = sysProperties.get(fileName);
			if (props == null) {
				props = new Properties();
				InputStreamReader is =new InputStreamReader(PropertiesUtil.class.getResourceAsStream("/"+fileName),"gbk");
				//InputStream is = PropsUtil.class.getResourceAsStream("/"+fileName);
				props.load(is);
				sysProperties.put(fileName, props);
			}
			return props.getProperty(key);

		} catch (IOException e) {
//			logger.error("not properties file found @" + fileName);
		}
		return null;
	}
	/**
	 * 获取指定properties文件中key对应的Value
	 * @param propFilePath  properties文件路径
	 * @param key
	 * @return 数据获取失败，返回值为null
	 */
	public static String getPropValue(String propFilePath, String key){
		PropertiesUtil pptu = new PropertiesUtil();
		InputStream in = pptu.getClass().getResourceAsStream(propFilePath);
		Properties prop = new Properties();
		String v = null;
		try{
			prop.load(in);
			v = prop.getProperty(key);
		}catch(Exception e){
//			logger.error("properties文件加载异常："+propFilePath,e);
		}
		return v;
	}
	
	/**
	 * 获取默认properties文件中key对应values值
	 * @param key
	 * @return
	 */
//	public static String getDefaulePropValue(String key){
//		//return getPropValue("/application.properties", key);
////		return PropertiesConfigurer.getProperty(key);
//	}
	
	/**
	 * 获取Ftp信息
	 * @param key
	 * @return
	 */
	public static String getFtpPropValue(String key){
		return getPropValue("/ftp.properties", key);
	}
	
	public static void main(String[] args) {
		System.out.println(getPropValue("/ws.properties", "mhws"));
	}
}
