package com.kyriexu.client;

import com.kyriexu.service.HelloService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:13
 **/
public class ClientTest {
    public static void main(String[] args) {
        // System.out.println(LocalDateTime.now().getSecond());
        ClientRpcProxy proxy = new ClientRpcProxy();
        HelloService helloService = ((HelloService) proxy.getService(HelloService.class, "127.0.0.1", 8080));
        System.out.println(helloService.add(1, 2));
        // System.out.println(LocalDateTime.now().getSecond());
    }
}
