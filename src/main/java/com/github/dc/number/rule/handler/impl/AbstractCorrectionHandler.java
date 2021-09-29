package com.github.dc.number.rule.handler.impl;

import com.github.dc.number.rule.constant.NumberRuleDetailField;
import com.github.dc.number.rule.constant.NumberRuleField;
import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.handler.ICorrectionHandler;
import com.github.dc.number.rule.mapper.NumberRuleDetailMapper;
import com.github.dc.number.rule.mapper.NumberRuleMapper;
import com.github.defaultcore.helper.ApplicationContextHelper;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.Update;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽象实现校正序列处理器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/29 17:16
 */
public abstract class AbstractCorrectionHandler implements ICorrectionHandler {

    @Override
    public void correctSequenceByBusinessData() {
        NumberRuleMapper numberRuleMapper = ApplicationContextHelper.getBean(NumberRuleMapper.class);
        NumberRuleDetailMapper numberRuleDetailMapper = ApplicationContextHelper.getBean(NumberRuleDetailMapper.class);

        Map<Pair<String, Integer>, Integer> config = this.getColumnSequenceIndexData();
        List<NumberRule> numberRules = numberRuleMapper.list(Condition.<NumberRule>builder(NumberRule.class).build().eq(NumberRuleField.IS_ENABLE, true));
        if (CollectionUtils.isEmpty(numberRules)) {
            return;
        }
        for (NumberRule numberRule : numberRules) {
            List<NumberRuleDetail> numberRuleDetails = numberRuleDetailMapper.getNumberRuleDetails(numberRule.getCode());
            if (CollectionUtils.isEmpty(numberRuleDetails)) {
                return;
            }
            for (NumberRuleDetail numberRuleDetail : numberRuleDetails) {
                if (config.containsKey((Pair.of(numberRule.getCode(), numberRuleDetail.getOrderSeq())))) {
                    Integer startIndex = config.get(Pair.of(numberRule.getCode(), numberRuleDetail.getOrderSeq()));
                    Long maxValue = this.getMaxValue(numberRule, numberRuleDetail, startIndex);
                    if (maxValue > Long.parseLong(numberRuleDetail.getValue())) {
                        numberRuleDetail.setValue(String.valueOf(maxValue));
                        numberRuleDetailMapper.update(Update.<NumberRuleDetail>builder()
                                .fields(Arrays.asList(NumberRuleDetailField.VALUE))
                                .condition(Condition.<NumberRuleDetail>builder(numberRuleDetail).build().eq(NumberRuleDetailField.ID))
                                .build());
                    }
                }
            }
        }

    }

    /**
     * 获取业务字段索引部分位置
     *
     * @return 业务字段索引部分位置。key: 编号规则头代码 和 索引的编号规则明细orderSeq，value: 起始位置
     */
    public abstract Map<Pair<String, Integer>, Integer> getColumnSequenceIndexData();

    /**
     * 获取业务表中索引最大值
     *
     * @param numberRule       编号规则头
     * @param numberRuleDetail 编号规则明细
     * @param startIndex       业务字段中，索引的起始位置
     * @return 业务表中索引最大值
     */
    public abstract Long getMaxValue(NumberRule numberRule, NumberRuleDetail numberRuleDetail, Integer startIndex);

}
