package cn.wym.rpc.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class Request implements Serializable {

	private static final long serialVersionUID = -5200571424236772650L;

	private String serviceName;

	private String method;

	private Map<String, String> headers = new HashMap<String, String>();

	private Object[] parameters;

    private Class<?>[] parameterTypes;
}
