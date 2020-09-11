package com.kyriexu.client;

import com.kyriexu.service.HelloService;
import com.kyriexu.spi.SpiScan;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:13
 **/
@SpiScan(scanPackage = "com.kyriexu.registry")
public class ClientTest {
    public static void main(String[] args) {
        ClientRpcProxy proxy = new ClientRpcProxy();
        HelloService helloService = ((HelloService) proxy.getService(HelloService.class, "127.0.0.1", 8080, ClientTest.class));
        System.out.println(helloService.add(1, 2));
    }
}
