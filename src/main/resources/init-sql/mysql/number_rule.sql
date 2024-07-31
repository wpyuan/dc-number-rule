create table number_rule
(
    id          varchar(60)       not null comment '主键'
        primary key,
    code        varchar(100)      not null comment '编号',
    description varchar(500)      null comment '描述',
    is_enable   tinyint default 1 null comment '是否启用',
    remark      varchar(1000)     null comment '备注',
    constraint number_rule_u1
        unique (code)
)
    comment '编号生成规则';