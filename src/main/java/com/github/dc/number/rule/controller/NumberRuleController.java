package com.github.dc.number.rule.controller;

import com.github.dc.common.base.controller.DefaultController;
import com.github.dc.number.rule.entity.NumberRule;
import com.github.dc.number.rule.service.NumberRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 编号生成规则 控制器
 *
 * @author wpyuan
 * @date 2021-09-26 15:18:46
 */
@RestController
@RequestMapping("/number-rule")
@Slf4j
public class NumberRuleController extends DefaultController<NumberRule> {

    @Autowired
    private NumberRuleService service;

}