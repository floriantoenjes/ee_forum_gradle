DROP TABLE message;
DROP TABLE post;
DROP TABLE thread;
DROP TABLE board;
DROP TABLE usr_role;
DROP TABLE usr;
DROP TABLE role;

CREATE TABLE role (
id NUMBER(19) PRIMARY KEY,
name VARCHAR2(40) NOT NULL UNIQUE
);
GRANT SELECT, INSERT, UPDATE, DELETE ON role TO forum_user;

CREATE TABLE usr (
id NUMBER(19) PRIMARY KEY,
email VARCHAR2(40) NOT NULL UNIQUE,
password VARCHAR2(10) NOT NULL CHECK(LENGTH(password) >= 6),
username VARCHAR2(15) NOT NULL UNIQUE,
avatar BLOB,
signature VARCHAR2(120)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON usr TO forum_user;

CREATE TABLE usr_role (
role_id NUMBER(19) NOT NULL,
usr_id NUMBER(19) NOT NULL,
CONSTRAINT pk_usr_role PRIMARY KEY (role_id, usr_id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON usr_role TO forum_user;

CREATE TABLE board (
id NUMBER(19) PRIMARY KEY,
name VARCHAR2(20) NOT NULL,
description VARCHAR2(120),
thread_count NUMBER(19)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON board TO forum_user;

CREATE TABLE thread (
id NUMBER(19) PRIMARY KEY,
name VARCHAR2(20) NOT NULL,
board_id NUMBER(19) NOT NULL,author_id NUMBER(19) NOT NULL,
created TIMESTAMP(3) NOT NULL,
updated TIMESTAMP(3),
board_one_to_one_id NUMBER(19),
post_count NUMBER(19) NOT NULL,
CONSTRAINT fk_board FOREIGN KEY (board_id) REFERENCES board(id),
CONSTRAINT fk_thread_author FOREIGN KEY (author_id) REFERENCES usr(id),
CONSTRAINT fk_board_one_to_one FOREIGN KEY (board_one_to_one_id) REFERENCES board(id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON thread TO forum_user;

CREATE TABLE post (
id NUMBER(19) PRIMARY KEY,
text VARCHAR2(1000) NOT NULL,
thread_id NUMBER(19) NOT NULL,
author_id NUMBER(19) NOT NULL,
created TIMESTAMP(3) NOT NULL,
thread_one_to_one_id NUMBER(19),
post_number NUMBER(19) NOT NULL,
deleted NUMBER(1) NOT NULL,
CONSTRAINT fk_thread FOREIGN KEY (thread_id) REFERENCES thread(id),
CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES usr(id),
CONSTRAINT fk_thread_one_to_one FOREIGN KEY (thread_one_to_one_id) REFERENCES thread(id)
);
GRANT SELECT, INSERT, UPDATE, DELETE ON post TO forum_user;

CREATE TABLE message (
id NUMBER(19) PRIMARY KEY,
subject VARCHAR2(40) NOT NULL,
text VARCHAR2(1000) NOT NULL,
sender_id NUMBER(19) NOT NULL,
receiver_id NUMBER(19) NOT NULL,
created TIMESTAMP(3) NOT NULL,
CONSTRAINT fk_sender_id FOREIGN KEY (sender_id) REFERENCES usr(id),
CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id) REFERENCES usr(id)
);



DROP SEQUENCE seq_role;
CREATE SEQUENCE seq_role MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_role TO forum_user;

DROP SEQUENCE seq_usr;
CREATE SEQUENCE seq_usr MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_usr TO forum_user;

DROP SEQUENCE seq_board;
CREATE SEQUENCE seq_board MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_board TO forum_user;

DROP SEQUENCE seq_thread;
CREATE SEQUENCE seq_thread MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_thread TO forum_user;

DROP SEQUENCE seq_post;
CREATE SEQUENCE seq_post MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_post TO forum_user;

DROP SEQUENCE seq_message;
CREATE SEQUENCE seq_message MINVALUE 1 MAXVALUE 1000 START WITH 1 INCREMENT BY 1;
GRANT ALL ON seq_message TO forum_user;


COMMIT;





