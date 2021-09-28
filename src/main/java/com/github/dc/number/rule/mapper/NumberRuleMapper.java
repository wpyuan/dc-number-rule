package com.github.dc.number.rule.mapper;

import com.github.mybatis.crud.mapper.BatchInsertMapper;
import com.github.mybatis.crud.mapper.DefaultMapper;
import com.github.dc.number.rule.entity.NumberRule;

/**
 *  编号生成规则 mapper
 *
 * @author wpyuan
 * @date 2021-09-26 15:18:46
 */
public interface NumberRuleMapper extends DefaultMapper<NumberRule>, BatchInsertMapper<NumberRule> {
}