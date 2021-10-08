package com.github.dc.number.rule.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/10/8 16:06
 */
@Configuration
public class RedissonClientConfig {

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        config.useSingleServer().setAddress(redisProperties.getUrl());
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
