package cn.wym.rpc.client.netclient;

import cn.wym.rpc.discovery.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyNetClient implements NetClient {

    public byte[] sendRequest(byte[] data, ServiceInfo sinfo) throws Throwable {
        String[] addInfoArray = sinfo.getAddress().split(":");

        final NettySendHandler sendHandler = new NettySendHandler(data);
        byte[] respData = null;
        // 配置客户端
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();

            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(sendHandler);
                        }
                    });
            // 启动客户端连接
            b.connect(addInfoArray[0], Integer.valueOf(addInfoArray[1])).sync();
            respData = (byte[]) sendHandler.rspData();
        } finally {
            // 释放线程组资源
            group.shutdownGracefully();
        }
        return respData;
    }
}
