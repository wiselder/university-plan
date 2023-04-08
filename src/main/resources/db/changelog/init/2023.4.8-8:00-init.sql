--liquibase formatted sql
--changeset wiselder:init_db

CREATE TABLE AUDITORIUMS
(
    AUDITORIUM_ID INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ADDRESS       VARCHAR NOT NULL,
    NAME          VARCHAR NOT NULL,
    UNIQUE (ADDRESS, NAME)
);

CREATE TABLE BELLS
(
    ORDINAL_ID INT  NOT NULL,
    START      TIME NOT NULL,
    FINISH     TIME NOT NULL,

    PRIMARY KEY (ORDINAL_ID),
    UNIQUE (START),
    UNIQUE (FINISH),
    CHECK (START < FINISH)
);

CREATE TABLE DISCIPLINES
(
    DISCIPLINE_ID INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME          VARCHAR NOT NULL,
    UNIQUE (NAME)
);

CREATE TABLE TEACHERS
(
    TEACHER_ID INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    FULL_NAME  VARCHAR NOT NULL
);

CREATE TABLE FACULTIES
(
    FACULTY_ID INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME       VARCHAR NOT NULL,

    UNIQUE (NAME)
);

CREATE TABLE GROUPS
(
    GROUP_ID   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    FACULTY_ID INT NOT NULL,
    COURSE     INT NOT NULL,
    NUMBER     INT NOT NULL,
    SUB_NUMBER INT NOT NULL,

    UNIQUE (FACULTY_ID, COURSE, NUMBER, SUB_NUMBER),
    FOREIGN KEY (FACULTY_ID) REFERENCES FACULTIES (FACULTY_ID)
);

CREATE TABLE LESSONS
(
    LESSON_ID     INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TEACHER_ID    INT NOT NULL,
    AUDITORIUM_ID INT NOT NULL,
    DISCIPLINE_ID INT NOT NULL,
    BELL_ID       INT NOT NULL,
    WEEK_DAY      INT NOT NULL,
    WEEK_NUMBER   INT NOT NULL,

    UNIQUE (TEACHER_ID, BELL_ID, WEEK_DAY, WEEK_NUMBER),
    UNIQUE (AUDITORIUM_ID, BELL_ID, WEEK_DAY, WEEK_NUMBER),

    FOREIGN KEY (AUDITORIUM_ID) REFERENCES AUDITORIUMS (AUDITORIUM_ID),
    FOREIGN KEY (BELL_ID) REFERENCES BELLS (ORDINAL_ID),
    FOREIGN KEY (DISCIPLINE_ID) REFERENCES DISCIPLINES (DISCIPLINE_ID),
    FOREIGN KEY (TEACHER_ID) REFERENCES TEACHERS (TEACHER_ID),
    CHECK (WEEK_DAY > 0 AND WEEK_DAY < 8),
    CHECK (WEEK_NUMBER = 1 OR WEEK_NUMBER = 2)
);

CREATE TABLE GROUPLESSONS
(
    LESSON_ID INT NOT NULL,
    GROUP_ID  INT NOT NULL,

    PRIMARY KEY (LESSON_ID, GROUP_ID),
    FOREIGN KEY (GROUP_ID) REFERENCES GROUPS (GROUP_ID),
    FOREIGN KEY (LESSON_ID) REFERENCES LESSONS (LESSON_ID)
);