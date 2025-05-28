insert into number_rule(id, code, description)
values (uuid(), 'document_number.1.1', '文档编号1'),
       (uuid(), 'document_number.1.2', '文档编号2');
-- e6d6ac4a-4e44-11ef-8820-fefcfe22e104
-- e6d80296-4e44-11ef-8820-fefcfe22e104

insert into number_rule_detail
(id, header_id, type, value, order_seq, start_value,
 step,reset_type, reset_handler, last_reset_date, max_length)
values (uuid(), 'e6d6ac4a-4e44-11ef-8820-fefcfe22e104', 'constant', 'CHINESE-STYLE-', 1,
        null, null, null, null, null, null),
       (uuid(), 'e6d6ac4a-4e44-11ef-8820-fefcfe22e104', 'sequence', null, 2, 1, 1, '-', null, null, 2);

insert into number_rule_detail
(id, header_id, type, value, order_seq, start_value,
 step,reset_type, reset_handler, last_reset_date, max_length)
values (uuid(), 'e6d80296-4e44-11ef-8820-fefcfe22e104', 'constant', 'OUTSIDE-STYLE-', 1,
        null, null, null, null, null, null),
       (uuid(), 'e6d80296-4e44-11ef-8820-fefcfe22e104', 'sequence', null, 2, 1, 1, '-', null, null, 2);
