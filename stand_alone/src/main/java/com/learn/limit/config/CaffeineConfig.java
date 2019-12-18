package com.learn.limit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @program: limit
 * @description:
 * @author: zjj
 * @create: 2019-12-17 14:20
 **/

/* 开启缓存 */
@EnableCaching
@Configuration
public class CaffeineConfig {
    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;
    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches{
        /* 有效期600秒 */
        getUserById(600),
        /* 有效期2个小时 , 最大容量1000 */
        listCustomers(7200,1000),
        ;
        /* 最大數量 */
        private int maxSize;
        /* 过期时间（秒） */
        private int ttl;

        Caches() {
        }
        Caches(int ttl) {
            this(ttl,DEFAULT_MAXSIZE);
        }
        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }

    /**
     * 创建基于Caffeine的Cache Manager
     * @return
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for(Caches c : Caches.values()){
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}



