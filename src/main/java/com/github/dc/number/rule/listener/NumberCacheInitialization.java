package com.github.dc.number.rule.listener;

import com.github.dc.number.rule.cache.NumberCacheAdapter;
import com.github.dc.number.rule.handler.ICorrectionHandler;
import com.github.defaultcore.helper.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 本地缓存初始化
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
        log.trace("-->> 开始加载[编号规则]缓存");
        // 校正应用编号数据与编号规则序列项数据的一致性
        ICorrectionHandler correctionHandler = this.getCorrectionHandler();
        if (correctionHandler != null) {
            correctionHandler.correctSequenceByBusinessData();
        }
        numberCacheAdapter.loadCache();
        log.debug("加载[编号规则]缓存完成");
    }

    /**
     * 获取客制化纠正序列数据处理器
     *
     * @return 客制化纠正序列数据处理器
     */
    private ICorrectionHandler getCorrectionHandler() {
        ICorrectionHandler correctionHandler = ApplicationContextHelper.getBean(ICorrectionHandler.class);
        return correctionHandler;
    }
}
