package org.chance.cache.redis;

import org.chance.cache.layering.LayeringCache;
import org.chance.cache.layering.LayeringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 手动刷新缓存实现类
 *
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-09 14:35:53
 */
//@Component
//@ConditionalOnMissingBean(value = {LayeringCache.class, LayeringCacheManager.class})
//@ConditionalOnBean(value = {CustomizedRedisCache.class, CustomizedRedisCacheManager.class})
public class RedisCacheSupportImpl extends AbstractCacheSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheSupportImpl.class);

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    public RedisCacheSupportImpl() {
        logger.debug("创建RedisCacheSupportImpl");
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }


    @Override
    public void registerCache(Cache cache, CachedMethodInvocation invocation) {
        if (cache instanceof CustomizedRedisCache) {
            CustomizedRedisCache redisCache = ((CustomizedRedisCache) cache);
            // 将方法信息放到redis缓存
            getRedisTemplate().opsForValue().set(getInvocationCacheKey(redisCache.getCacheKey(invocation.getKey())),
                    invocation, redisCache.getExpirationSecondTime(), TimeUnit.SECONDS);
        }
    }

    /**
     * @param cache
     * @param invocation
     */
    @Override
    protected void refreshCache(Cache cache, CachedMethodInvocation invocation) {
        if (cache instanceof CustomizedRedisCache) {
            CustomizedRedisCache redisCache = (CustomizedRedisCache) cache;
            long expireTime = redisCache.getExpirationSecondTime();
            // 刷新redis中缓存法信息key的有效时间
            getRedisTemplate().expire(getInvocationCacheKey(redisCache.getCacheKey(invocation.getKey())), expireTime, TimeUnit.SECONDS);

        }
    }

    /**
     * 获取注册方法的key
     *
     * @param cacheKey
     * @return
     */
    @Override
    protected String getInvocationCacheKey(String cacheKey) {
        return cacheKey + CustomizedRedisCache.INVOCATION_CACHE_KEY_SUFFIX;
    }

}
