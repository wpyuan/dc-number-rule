package com.github.dc.number.rule.cache;

import com.github.dc.number.rule.constant.NumberRuleDetailField;
import com.github.dc.number.rule.constant.NumberRuleField;
import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.mapper.NumberRuleDetailMapper;
import com.github.dc.number.rule.mapper.NumberRuleMapper;
import com.github.mybatis.crud.structure.Condition;
import com.github.mybatis.crud.structure.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽象编号缓存实现
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/28 9:09
 */
@Slf4j
public abstract class AbstractNumberCache implements NumberCache {

    /**
     * 缓存类型
     *
     * @return
     */
    @Override
    public abstract String type();

    /**
     * 初始化缓存，刷新缓存
     */
    @Override
    public abstract void loadCache();

    @Override
    public NumberRuleDTO getNumberRule(String code) {
        // 1. 先取缓存
        NumberRuleDTO numberRuleDTO = this.getDTOByCache(code);
        if (numberRuleDTO != null) {
            return numberRuleDTO;
        }

        // 2. 再取数据库，再补缓存
        List<NumberRuleDetail> numberRuleDetails = getNumberRuleDetailMapper().getNumberRuleDetails(code);
        NumberRule numberRule = getNumberRuleMapper().detail(Condition.<NumberRule>builder(NumberRule.class).build().eq(NumberRuleField.CODE, code).eq(NumberRuleField.IS_ENABLE, true));
        numberRuleDTO = NumberRuleDTO.toDTO(numberRule, numberRuleDetails);
        this.setDTOCache(code, numberRuleDTO);

        return numberRuleDTO;
    }

    @Override
    public synchronized String handleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        String sequence = "";
        // 0、获取最新的numberRuleDetail
        // 0.1 从缓存中取
        NumberRuleDetail numberRuleDetailCache = this.getLatestNumberRuleDetailByCache(code, numberRuleDetail, param);
        if (numberRuleDetailCache == null) {
            // 0.2 从数据库取
            numberRuleDetail = getNumberRuleDetailMapper().selectByPrimaryKey(NumberRuleDetail.builder().id(numberRuleDetail.getId()).build());
        } else {
            numberRuleDetail = numberRuleDetailCache;
        }
        // 1、判断是否需要重置
        boolean isReset = this.isReset(numberRuleDetail, param);
        if (isReset) {
            // 1.1 需要重置，设置当前值为起始值
            numberRuleDetail.setValue(String.valueOf(numberRuleDetail.getStartValue()));
            numberRuleDetail.setLastResetDate(new Date());
            // 更新缓存
            this.updateCacheWhenReset(code, numberRuleDetail, param);
            // 更新数据库
            NumberRuleDetail detail = NumberRuleDetail.builder()
                    .id(numberRuleDetail.getId())
                    .value(numberRuleDetail.getValue())
                    .lastResetDate(numberRuleDetail.getLastResetDate())
                    .build();
            getNumberRuleDetailMapper().update(Update.<NumberRuleDetail>builder()
                    .condition(Condition.<NumberRuleDetail>builder(detail).build().eq(NumberRuleDetailField.ID))
                    .isUpdateSelective(true)
                    .build());
            log.debug("序列已重置起始值，序列：{}，明细ID：{}", code, numberRuleDetail.getId());
        } else {
            // 1.2 不需要重置，取下一个值
            numberRuleDetail.setValue(String.valueOf(this.getAndSet(code, numberRuleDetail)));
        }

        sequence = String.format("%0" + numberRuleDetail.getMaxLength() + "d", Long.valueOf(numberRuleDetail.getValue()));
        return sequence;
    }

    /**
     * 在原子操作下取序列值
     * @param code 编号规则
     * @param numberRuleDetail
     * @return
     */
    private Long getAndSet(String code, NumberRuleDetail numberRuleDetail) {
        // 取的序列值，若缓存中有，则取缓存; 缓存若没有则取数据库，再补足本地缓存
        Long sequence = this.getAndSetSequenceByCache(code, numberRuleDetail);
        if (sequence == null) {
            Assert.isTrue(getNumberRuleDetailMapper().incrementSeqValue(numberRuleDetail) == 1, "序列自增异常");
            sequence = getNumberRuleDetailMapper().getValue(numberRuleDetail.getId());
            numberRuleDetail.setValue(sequence.toString());
            this.setSequenceCache(code, numberRuleDetail, sequence);
        }
        return sequence;
    }

    /**
     * 根据缓存更新数据库
     */
    @Override
    public abstract void updateDbByCache();

    /**
     * 根据代码获取缓存中的编号规则头行DTO
     *
     * @param code 编号规则头代码
     * @return 缓存中的编号规则头行DTO
     */
    public abstract NumberRuleDTO getDTOByCache(String code);

    /**
     * 添加编号规则头行DTO缓存
     *
     * @param code          编号规则头代码
     * @param numberRuleDTO 编号规则头行DTO
     */
    public abstract void setDTOCache(String code, NumberRuleDTO numberRuleDTO);

    /**
     * 从缓存中取最新的编号规则明细
     *
     * @param code             编号规则头代码
     * @param numberRuleDetail 编号规则明细
     * @param param            调用方传入参数
     * @return 缓存中最新的编号规则明细
     */
    public abstract NumberRuleDetail getLatestNumberRuleDetailByCache(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param);

    /**
     * 重置时，更新缓存
     *
     * @param code             编号规则头代码
     * @param numberRuleDetail 编号规则明细
     * @param param            调用方传入参数
     */
    public abstract void updateCacheWhenReset(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param);

    /**
     * 通过缓存自增序列，保证其原子性
     *
     * @param code             编号规则头代码
     * @param numberRuleDetail 编号规则明细
     * @return 自增后的序列
     */
    public abstract Long getAndSetSequenceByCache(String code, NumberRuleDetail numberRuleDetail);

    /**
     * 更新序列缓存
     *
     * @param code             编号规则头代码
     * @param numberRuleDetail 编号规则明细
     * @param sequence         序列更新值
     */
    public abstract void setSequenceCache(String code, NumberRuleDetail numberRuleDetail, Long sequence);

    /**
     * 内部方法，返回表mapper即可
     *
     * @return 代码规则表mapper
     */
    public abstract NumberRuleMapper getNumberRuleMapper();

    /**
     * 内部方法，返回表mapper即可
     *
     * @return 代码规则明细表mapper
     */
    public abstract NumberRuleDetailMapper getNumberRuleDetailMapper();
}
