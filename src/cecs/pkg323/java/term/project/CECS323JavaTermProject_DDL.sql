CREATE TABLE WritingGroups (
  groupName VARCHAR(30) NOT NULL,
  headWriter VARCHAR(30),
  yearFormed INTEGER,
  subject VARCHAR(20),

  CONSTRAINT writingGroups_pk
    PRIMARY KEY (groupName)
);

CREATE TABLE publishers (
  publisherName VARCHAR(30) NOT NULL,
  publisherAddress VARCHAR(50),
  publisherPhone VARCHAR(20),
  publisherEmail VARCHAR(20),

  CONSTRAINT publishers_pk
  PRIMARY KEY (publisherName)
);

CREATE TABLE books (
  groupName VARCHAR(30) NOT NULL,
  bookTitle VARCHAR(30) NOT NULL,
  publisherName VARCHAR(30) NOT NULL,
  yearPublished INTEGER,
  numberOfPages INTEGER,

  CONSTRAINT books_pk
  PRIMARY KEY (groupName, bookTitle),

  CONSTRAINT books_uk01
  UNIQUE (bookTitle, publisherName),

  CONSTRAINT books_writingGroups_fk
  FOREIGN KEY (groupName)
    REFERENCES WritingGroups (groupName),

  CONSTRAINT books_publishers_fk
  FOREIGN KEY (publisherName)
    REFERENCES publishers (publisherName)
);