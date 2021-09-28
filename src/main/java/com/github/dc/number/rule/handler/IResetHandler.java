package com.github.dc.number.rule.handler;


import com.github.dc.number.rule.entity.NumberRuleDetail;

import java.util.Map;

/**
 * <p>
 * 重置处理器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 11:21
 */
public interface IResetHandler {

    /**
     * 是否重置
     *
     * @param numberRuleDetail
     * @param param
     * @return 是否重置
     */
    boolean isReset(NumberRuleDetail numberRuleDetail, Map<String, Object> param);
}
