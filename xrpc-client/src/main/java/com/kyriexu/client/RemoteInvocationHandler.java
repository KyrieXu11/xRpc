package com.kyriexu.client;

import com.kyriexu.enity.Request;
import com.kyriexu.transport.RpcTransport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteInvocationHandler implements InvocationHandler {
    public static final Logger logger= LoggerFactory.getLogger(RemoteInvocationHandler.class);

    private String host;

    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setMethodName(method.getName());
        request.setArgs(args);
        RpcTransport transport = new RpcTransport(host, port);
        return transport.sendRequest(request);
    }
}
