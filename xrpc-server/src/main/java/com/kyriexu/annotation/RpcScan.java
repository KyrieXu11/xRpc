package com.kyriexu.annotation;

import java.lang.annotation.*;

/**
 * @author KyrieXu
 * @since 2020/9/9 15:45
 **/
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcScan {
    String basePackage();
}
