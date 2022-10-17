CREATE TABLE student (
	id		 SERIAL,
	name	 VARCHAR(255) NOT NULL,
	birthdate	 DATE NOT NULL,
	credits	 INTEGER,
	averagegrade INTEGER NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE professor (
	id	 SERIAL,
	name VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE student_professor (
	id	INTEGER,
	student_id	 INTEGER,
	professor_id INTEGER,
	--PRIMARY KEY(student_id,professor_id)
	PRIMARY KEY(id)
);

ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk1 FOREIGN KEY (student_id) REFERENCES student(id);
ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk2 FOREIGN KEY (professor_id) REFERENCES professor(id);

