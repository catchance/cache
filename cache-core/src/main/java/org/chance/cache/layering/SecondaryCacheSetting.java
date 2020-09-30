package org.chance.cache.layering;

/**
 * 二级缓存的配置
 *
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-09 16:38:44
 */
public class SecondaryCacheSetting {

    /**
     * 默认开启一级缓存
     */
    public static final boolean USED_FIRST_CACHE_DEFAULT = Boolean.TRUE;

    /**
     * 默认开启强制缓存
     */
    public static final boolean FORCE_REFESH_DEFAULT = Boolean.TRUE;

    /**
     * 默认缓存主动在失效前强制刷新缓存的时间，0表示不刷新
     */
    public static final long PRELOAD_SECOND_TIME_DEFAULT = 0L;

    /**
     * 缓存有效时间
     */
    private long expirationSecondTime;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime;

    /**
     * 是否使用一级缓存，默认是true
     */
    private boolean usedFirstCache;

    /**
     * 是否使用强刷新（走数据库），默认是false
     */
    private boolean forceRefresh;

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime) {
        this(expirationSecondTime, preloadSecondTime, USED_FIRST_CACHE_DEFAULT, FORCE_REFESH_DEFAULT);
    }

    /**
     * @param usedFirstCache       是否启用一级缓存，默认true
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     */
    public SecondaryCacheSetting(boolean usedFirstCache, long expirationSecondTime, long preloadSecondTime) {
        this(expirationSecondTime, preloadSecondTime, usedFirstCache, FORCE_REFESH_DEFAULT);
    }

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     * @param forceRefresh         是否使用强制刷新（走数据库），默认false
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime, boolean forceRefresh) {
        this(expirationSecondTime, preloadSecondTime, USED_FIRST_CACHE_DEFAULT, forceRefresh);
    }

    /**
     * @param expirationSecondTime 设置redis缓存的有效时间，单位秒
     * @param preloadSecondTime    设置redis缓存的自动刷新时间，单位秒
     * @param usedFirstCache       是否启用一级缓存，默认true
     * @param forceRefresh         是否使用强制刷新（走数据库），默认false
     */
    public SecondaryCacheSetting(long expirationSecondTime, long preloadSecondTime, boolean usedFirstCache, boolean forceRefresh) {
        this.expirationSecondTime = expirationSecondTime;
        this.preloadSecondTime = preloadSecondTime;
        this.usedFirstCache = usedFirstCache;
        this.forceRefresh = forceRefresh;
    }

    public long getPreloadSecondTime() {
        return preloadSecondTime;
    }

    public long getExpirationSecondTime() {
        return expirationSecondTime;
    }

    public boolean getUsedFirstCache() {
        return usedFirstCache;
    }

    public boolean getForceRefresh() {
        return forceRefresh;
    }

}
