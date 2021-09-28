package com.github.dc.number.rule.cache;

import com.github.dc.number.rule.cache.impl.NoNumberCache;
import com.github.dc.number.rule.config.NumberRuleProperties;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.exception.NotFoundException;
import com.github.defaultcore.helper.ApplicationContextHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 编号缓存适配器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 16:55
 */
@AllArgsConstructor
@Slf4j
public class NumberCacheAdapter implements NumberCache {

    private final NumberRuleProperties numberRuleProperties;

    /**
     * 各种缓存处理器，应对不同场景
     * <p>
     * 可在NumberCacheAdapter Bean定义处扩展
     * {@link com.github.dc.number.rule.config.NumberCacheAutoConfiguration#numberCacheAdapter(com.github.dc.number.rule.config.NumberRuleProperties)}
     */
    private static List<NumberCache> caches = new ArrayList<>();

    @Override
    public String type() {
        return "adapter";
    }

    @Override
    public void loadCache() {
        if (noCache()) {
            NoNumberCache noNumberCache = ApplicationContextHelper.getBean(NoNumberCache.class);
            noNumberCache.loadCache();
            return;
        }

        caches.stream().filter(c -> numberRuleProperties.getCache().getType().equals(c.type())).findFirst()
                .orElseThrow(() -> new NotFoundException("找不到该类型的编号缓存处理器：" + numberRuleProperties.getCache().getType())).loadCache();
    }

    @Override
    public NumberRuleDTO getNumberRule(String code) {
        if (noCache()) {
            NoNumberCache noNumberCache = ApplicationContextHelper.getBean(NoNumberCache.class);
            return noNumberCache.getNumberRule(code);
        }

        return caches.stream().filter(c -> numberRuleProperties.getCache().getType().equals(c.type())).findFirst()
                .orElseThrow(() -> new NotFoundException("找不到该类型的编号缓存处理器：" + numberRuleProperties.getCache().getType())).getNumberRule(code);
    }

    @Override
    public String handleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        if (noCache()) {
            NoNumberCache noNumberCache = ApplicationContextHelper.getBean(NoNumberCache.class);
            return noNumberCache.handleSequence(code, numberRuleDetail, param);
        }

        return caches.stream().filter(c -> numberRuleProperties.getCache().getType().equals(c.type())).findFirst()
                .orElseThrow(() -> new NotFoundException("找不到该类型的编号缓存处理器：" + numberRuleProperties.getCache().getType())).handleSequence(code, numberRuleDetail, param);
    }

    @Override
    public void updateDbByCache() {
        if (noCache()) {
            NoNumberCache noNumberCache = ApplicationContextHelper.getBean(NoNumberCache.class);
            noNumberCache.updateDbByCache();
            return;
        }

        caches.stream().filter(c -> numberRuleProperties.getCache().getType().equals(c.type())).findFirst()
                .orElseThrow(() -> new NotFoundException("找不到该类型的编号缓存处理器：" + numberRuleProperties.getCache().getType())).updateDbByCache();
    }

    /**
     * 是否不使用缓存
     * @return 是否不使用缓存
     */
    private Boolean noCache() {
        // 不启用缓存 或者 启用缓存但没配置缓存处理器类型 或者 缓存处理器列表为空
        return !numberRuleProperties.getCache().getEnable() || StringUtils.isBlank(numberRuleProperties.getCache().getType()) || CollectionUtils.isEmpty(caches);
    }


    public void setCaches(NumberCache... numberCache) {
        caches.addAll(Arrays.asList(numberCache));
    }

    public List<NumberCache> getCaches() {
        return Collections.unmodifiableList(caches);
    }
}
