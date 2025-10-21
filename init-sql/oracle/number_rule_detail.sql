create table DC_NUMBER_RULE_DETAIL
(
    ID              VARCHAR2(300) not null
        constraint DC_NUMBER_RULE_DETAIL_PK
        primary key,
    HEADER_ID       VARCHAR2(300),
    TYPE            VARCHAR2(300),
    VALUE           VARCHAR2(300),
    ORDER_SEQ       NUMBER(22),
    START_VALUE     NUMBER(22),
    STEP            NUMBER,
    RESET_TYPE      VARCHAR2(300),
    RESET_HANDLER   VARCHAR2(300),
    LAST_RESET_DATE DATE,
    MAX_LENGTH      NUMBER(22)    default null,
    FORMATTER       VARCHAR2(300) default null,
    IS_ENABLE       NUMBER(1)     default 1
)
    /

comment on table DC_NUMBER_RULE_DETAIL is '编号规则明细'
/

comment on column DC_NUMBER_RULE_DETAIL.HEADER_ID is '头ID，取自DC_NUMBER_RULE表.ID'
/

comment on column DC_NUMBER_RULE_DETAIL.TYPE is '类型，有sequence：序列；constant：常量；date：日期；variable：变量；'
/

comment on column DC_NUMBER_RULE_DETAIL.VALUE is '值，有常量的值；变量名'
/

comment on column DC_NUMBER_RULE_DETAIL.ORDER_SEQ is '此规则在编号组成部分的排序号'
/

comment on column DC_NUMBER_RULE_DETAIL.START_VALUE is '起始值，type为序列时必配置'
/

comment on column DC_NUMBER_RULE_DETAIL.STEP is '步长，type为序列时必配置'
/

comment on column DC_NUMBER_RULE_DETAIL.RESET_TYPE is '序列重置类型，有year：年重置；month：月重置；day：日重置，type为序列时必配置'
/

comment on column DC_NUMBER_RULE_DETAIL.RESET_HANDLER is '重置处理器，填全限定类名，用于处理客制化重置逻辑，type为序列时可配置'
/

comment on column DC_NUMBER_RULE_DETAIL.LAST_RESET_DATE is '上次重置日期，type为序列时必配置，无则默认取号时的当前日期'
/

comment on column DC_NUMBER_RULE_DETAIL.MAX_LENGTH is '序列最大长度，type为序列时必配置'
/

comment on column DC_NUMBER_RULE_DETAIL.FORMATTER is '日期格式化pattern，type为日期时必配置'
/

comment on column DC_NUMBER_RULE_DETAIL.IS_ENABLE is '是否启用'
/

create unique index DC_NUMBER_RULE_DETAIL_ORDER_SEQ_U
    on DC_NUMBER_RULE_DETAIL (HEADER_ID, ORDER_SEQ)
/

