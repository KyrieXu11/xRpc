package com.kyriexu.client;

import com.kyriexu.service.HelloService;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:13
 **/
public class ClientTest {
    public static void main(String[] args) {
        ClientRpcProxy proxy = new ClientRpcProxy();
        HelloService helloService = proxy.getService(HelloService.class, "localhost", 8080);
        System.out.println(helloService.add(1, 2));
    }
}
