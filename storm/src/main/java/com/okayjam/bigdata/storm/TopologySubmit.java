package com.okayjam.bigdata.storm;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.Nimbus;
import org.apache.storm.shade.org.json.simple.JSONValue;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.Utils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/24 11:46
 **/
public class TopologySubmit {
    public static void main(String[] args) {
        try {
            TopologyBuilder builder = new TopologyBuilder();

            Config conf = new Config();
            // 读取本地storm-core包下的storm.yaml配置
            Map stormConf = Utils.readStormConfig();
            // 读取classpath下的配置文件
            // Map stormConf = Utils.findAndReadConfigFile("storm.yaml");
            stormConf.put(Config.NIMBUS_SEEDS,  Arrays.asList("xxx"));
            stormConf.put(Config.WORKER_CHILDOPTS, "-Xmx8192m");
            stormConf.putAll(conf);
            System.out.println(stormConf);

            // 提交集群运行的jar
            String inputJar = "/home/jam/app/xx/test.jar";
            // 参数接收包名
            if (args.length > 0 ) {
                inputJar = args[0];
            }

            // 使用StormSubmitter提交jar包
            String uploadedJarLocation = StormSubmitter.submitJar(stormConf, inputJar);
            String jsonConf = JSONValue.toJSONString(stormConf);

            // 这种方式也可以，跟下面两句代码效果一致
            Nimbus.Iface client = NimbusClient.getConfiguredClient(stormConf).getClient();
            client.submitTopology("NEWTOP", uploadedJarLocation, jsonConf, builder.createTopology());

//            NimbusClient nimbus = new NimbusClient(stormConf, "111.111.111.111", 6627);
//            nimbus.getClient().submitTopology(TOPOLOGY_NAME, uploadedJarLocation, jsonConf, builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
