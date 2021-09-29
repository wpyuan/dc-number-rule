package com.github.dc.number.rule.controller;

import com.github.dc.number.rule.builder.SerialNumberHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 编号生成规则 控制器
 *
 * @author wpyuan
 * @date 2021-09-26 15:18:46
 */
@RestController
@RequestMapping("/serial-number")
@Slf4j
public class SerialNumberGenerateController {

    @Autowired
    private SerialNumberHelper serialNumberHelper;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam(name = "code") String code, @RequestBody Map<String, String> param) {
        return ResponseEntity.ok(serialNumberHelper.generate(code, param));
    }

}