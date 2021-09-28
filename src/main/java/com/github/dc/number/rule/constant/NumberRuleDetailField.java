package com.github.dc.number.rule.constant;

/**
 *  编号规则明细字段常量，供查询方法使用
 *
 * @author wpyuan
 * @date 2021-09-26 10:47:00
 */
public interface NumberRuleDetailField {

    /**
      * 
      */
    String ID = "id";
    /**
      * 头ID，取自NUMBER_RULE表.ID
      */
    String HEADER_ID = "headerId";
    /**
      * 类型，有sequence：序列；constant：常量；date：日期；variable：变量；
      */
    String TYPE = "type";
    /**
      * 值，有常量的值；变量名
      */
    String VALUE = "value";
    /**
      * 此规则在编号组成部分的排序号
      */
    String ORDER_SEQ = "orderSeq";
    /**
      * 起始值，type为序列时必配置
      */
    String START_VALUE = "startValue";
    /**
      * 步长，type为序列时必配置
      */
    String STEP = "step";
    /**
      * 序列重置类型，有year：年重置；month：月重置；day：日重置，type为序列时必配置
      */
    String RESET_TYPE = "resetType";
    /**
      * 重置处理器，填全限定类名，用于处理客制化重置逻辑，type为序列时可配置
      */
    String RESET_HANDLER = "resetHandler";
    /**
      * 上次重置日期，type为序列时必配置，无则默认取号时的当前日期
      */
    String LAST_RESET_DATE = "lastResetDate";
    /**
      * 序列最大长度，type为序列时必配置
      */
    String MAX_LENGTH = "maxLength";
    /**
      * 日期格式化pattern，type为日期时必配置
      */
    String FORMATTER = "formatter";
    /**
      * 是否启用
      */
    String IS_ENABLE = "isEnable";

}