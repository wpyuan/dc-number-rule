package com.github.dc.number.rule.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     编号规则配置属性
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/9/28 8:56
 */
@Configuration
@ConfigurationProperties(prefix = "dc.number")
@Data
public class NumberRuleProperties {

    private Cache cache = new Cache();

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class Cache {
        private Boolean enable = Boolean.TRUE;
        private String type = "no-cache";
    }
}
