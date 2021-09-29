# dc-number-rule
DC框架编号规则默认实现

## 使用说明
### 第一步，[引入依赖](https://search.maven.org/artifact/com.github.wpyuan/dc-number-rule/0.0.1/jar)

### 第二步，添加配置
`application.yml`文件加入如下配置
```yml
dc:
  number:
    cache:
      # 是否启用缓存：true or false，默认true
      enable: true
      # 缓存类型 no-cache（无缓存实现） or local（本地缓存实现） or redis （redis缓存实现） or 自定义实现，默认no-cache
      type: no-cache
```

### 第三步，引入构造器，并使用
```java
@SpringBootTest
@Slf4j
public class BuildTest {

    @Autowired
    private SerialNumberHelper serialNumberHelper;

    @Test
    public void generateOne() {
        String code = "order_number";
        String orderNumber = serialNumberHelper.generate(code, null);
        log.debug("生成订单编号：{}", orderNumber);
    }
    
    // ...
}
```

## 客制化扩展

### 第一种，重头开始客制化扩展
添加`SerialNumberHelper`实现类，后续注入使用，注意bean定义名

### 第二种，添加缓存类型
添加`NumberCache`实现类，后续在配置文件中把`type`更改为定义名称

## 使用建议
建议使用一种缓存类型，方便统一管理