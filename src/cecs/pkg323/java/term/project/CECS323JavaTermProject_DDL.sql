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

insert into WritingGroups
(groupName, headWriter,yearFormed,subject ) values
('J.K. Rowling','J.K. Rowling',1997,'Fiction'),
('New Day Fiction','John Edward',1996,'Adult Fiction'),
('Coffee House','Christine Bryant',1999,'Novel'),
('New England Science Fiction Association, Inc.','Mathew Wing',2011,'Science Fiction'),
('Northwest Science Writers Association','Keith Seinfeld',1967,'Science'),
('California Writers Club','Jack London',2000,'All Genres');

insert into publishers
(publisherName, publisherAddress,publisherPhone,publisherEmail ) values
('Scholastic','557 Broadway','646-666-5768','scholastic@gmail.com'),
('Aberdeen Bay','5109 Eaton Rapids Road','703-346-6547','aberdeenbay@gmail.com'), 
('Alfresco Press','444 Jade St.','555-673-6958','alfrescopress@tampabay.rr.com'),
('Broadway Books','1745 Broadway','212-782-9000','broadwaybooks@gmail.com'),
('Crown','1745 Broadway','212-666-53880','crown@gmail.com');

insert into books
(groupName, bookTitle,publisherName,yearPublished, numberOfPages ) values
('J.K. Rowling','Harry Potter and the Philosophers Stone','Scholastic',2013,336),
('J.K. Rowling','Fantastic Beast and Where to Find Them','Scholastic',2001,128),
('New Day Friction','The Ruler of New World','Alfresco Press',2007,357),
('Coffee House','The Day I Met You','Aberdeen Bay',2017,666),
('New England Scien Fiction Association, Inc.','Down the Robot Street','Aberdeen Bay',2001,673),
('Northwest Science Writers Association','To the Road of Mars','Broadway Books',2005,274),
('California Writers Club','The Truth Behind the Face','Crown',2011,174),
('Coffee House','How to Raise a Child','Scholastic',2014,999),
('Northwest Science Writers Association','Burning Sky','Scholastic',1999,244),
('New Day Fiction','The Girl Under the Bodhi Tree','Broadway Books',2015,365);