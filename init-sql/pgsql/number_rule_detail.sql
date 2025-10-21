CREATE TABLE dc_number_rule_detail (
                                    id              VARCHAR(60)   PRIMARY KEY,
                                    header_id       VARCHAR(60),
                                    type            VARCHAR(50),
                                    value           VARCHAR(100),
                                    order_seq       SMALLINT,
                                    start_value     INTEGER,
                                    step            INTEGER,
                                    reset_type      VARCHAR(10),
                                    reset_handler   VARCHAR(100),
                                    last_reset_date TIMESTAMP,
                                    max_length      INTEGER,
                                    formatter       VARCHAR(50),
                                    is_enable       SMALLINT      DEFAULT 1
);

-- 表注释
COMMENT ON TABLE dc_number_rule_detail IS '编号规则明细';

-- 字段注释
COMMENT ON COLUMN dc_number_rule_detail.id IS '主键';
COMMENT ON COLUMN dc_number_rule_detail.header_id IS '头id，取自dc_number_rule表.id';
COMMENT ON COLUMN dc_number_rule_detail.type IS '类型，sequence：序列；constant：常量；date：日期；variable：变量';
COMMENT ON COLUMN dc_number_rule_detail.value IS '值。常量/变量名';
COMMENT ON COLUMN dc_number_rule_detail.order_seq IS '此规则在编号组成部分的排序号';
COMMENT ON COLUMN dc_number_rule_detail.start_value IS '起始值，type为序列时必配置';
COMMENT ON COLUMN dc_number_rule_detail.step IS '步长。type为序列时必配置';
COMMENT ON COLUMN dc_number_rule_detail.reset_type IS '序列重置类型。year：年重置；month：月重置；day：日重置，type为序列时必配置';
COMMENT ON COLUMN dc_number_rule_detail.reset_handler IS '重置处理器，填全限定类名，用于处理客制化重置逻辑，type为序列时可配置';
COMMENT ON COLUMN dc_number_rule_detail.last_reset_date IS '最近重置日期。type为序列时必配置，无则默认取号时的当前日期';
COMMENT ON COLUMN dc_number_rule_detail.max_length IS '序列最大长度，type为序列时必配置';
COMMENT ON COLUMN dc_number_rule_detail.formatter IS '日期格式化pattern，type为日期时必配置';
COMMENT ON COLUMN dc_number_rule_detail.is_enable IS '是否启用';