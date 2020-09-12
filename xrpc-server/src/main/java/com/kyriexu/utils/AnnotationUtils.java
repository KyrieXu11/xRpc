package com.kyriexu.utils;

import com.kyriexu.annotation.RpcScan;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author KyrieXu
 * @since 2020/9/9 15:16
 **/
public class AnnotationUtils {
    /**
     * 扫描给定包下的类对象
     * @param aClazz 启动类的类对象
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getAllClass(Class<?> aClazz, Class<? extends Annotation> clazz) throws NoSuchMethodException {
        String packageName = getBasePackage(aClazz);
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(clazz);
    }

    /**
     * 扫描给定包下有给定注解的类的实例
     *
     * @param aClazz 启动类的类对象
     * @param clazz 注解的class对象
     * @return map的key为 类全限定类名 eg:com.kyriexu.service.HelloServiceImpl
     *  map的vaklue为 实例化好的空属性的对象，但是对于功能性类（只用调用功能）来说，这个有无属性是无所谓的
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static synchronized Map<String,Object> getInstance(Class<?> aClazz,Class<? extends Annotation> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Set<Class<?>> aClasses = getAllClass(aClazz, clazz);
        Map<String,Object> map = new HashMap<>();
        for (Class<?> aClass : aClasses) {
            Object o = aClass.getConstructor().newInstance();
            for (Class<?> aInterface : aClass.getInterfaces()) {
                map.put(aInterface.getName(),o);
            }
        }
        return map;
    }

    public static String getBasePackage(Class<?> aClazz) throws NoSuchMethodException {
        return ReflectionUtils.getScanPackage(aClazz,RpcScan.class);
    }
}
