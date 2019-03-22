package com.okayjam.bigdata.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @Description：加载properties配置文件
 * @Author 01381119
 * @CreateDate: 2018/11/28 13:46
 */
public class PropertiesConfig {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);

    private static final String ES_CONFIG_PATH = "system.properties";

    private static Properties prop = null;

    static {
        try {
            prop = new Properties();
            prop.load(PropertiesConfig.class.getClassLoader().getResourceAsStream(ES_CONFIG_PATH));

        } catch (Exception e) {
            logger.error("初始化加载ElasticSearch配置文件异常: {}", ES_CONFIG_PATH, e);
        }
    }

    private PropertiesConfig() {
    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }
}
