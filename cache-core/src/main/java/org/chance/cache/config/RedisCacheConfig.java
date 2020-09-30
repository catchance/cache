package org.chance.cache.config;

import org.chance.cache.layering.ChannelTopicEnum;
import org.chance.cache.redis.CacheTime;
import org.chance.cache.redis.CustomizedRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-08 10:08:53
 */

@Configuration
public class RedisCacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

    /**
     * redis缓存的有效时间单位是秒
     */
    @Value("${redis.default.expiration:3600}")
    private long redisDefaultExpiration;

    /**
     * 查询缓存有效时间
     */
    @Value("${select.cache.timeout:1800}")
    private long selectCacheTimeout;

    /**
     * 查询缓存自动刷新时间
     */
    @Value("${select.cache.refresh:1790}")
    private long selectCacheRefresh;


//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        cacheManager.setDefaultExpiration(60);
//        cacheManager.setUsePrefix(true);
//
//        Map<String, Long> expiresMap = new HashMap<>();
//
//        expiresMap.put("Product", 5L);
//        cacheManager.setExpires(expiresMap);
//
//        return cacheManager;
//    }

    /**
     * 重写RedisCacheManager的getCache方法，实现设置key的有效时间
     * 重写RedisCache的get方法，实现触发式自动刷新
     * <p>
     * 自动刷新方案：
     * 1、获取缓存后再获取一次有效时间，拿这个时间和我们配置的自动刷新时间比较，如果小于这个时间就刷新。
     * 2、每次创建缓存的时候维护一个Map，存放key和方法信息（反射）。当要刷新缓存的时候，根据key获取方法信息。
     * 通过获取其代理对象执行方法，刷新缓存。
     *
     * @param redisTemplate
     * @return
     */
//    @Bean
//    public CacheManager customizedRedisCacheManager(RedisTemplate redisTemplate) {
//
//        CustomizedRedisCacheManager customizedRedisCacheManager =
//                new CustomizedRedisCacheManager();
////                new CustomizedRedisCacheManager(redisTemplate);
////        customizedRedisCacheManager.setDefaultExpiration(60);
////        customizedRedisCacheManager.setUsePrefix(true);
//
//        // 设置缓存的过期时间和自动刷新时间
//        Map<String, CacheTime> cacheTimes = new HashMap<>();
//        cacheTimes.put("freemall:goods:sku", new CacheTime(120, 60));
////        cacheTimes.put("people1", new CacheTime(120, 115));
////        cacheTimes.put("people2", new CacheTime(120, 115));
//
//        customizedRedisCacheManager.setCacheTimess(cacheTimes);
//
//        return customizedRedisCacheManager;
//    }

    /**
     * 显示声明缓存key生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
//        redisCacheManager.setDefaultExpiration(3);
//        return redisCacheManager;
//    }

//    @Bean
//    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListener) {
//        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//
//        container.setConnectionFactory(redisConnectionFactory);
//        container.addMessageListener(messageListener, ChannelTopicEnum.REDIS_CACHE_DELETE_TOPIC.getChannelTopic());
//        container.addMessageListener(messageListener, ChannelTopicEnum.REDIS_CACHE_CLEAR_TOPIC.getChannelTopic());
//        return container;
//    }
}
