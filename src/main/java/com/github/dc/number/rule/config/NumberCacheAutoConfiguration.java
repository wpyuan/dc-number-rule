package com.github.dc.number.rule.config;

import com.github.dc.number.rule.cache.impl.LocalNumberCache;
import com.github.dc.number.rule.cache.impl.NoNumberCache;
import com.github.dc.number.rule.cache.NumberCacheAdapter;
import com.github.dc.number.rule.cache.impl.RedisNumberCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/28 8:36
 */
@Configuration
public class NumberCacheAutoConfiguration {

    @Autowired
    private NoNumberCache numberCache;
    @Autowired
    private LocalNumberCache localNumberCache;
    @Autowired
    private RedisNumberCache redisNumberCache;

    @Bean
    @ConditionalOnMissingBean
    public NumberCacheAdapter numberCacheAdapter(NumberRuleProperties numberRuleProperties) {
        NumberCacheAdapter numberCacheAdapter = new NumberCacheAdapter(numberRuleProperties);
        numberCacheAdapter.setCaches(numberCache, localNumberCache, redisNumberCache);
        return numberCacheAdapter;
    }
}
