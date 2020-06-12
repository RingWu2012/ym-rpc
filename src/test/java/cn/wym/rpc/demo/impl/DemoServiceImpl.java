package cn.wym.rpc.demo.impl;

import cn.wym.rpc.demo.DemoService;

public class DemoServiceImpl implements DemoService {

    public String hello(String caller) {
        return "hello " + caller;
    }
}
