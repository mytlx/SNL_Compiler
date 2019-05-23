package com.mytlx.compiler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 解析properties配置文件
 *
 * @author TLX
 * @date 2019.5.23
 * @time 23:02
 */
public class PropertiesUtils {
    // 配置文件路径
    private final static String CONFIG_NAME = "path.properties";

    /**
     * 获取配置文件中key对应的value值
     *
     * @param string key
     * @return value
     */
    public static String getProperty(String string) {
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
        Properties prop = new Properties();
        String result = null;
        try {
            prop.load(in);
            result = prop.getProperty(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}
