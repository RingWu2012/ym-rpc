package cn.wym.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class ChannelRequestHandler  extends ChannelInboundHandlerAdapter {

    private RequestHandler handler;

    public ChannelRequestHandler(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端收到消息：" + msg);
        ByteBuf msgBuf = (ByteBuf) msg;
        byte[] req = new byte[msgBuf.readableBytes()];
        msgBuf.readBytes(req);
        byte[] res = handler.handleRequest(req);
        log.info("发送响应：" + msg);
        ByteBuf respBuf = Unpooled.buffer(res.length);
        respBuf.writeBytes(res);
        ctx.write(respBuf);
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
