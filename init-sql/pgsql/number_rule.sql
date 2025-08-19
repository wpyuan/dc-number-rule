CREATE TABLE number_rule (
                             id          VARCHAR(60)   PRIMARY KEY,
                             code        VARCHAR(100)  NOT NULL,
                             description VARCHAR(500),
                             is_enable   SMALLINT      DEFAULT 1,
                             remark      VARCHAR(1000),
                             CONSTRAINT number_rule_u1 UNIQUE (code)
);

-- 表注释
COMMENT ON TABLE number_rule IS '编号生成规则';

-- 字段注释
COMMENT ON COLUMN number_rule.id IS '主键';
COMMENT ON COLUMN number_rule.code IS '编号';
COMMENT ON COLUMN number_rule.description IS '描述';
COMMENT ON COLUMN number_rule.is_enable IS '是否启用';
COMMENT ON COLUMN number_rule.remark IS '备注';