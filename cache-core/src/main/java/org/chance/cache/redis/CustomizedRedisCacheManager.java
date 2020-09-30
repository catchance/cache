package org.chance.cache.redis;

import org.chance.cache.util.ReflectionUtils;
import org.chance.cache.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-08 19:05:33
 */
public class CustomizedRedisCacheManager extends RedisCacheManager {

    private static final Logger log = LoggerFactory
        .getLogger(CustomizedRedisCacheManager.class);

    /**
     * 父类dynamic字段
     */
    private static final String SUPER_FIELD_DYNAMIC = "dynamic";

    /**
     * 父类cacheNullValues字段
     */
    private static final String SUPER_FIELD_CACHENULLVALUES = "cacheNullValues";

    RedisCacheManager redisCacheManager = null;

    // 0 - never expire
    private long defaultExpiration = 0;

    private Map<String, CacheTime> cacheTimes = null;

    public CustomizedRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

//    public CustomizedRedisCacheManager(RedisOperations redisOperations) {
//        super(redisOperations);
//    }
//
//    public CustomizedRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
//        super(redisOperations, cacheNames);
//    }

    public RedisCacheManager getInstance() {
        if (redisCacheManager == null) {
            redisCacheManager = SpringContextHolder.getBean(RedisCacheManager.class);
        }
        return redisCacheManager;
    }

    /**
     * 获取过期时间
     *
     * @return
     */
    public long getExpirationSecondTime(String name) {
        if (StringUtils.isEmpty(name)) {
            return 0;
        }

        CacheTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long expiration = cacheTime != null ? cacheTime.getExpirationSecondTime() : defaultExpiration;
        return expiration < 0 ? 0 : expiration;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getPreloadSecondTime(String name) {
        // 自动刷新时间，默认是0
        CacheTime cacheTime = null;
        if (!CollectionUtils.isEmpty(cacheTimes)) {
            cacheTime = cacheTimes.get(name);
        }
        Long preloadSecondTime = cacheTime != null ? cacheTime.getPreloadSecondTime() : 0;
        return preloadSecondTime < 0 ? 0 : preloadSecondTime;
    }

    /**
     * 创建缓存
     *
     * @param cacheName 缓存名称
     * @return
     */
    @Override
    public CustomizedRedisCache getMissingCache(String cacheName) {

        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = getExpirationSecondTime(cacheName);
        // 自动刷新时间，默认是0
        Long preloadSecondTime = getPreloadSecondTime(cacheName);

        log.info("缓存 cacheName：{}，过期时间:{}, 自动刷新时间:{}", cacheName, expirationSecondTime, preloadSecondTime);

        // 是否在运行时创建Cache
        Boolean dynamic = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_DYNAMIC);

        // 是否允许存放NULL
//        Boolean cacheNullValues = (Boolean) ReflectionUtils.getFieldValue(getInstance(), SUPER_FIELD_CACHENULLVALUES);

//        return dynamic ?
//                new CustomizedRedisCache(cacheName,
//                        (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null),
//                        this.getRedisOperations(), expirationSecondTime,
//                        preloadSecondTime,
//                        true) : null;
        return null;
    }

    /**
     * 根据缓存名称设置缓存的有效时间和刷新时间，单位秒
     *
     * @param cacheTimes
     */
    public void setCacheTimess(Map<String, CacheTime> cacheTimes) {
        this.cacheTimes = (cacheTimes != null ? new ConcurrentHashMap<String, CacheTime>(cacheTimes) : null);
    }

    /**
     * 设置默认的过去时间， 单位：秒
     *
     * @param defaultExpireTime
     */
//    @Override
//    public void setDefaultExpiration(long defaultExpireTime) {
//        super.setDefaultExpiration(defaultExpireTime);
//        this.defaultExpiration = defaultExpireTime;
//    }
//
//    @Deprecated
//    @Override
//    public void setExpires(Map<String, Long> expires) {
//
//    }
}
