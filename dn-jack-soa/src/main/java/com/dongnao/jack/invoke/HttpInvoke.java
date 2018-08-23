package com.dongnao.jack.invoke;

import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.loadbalance.LoadBalance;
import com.dongnao.jack.loadbalance.NodeInfo;
import com.dongnao.jack.rpc.http.HttpRequest;

import java.util.List;

public class HttpInvoke implements Invoke {
    public String invoke(Invocation invocation) {
        String result = null;
        try {
            /*获取registry中的JSON字符串信息*/
            List<String> registryInfo = invocation.getReference().getRegistryInfo();
            /*负载均衡算法*/
            String loadbalance = invocation.getReference().getLoadbalance();
            Reference reference = invocation.getReference();
            LoadBalance loadbalanceBean = Reference.getLoadBalances().get(loadbalance);
            NodeInfo nodeInfo = loadbalanceBean.doSelect(registryInfo);
            /*既然要远程调用，就要传一下参数给远程的生产者，这里 通过 json字符串 传递*/
            //我们调用远程的生产者是传输的json字符串
            //根据serviceid去对端生产者的spring容器中获取serviceid对应的实例
            //根据methodName和methodType获取实例的method对象
            //然后反射调用method方法
            JSONObject sendparam = new JSONObject();
            sendparam.put("methodName", invocation.getMethod().getName());
            sendparam.put("methodParams", invocation.getArgs());
            /*从远程生产者的工程的spring容器中，获取serviceId对应的服务层的实例*/
            sendparam.put("serviceId", reference.getId());
            sendparam.put("paramTypes", invocation.getMethod().getParameterTypes());

            String url = "http://" + nodeInfo.getHost() + ":" + nodeInfo.getPort() + "/" + nodeInfo.getContextpath();
            /*调用对端的生产者的服务*/
            result = HttpRequest.sendPost(url, sendparam.toJSONString());
            /*消费者这里发出了请求，那么生产者就一定要接收请求*/
            /*生产者怎么接收请求呢？答：servlet，因此建立 *.remote.servlet包，创建DispatcherServlet类 */

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
