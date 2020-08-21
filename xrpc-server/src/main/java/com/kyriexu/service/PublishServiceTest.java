package com.kyriexu.service;

import com.kyriexu.server.ServerRpcProxy;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:47
 **/
public class PublishServiceTest {
    public static void main(String[] args) throws Exception {
        ServerRpcProxy proxy = new ServerRpcProxy();
        HelloService service = new HelloServiceImpl();
        proxy.publishService(service,8080);
    }
}
