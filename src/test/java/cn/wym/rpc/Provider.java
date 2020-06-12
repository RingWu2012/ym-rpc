package cn.wym.rpc;

import cn.wym.rpc.demo.DemoService;
import cn.wym.rpc.demo.impl.DemoServiceImpl;
import cn.wym.rpc.protocol.JSONRpcPRotocol;
import cn.wym.rpc.server.NettyRpcServer;
import cn.wym.rpc.server.RequestHandler;
import cn.wym.rpc.server.registry.ServiceObject;
import cn.wym.rpc.server.registry.ServiceRegister;
import cn.wym.rpc.server.registry.ZookeeperExportServiceRegister;
import cn.wym.rpc.util.PropertiesUtils;

public class Provider {

    public static void main(String[] args) throws Exception {

        String protocol = PropertiesUtils.getProperties("rpc.protocol");

        // 服务注册
        ServiceRegister sr = new ZookeeperExportServiceRegister();
        DemoService ds = new DemoServiceImpl();
        ServiceObject so = new ServiceObject(DemoService.class.getName(), DemoService.class, ds);
        sr.register(so, protocol, 19000);

        RequestHandler reqHandler = new RequestHandler(new JSONRpcPRotocol(), sr);

        NettyRpcServer server = new NettyRpcServer(19000, protocol, reqHandler);
        server.start();
        System.in.read(); // 按任意键退出
        server.stop();
    }
}
