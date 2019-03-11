package com.okayjam.bigdata.redis;


import com.okayjam.bigdata.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description：redis连接池
 * @CreateDate: 2019/1/9 19:49
 */
public class RedisSentinelCluster {

    private static final Logger logger = LoggerFactory.getLogger(RedisSentinelCluster.class);

    private static JedisSentinelPool pool;

    private RedisSentinelCluster() {}

    static { init(); }

    public static JedisSentinelPool bulider() {
        if(pool == null) {init();}
        return pool;
    }

    private static void init() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(Integer.parseInt(PropertiesUtil.getValue("redis.maxTotal")));
        // 最大空闲数
        poolConfig.setMaxIdle(Integer.parseInt(PropertiesUtil.getValue("redis.maxIdle")));
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(Integer.parseInt(PropertiesUtil.getValue("redis.maxWait")));

        String[] hostAndPortArray = PropertiesUtil.getValue("redis.cluster").split(",");
        Set<String> sentinels = new HashSet<>();
        for (int i = 0; i < hostAndPortArray.length; i++) {
            String host = hostAndPortArray[i].split(":")[0];
            int port = Integer.parseInt(hostAndPortArray[i].split(":")[1]);
            sentinels.add(new HostAndPort(host, port).toString());
        }
        String password = PropertiesUtil.getValue("redis.auth");
        String masterName = PropertiesUtil.getValue("redis.master.name");
        pool = new JedisSentinelPool(masterName, sentinels, poolConfig, password);
        logger.info("the redis cluster pool init success: " + PropertiesUtil.getValue("redis.cluster"));
    }

    public static  void close() {
        if (pool != null) {
            try {
                pool.close();
            } catch (Exception e) {
                logger.error("close the redis pool error:", e);
            }
        }
    }
}
