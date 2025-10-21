create table DC_NUMBER_RULE
(
    ID          VARCHAR2(300) not null
        constraint DC_NUMBER_RULE_PK
        primary key,
    CODE        VARCHAR2(300),
    DESCRIPTION VARCHAR2(3000),
    IS_ENABLE   NUMBER(1) default 1,
    REMARK      CLOB
)
    /

comment on table DC_NUMBER_RULE is '编号生成规则'
/

comment on column DC_NUMBER_RULE.CODE is '编号'
/

comment on column DC_NUMBER_RULE.DESCRIPTION is '描述'
/

comment on column DC_NUMBER_RULE.IS_ENABLE is '是否启用'
/

comment on column DC_NUMBER_RULE.REMARK is '备注'
/

create unique index DC_NUMBER_RULE_CODE_UINDEX
    on DC_NUMBER_RULE (CODE)
/

