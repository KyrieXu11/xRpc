package com.kyriexu.utils;

import com.kyriexu.annotation.RpcScan;
import com.kyriexu.spi.SpiScan;

import java.lang.annotation.Annotation;

/**
 * @author KyrieXu
 * @since 2020/9/10 10:44
 **/
public class ReflectionUtils {
    public static String getScanPackage(Class<?> clazz, Class<? extends Annotation> aClazz) throws NoSuchMethodException {
        Annotation annotation = clazz.getAnnotation(aClazz);
        if(annotation.annotationType() == RpcScan.class){
            return clazz.getAnnotation(RpcScan.class).basePackage();
        }else if (annotation.annotationType() == SpiScan.class){
            return clazz.getAnnotation(SpiScan.class).scanPackage();
        }
        return null;
    }
}
