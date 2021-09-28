create table NUMBER_RULE
(
    ID          VARCHAR2(300) not null
        constraint NUMBER_RULE_PK
        primary key,
    CODE        VARCHAR2(300),
    DESCRIPTION VARCHAR2(3000),
    IS_ENABLE   NUMBER(1) default 1,
    REMARK      CLOB
)
    /

comment on table NUMBER_RULE is '编号生成规则'
/

comment on column NUMBER_RULE.CODE is '编号'
/

comment on column NUMBER_RULE.DESCRIPTION is '描述'
/

comment on column NUMBER_RULE.IS_ENABLE is '是否启用'
/

comment on column NUMBER_RULE.REMARK is '备注'
/

create unique index NUMBER_RULE_CODE_UINDEX
    on NUMBER_RULE (CODE)
/

