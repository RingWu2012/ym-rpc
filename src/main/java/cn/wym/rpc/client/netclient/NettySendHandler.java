package cn.wym.rpc.client.netclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class NettySendHandler extends ChannelInboundHandlerAdapter {

    private CountDownLatch cdl = null;
    private Object readMsg = null;
    private byte[] data;

    public NettySendHandler(byte[] data) {
        cdl = new CountDownLatch(1);
        this.data = data;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接服务端成功：" + ctx);
        ByteBuf reqBuf = Unpooled.buffer(data.length);
        reqBuf.writeBytes(data);
        log.info("客户端发送消息：" + reqBuf);
        ctx.writeAndFlush(reqBuf);
    }

    public Object rspData() {

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return readMsg;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("客户端读取到的数据: " + msg);
        ByteBuf msgBuf = (ByteBuf) msg;
        byte[] resp = new byte[msgBuf.readableBytes()];
        msgBuf.readBytes(resp);
        readMsg = resp;
        cdl.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(ExceptionUtils.getStackTrace(cause));
        ctx.close();
    }
}
