package com.github.dc.number.rule.entity;

import com.github.mybatis.crud.annotation.Id;
import com.github.mybatis.crud.annotation.Table;
import com.github.mybatis.crud.enums.CustomFillIdMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *  编号规则明细
 *
 * @author wpyuan
 * @date 2021-09-26 10:47:00
 */
@Table(name = "DC_NUMBER_RULE_DETAIL")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NumberRuleDetail implements Serializable {

    /**
      * 
      */
    @Id(fillMethod = CustomFillIdMethod.UUID)
    private String id;
    /**
      * 头ID，取自NUMBER_RULE表.ID
      */
    private String headerId;
    /**
      * 类型，有sequence：序列；constant：常量；date：日期；variable：变量；
      */
    private String type;
    /**
      * 值，有常量的值；变量名
      */
    private String value;
    /**
      * 此规则在编号组成部分的排序号
      */
    private Integer orderSeq;
    /**
      * 起始值，type为序列时必配置
      */
    private Long startValue;
    /**
      * 步长，type为序列时必配置
      */
    private Integer step;
    /**
      * 序列重置类型，有year：年重置；month：月重置；day：日重置，type为序列时必配置
      */
    private String resetType;
    /**
      * 重置处理器，填全限定类名，用于处理客制化重置逻辑，type为序列时可配置
      */
    private String resetHandler;
    /**
      * 上次重置日期，type为序列时必配置，无则默认取号时的当前日期
      */
    private Date lastResetDate;
    /**
      * 序列最大长度，type为序列时必配置
      */
    private Integer maxLength;
    /**
      * 日期格式化pattern，type为日期时必配置
      */
    private String formatter;
    /**
      * 是否启用
      */
    private Boolean isEnable;
}