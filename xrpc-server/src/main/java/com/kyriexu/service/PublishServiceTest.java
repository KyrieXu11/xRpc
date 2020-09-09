package com.kyriexu.service;

import com.kyriexu.annotation.RpcScan;
import com.kyriexu.server.ServerRpcProxy;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:47
 **/
@RpcScan(basePackage = "com.kyriexu.service")
public class PublishServiceTest {
    public static void main(String[] args) throws Exception {
        ServerRpcProxy proxy = new ServerRpcProxy();
        proxy.run(PublishServiceTest.class);
    }
}
