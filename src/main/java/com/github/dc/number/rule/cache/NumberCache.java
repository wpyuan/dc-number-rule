package com.github.dc.number.rule.cache;

import com.github.dc.number.rule.dto.NumberRuleDTO;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.enums.SequenceResetType;
import com.github.dc.number.rule.handler.IResetHandler;
import com.github.defaultcore.helper.ApplicationContextHelper;
import com.github.mybatis.crud.util.EntityUtil;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 编号缓存
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/24 9:16
 */
public interface NumberCache {

    /**
     * 缓存类型
     *
     * @return
     */
    String type();

    /**
     * 初始化缓存，刷新缓存
     */
    void loadCache();

    /**
     * 根据编码获取编号规则头行数据
     *
     * @param code 编号规则编码
     * @return 编号规则头行数据
     */
    NumberRuleDTO getNumberRule(String code);

    /**
     * 获取序列值
     *
     * @param code             编号规则编码
     * @param numberRuleDetail 编号规则明细
     * @param param            调用方入参
     * @return 序列值
     */
    String handleSequence(String code, NumberRuleDetail numberRuleDetail, Map<String, Object> param);

    /**
     * 根据缓存更新数据库
     */
    void updateDbByCache();

    /**
     * 判断是否需要重置
     *
     * @param numberRuleDetail 编号规则明细
     * @param param            调用方入参
     * @return 默认false不重置
     */
    default boolean isReset(NumberRuleDetail numberRuleDetail, Map<String, Object> param) {
        //
        // 1.有客制化逻辑，优先执行
        //===============================================
        if (StringUtils.isNotBlank(numberRuleDetail.getResetHandler())) {
            IResetHandler resetHandler = this.getResetHandler(numberRuleDetail.getResetHandler());
            if (resetHandler == null) {
                return false;
            }
            return resetHandler.isReset(numberRuleDetail, param);
        }

        //
        // 2.无客制化逻辑
        //===============================================
        if (numberRuleDetail.getLastResetDate() == null) {
            // 2.1.若当前无上次重置日期，默认走一遍重置逻辑
            numberRuleDetail.setLastResetDate(new Date());
            return true;
        }

        // 2.2 距离上次重置日期，超过设定的重置类型，则需要重置
        switch (SequenceResetType.getType(numberRuleDetail.getResetType())) {
            case Y:
                return DateUtils.addYears(numberRuleDetail.getLastResetDate(), 1).before(new Date());
            case M:
                return DateUtils.addMonths(numberRuleDetail.getLastResetDate(), 1).before(new Date());
            case D:
                return DateUtils.addDays(numberRuleDetail.getLastResetDate(), 1).before(new Date());
            default:
                ;
        }

        // 默认不重置
        return false;
    }

    /**
     * 获取客制化重置逻辑处理器
     *
     * @param handlerName 处理器全限定类名
     * @return 客制化重置逻辑处理器
     */
    default IResetHandler getResetHandler(String handlerName) {
        IResetHandler resetHandler = (IResetHandler) ApplicationContextHelper.getBean(handlerName);

        if (resetHandler != null) {
            return resetHandler;
        }

        try {
            return (IResetHandler) EntityUtil.instance(ClassUtils.getClass(handlerName));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
