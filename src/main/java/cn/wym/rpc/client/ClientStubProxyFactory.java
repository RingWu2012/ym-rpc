package cn.wym.rpc.client;


import cn.wym.rpc.client.netclient.NetClient;
import cn.wym.rpc.client.netclient.NettyNetClient;
import cn.wym.rpc.discovery.ServiceInfoDiscoverer;
import cn.wym.rpc.discovery.ZookeeperServiceInfoDiscoverer;
import cn.wym.rpc.protocol.RpcProtocol;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class ClientStubProxyFactory {

    //服务发现
    private ServiceInfoDiscoverer serviceInfoDiscoverer= new ZookeeperServiceInfoDiscoverer();

    //代理对象缓存，避免每次都新建
    private Map<Class<?>, Object> objectCache = new HashMap<Class<?>, Object>();

    //通信客户端，用于发送请求
    private NetClient netClient = new NettyNetClient();

    private Map<String, RpcProtocol> supportRpcProtocols;

    public <T> T getProxy(Class<T> interf) {
        T obj = (T) this.objectCache.get(interf);
        if (obj == null) {
            obj = (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[] { interf },
                    new ClientStubInvocationHandler(interf, serviceInfoDiscoverer, netClient));
            this.objectCache.put(interf, obj);
        }
        return obj;
    }
}
