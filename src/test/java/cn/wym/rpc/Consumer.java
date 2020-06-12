package cn.wym.rpc;

import cn.wym.rpc.client.ClientStubProxyFactory;
import cn.wym.rpc.client.netclient.NettyNetClient;
import cn.wym.rpc.demo.DemoService;
import cn.wym.rpc.discovery.ZookeeperServiceInfoDiscoverer;
import cn.wym.rpc.protocol.JSONRpcPRotocol;
import cn.wym.rpc.protocol.RpcProtocol;
import cn.wym.rpc.util.PropertiesUtils;

import java.util.HashMap;

public class Consumer {

    public static void main(String[] args) {
        String protocol = PropertiesUtils.getProperties("rpc.protocol");
        ClientStubProxyFactory cspf = new ClientStubProxyFactory();
        // 设置服务发现者
        cspf.setServiceInfoDiscoverer(new ZookeeperServiceInfoDiscoverer());

        // 设置支持的协议
        HashMap<String, RpcProtocol> supportRpcProtocols = new HashMap<String, RpcProtocol>();
        supportRpcProtocols.put(protocol, new JSONRpcPRotocol());
        cspf.setSupportRpcProtocols(supportRpcProtocols);

        // 设置网络层实现
        cspf.setNetClient(new NettyNetClient());

        DemoService demoService = cspf.getProxy(DemoService.class); // 获取远程服务代理
        String result = demoService.hello("jimi"); // 执行远程方法

        System.out.println("获取到的结果；" + result);
    }
}
