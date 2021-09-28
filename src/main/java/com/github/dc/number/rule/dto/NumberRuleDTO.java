package com.github.dc.number.rule.dto;

import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 16:38
 */
@Data
@Builder
public class NumberRuleDTO implements Serializable {
    private String id;
    private String code;
    private String desc;
    private Boolean isEnable;
    private String remark;
    private List<NumberRuleDetail> details;

    public static NumberRuleDTO toDTO(NumberRule numberRule, List<NumberRuleDetail> details) {
        return NumberRuleDTO.builder()
                .id(numberRule.getId())
                .code(numberRule.getCode())
                .desc(numberRule.getDescription())
                .isEnable(numberRule.getIsEnable())
                .remark(numberRule.getRemark())
                .details(details)
                .build();
    }
}
