package com.okayjam.bigdata.redis.lock;

import com.okayjam.bigdata.redis.jedis.RedisClientPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class RedisLock  implements ILock{

    Jedis jedis = RedisClientPool.bulider().getResource();


    public void lock(String key) {
        while(true){
            String result = jedis.set(key, "value");
            if(OK.equals(result)){
                System.out.println(Thread.currentThread().getId()+"加锁成功!");
                break;
            }
        }
    }

    public String lock(String key, int secondsToExpire) {
        // 生成随机标识
        String value = UUID.randomUUID().toString();
        while(true){
            String result = jedis.set(key, "value", new SetParams().nx().ex(secondsToExpire));
            if(OK.equals(result)){
                System.out.println(Thread.currentThread().getId()+"加锁成功!");
                break;
            }
        }
        return  value;
    }


    public void unlock(String key, String value) {
       // jedis.del(key);
        // 使用lua脚本进行原子删除操作
        String checkAndDelScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";
        jedis.eval(checkAndDelScript, 1, key, value);
    }

}
