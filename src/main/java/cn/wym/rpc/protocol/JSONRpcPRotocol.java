package cn.wym.rpc.protocol;

import cn.wym.rpc.common.Request;
import cn.wym.rpc.common.Response;
import com.alibaba.fastjson.JSONObject;

public class JSONRpcPRotocol implements RpcProtocol {

    public byte[] marshallingRequest(Request req) throws Exception {
        return JSONObject.toJSONBytes(req);
    }

    public Request unmarshallingRequest(byte[] data) throws Exception {
       return JSONObject.parseObject(data, Request.class);
    }

    public byte[] marshallingResponse(Response rsp) throws Exception {
        return JSONObject.toJSONBytes(rsp);
    }

    public Response unmarshallingResponse(byte[] data) throws Exception {
        return JSONObject.parseObject(data, Response.class);
    }
}
