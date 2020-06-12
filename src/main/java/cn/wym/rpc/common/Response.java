package cn.wym.rpc.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class Response implements Serializable {

	private static final long serialVersionUID = -4317845782629589997L;

	private Status status;

	private Map<String, String> headers = new HashMap<String, String>();

	private Object returnValue;

	private Exception exception;

	public Response() {
	};

	public Response(Status status) {
		this.status = status;
	}
}
