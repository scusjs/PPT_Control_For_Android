CREATE DATABASE MySony;

DROP TABLE IF EXISTS ppt_edit;
CREATE TABLE ppt_edit(
	PPT_index INT PRIMARY KEY,
	PPT_speechTime CHAR(10) NOT NULL DEFAULT '2:00',
	PPT_comment CHAR(200)
);

INSERT INTO ppt_edit VALUES(1,"2:00","≤‚ ‘1");
INSERT INTO ppt_edit VALUES(2,"2:00","≤‚ ‘2");
INSERT INTO ppt_edit VALUES(3,"2:00","≤‚ ‘3");
INSERT INTO ppt_edit VALUES(4,"2:00","≤‚ ‘4");
INSERT INTO ppt_edit VALUES(5,"2:00","≤‚ ‘5");