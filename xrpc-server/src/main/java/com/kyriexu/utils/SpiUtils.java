package com.kyriexu.utils;

import com.kyriexu.annotation.service.SPI;
import com.kyriexu.annotation.service.SpiScan;
import com.kyriexu.registry.ServiceDiscovery;
import com.kyriexu.registry.ServiceRegistry;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author KyrieXu
 * @since 2020/9/10 10:40
 **/
public class SpiUtils {

    private static Map<String,Class<?>> getMap(Class<?> aClazz) throws NoSuchMethodException {
        String scanPackage = ReflectionUtils.getScanPackage(aClazz, SpiScan.class);
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> aClasses = reflections.getTypesAnnotatedWith(SPI.class);
        Map<String,Class<?>> map = new HashMap<>();
        for (Class<?> aClass : aClasses) {
            if(aClass.isInterface())
                map.put(aClass.getSimpleName(),aClass);
        }
        return map;
    }

    public static ServiceRegistry getServiceRegistry(Class<?> aClazz) throws NoSuchMethodException {
        return ((ServiceRegistry) getSpi(aClazz, "ServiceRegistry"));
    }

    private static Object getSpi(Class<?> aClazz, String name) throws NoSuchMethodException {
        Map<String, Class<?>> map = getMap(aClazz);
        if (map.keySet().size() == 0)
            throw new RuntimeException("检查一下META-INF/services/");
        for (String key : map.keySet()) {
            // 不是服务注册的接口就跳过
            if(!name.equals(key))
                continue;
            Class<?> aClass = map.get(key);
            if (aClass.getDeclaredMethods().length != 1) {
                throw new RuntimeException("SPI注解只能加在服务注册接口上！");
            }
            ServiceLoader<?> load = ServiceLoader.load(aClass);
            for (Object o : load) {
                return o;
            }
        }
        throw new RuntimeException("未知异常");
    }

    public static ServiceDiscovery getServiceDiscovery(Class<?> aClazz) throws NoSuchMethodException {
        return ((ServiceDiscovery) getSpi(aClazz, "ServiceDiscovery"));
    }
}
