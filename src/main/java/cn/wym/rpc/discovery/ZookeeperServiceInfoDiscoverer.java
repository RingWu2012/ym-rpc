package cn.wym.rpc.discovery;

import cn.wym.rpc.util.PropertiesUtils;
import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperServiceInfoDiscoverer implements ServiceInfoDiscoverer{

	ZkClient client;

	private String centerRootPath = "/ym-rpc";

	public ZookeeperServiceInfoDiscoverer() {
		String addr = PropertiesUtils.getProperties("zk.address");
		client = new ZkClient(addr);
		client.setZkSerializer(new DefaultZkSerializer());
	}

	public List<ServiceInfo> getServiceInfo(String name) {
		String servicePath = centerRootPath + "/" + name + "/service";
		List<String> children = client.getChildren(servicePath);
		List<ServiceInfo> resources = new ArrayList<ServiceInfo>();
		for (String ch : children) {
			try {
				String deCh = URLDecoder.decode(ch, "UTF-8");
				ServiceInfo r = JSON.parseObject(deCh, ServiceInfo.class);
				resources.add(r);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return resources;
	}

}
