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
        String packageName = getScanPackage(aClazz);
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
    public static Map<String,Object> getInstance(Class<?> aClazz,Class<? extends Annotation> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Set<Class<?>> aClasses = getAllClass(aClazz, clazz);
        Map<String,Object> map = new HashMap<>();
        for (Class<?> aClass : aClasses) {
            Object o = aClass.getConstructor().newInstance();
            map.put(aClass.getName(),o);
        }
        return map;
    }

    /**
     * 通过RpcScan来扫描给定的扫描的包。
     * @param clazz 使用RpcScan 注解的类
     * @return basepackage
     * @throws NoSuchMethodException
     */
    private static String getScanPackage(Class<?> clazz) throws NoSuchMethodException {
        RpcScan rpcScan = clazz.getAnnotation(RpcScan.class);
        return rpcScan.basePackage();
    }
}
