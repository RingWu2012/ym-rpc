package cn.wym.rpc.server.registry;

import cn.wym.rpc.discovery.DefaultZkSerializer;
import cn.wym.rpc.discovery.ServiceInfo;
import cn.wym.rpc.util.PropertiesUtils;
import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Zookeeper方式获取远程服务信息类。
 * 
 * ZookeeperServiceInfoDiscoverer
 */
public class ZookeeperExportServiceRegister implements ServiceRegister {

	private ZkClient client;

	private String centerRootPath = "/ym-rpc";

	private Map<String, ServiceObject> serviceMap = new HashMap<String, ServiceObject>();


	public ServiceObject getServiceObject(String name) {
		return this.serviceMap.get(name);
	}

	public ZookeeperExportServiceRegister() {
		String addr = PropertiesUtils.getProperties("zk.address");
		client = new ZkClient(addr);
		client.setZkSerializer(new DefaultZkSerializer());
	}


	public void register(ServiceObject so, String protocolName, int port) throws Exception {
		if (so == null) {
			throw new IllegalArgumentException("参数不能为空");
		}

		this.serviceMap.put(so.getName(), so);

		ServiceInfo soInf = new ServiceInfo();

		String host = InetAddress.getLocalHost().getHostAddress();
		String address = host + ":" + port;
		soInf.setAddress(address);
		soInf.setName(so.getInterf().getName());
		soInf.setProtocol(protocolName);
		this.exportService(soInf);

	}

	private void exportService(ServiceInfo serviceResource) {
		String serviceName = serviceResource.getName();
		String uri = JSON.toJSONString(serviceResource);
		try {
			uri = URLEncoder.encode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String servicePath = centerRootPath + "/" + serviceName + "/service";
		if (!client.exists(servicePath)) {
			client.createPersistent(servicePath, true);
		}
		String uriPath = servicePath + "/" + uri;
		if (client.exists(uriPath)) {
			client.delete(uriPath);
		}
		client.createEphemeral(uriPath);
	}
}
