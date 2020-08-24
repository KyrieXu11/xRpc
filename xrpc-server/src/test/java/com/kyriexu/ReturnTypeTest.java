package com.kyriexu;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author KyrieXu
 * @since 2020/8/24 12:34
 **/
public class ReturnTypeTest {

    public void func1(){

    }

    @SneakyThrows
    @Test
    public void TestReturnType(){
        Method func1 = this.getClass().getMethod("func1");
        Class<?> returnType = func1.getReturnType();
        System.out.println(returnType.toString());
    }
}
