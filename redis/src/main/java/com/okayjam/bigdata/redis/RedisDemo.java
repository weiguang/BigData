package com.okayjam.bigdata.redis;

import redis.clients.jedis.*;

import java.util.*;

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

//        testRedis();
//        testSentinel();
        testSentinel1();
//        RedisSentinelCluster.close();
    }

    public static void  testRedis() {
        //连接 Redis 服务
        Jedis jedis = new Jedis("192.168.20.122");
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


    public static void testSentinel() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);
        // 哨兵信息
        Set<String> sentinels = new HashSet<>(Arrays.asList("192.168.20.120:26379", "192.168.20.120:26379","192.168.20.120:26379"));
        // 创建连接池
        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels,jedisPoolConfig,"123456");
        // 获取客户端
        Jedis jedis = pool.getResource();
        // 执行两个命令
        jedis.set("mykey", "myvalue");
        String value = jedis.get("mykey");
        System.out.println(value);
    }

    public static void testSentinel1() {
        Jedis jedis = RedisSentinelCluster.bulider().getResource();
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
