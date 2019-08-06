package com.okayjam.bigdata.redis.redission;

import com.okayjam.bigdata.util.PropertiesConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

import java.util.Arrays;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/08/05 20:12
 **/
public class RedissionSentinel {
    private static RedissonClient redisson;

    private RedissionSentinel() {}

    public static RedissonClient bulider() {
        if (redisson == null) {
            init();
        }
        return  redisson;
    }

    private static void init() {
        Config config = new Config();
        config.useSentinelServers().setMasterName(PropertiesConfig.getValue("redis.master.name"));
        String hosts = PropertiesConfig.getValue("redis.cluster");
        Arrays.stream(hosts.split(",")).forEach(host->  config.useSentinelServers().addSentinelAddress("redis://"+ host));
        redisson = Redisson.create(config);
    }

}
