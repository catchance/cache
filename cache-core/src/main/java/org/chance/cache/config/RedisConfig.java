package org.chance.cache.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GengChao
 * @email chao_geng@sui.com
 * @date 2018-08-09 19:28:11
 */

@Configuration
@ConditionalOnBean(name = "cacheFinanceRedisShareSettingBean")
@ConditionalOnMissingBean(CacheManager.class)
@ConditionalOnClass({JedisConnectionFactory.class, RedisTemplate.class})
@AutoConfigureAfter(RedisCacheConfig.class)
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /**
     * 连接Redis默认超时时间：15s
     * 取值于com.kingdee.finance.redis.connection.jedis.ShardedJedisPoolFactory的timeout值
     */
    private final static int REDIS_CONNECT_TIMEOUT = 15 * 1000;

    /**
     * jedis连接池配置
     */
    @Autowired
    JedisPoolConfig jedisPoolConfig;

    /**
     * 系统中redis配置
     */
//    @Resource(name = "cacheFinanceRedisShareSettingBean")
//    FinanceRedisShareSettingBean financeRedisShareSettingBean;

    /**
     * JedisConnectionFactory
     *
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(value = JedisConnectionFactory.class)
//    public JedisConnectionFactory jedisConnectionFactory() {
//        List<JedisShardInfo>
//                jedisShardInfos = initJedisShardInfo(financeRedisShareSettingBean.getShareSetting(),
//                financeRedisShareSettingBean.getAuthPassword());
//
//        JedisConnectionFactory jedisConnectionFactory =
//                new JedisConnectionFactory(jedisPoolConfig);
//
//        if (CollectionUtils.isNotEmpty(jedisShardInfos)) {
//            jedisConnectionFactory.setShardInfo(jedisShardInfos.get(0));
//            jedisConnectionFactory.setUsePool(true);
//            jedisConnectionFactory.setPassword(financeRedisShareSettingBean.getAuthPassword());
//            jedisConnectionFactory.setPort(jedisShardInfos.get(0).getPort());
//            jedisConnectionFactory.setTimeout(jedisShardInfos.get(0).getConnectionTimeout());
//            jedisConnectionFactory.setHostName(jedisShardInfos.get(0).getHost());
//            jedisConnectionFactory.afterPropertiesSet();
//        }
//
//        return jedisConnectionFactory;
//    }

    /**
     * RedisTemplate
     *
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = RedisTemplate.class)
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        GenericFastJsonRedisSerializer fastJsonRedisSerializer =
                new GenericFastJsonRedisSerializer();

        // 全局开启AutoType，不建议使用
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        // 建议使用这种方式，小范围指定白名单
        ParserConfig.getGlobalInstance().addAccept("com.kingdee.");

        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

//    private List<JedisShardInfo> initJedisShardInfo(String shareRedisUrl, String password) {
//
//        logger.info("初始化redis配置信息shareRedisUrl={}", shareRedisUrl);
//        String[] urls = StringUtils.split(shareRedisUrl, ",");
//        List<JedisShardInfo> urlList = new ArrayList<JedisShardInfo>();
//        for (String url : urls) {
//            String[] cnf = StringUtils.split(url, ":");
//            if (cnf.length != 2) {
//                logger.error("redis配置信息不合规url={}", url);
//                continue;
//            }
//            String host = StringUtils.trim(cnf[0]);
//            String port = StringUtils.trim(cnf[1]);
//            if (StringUtils.isBlank(host) || !NumberUtils.isDigits(port)) {
//                logger.error("redis配置信息不合规url={}", url);
//                continue;
//            }
//            urlList.add(initJedisShardInfo(host, Integer.parseInt(port), password));
//        }
//        return urlList;
//    }

    private JedisShardInfo initJedisShardInfo(String ip, int port, String password) {

        JedisShardInfo info = new JedisShardInfo(ip, port, REDIS_CONNECT_TIMEOUT, ip + ":" + port);
        if (StringUtils.isEmpty(password)) {
            return info;
        }

        info.setPassword(password);
        return info;
    }

}
