package com.kyriexu.codec;

import com.kyriexu.enity.Request;

/**
 * @author KyrieXu
 * @since 2020/8/21 10:55
 **/
public interface Encoder {
    /**
     * encode 是将json转化成字节流
     */
    byte[] encode(Object object);
}
