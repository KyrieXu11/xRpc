package com.kyriexu.spi;

import java.lang.annotation.*;

/**
 * @author KyrieXu
 * @since 2020/9/10 10:39
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpiScan {
    String scanPackage();
}
