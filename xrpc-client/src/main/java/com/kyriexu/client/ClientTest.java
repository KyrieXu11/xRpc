package com.kyriexu.client;

import com.kyriexu.service.HelloService;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:13
 **/
public class ClientTest {
    public static void main(String[] args) {
        ClientRpcProxy proxy = new ClientRpcProxy();
        HelloService helloService = ((HelloService) proxy.getService(HelloService.class, "127.0.0.1", 8080));
        helloService.say();
        System.out.println(helloService.add(1, 2));
    }
}
