package com.github.dc.number.rule.entity;

import com.github.mybatis.crud.annotation.Id;
import com.github.mybatis.crud.annotation.Table;
import com.github.mybatis.crud.enums.CustomFillIdMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  编号生成规则
 *
 * @author wpyuan
 * @date 2021-09-26 15:18:46
 */
@Table(name = "NUMBER_RULE")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NumberRule {

    /**
      * 
      */
    @Id(fillMethod = CustomFillIdMethod.UUID)
    private String id;
    /**
      * 编号
      */
    private String code;
    /**
      * 描述
      */
    private String description;
    /**
      * 是否启用
      */
    private Boolean isEnable;
    /**
      * 备注
      */
    private String remark;
}