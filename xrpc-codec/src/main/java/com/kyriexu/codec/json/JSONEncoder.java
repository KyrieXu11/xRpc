package com.kyriexu.codec.json;

import com.alibaba.fastjson.JSON;
import com.kyriexu.codec.Encoder;

/**
 * @author KyrieXu
 * @since 2020/8/21 10:59
 **/
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object object) {
        return JSON.toJSONBytes(object);
    }
}
