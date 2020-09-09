package com.kyriexu.codec.utils;

import com.kyriexu.codec.Decoder;
import com.kyriexu.codec.Encoder;
import com.kyriexu.codec.json.JSONDecoder;
import com.kyriexu.codec.json.JSONEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author KyrieXu
 * @since 2020/8/22 15:12
 **/
public class ConvertUtils {
    public static <T> T BufToByte(Object msg,Class<T> clazz){
        ByteBuf in = (ByteBuf) msg;
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        Decoder decoder = new JSONDecoder();
        return decoder.decode(bytes, clazz);
    }

    public static ByteBuf ObjectToBuf(Object object){
        Encoder encoder = new JSONEncoder();
        byte[] bytes = encoder.encode(object);
        return Unpooled.wrappedBuffer(bytes);
    }
}
