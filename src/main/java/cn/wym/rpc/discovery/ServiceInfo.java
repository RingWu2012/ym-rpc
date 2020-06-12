package cn.wym.rpc.discovery;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ServiceInfo {

    private String name;

    private String protocol;

    private String address;
}
