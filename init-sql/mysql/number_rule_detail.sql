create table dc_number_rule_detail
(
    id              varchar(60)       not null comment '主键'
        primary key,
    header_id       varchar(60)       null comment '头id，取自dc_number_rule表.id',
    type            varchar(50)       null comment '类型，sequence：序列；constant：常量；date：日期；variable：变量',
    value           varchar(100)      null comment '值。常量/变量名',
    order_seq       smallint          null comment '此规则在编号组成部分的排序号',
    start_value     int               null comment '起始值，type为序列时必配置',
    step            int               null comment '步长。type为序列时必配置',
    reset_type      varchar(10)       null comment '序列重置类型。year：年重置；month：月重置；day：日重置，type为序列时必配置',
    reset_handler   varchar(100)      null comment '重置处理器，填全限定类名，用于处理客制化重置逻辑，type为序列时可配置',
    last_reset_date datetime          null comment '最近重置日期。type为序列时必配置，无则默认取号时的当前日期',
    max_length      int               null comment '序列最大长度，type为序列时必配置',
    formatter       varchar(50)       null comment '日期格式化pattern，type为日期时必配置',
    is_enable       tinyint default 1 null comment '是否启用'
)
    comment '编号规则明细';

