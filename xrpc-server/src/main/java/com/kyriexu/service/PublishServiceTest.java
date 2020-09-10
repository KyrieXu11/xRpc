package com.kyriexu.service;

import com.kyriexu.annotation.rpc.RpcScan;
import com.kyriexu.annotation.service.SpiScan;
import com.kyriexu.server.ServerRpcProxy;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:47
 **/
@RpcScan(basePackage = "com.kyriexu.service")
@SpiScan(scanPackage = "com.kyriexu.registry")
public class PublishServiceTest {
    public static void main(String[] args) throws Exception {
        ServerRpcProxy proxy = new ServerRpcProxy();
        proxy.run(PublishServiceTest.class);
    }
}
