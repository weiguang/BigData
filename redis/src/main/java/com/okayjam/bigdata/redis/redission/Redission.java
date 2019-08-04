package com.okayjam.bigdata.redis.redission;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;


public class Redission {
    public static  void main(String args[]) throws InterruptedException {
       normal();
    }
    public static void normal() throws InterruptedException {
        // 默认连接地址 127.0.0.1:6379
//        RedissonClient redisson = Redisson.create();

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.98.100:6379");
        RedissonClient redisson = Redisson.create(config);
      //  redisson.getBucket("okayjam").set("nihao");
        RBucket but = redisson.getBucket("okayjam");

        System.out.println(but.get());
        System.out.println(1234);
    }


    public static void lock() throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.98.100:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("lock");
        // 加锁以后10秒钟自动解锁
// 无需调用unlock方法手动解锁
        lock.lock(10, TimeUnit.SECONDS);

// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
                System.out.println("lock");
            } finally {
                lock.unlock();
            }
        }
    }
}
