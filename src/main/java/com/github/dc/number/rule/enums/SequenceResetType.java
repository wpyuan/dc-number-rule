package com.github.dc.number.rule.enums;


import com.github.dc.number.rule.exception.NotFoundException;

import java.util.Arrays;

/**
 * <p>
 *     序列重置类型
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 16:09
 */
public enum SequenceResetType {
    /**
     * 年重置
     */
    Y("year"),
    /**
     * 月重置
     */
    M("month"),
    /**
     * 日重置
     */
    D("day");

    private String value;

    SequenceResetType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static SequenceResetType getType(String value) {
        return Arrays.stream(SequenceResetType.values()).filter(t -> t.value.equals(value)).findFirst().orElseThrow(() -> new NotFoundException("无此重置类型：" + value));
    }
}
