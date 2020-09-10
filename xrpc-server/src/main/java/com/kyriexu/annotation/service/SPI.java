package com.kyriexu.annotation.service;

import java.lang.annotation.*;

/**
 * @author KyrieXu
 * @since 2020/9/10 10:27
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {
}
