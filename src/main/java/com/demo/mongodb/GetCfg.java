package com.demo.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 读取classpath下的配置文件"config.properties"
 *
 */
public class GetCfg {

	private static Properties pro = null;

	static {
		pro = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = GetCfg.class.getClassLoader().getResourceAsStream("config.properties");
			if (inputStream != null) {
				pro.load(inputStream);
			}
			System.out.println("pro = " + pro);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("======================syscfg.properties is not found======================");
			throw new IllegalArgumentException("[syscfg.properties] is not found!");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 直接通过键取值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		String val = pro.getProperty(key.trim());
		if (val == null)
			return "";

		String str = null;
		try {
			str = new String(val.trim().getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		str = str == null ? "" : str.trim();
		return str;
	}

	/**
	 * 通过key取值（如果通过此键key取不到值，那么返回默认值defaultStr）
	 * 
	 * @param key
	 * @param defaultStr
	 * @return
	 */
	public static String getDefaultValue(String key, String defaultStr) {
		// 如果通过此键key取不到值，那么返回默认值defaultStr
		String val = pro.getProperty(key.trim(), defaultStr).trim();
		String str = null;

		try {
			str = new String(val.trim().getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		str = str == null ? "" : str.trim();
		return str;
	}
}
