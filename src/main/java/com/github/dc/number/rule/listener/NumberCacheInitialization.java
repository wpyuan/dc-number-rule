package com.github.dc.number.rule.listener;

import com.github.dc.number.rule.cache.NumberCacheAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     本地缓存初始化
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/26 16:46
 */
@Component
@Slf4j
public class NumberCacheInitialization implements CommandLineRunner {

    @Autowired
    private NumberCacheAdapter numberCacheAdapter;

    @Override
    public void run(String... args) throws Exception {
        log.debug("-->> 开始加载[编号规则]缓存");
        numberCacheAdapter.loadCache();
        log.debug("加载[编号规则]缓存完成");
    }
}
