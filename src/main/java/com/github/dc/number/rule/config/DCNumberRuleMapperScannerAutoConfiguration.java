package com.github.dc.number.rule.config;

import com.github.mybatis.crud.config.MyBatisSqlSessionFactoryConfig;
import com.github.mybatis.crud.util.StringUtil;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>
 * 默认实现mapper自动配置
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/18 11:38
 */
@Configuration
@AutoConfigureAfter(MyBatisSqlSessionFactoryConfig.class)
public class DCNumberRuleMapperScannerAutoConfiguration implements EnvironmentAware {

    private Environment env;
    private String sqlSessionFactoryBeanName;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        this.sqlSessionFactoryBeanName = this.env.getProperty("dc.mybatis.mapperScannerConfigurer.sqlSessionFactoryBeanName");
    }

    @Bean
    @ConditionalOnMissingBean(name = "dcNumberRuleMapperScannerAutoConfiguration")
    public MapperScannerConfigurer dcNumberRuleMapperScannerAutoConfiguration() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        if (StringUtil.isNotBlank(sqlSessionFactoryBeanName)) {
            mapperScannerConfigurer.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        }
        mapperScannerConfigurer.setBasePackage("com.github.dc.number.rule.mapper");
        return mapperScannerConfigurer;
    }
}
