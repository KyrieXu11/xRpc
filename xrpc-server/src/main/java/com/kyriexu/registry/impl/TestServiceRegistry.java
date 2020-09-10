package com.kyriexu.registry.impl;

import com.kyriexu.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/9/10 11:12
 **/
public class TestServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(InetSocketAddress address, String serviceName) {
        System.out.println("服务发现SPI测试成功");
    }
}
