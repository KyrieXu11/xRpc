# 粘包和拆包的解决方案

1. 直接在管道最后添加FixedLengthDecoder
2. 拆包：在接收的时候看缓冲区有没有上次没有接收完的数据包，如果有就将本次接收的和上次还剩下的数据包进行合并操作。如果包长度过长的话就进行一个拆包的操作，将包拆开放在本地的缓冲区中。具体可看[Netty粘包和拆包的解决方案](https://www.cnblogs.com/coding-diary/p/11650686.html)
3. 本项目使用了游标来进行控制
```java
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg != null) {
            byte[] bytes = encoder.encode(msg);
            int dataLength = bytes.length;
            // 一个int 4个字节 + 字节数组的长度 肯定大于或者等于4个字节了
            out.writeInt(dataLength);
            out.writeBytes(bytes);
        }
    }
```
在进行编码的时候，将消息长度写进缓冲区，而在解码的时候如果消息长度
```java
       @Override
       protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
           if (in == Unpooled.EMPTY_BUFFER){
               System.out.println("空buffer");
               return;
           }
           // 字节长度小于4就说明异常了，因为一个消息总长度是个整数
           // 整数需要4个字节
           // 如果连存放整数的空间都没有则说明异常了
           if(in.readableBytes() < BODY_MIN_LENGTH){
               return;
           }
           // 标记游标
           in.markReaderIndex();
           // 读取消息总长度
           int len = in.readInt();
           if (len < 0){
               log.error("字节长度为：{}",len);
               return;
           }
           // 如果消息总长度大于了可读取字节长度的话
           // 则说明是不完整的消息，重置游标
           if (in.readableBytes() < len) {
               in.resetReaderIndex();
               return;
           }
   
           byte[] bytes = new byte[len];
           in.readBytes(bytes);
           Object res = decoder.decode(bytes, clazz);
           out.add(res);
           log.info("成功读取");
       }
```