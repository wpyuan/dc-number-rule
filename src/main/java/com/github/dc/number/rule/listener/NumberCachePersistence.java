package com.github.dc.number.rule.listener;

import com.github.dc.number.rule.cache.NumberCacheAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 编号规则数据持久化
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/29 9:59
 */
@Component
@Slf4j
public class NumberCachePersistence implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private NumberCacheAdapter numberCacheAdapter;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.trace("-->> [编号规则]缓存持久化处理");
        numberCacheAdapter.handleCachePersistenceWhenClose();
        log.debug("[编号规则]缓存持久化处理完成");
    }
}
