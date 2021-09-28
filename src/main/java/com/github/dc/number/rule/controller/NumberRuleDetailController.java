package com.github.dc.number.rule.controller;

import com.github.dc.common.base.controller.DefaultController;
import com.github.dc.number.rule.entity.NumberRuleDetail;
import com.github.dc.number.rule.service.NumberRuleDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 编号规则明细 控制器
 *
 * @author wpyuan
 * @date 2021-09-26 10:47:00
 */
@RestController
@RequestMapping("/number-rule-detail")
@Slf4j
public class NumberRuleDetailController extends DefaultController<NumberRuleDetail> {

    @Autowired
    private NumberRuleDetailService service;

}