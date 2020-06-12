package cn.wym.rpc.client.netclient;

import cn.wym.rpc.discovery.ServiceInfo;

public interface NetClient {

    byte[] sendRequest(byte[] data, ServiceInfo sinfo) throws Throwable;
}
