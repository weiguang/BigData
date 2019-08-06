package com.okayjam.bigdata.redis.redission;

import com.okayjam.bigdata.util.PropertiesConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import java.util.Arrays;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/08/05 20:12
 **/
public class RedissionCluster {
    private static RedissonClient redisson;

    private RedissionCluster() {}

    public static RedissonClient bulider() {
        if (redisson == null) {
            init();
        }
        return  redisson;
    }

    private static void init() {
        Config config = new Config();
        String hosts = PropertiesConfig.getValue("redis.cluster");
        Arrays.stream(hosts.split(",")).forEach(host->  config.useClusterServers().addNodeAddress("redis://"+ host));
        redisson = Redisson.create(config);
    }

}
