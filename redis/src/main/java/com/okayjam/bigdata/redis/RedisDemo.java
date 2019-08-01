package com.okayjam.bigdata.redis;

import redis.clients.jedis.*;

import java.util.*;

/**
 * @author: Chen weiguang <chen2621978@gmail.com>
 * @create: 2019/03/04 17:17
 **/
public class RedisDemo {
/*    private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("eos-ops-core-new1.cachesit.sfdc.com.cn", 8001);
//        jedisShardInfo1.setPassword("123456888888");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
    }*/

    public static void main(String[] args) {

//        testRedis();
        testRedisPool();
//        testCluster1();
//        testSentinel();
//        testSentinel1();
//        RedisSentinelCluster.close();
    }

    public static void  testRedis() {
        String host = "192.168.242.128";
        int port = 6379;
        //连接 Redis 服务
        Jedis jedis = new Jedis(host, port);
//        jedis.auth(password);
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("okayjam", "www.okayjam.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("okayjam"));
        jedis.close();
    }

    public static void  testRedisPool() {
        String host = "192.168.242.128";
        int port = 6379;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(18);
        //连接 Redis 服务
        JedisPool pool = new JedisPool(config, host, port, 2000);
//        JedisPool pool = new JedisPool(config, host, port, 2000,"password");
        Jedis jedis = pool.getResource();
        //设置 redis 字符串数据
        jedis.set("okayjam", "www.okayjam.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("okayjam"));
        jedis.close();
    }

    public static void testCluster() {
        Set<HostAndPort> jedisClusterSet = new HashSet<>();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8201));
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8202));
        jedisClusterSet.add(new HostAndPort("192.168.242.128", 8203));
        JedisCluster jedisCluster = new JedisCluster(jedisClusterSet, config);
        jedisCluster.set("okayjam", "www.okayjam.com");
        System.out.println(jedisCluster.get("okayjam"));
    }

    public static void testCluster1() {
        JedisCluster jedisCluster = RedisCluster.bulider();
        jedisCluster.set("okayjam", "www.okayjam.com");
        System.out.println(jedisCluster.get("okayjam"));
    }




    public static void testSentinel() {
        String masterName = "mymaster";
        String password = "";
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);
        // 哨兵信息
        Set<String> sentinels = new HashSet<>(Arrays.asList("192.168.242.128:28021","192.168.242.128:28022","192.168.242.128:28023"));
        // 创建连接池
        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels,jedisPoolConfig);
//        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels,jedisPoolConfig, password);
        // 获取客户端
        Jedis jedis = pool.getResource();
        // 执行两个命令
        jedis.set("okayjam", "www.okayjam.com");
        String value = jedis.get("okayjam");
        System.out.println(value);
    }

    public static void testSentinel1() {
        Jedis jedis = RedisSentinelCluster.bulider().getResource();
        // 获取数据并输出
        jedis.set("mykey", "yueyang");
        Set<String> keys = jedis.keys("mykey");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key + " : " + jedis.get(key));
        }
        jedis.close();
    }

}
