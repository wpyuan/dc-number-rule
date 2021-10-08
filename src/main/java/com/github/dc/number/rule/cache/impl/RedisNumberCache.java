package com.github.dc.number.rule.cache.impl;


import com.github.dc.number.rule.cache.NumberCache;
import com.github.dc.number.rule.constant.NumberRuleDetailField;
import com.github.dc.number.rule.constant.NumberRuleField;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.enums.NumberRuleType;
import com.github.dc.number.rule.mapper.NumberRuleDetailMapper;
import com.github.dc.number.rule.mapper.NumberRuleMapper;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.Update;
import com.github.mybatis.crud.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private static final String RULE_KEY_PREFIX = "dc:number-rule:";
    private static final String DETAIL_KEY_PREFIX = "dc:number-rule-detail:";
    private static final String SEQ_KEY_PREFIX = "dc:number-rule:sequence:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private NumberRuleMapper numberRuleMapper;
    @Autowired
    private NumberRuleDetailMapper numberRuleDetailMapper;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public void loadCache() {
        List<NumberRule> numberRules = numberRuleMapper.list(Condition.<NumberRule>builder(NumberRule.class).build()
                .eq(NumberRuleField.IS_ENABLE, true));
        if (CollectionUtils.isEmpty(numberRules)) {
            return;
        }
        for (NumberRule numberRule : numberRules) {
            List<NumberRuleDetail> numberRuleDetails = numberRuleDetailMapper.getNumberRuleDetails(numberRule.getCode());
            stringRedisTemplate.opsForHash().putAll(RULE_KEY_PREFIX + numberRule.getCode(), beanToMap(numberRule));
            for (NumberRuleDetail numberRuleDetail : numberRuleDetails) {
                stringRedisTemplate.opsForHash().putAll(DETAIL_KEY_PREFIX + numberRule.getCode() + ":" + numberRuleDetail.getId(), beanToMap(numberRuleDetail));
                if (NumberRuleType.SEQ.value().equals(numberRuleDetail.getType())) {
                    stringRedisTemplate.opsForValue().set(SEQ_KEY_PREFIX + numberRuleDetail.getId(), numberRuleDetail.getValue());
                }
            }
        }
    }

    @Override
    public NumberRuleDTO getNumberRule(String code) {
        // 1. 先取缓存
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(RULE_KEY_PREFIX + code))) {
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            Map<String, String> ruleMap = hashOperations.entries(RULE_KEY_PREFIX + code);
            NumberRule numberRule = mapToBean(ruleMap, NumberRule.class);

            Set<String> detailKeys = stringRedisTemplate.keys(DETAIL_KEY_PREFIX + code + "*");
            List<NumberRuleDetail> numberRuleDetails = new ArrayList<>();
            NumberRuleDetail numberRuleDetail = null;
            for (String detailKey : detailKeys) {
                Map<String, String> ruleDetailMap = hashOperations.entries(detailKey);
                numberRuleDetail = mapToBean(ruleDetailMap, NumberRuleDetail.class);
                numberRuleDetails.add(numberRuleDetail);
            }

            return NumberRuleDTO.toDTO(numberRule, numberRuleDetails);
        }

        // 2. 再取数据库，再补缓存
        NumberRule numberRule = numberRuleMapper.detail(Condition.<NumberRule>builder(NumberRule.class).build().eq(NumberRuleField.CODE, code).eq(NumberRuleField.IS_ENABLE, true));
        stringRedisTemplate.opsForHash().putAll(RULE_KEY_PREFIX + code, beanToMap(numberRule));
        List<NumberRuleDetail> numberRuleDetails = numberRuleDetailMapper.getNumberRuleDetails(code);
        for (NumberRuleDetail numberRuleDetail : numberRuleDetails) {
            stringRedisTemplate.opsForHash().putAll(DETAIL_KEY_PREFIX + code + ":" + numberRuleDetail.getId(), beanToMap(numberRuleDetail));
            if (NumberRuleType.SEQ.value().equals(numberRuleDetail.getType())) {
                stringRedisTemplate.opsForValue().set(SEQ_KEY_PREFIX + numberRuleDetail.getId(), numberRuleDetail.getValue());
            }
        }

        return NumberRuleDTO.toDTO(numberRule, numberRuleDetails);
    }

    @Override
    public String handleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, String> param) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(numberRuleDetail.getId()+"_lock");
        try {
            boolean res = lock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                //成功
                return this.customHandleSequence(code, numberRuleDetail, param);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("redis tryLock error", e);
        } finally {
            lock.writeLock().unlock();
        }

        return null;
    }

    private String customHandleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, String> param) {
        String sequence = "";
        // 0、获取最新的numberRuleDetail
        // 0.1 从缓存中取
        NumberRuleDetail numberRuleDetailCache = this.getLatestNumberRuleDetailByCache(code, numberRuleDetail, param);
        if (numberRuleDetailCache == null) {
            // 0.2 从数据库取
            numberRuleDetail = numberRuleDetailMapper.selectByPrimaryKey(NumberRuleDetail.builder().id(numberRuleDetail.getId()).build());
        } else {
            numberRuleDetail = numberRuleDetailCache;
        }
        // 1、判断是否需要重置
        boolean isReset = this.isReset(numberRuleDetail, param);
        if (isReset) {
            // 1.1 需要重置，设置当前值为起始值
            numberRuleDetail.setValue(String.valueOf(numberRuleDetail.getStartValue()));
            numberRuleDetail.setLastResetDate(new Date());
            // 更新缓存
            this.updateCacheWhenReset(code, numberRuleDetail, param);
            // 更新数据库
            NumberRuleDetail detail = NumberRuleDetail.builder()
                    .id(numberRuleDetail.getId())
                    .value(numberRuleDetail.getValue())
                    .lastResetDate(numberRuleDetail.getLastResetDate())
                    .build();
            numberRuleDetailMapper.update(Update.<NumberRuleDetail>builder()
                    .condition(Condition.<NumberRuleDetail>builder(detail).build().eq(NumberRuleDetailField.ID))
                    .isUpdateSelective(true)
                    .build());
            log.debug("序列已重置起始值，序列：{}，明细ID：{}", code, numberRuleDetail.getId());
        } else {
            // 1.2 不需要重置，取下一个值
            numberRuleDetail.setValue(String.valueOf(this.getAndSet(code, numberRuleDetail)));
            if (numberRuleDetail.getMaxLength() < numberRuleDetail.getValue().length()) {
                log.error("序列超出最大长度，请调整！编号规则：{}, 明细ID：{}", code, numberRuleDetail.getId());
            }
        }

        sequence = String.format("%0" + numberRuleDetail.getMaxLength() + "d", Long.valueOf(numberRuleDetail.getValue()));
        return sequence;
    }

    public NumberRuleDetail getLatestNumberRuleDetailByCache(String code, NumberRuleDetail numberRuleDetail, Map<String, String> param) {
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(DETAIL_KEY_PREFIX + code + ":" + numberRuleDetail.getId()))) {
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            Map<String, String> ruleDetailMap = hashOperations.entries(DETAIL_KEY_PREFIX + code + ":" + numberRuleDetail.getId());
            return mapToBean(ruleDetailMap, NumberRuleDetail.class);
        }
        return null;
    }

    public void updateCacheWhenReset(String code, NumberRuleDetail numberRuleDetail, Map<String, String> param) {
        stringRedisTemplate.opsForHash().putAll(DETAIL_KEY_PREFIX + code + ":" + numberRuleDetail.getId(), beanToMap(numberRuleDetail));
        stringRedisTemplate.opsForValue().set(SEQ_KEY_PREFIX + numberRuleDetail.getId(), String.valueOf(numberRuleDetail.getStartValue()));
    }

    /**
     * 在原子操作下取序列值
     * @param code 编号规则
     * @param numberRuleDetail
     * @return
     */
    private Long getAndSet(String code, NumberRuleDetail numberRuleDetail) {
        // 取的序列值，若缓存中有，则取缓存; 缓存若没有则取数据库，再补足本地缓存
        Long sequence = this.getAndSetSequenceByCache(code, numberRuleDetail);
        if (sequence == null) {
            Assert.isTrue(numberRuleDetailMapper.incrementSeqValue(numberRuleDetail) == 1, "序列自增异常");
            sequence = numberRuleDetailMapper.getValue(numberRuleDetail.getId());
            numberRuleDetail.setValue(sequence.toString());
            this.setSequenceCache(code, numberRuleDetail, sequence);
        }
        return sequence;
    }

    public Long getAndSetSequenceByCache(String code, NumberRuleDetail numberRuleDetail) {
        Long sequence = null;
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(SEQ_KEY_PREFIX + numberRuleDetail.getId()))) {
            sequence = stringRedisTemplate.opsForValue().increment(SEQ_KEY_PREFIX + numberRuleDetail.getId(), numberRuleDetail.getStep());
        }
        return sequence;
    }

    public void setSequenceCache(String code, NumberRuleDetail numberRuleDetail, Long sequence) {
        stringRedisTemplate.opsForValue().set(SEQ_KEY_PREFIX + numberRuleDetail.getId(), String.valueOf(sequence));
    }

    @Override
    public void handleCachePersistenceWhenClose() {
        Set<String> sequenceCacheKeys = stringRedisTemplate.keys(SEQ_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(sequenceCacheKeys)) {
            return;
        }
        for (String key : sequenceCacheKeys) {
            String id = key.split(":")[3];
            String value = stringRedisTemplate.opsForValue().get(key);

            // 缓存持久化
            NumberRuleDetail numberRuleDetail = NumberRuleDetail.builder()
                    .id(id)
                    .value(String.valueOf(value))
                    .build();
            numberRuleDetailMapper.update(Update.<NumberRuleDetail>builder()
                    .fields(Arrays.asList(NumberRuleDetailField.VALUE))
                    .condition(Condition.<NumberRuleDetail>builder(numberRuleDetail).build().eq(NumberRuleDetailField.ID))
                    .build());
        }
    }

    private <B> Map<String, String> beanToMap(B bean) {
        Map<String, String> map = new HashMap<>(1);
        Class clazz = bean.getClass();
        Field[] field = clazz.getDeclaredFields();
        for (Field f : field) {
            f.setAccessible(true);
            try {
                if (f.get(bean) == null) {
                    continue;
                } else if (Date.class.equals(f.getType())) {
                    map.put(f.getName(), DateFormatUtils.format((Date) f.get(bean), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    map.put(f.getName(), String.valueOf(f.get(bean)));
                }
            } catch (IllegalAccessException e) {
                log.warn("'beanToMap' Function Running ERROR", e);
            }
        }
        return map;
    }

    private <B> B mapToBean(Map<String, String> map, Class<B> bClass) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        B b = EntityUtil.instance(bClass);
        Field[] field = bClass.getDeclaredFields();
        for (Field f : field) {
            f.setAccessible(true);
            try {
                if (map.get(f.getName()) == null) {
                    continue;
                } else if (Date.class.equals(f.getType())) {
                    f.set(b, DateUtils.parseDate(map.get(f.getName()), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    f.set(b, ConvertUtils.convert(map.get(f.getName()), f.getType()));
                }
            } catch (IllegalAccessException | ParseException e) {
                log.warn("'mapToBean' Function Running ERROR", e);
            }

        }
        return b;
    }
}
