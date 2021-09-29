package com.github.dc.number.rule.builder.impl;

import com.github.dc.number.rule.builder.SerialNumberHelper;
import com.github.dc.number.rule.cache.NumberCacheAdapter;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.enums.NumberRuleType;
import com.github.dc.number.rule.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * 编号构造器默认实现
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 11:37
 */
@Component
@Slf4j
@AllArgsConstructor
public class DefaultSerialNumberHelper implements SerialNumberHelper {
    private final NumberCacheAdapter numberCacheAdapter;

    @Override
    public String generate(String code, Map<String, String> param) {
        StringBuffer numberBuffer = new StringBuffer();
        // 操作缓存适配器，取编号规则
        NumberRuleDTO numberRuleDTO = numberCacheAdapter.getNumberRule(code);
        List<NumberRuleDetail> numberRuleDetails = numberRuleDTO.getDetails();
        if (CollectionUtils.isEmpty(numberRuleDetails)) {
            throw new NotFoundException("找不到编号规则数据，检查数据是否存在或者是否启用");
        }
        for (NumberRuleDetail numberRuleDetail : numberRuleDetails) {
            Objects.requireNonNull(numberRuleDetail, "编号规则明细为空，检查数据是否存在或者是否启用");
            switch (NumberRuleType.getType(numberRuleDetail.getType())) {
                case CONST:
                    numberBuffer.append(numberRuleDetail.getValue());
                    break;
                case VAR:
                    numberBuffer.append(param.get(numberRuleDetail.getValue()));
                    break;
                case DATE:
                    numberBuffer.append(DateFormatUtils.format(new Date(), numberRuleDetail.getFormatter()));
                    break;
                case SEQ:
                    numberBuffer.append(numberCacheAdapter.handleSequence(code, numberRuleDetail, param));
                    break;
                default:
                    log.warn("无对应编号规则类型：{}", numberRuleDetail.getType());
            }
        }

        return numberBuffer.toString();
    }
}
