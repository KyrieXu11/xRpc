package com.kyriexu.utils;

import com.kyriexu.annotation.RpcService;
import com.kyriexu.service.PublishServiceTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author KyrieXu
 * @since 2020/9/9 15:24
 **/
class AnnotationUtilsTest {

    @Test
    void getAllClassName() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String packageName = "com.kyriexu.service";
        AnnotationUtils.getAllClass(packageName, RpcService.class);
    }

    @Test
    void getScanPackages() throws NoSuchMethodException {
        String scanPackages = AnnotationUtils.getScanPackages(PublishServiceTest.class);
        System.out.println(scanPackages);
    }
}