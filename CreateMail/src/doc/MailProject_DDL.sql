-- Project Name : noname
-- Date/Time    : 2025/03/27 14:42:57
-- Author       : y_hashimoto
-- RDBMS Type   : Oracle Database
-- Application  : A5:SQL Mk-2

/*
  << 注意！！ >>
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
  この機能は A5:SQL Mk-2でのみ有効であることに注意してください。
*/

-- recipient
-- * RestoreFromTempTable
create table recipient (
  id bigint auto_increment not null
  , email varchar(255)
  , message varchar(255)
  , subject varchar(255)
  , constraint recipient_PKC primary key (id)
) ;

comment on table recipient is 'recipient';
comment on column recipient.id is 'id';
comment on column recipient.email is 'email';
comment on column recipient.message is 'message';
comment on column recipient.subject is 'subject';

