package com.kyriexu.codec.impl;

import com.alibaba.fastjson.JSON;
import com.kyriexu.codec.Decoder;

/**
 * @author KyrieXu
 * @since 2020/8/21 11:00
 **/
public class JSONDecoder implements Decoder {
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
