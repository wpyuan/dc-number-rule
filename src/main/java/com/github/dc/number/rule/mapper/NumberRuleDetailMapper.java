package com.github.dc.number.rule.mapper;

import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.mybatis.crud.mapper.BatchInsertMapper;
import com.github.mybatis.crud.mapper.DefaultMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 编号规则明细 mapper
 *
 * @author wpyuan
 * @date 2021-09-26 10:47:00
 */
public interface NumberRuleDetailMapper extends DefaultMapper<NumberRuleDetail>, BatchInsertMapper<NumberRuleDetail> {

    /**
     * 根据代码获取编号规则明细列表
     * @param code 编号规则头代码
     * @return 编号规则明细列表
     */
    @Results(id = "baseResults", value = {
            @Result(id = true, column = "ID", property = "id"),
            @Result(column = "HEADER_ID", property = "headerId"),
            @Result(column = "TYPE", property = "type"),
            @Result(column = "VALUE", property = "value"),
            @Result(column = "ORDER_SEQ", property = "orderSeq"),
            @Result(column = "START_VALUE", property = "startValue"),
            @Result(column = "STEP", property = "step"),
            @Result(column = "RESET_TYPE", property = "resetType"),
            @Result(column = "RESET_HANDLER", property = "resetHandler"),
            @Result(column = "LAST_RESET_DATE", property = "lastResetDate"),
            @Result(column = "MAX_LENGTH", property = "maxLength"),
            @Result(column = "FORMATTER", property = "formatter"),
            @Result(column = "IS_ENABLE", property = "isEnable")
    })
    @Select("select nrd.*\n" +
            "        from DC_NUMBER_RULE_DETAIL nrd\n" +
            "        where nrd.HEADER_ID = (\n" +
            "            select nr.ID from DC_NUMBER_RULE nr where nr.CODE = #{code}\n" +
            "            )\n" +
            "        and nrd.IS_ENABLE = 1")
    List<NumberRuleDetail> getNumberRuleDetails(String code);

    /**
     * 获取值
     * @param id 编号规则明细id
     * @return 编号规则明细值
     */
    @Select("select value from DC_NUMBER_RULE_DETAIL where ID = #{id}")
    Long getValue(String id);

    /**
     *  自增序列值
     * @param numberRuleDetail 编号规则明细
     * @return 影响条数
     */
    @Update("update DC_NUMBER_RULE_DETAIL\n" +
            "        SET VALUE = VALUE + STEP\n" +
            "        WHERE ID = #{id}")
    int incrementSeqValue(NumberRuleDetail numberRuleDetail);
}