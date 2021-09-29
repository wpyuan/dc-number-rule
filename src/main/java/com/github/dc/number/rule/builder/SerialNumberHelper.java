package com.github.dc.number.rule.builder;

import java.util.Map;

/**
 * <p>
 *     编号构造器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 11:30
 */
public interface SerialNumberHelper {

    /**
     * 执行构造获取编号
     *
     * @param code 编号规则代码
     * @param param 调用方传参
     * @return 编号
     */
    String generate(String code, Map<String, String> param);
}
