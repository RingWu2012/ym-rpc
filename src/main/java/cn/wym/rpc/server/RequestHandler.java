package cn.wym.rpc.server;

import cn.wym.rpc.common.Request;
import cn.wym.rpc.common.Response;
import cn.wym.rpc.common.Status;
import cn.wym.rpc.protocol.RpcProtocol;
import cn.wym.rpc.server.registry.ServiceObject;
import cn.wym.rpc.server.registry.ServiceRegister;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter@Setter
public class RequestHandler {
    private RpcProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandler(RpcProtocol protocol, ServiceRegister serviceRegister) {
        super();
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        // 1、解组消息
        Request req = this.protocol.unmarshallingRequest(data);

        // 2、查找服务对象
        ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

        Response rsp = null;

        if (so == null) {
            rsp = new Response(Status.NOT_FOUND);
        } else {
            // 3、反射调用对应的过程方法
            try {
                Method m = so.getInterf().getMethod(req.getMethod(), req.getParameterTypes());
                Object returnValue = m.invoke(so.getObj(), req.getParameters());
                rsp = new Response(Status.SUCCESS);
                rsp.setReturnValue(returnValue);
            } catch (Exception e) {
                rsp = new Response(Status.ERROR);
                rsp.setException(e);
            }
        }

        // 4、编组响应消息
        return this.protocol.marshallingResponse(rsp);
    }
}
