package com.kyriexu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author KyrieXu
 * @since 2020/9/8 22:26
 **/
public class PropertiesUtils {
    public static Properties getProperties(String path) throws IOException {
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(path);
        if (in == null)
            return null;
        Properties res = new Properties();
        res.load(in);
        return res;
    }
}
