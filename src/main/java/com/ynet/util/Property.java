package com.ynet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Property {

	private final static String CONF_NAME = "config.properties";

	private static Properties contextProperties;

	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONF_NAME);
		contextProperties = new Properties();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
			contextProperties.load(inputStreamReader);
		} catch (IOException e) {
			System.err.println(">>>YNET<<<资源文件加载失败!");
			e.printStackTrace();
		}
		System.out.println(">>>YNET<<<资源文件加载成功");
	}
	
	public static String getStrValue(String key) {
		return contextProperties.getProperty(key);
	}

}