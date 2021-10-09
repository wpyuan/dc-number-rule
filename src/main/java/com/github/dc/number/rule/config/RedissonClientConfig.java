package com.github.dc.number.rule.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     RedissonClient配置，供redis缓存处理器使用
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/10/8 16:06
 */
@Configuration
@ConditionalOnProperty(
        name = {"dc.number.cache.enable"},
        havingValue = "true"
)
public class RedissonClientConfig {

    @Bean
    @ConditionalOnProperty(
            name = {"dc.number.cache.type"},
            havingValue = "redis"
    )
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        if (StringUtils.isNotBlank(redisProperties.getUrl())) {
            config.useSingleServer().setAddress(redisProperties.getUrl());
        } else {
            config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        }
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
