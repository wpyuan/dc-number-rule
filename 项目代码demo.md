# 要求
年度-行政区号-类型编码-00000，00000按+1递增

## code
xmdm

#### 第一步、规则编号配置
```sql
-- 1、生成规则头
insert into NUMBER_RULE(ID, CODE, DESCRIPTION) values (sys_guid(), 'xmdm', '项目代码');

-- 2、生成规则明细
-- 年度规则
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, ORDER_SEQ, FORMATTER)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'date', 1, 'yyyy');
-- 连接符-
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'constant', '-', 2);
-- 行政区号
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'variable', 'xzqdm', 3);
-- 连接符-
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'constant', '-', 4);
-- 项目类型编码
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'variable', 'xmlx', 5);
-- 连接符-
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'constant', '-', 6);
-- 序列00000，00000按+1递增
insert into NUMBER_RULE_DETAIL(ID, HEADER_ID, TYPE, VALUE, ORDER_SEQ, START_VALUE, STEP, RESET_TYPE, LAST_RESET_DATE, MAX_LENGTH)
values (sys_guid(), (select ID from NUMBER_RULE where code = 'xmdm'), 'sequence', 0, 7, 0, 1, 'year', sysdate, 5);
```

#### 第二步、代码使用
```java
    @Autowired
    private SerialNumberHelper serialNumberHelper;

    @Test
    public void generate() {
        String code = "xmdm";
        Map<String, String> param = new HashMap<>(2);
        // 行政区
        param.put("xzqdm", "541200");
        // 类型
        param.put("lx", "1131");
        String xmdm = serialNumberHelper.generate(code, param);
        log.info("生成编号：{}", xmdm);
    }
```