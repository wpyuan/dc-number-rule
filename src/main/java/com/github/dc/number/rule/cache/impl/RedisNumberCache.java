package com.github.dc.number.rule.cache.impl;


import com.github.dc.number.rule.cache.NumberCache;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 *     redis编号缓存实现
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 9:28
 */
@Component
@Slf4j
public class RedisNumberCache implements NumberCache {

    @Override
    public NumberRuleDTO getNumberRule(String code) {
        return null;
    }

    @Override
    public String handleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        return null;
    }

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public void updateDbByCache() {

    }

    @Override
    public void loadCache() {

    }
}
