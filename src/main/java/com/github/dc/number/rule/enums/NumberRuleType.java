package com.github.dc.number.rule.enums;


import com.github.dc.number.rule.exception.NotFoundException;

import java.util.Arrays;

/**
 * <p>
 *     编号构造类型
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 11:08
 */
public enum NumberRuleType {
    /**
     * 序列
     */
    SEQ("sequence"),
    /**
     * 常量
     */
    CONST("constant"),
    /**
     * 变量
     */
    VAR("variable"),
    /**
     * 日期
     */
    DATE("date");

    private String value;

    NumberRuleType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static NumberRuleType getType(String value) {
        return Arrays.stream(NumberRuleType.values()).filter(t -> t.value.equals(value)).findFirst().orElseThrow(()-> new NotFoundException("无编号构造类型：" + value));
    }
}
