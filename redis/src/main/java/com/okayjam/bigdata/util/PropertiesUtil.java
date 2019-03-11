package com.okayjam.bigdata.util;

import java.util.Properties;

public class PropertiesUtil {

	private static Properties prop = null;     
	private static String properPath = "config/test/system.properties";
	
	//静态块中的内容会在类别加载的时候先被执行    
	static {
		try {
			 prop = new Properties();            
			 prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(properPath));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//静态方法可以被类名直接调用
    public static String getValue(String key) {
        return prop.getProperty(key);
    }

}
