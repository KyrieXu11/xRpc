package com.kyriexu.client;

import java.lang.reflect.Proxy;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:55
 **/
public class ClientRpcProxy {
    public <T> T getService(Class<T> service, String host, int port) {
        return (T)Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new RemoteInvocationHandler(host, port));
    }
}
