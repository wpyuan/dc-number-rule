package com.github.dc.number.rule.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 编号规则明细 mapper
 *
 * @author wpyuan
 * @date 2021-09-26 10:47:00
 */
public interface CommonGetMaxValueMapper {

    /**
     * 获取业务表中索引最大值
     *
     * @param table      业务表名
     * @param column     索引所在字段
     * @param startIndex 索引在字段的起始位置
     * @param length     索引最大长度
     * @return 业务表中索引最大值
     */
    @Select("select max(substr(${column}, #{startIndex}, #{length})) from ${table}")
    Long getMaxValue(@Param("table") String table, @Param("column") String column, @Param("startIndex") Integer startIndex, @Param("length") Integer length);

}