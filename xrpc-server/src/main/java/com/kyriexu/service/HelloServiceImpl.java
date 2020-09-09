package com.kyriexu.service;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:46
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public int add(Integer a, Integer b) {
        return a + b;
    }
}
