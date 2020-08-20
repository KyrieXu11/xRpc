package com.kyriexu.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author KyrieXu
 * @since 2020/8/20 18:57
 **/
public class ServerRpcProxy {
    public static final Logger logger= LoggerFactory.getLogger(ServerRpcProxy.class);

    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public void publishService(Object service,int port){
        try {
            logger.info("publish");
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                threadPool.submit(new ServiceHandler(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
