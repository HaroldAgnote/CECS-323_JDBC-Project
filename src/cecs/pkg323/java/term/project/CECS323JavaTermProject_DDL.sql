CREATE TABLE WritingGroups (
  groupName VARCHAR(50) NOT NULL,
  headWriter VARCHAR(50),
  yearFormed INTEGER,
  subject VARCHAR(30),

  CONSTRAINT writingGroups_pk
    PRIMARY KEY (groupName)
);

CREATE TABLE publishers (
  publisherName VARCHAR(50) NOT NULL,
  publisherAddress VARCHAR(50),
  publisherPhone VARCHAR(20),
  publisherEmail VARCHAR(30),

  CONSTRAINT publishers_pk
  PRIMARY KEY (publisherName)
);

CREATE TABLE books (
  groupName VARCHAR(50) NOT NULL,
  bookTitle VARCHAR(50) NOT NULL,
  publisherName VARCHAR(50) NOT NULL,
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

/* with only group Name, others null*/
insert into WritingGroups (groupName) values ('Public Writer Group');

/*yearFormed and subject being null*/
insert into WritingGroups(groupName, headWriter) values ('Johnson Writing Club','Jason Johnson');

/*subject being null*/
insert into WritingGroups(groupName, headWriter, yearFormed) values ('Deluxe Book Room','Oxford Nielson',1995);
/*headWriter and subject are null*/
insert into WritingGroups(groupName, yearFormed) values ('Bixby Writing Center',2005);
/*headWriter and yearFormed being null*/
insert into WritingGroups(groupName, subject) values ('Senior Writers', 'All Genres');


insert into publishers
(publisherName, publisherAddress,publisherPhone,publisherEmail ) values
('Scholastic','557 Broadway','646-666-5768','scholastic@gmail.com'),
('Aberdeen Bay','5109 Eaton Rapids Road','703-346-6547','aberdeenbay@gmail.com'), 
('Alfresco Press','444 Jade St.','555-673-6958','alfrescopress@tampabay.rr.com'),
('Broadway Books','1745 Broadway','212-782-9000','broadwaybooks@gmail.com'),
('Crown','1745 Broadway','212-666-53880','crown@gmail.com');

/* only publisher name is not null*/
insert into publishers (publisherName) values('JFK Publish Center');

/*phone, email null*/
insert into publishers (publisherName, publisherAddress) values ('ABC Books','232 Orange Ave');

/*address null*/
insert into publishers (publisherName, publisherPhone, publisherEmail) values ('Child Development Association','666-573-5555','childdev@yahoo.com');

/*address, email null*/
insert into publishers (publisherName, publisherPhone) values ('McGraw-Hill Education','800-666-8888');

/*phone address null*/
insert into publishers (publisherName, publisherEmail) values ('Cengage','cengage@gmail.com');



insert into books
(groupName, bookTitle,publisherName,yearPublished, numberOfPages ) values
('J.K. Rowling','Harry Potter and the Philosophers Stone','Scholastic',2013,336),
('J.K. Rowling','Fantastic Beast and Where to Find Them','Scholastic',2001,128),
('New Day Fiction','The Ruler of New World','Alfresco Press',2007,357),
('Coffee House','The Day I Met You','Aberdeen Bay',2017,666),
('New England Science Fiction Association, Inc.','Down the Robot Street','Aberdeen Bay',2001,673),
('Northwest Science Writers Association','To the Road of Mars','Broadway Books',2005,274),
('California Writers Club','The Truth Behind the Face','Crown',2011,174),
('Coffee House','How to Raise a Child','Scholastic',2014,999),
('Northwest Science Writers Association','Burning Sky','Scholastic',1999,244),
('New Day Fiction','The Girl Under the Bodhi Tree','Broadway Books',2015,365);

insert into books(groupName,bookTitle, publisherName) values ('Bixby Writing Center','The Devil From the Other Side of Mountain','ABC Books');

insert into books(groupName, bookTitle, publisherName, yearPublished) values ('Senior Writers','The World of Wonderland','Cengage',2007);

insert into books(groupName, bookTitle, publisherName, numberOfPages) values ('Senior Writers','Guide for Golf Beginners','ABC Books',2011);

