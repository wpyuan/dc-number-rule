package com.github.dc.number.rule.cache.impl;

import com.github.dc.number.rule.cache.AbstractNumberCache;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.mapper.NumberRuleDetailMapper;
import com.github.dc.number.rule.mapper.NumberRuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 无缓存实现
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/28 9:04
 */
@Component
@Slf4j
public class NoNumberCache extends AbstractNumberCache {

    @Autowired
    private NumberRuleMapper numberRuleMapper;
    @Autowired
    private NumberRuleDetailMapper numberRuleDetailMapper;

    @Override
    public String type() {
        return "no-cache";
    }

    @Override
    public void loadCache() {
        // no cache
    }

    @Override
    public void updateDbByCache() {
        // no cache
    }

    @Override
    public NumberRuleDTO getDTOByCache(String code) {
        // no cache
        return null;
    }

    @Override
    public void setDTOCache(String code, NumberRuleDTO numberRuleDTO) {
        // no cache
    }

    @Override
    public NumberRuleDetail getLatestNumberRuleDetailByCache(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        // no cache
        return null;
    }

    @Override
    public void updateCacheWhenReset(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        // no cache
    }

    @Override
    public Long getAndSetSequenceByCache(String code, NumberRuleDetail numberRuleDetail) {
        // no cache
        return null;
    }

    @Override
    public void setSequenceCache(String code, NumberRuleDetail numberRuleDetail, Long sequence) {
        // no cache
    }

    @Override
    public NumberRuleMapper getNumberRuleMapper() {
        return this.numberRuleMapper;
    }

    @Override
    public NumberRuleDetailMapper getNumberRuleDetailMapper() {
        return this.numberRuleDetailMapper;
    }
}
