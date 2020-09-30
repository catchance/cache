package org.chance.cache.layering;

import com.github.benmanes.caffeine.cache.CaffeineSpec;

/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-09 16:38:16
 */
public class FirstCacheSetting {

    /**
     * caffeineSpec缓存配置
     */
    private String cacheSpecification;

    /**
     * 一级缓存配置，配置项请点击这里 {@link CaffeineSpec#configure(String, String)}
     *
     * @param cacheSpecification
     */
    public FirstCacheSetting(String cacheSpecification) {
        this.cacheSpecification = cacheSpecification;
    }

    public String getCacheSpecification() {
        return cacheSpecification;
    }

}
