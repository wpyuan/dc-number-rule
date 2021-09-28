package com.github.dc.number.rule.cache.impl;

import com.github.dc.number.rule.cache.AbstractNumberCache;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.Update;
import com.github.dc.number.rule.constant.NumberRuleDetailField;
import com.github.dc.number.rule.constant.NumberRuleField;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.enums.NumberRuleType;
import com.github.dc.number.rule.mapper.NumberRuleDetailMapper;
import com.github.dc.number.rule.mapper.NumberRuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 本地编号缓存
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 9:28
 */
@Component
@Slf4j
public class LocalNumberCache extends AbstractNumberCache {

    /**
     * 编码数据缓存
     */
    private static final ConcurrentMap<String, NumberRuleDTO> NUMBER_CACHE_DATA = new ConcurrentHashMap<>();
    /**
     * 序列缓存。key：编号规则明细主键id，value：序列当前值
     */
    private static final ConcurrentMap<String, AtomicLong> SEQUENCE_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private NumberRuleMapper numberRuleMapper;
    @Autowired
    private NumberRuleDetailMapper numberRuleDetailMapper;

    @Override
    public String type() {
        return "local";
    }

    @Override
    public void loadCache() {
        Map<String, NumberRuleDTO> cacheData = new HashMap<>();
        List<NumberRule> numberRules = numberRuleMapper.list(Condition.<NumberRule>builder(NumberRule.class).build().eq(NumberRuleField.IS_ENABLE, true));
        if (CollectionUtils.isEmpty(numberRules)) {
            return;
        }

        for (NumberRule numberRule : numberRules) {
            List<NumberRuleDetail> numberRuleDetails = numberRuleDetailMapper.getNumberRuleDetails(numberRule.getCode());
            cacheData.put(numberRule.getCode(), NumberRuleDTO.toDTO(numberRule, numberRuleDetails));

            if (CollectionUtils.isEmpty(numberRuleDetails)) {
                continue;
            }

            for (NumberRuleDetail numberRuleDetail : numberRuleDetails) {
                NumberRuleType type = NumberRuleType.getType(numberRuleDetail.getType());
                if (type == null || !type.equals(NumberRuleType.SEQ)) {
                    continue;
                }
                SEQUENCE_CACHE.put(numberRuleDetail.getId(), new AtomicLong(Long.parseLong(numberRuleDetail.getValue())));
            }
        }

        NUMBER_CACHE_DATA.putAll(cacheData);
    }


    @Override
    public NumberRuleDTO getDTOByCache(String code) {
        if (NUMBER_CACHE_DATA.containsKey(code)) {
            return NUMBER_CACHE_DATA.get(code);
        }
        return null;
    }

    @Override
    public void setDTOCache(String code, NumberRuleDTO numberRuleDTO) {
        NUMBER_CACHE_DATA.put(code, numberRuleDTO);
    }

    @Override
    public NumberRuleDetail getLatestNumberRuleDetailByCache(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        if (NUMBER_CACHE_DATA.containsKey(code)) {
            NumberRuleDTO numberRuleDTO = NUMBER_CACHE_DATA.get(code);
            return numberRuleDTO.getDetails().stream().filter(d -> d.getId().equals(numberRuleDetail.getId())).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public void updateCacheWhenReset(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        for (NumberRuleDetail detailCache : NUMBER_CACHE_DATA.get(code).getDetails()) {
            if (detailCache.getId().equals(numberRuleDetail.getId())) {
                detailCache = numberRuleDetail;
                break;
            }
        }
        SEQUENCE_CACHE.get(numberRuleDetail.getId()).set(numberRuleDetail.getStartValue());
    }

    @Override
    public Long getAndSetSequenceByCache(String code, NumberRuleDetail numberRuleDetail) {
        Long sequence = null;
        if (SEQUENCE_CACHE.containsKey(numberRuleDetail.getId())) {
            sequence = SEQUENCE_CACHE.get(numberRuleDetail.getId()).addAndGet(numberRuleDetail.getStep());
        }
        return sequence;
    }

    @Override
    public void setSequenceCache(String code, NumberRuleDetail numberRuleDetail, Long sequence) {
        SEQUENCE_CACHE.put(numberRuleDetail.getId(), new AtomicLong(sequence));
    }

    @Override
    @PreDestroy
    public void updateDbByCache() {
        SEQUENCE_CACHE.forEach((id, value) -> {
            // 缓存持久化
            NumberRuleDetail numberRuleDetail = NumberRuleDetail.builder()
                    .id(id)
                    .value(String.valueOf(value))
                    .build();
            numberRuleDetailMapper.update(Update.<NumberRuleDetail>builder()
                    .fields(Arrays.asList(NumberRuleDetailField.VALUE))
                    .condition(Condition.<NumberRuleDetail>builder(numberRuleDetail).build().eq(NumberRuleDetailField.ID))
                    .build());
        });
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
