package com.okayjam.bigdata.redis;


import com.okayjam.bigdata.util.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description：redis连接池
 * @CreateDate: 2019/1/9 19:49
 */
public class RedisCluster {

    private static final Logger logger = LoggerFactory.getLogger(RedisCluster.class);

    private static JedisCluster pool;

    private RedisCluster() {}

    static { init(); }

    public static JedisCluster bulider() {
        if(pool == null) {init();}
        return pool;
    }

    private static void init() {

        JedisPoolConfig config = new JedisPoolConfig();

        // 最大连接数
        config.setMaxTotal(Integer.parseInt(PropertiesConfig.getValue("redis.maxTotal")));
        // 最大空闲数
        config.setMaxIdle(Integer.parseInt(PropertiesConfig.getValue("redis.maxIdle")));
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：Could not get a resource from the pool
        config.setMaxWaitMillis(Integer.parseInt(PropertiesConfig.getValue("redis.maxWait")));

        String password = PropertiesConfig.getValue("redis.auth");
        String masterName = PropertiesConfig.getValue("redis.master.name");


        Set<HostAndPort> jedisClusterSet = new HashSet<>();
        String[] hostAndPortArray = PropertiesConfig.getValue("redis.cluster").split(",");
        for (int i = 0; i < hostAndPortArray.length; i++) {
            String host = hostAndPortArray[i].split(":")[0];
            int port = Integer.parseInt(hostAndPortArray[i].split(":")[1]);
            jedisClusterSet.add(new HostAndPort(host, port));
        }

        pool = new JedisCluster(jedisClusterSet, config);
//        pool = new JedisCluster(jedisClusterSet,1000, 1000, 5, password, config);
        logger.info("the redis cluster pool init success: " + PropertiesConfig.getValue("redis.cluster"));
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
