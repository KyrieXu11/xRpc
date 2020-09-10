package com.kyriexu.utils;

import com.kyriexu.annotation.service.SPI;
import com.kyriexu.annotation.service.SpiScan;
import com.kyriexu.registry.ServiceRegistry;
import org.reflections.Reflections;

import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author KyrieXu
 * @since 2020/9/10 10:40
 **/
public class SpiUtils {
    public static ServiceRegistry getServiceRegistry(Class<?> aClazz) throws NoSuchMethodException {
        String scanPackage = ReflectionUtils.getScanPackage(aClazz, SpiScan.class);
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(SPI.class);
        if (set.size() == 0)
            throw new RuntimeException("检查一下META-INF/services/");
        for (Class<?> aClass : set) {
            if (aClass.getDeclaredMethods().length != 1 || !aClass.getDeclaredMethods()[0].getName().equals("registerService")) {
                throw new RuntimeException("SPI注解只能加在服务注册接口上！");
            }
            ServiceLoader<?> load = ServiceLoader.load(aClass);
            for (Object o : load) {
                return ((ServiceRegistry) o);
            }
        }
        throw new RuntimeException("未知异常");
    }
}
