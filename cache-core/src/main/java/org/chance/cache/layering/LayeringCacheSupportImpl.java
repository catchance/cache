package org.chance.cache.layering;

import org.chance.cache.redis.AbstractCacheSupport;
import org.chance.cache.redis.CachedMethodInvocation;
import org.chance.cache.redis.CustomizedRedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@Component
@ConditionalOnClass(value = {LayeringCache.class, LayeringCacheManager.class})
public class LayeringCacheSupportImpl extends AbstractCacheSupport {

    private static final Logger logger = LoggerFactory.getLogger(LayeringCacheSupportImpl.class);

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    public LayeringCacheSupportImpl() {
        logger.debug("创建LayeringCacheSupportImpl");
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
        if (cache instanceof LayeringCache) {
            CustomizedRedisCache redisCache = getRedisCache(cache);
            // 判断是否需要强制刷新（走数据库）
            if (redisCache != null && redisCache.getForceRefresh()) {
                // 将方法信息放到redis缓存
                redisTemplate.opsForValue().set(getInvocationCacheKey(redisCache.getCacheKey(invocation.getKey())),
                        invocation, redisCache.getExpirationSecondTime(), TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 获取rediscache
     *
     * @param cache
     * @return
     */
    private CustomizedRedisCache getRedisCache(Cache cache) {
        LayeringCache layeringCache = ((LayeringCache) cache);
        return layeringCache.getSecondaryCache();
    }

    /**
     * @param cache
     * @param invocation
     */
    @Override
    protected void refreshCache(Cache cache, CachedMethodInvocation invocation) {
        if (cache instanceof LayeringCache) {
            CustomizedRedisCache redisCache = getRedisCache(cache);
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
