package com.okayjam.bigdata.redis.jedis;

import com.okayjam.bigdata.util.PropertiesConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClientPool {
    private static JedisPool pool  ;

    private RedisClientPool() {}

    static { init(); }

    public static JedisPool bulider() {
        if(pool == null) {init();}
        return pool;
    }

    private static void init() {
        String host = PropertiesConfig.getValue("redis.host").split(",")[0];
        int port = Integer.valueOf(PropertiesConfig.getValue("redis.host").split(",")[1]);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(18);
        //连接 Redis 服务
        pool = new JedisPool(config, host, port, 2000);
    }
}
