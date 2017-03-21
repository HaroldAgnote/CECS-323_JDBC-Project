CREATE TABLE WritingGroups (
    groupName  VARCHAR(50) NOT NULL,
    headWriter VARCHAR(50),
    yearFormed INTEGER,
    subject    VARCHAR(30),

    CONSTRAINT writingGroups_pk
    PRIMARY KEY (groupName)
);

CREATE TABLE publishers (
    publisherName    VARCHAR(50) NOT NULL,
    publisherAddress VARCHAR(50),
    publisherPhone   VARCHAR(20),
    publisherEmail   VARCHAR(30),

    CONSTRAINT publishers_pk
    PRIMARY KEY (publisherName)
);

CREATE TABLE books (
    groupName     VARCHAR(50) NOT NULL,
    bookTitle     VARCHAR(50) NOT NULL,
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

INSERT INTO WritingGroups
(groupName, headWriter, yearFormed, subject)
VALUES
    ('J.K. Rowling', 'J.K. Rowling', 1997, 'Fiction'),
    ('New Day Fiction', 'John Edward', 1996, 'Adult Fiction'),
    ('Coffee House', 'Christine Bryant', 1999, 'Novel'),
    ('New England Science Fiction Association, Inc.', 'Mathew Wing', 2011,
     'Science Fiction'),
    ('Northwest Science Writers Association', 'Keith Seinfeld', 1967, 'Science'),
    ('California Writers Club', 'Jack London', 2000, 'All Genres');

/* with only group Name, others null*/
INSERT INTO WritingGroups (groupName)
VALUES ('Public Writer Group');

/*yearFormed and subject being null*/
INSERT INTO WritingGroups (groupName, headWriter)
VALUES ('Johnson Writing Club', 'Jason Johnson');

/*subject being null*/
INSERT INTO WritingGroups (groupName, headWriter, yearFormed)
VALUES ('Deluxe Book Room', 'Oxford Nielson', 1995);
/*headWriter and subject are null*/
INSERT INTO WritingGroups (groupName, yearFormed)
VALUES ('Bixby Writing Center', 2005);
/*headWriter and yearFormed being null*/
INSERT INTO WritingGroups (groupName, subject)
VALUES ('Senior Writers', 'All Genres');


INSERT INTO publishers
(publisherName, publisherAddress, publisherPhone, publisherEmail)
VALUES
    ('Scholastic', '557 Broadway', '646-666-5768', 'scholastic@gmail.com'),
    ('Aberdeen Bay', '5109 Eaton Rapids Road', '703-346-6547',
     'aberdeenbay@gmail.com'),
    ('Alfresco Press', '444 Jade St.', '555-673-6958',
     'alfrescopress@tampabay.rr.com'),
    ('Broadway Books', '1745 Broadway', '212-782-9000', 'broadwaybooks@gmail.com'),
    ('Crown', '1745 Broadway', '212-666-5388', 'crown@gmail.com');

/* only publisher name is not null*/
INSERT INTO publishers (publisherName)
VALUES ('JFK Publish Center');

/*phone, email null*/
INSERT INTO publishers (publisherName, publisherAddress)
VALUES ('ABC Books', '232 Orange Ave');

/*address null*/
INSERT INTO publishers (publisherName, publisherPhone, publisherEmail)
VALUES ('Child Development Association', '666-573-5555', 'childdev@yahoo.com');

/*address, email null*/
INSERT INTO publishers (publisherName, publisherPhone)
VALUES ('McGraw-Hill Education', '800-666-8888');

/*phone address null*/
INSERT INTO publishers (publisherName, publisherEmail)
VALUES ('Cengage', 'cengage@gmail.com');


INSERT INTO books
(groupName, bookTitle, publisherName, yearPublished, numberOfPages)
VALUES
    ('J.K. Rowling', 'Harry Potter and the Philosophers Stone', 'Scholastic', 2013,
     336),
    ('J.K. Rowling', 'Fantastic Beast and Where to Find Them', 'Scholastic', 2001,
     128),
    ('New Day Fiction', 'The Ruler of New World', 'Alfresco Press', 2007, 357),
    ('Coffee House', 'The Day I Met You', 'Aberdeen Bay', 2017, 666),
    ('New England Science Fiction Association, Inc.', 'Down the Robot Street',
     'Aberdeen Bay', 2001, 673),
    ('Northwest Science Writers Association', 'To the Road of Mars',
     'Broadway Books', 2005, 274),
    ('California Writers Club', 'The Truth Behind the Face', 'Crown', 2011, 174),
    ('Coffee House', 'How to Raise a Child', 'Scholastic', 2014, 999),
    ('Northwest Science Writers Association', 'Burning Sky', 'Scholastic', 1999,
     244),
    ('New Day Fiction', 'The Girl Under the Bodhi Tree', 'Broadway Books', 2015,
     365);

INSERT INTO books (groupName, bookTitle, publisherName)
VALUES
    ('Bixby Writing Center', 'The Devil From the Other Side of Mountain',
     'ABC Books');

INSERT INTO books (groupName, bookTitle, publisherName, yearPublished)
VALUES ('Senior Writers', 'The World of Wonderland', 'Cengage', 2007);

INSERT INTO books (groupName, bookTitle, publisherName, numberOfPages)
VALUES ('Senior Writers', 'Guide for Golf Beginners', 'ABC Books', 2011);

INSERT INTO books (groupName, bookTitle, publisherName, yearPublished, numberOfPages)
VALUES
    ('Coffee House', 'Alex Rider', 'Crown', 2008, 120),
    ('New Day Fiction', 'Alex Rider', 'ABC Books', NULL, 232),
    ('California Writers Club', 'The Martian', 'Scholastic', 2014, 999),
    ('New Day Fiction', 'The Martian', 'Crown', 2001, NULL),
    ('J.K. Rowling', 'The Martian', 'ABC Books', NULL, 333),
    ('Coffee House', 'Web Design for Beginners', 'Scholastic', 2001, 120),
    ('Northwest Science Writers Association', 'Web Design for Beginners', 'Crown',
     2008, 200),
    ('J.K. Rowling', 'Web Design for Beginners', 'ABC Books', 2013, 250),
    ('Senior Writers', 'Web Design for Beginners', 'Broadway Books', 2017, 300);
