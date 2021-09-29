package com.github.dc.number.rule.handler;

/**
 * <p>
 * 纠正序列数据处理器，在缓存初始化之前调用
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/29 10:52
 */
public interface ICorrectionHandler {

    /**
     * 根据业务数据校正序列值
     */
    void correctSequenceByBusinessData();
}
