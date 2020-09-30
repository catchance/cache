package org.chance.cache.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.chance.cache.layering.FirstCacheSetting;
import org.chance.cache.layering.LayeringCacheManager;
import org.chance.cache.layering.SecondaryCacheSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-09 17:34:19
 */

@Configuration
public class LayeringCacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(LayeringCacheConfig.class);

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
    @Value("${select.cache.refresh:1780}")
    private long selectCacheRefresh;

    @Bean
    @Primary
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {

        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // Caffeine缓存设置
        setFirstCacheConfig(layeringCacheManager);

        // redis缓存设置
        setSecondaryCacheConfig(layeringCacheManager);

        // 允许存null，防止缓存击穿
        layeringCacheManager.setAllowNullValues(true);

        return layeringCacheManager;
    }

    /**
     * 设置一级缓存
     *
     * @param layeringCacheManager
     */
    private void setFirstCacheConfig(LayeringCacheManager layeringCacheManager) {

        // 设置默认的一级缓存配置
        String specification = "initialCapacity=5,maximumSize=500,expireAfterWrite=60s";

//                this.cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            layeringCacheManager.setCaffeineSpec(CaffeineSpec.parse(specification));
        }

        // 设置每个一级缓存的过期时间和自动刷新时间
        Map<String, FirstCacheSetting> firstCacheSettings = new HashMap<>();
        firstCacheSettings.put("freemall:goods:sku", new FirstCacheSetting("initialCapacity=5,maximumSize=500,expireAfterWrite=70s"));
        layeringCacheManager.setFirstCacheSettings(firstCacheSettings);
    }

    /**
     * 设置二级缓存
     *
     * @param layeringCacheManager
     */
    private void setSecondaryCacheConfig(LayeringCacheManager layeringCacheManager) {
        // 设置使用缓存名称（value属性）作为redis缓存前缀
        layeringCacheManager.setUsePrefix(true);
        //这里可以设置一个默认的过期时间 单位是秒
        layeringCacheManager.setSecondaryCacheDefaultExpiration(redisDefaultExpiration);

        // 设置每个二级缓存的过期时间和自动刷新时间
        Map<String, SecondaryCacheSetting> secondaryCacheSettings = new HashMap<>();
        secondaryCacheSettings.put("freemall:goods:sku", new SecondaryCacheSetting(180, 90));
        layeringCacheManager.setSecondaryCacheSettings(secondaryCacheSettings);
    }

    /**
     * 显示声明缓存key生成器
     *
     * @return
     */
    @Bean
    @Primary
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

}
