package org.luck.xhga.middleware.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author GEEX1928
 * @date 2022/9/4
 */
public class JedisConfig {
    public static void main(String[] args) {
        // 链接redis需要证书方式
        SSLSocketFactory socketFactory = null;
        try {
            socketFactory = SocketFactory.getSocketFactory("L:\\me\\ca.crt", "L:\\me\\client.crt", "L:\\me\\client.key", "123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),
                "192.168.8.44",
                6379,
                0,
                null,
                true,
                socketFactory,null,null);
        Jedis resource = jedisPool.getResource();
        resource.set("test","123");
        System.out.println(resource.get("test"));

    }
}
