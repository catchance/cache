package org.chance.cache.redis;

/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-08 18:54:45
 */
public class CacheTime {

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    /**
     * 缓存有效时间
     */
    private long expirationSecondTime;


    public CacheTime(long expirationSecondTime, long preloadSecondTime) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
    }

    public long getPreloadSecondTime() {
        return preloadSecondTime;
    }

    public long getExpirationSecondTime() {
        return expirationSecondTime;
    }
}
