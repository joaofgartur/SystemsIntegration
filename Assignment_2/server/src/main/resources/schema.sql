DROP TABLE IF EXISTS student_professor;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS professor;

CREATE TABLE student (
	id		 SERIAL,
	name	 VARCHAR(255) NOT NULL,
	birthdate	 VARCHAR(255) NOT NULL,
	credits	 INTEGER,
	average_grade INTEGER NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE professor (
	id	 SERIAL,
	name VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE student_professor (
	id	SERIAL,
	student_id	 BIGINT,
	professor_id BIGINT,
	--PRIMARY KEY(student_id,professor_id)
	PRIMARY KEY(id)
);

ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk1 FOREIGN KEY (student_id) REFERENCES student(id);
ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk2 FOREIGN KEY (professor_id) REFERENCES professor(id);

-------- DUMMY DATA -------
-- Students
insert into student (name, birthdate, credits, average_grade) values ('Lucille', '04/12/2000', 103, 14);
insert into student (name, birthdate, credits, average_grade) values ('Harrison', '11/10/2003', 4, 15);
insert into student (name, birthdate, credits, average_grade) values ('Carl', '02/10/2004', 119, 20);
insert into student (name, birthdate, credits, average_grade) values ('Bryon', '07/08/1998', 86, 15);
insert into student (name, birthdate, credits, average_grade) values ('Angus', '08/09/2002', 160, 17);
insert into student (name, birthdate, credits, average_grade) values ('Berti', '04/11/1999', 107, 17);
insert into student (name, birthdate, credits, average_grade) values ('Faber', '03/02/1998', 169, 13);
insert into student (name, birthdate, credits, average_grade) values ('Hersch', '10/04/2003', 23, 18);
insert into student (name, birthdate, credits, average_grade) values ('Reggis', '07/12/2000', 146, 12);
insert into student (name, birthdate, credits, average_grade) values ('Kimberley', '01/03/2003', 60, 9);
insert into student (name, birthdate, credits, average_grade) values ('John', '01/03/2003', 180, 18);
insert into student (name, birthdate, credits, average_grade) values ('Jake', '01/03/2003', 180, 10);
insert into student (name, birthdate, credits, average_grade) values ('John', '01/03/2003', 180, 18);
insert into student (name, birthdate, credits, average_grade) values ('Jake', '01/03/2003', 180, 10);
insert into student (name, birthdate, credits, average_grade) values ('Oliver', '01/03/2003', 180, 18);
insert into student (name, birthdate, credits, average_grade) values ('Olivia', '01/03/2003', 180, 10);

-- Professors
insert into professor (name) values ('Ruthe');
insert into professor (name) values ('Salmon');
insert into professor (name) values ('Land');

-- Relation
insert into student_professor (student_id, professor_id) values (1,1);
insert into student_professor (student_id, professor_id) values (1,3);
insert into student_professor (student_id, professor_id) values (2,1);
insert into student_professor (student_id, professor_id) values (3,1);
insert into student_professor (student_id, professor_id) values (4,1);
insert into student_professor (student_id, professor_id) values (5,2);
insert into student_professor (student_id, professor_id) values (6,2);
insert into student_professor (student_id, professor_id) values (7,2);
insert into student_professor (student_id, professor_id) values (8,3);
insert into student_professor (student_id, professor_id) values (9,3);
