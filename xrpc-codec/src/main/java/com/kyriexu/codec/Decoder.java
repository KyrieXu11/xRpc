package com.kyriexu.codec;

/**
 * @author KyrieXu
 * @since 2020/8/21 10:58
 **/
public interface Decoder {
    /**
     * 解码是将字节流转换成json对象
     */
    <T> T decode(byte [] bytes, Class<T> clazz);
}
