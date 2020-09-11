package com.kyriexu.service;

import com.kyriexu.annotation.RpcService;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:46
 **/
@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public int add(Integer a, Integer b) {
        return a + b;
    }
}
