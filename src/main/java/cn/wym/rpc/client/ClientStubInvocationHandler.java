package cn.wym.rpc.client;

import cn.wym.rpc.client.netclient.NetClient;
import cn.wym.rpc.common.Request;
import cn.wym.rpc.common.Response;
import cn.wym.rpc.discovery.ServiceInfo;
import cn.wym.rpc.discovery.ServiceInfoDiscoverer;
import cn.wym.rpc.protocol.JSONRpcPRotocol;
import cn.wym.rpc.protocol.RpcProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class ClientStubInvocationHandler implements InvocationHandler {

    private Class<?> interf;

    private ServiceInfoDiscoverer serviceInfoDiscoverer;

    private NetClient netClient;

    private Random random = new Random();

    public <T> ClientStubInvocationHandler(Class<T> interf, ServiceInfoDiscoverer serviceInfoDiscoverer, NetClient netClient) {
        this.interf = interf;
        this.serviceInfoDiscoverer = serviceInfoDiscoverer;
        this.netClient = netClient;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")) {
            return proxy.getClass().toString();
        }

        if (method.getName().equals("hashCode")) {
            return 0;
        }
        //根据名称去注册中心找到对应的服务
        String serviceName = this.interf.getName();
        List<ServiceInfo> serviceInfos = serviceInfoDiscoverer.getServiceInfo(serviceName);
        //缘份负载均衡
        ServiceInfo serviceInfo = serviceInfos.get(random.nextInt(serviceInfos.size()));

        // 2、构造request对象
        Request req = new Request();
        req.setServiceName(serviceInfo.getName());
        req.setMethod(method.getName());
        req.setParameters(args);
        req.setParameterTypes(method.getParameterTypes());

        // 3、协议
        RpcProtocol protocol = new JSONRpcPRotocol();
        // 编组请求
        byte[] data = protocol.marshallingRequest(req);

        // 4、调用网络层发送请求
        byte[] repData = netClient.sendRequest(data, serviceInfo);

        // 5解组响应消息
        Response rsp = protocol.unmarshallingResponse(repData);

        // 6、结果处理
        if (rsp.getException() != null) {
            throw rsp.getException();
        }

        return rsp.getReturnValue();
    }
}
