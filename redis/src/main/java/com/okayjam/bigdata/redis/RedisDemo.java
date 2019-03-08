package com.okayjam.bigdata.redis;

import redis.clients.jedis.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/04 17:17
 **/
public class RedisDemo {
    private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("192.168.20.129", 6379);
//        jedisShardInfo1.setPassword("123456888888");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
    }

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.20.129");
//        ShardedJedis jedis = pool.getResource();
        System.out.println("连接成功");

        // 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key + " : " + jedis.get(key));
        }
        jedis.close();
    }
}
