package com.kyriexu.transport;

import com.kyriexu.enity.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcTransport {
    public static final Logger logger= LoggerFactory.getLogger(RpcTransport.class);

    private String host;
    private int port;

    public Object sendRequest(Request request){
        logger.info("send");
        Socket socket;
        try {
            socket = new Socket(host,port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("出异常啦");
            e.printStackTrace();
        }
        return null;
    }

}
