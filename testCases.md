# CECS 323 - JDBC Project
## Test Cases

Fill each [ ] with 'X' if test case was passed.

 * [X] Pass
 * [ ] Fail/Untested


### Listing Rows

 * [X] All rows from a table are listed
 * [X] Each row from the table can be selected/listed
 * [X] All data from each row correctly matches data in Database
 * [X] Can view multiple books with the same title separately with respective attribute information
 * [X] Can return to main menu after viewing data in rows
 

### Insert Book

 * [X] Book's Writing Group cannot be blank (Test by just pressing Enter or putting spaces and then Enter)
 * [X] Book's Writing Group cannot exceed 50 Characters
 * [X] Book's Writing Group must exist in table WRITINGGROUP
 * [X] Book's Title cannot be blank
 * [X] Book's Title cannot exceed 50 characters
 * [X] Book's Publisher cannot be blank
 * [X] Book's Publisher cannot exceed 50 characters
 * [X] Book's Publisher must exist in table PUBLISHERS
 * [X] New Book must have UNIQUE Title-WritingGroup Pair
 * [X] New Book must have UNIQUE Title-Publisher Pair
 * [X] New Books must have been published at least after the 1900's
 * [X] New Books cannot be published in the future
 * [X] New Books must have at least 1 page
 * [X] Make it optional to input 'year' and 'pages' attribute for new Books
 * [X] If 'year' and 'pages' not inputted, verify that the attribute's value is 'NULL'
 * [X] Verify that the new Book was added to database upon execution of SQL Statements
 
 
### Add Publisher

 * [X] New Publisher's name cannot be blank
 * [X] New Publisher's name cannot exceed 50 characters
 * [X] New Publisher's name must not already exist in table PUBLISHERS
 * [X] Make it optional to input address, phone, and email attributes
 * [X] If either address, phone and email are not inputted, verify that they are 'NULL' in database
 * [X] Address, phone and email cannot exceed 50, 20, 30 characters, respectively
 * [X] Verify that the new Publisher was added to database upon execution of SQL Code
 * [X] Publisher to be replaced must exist in table PUBLISHERS
 * [X] Publisher to be replaced cannot be the same as the recently added Publisher
 * [X] List all books that have been published by the Publisher to be replaced
 * [X] Verify that the books have been updated such that they are published by the new Publisher
 
### Remove Book
 * [X] Removed Book's title cannot be blank
 * [X] Removed Book's title cannot exceed 50 characters
 * [X] Removed Book must exist in table Books
 * [X] If there are multiple books with the same title, be able to specify exactly which book to remove
 * [X] Verify that the Book has been removed from the Database
 
 ### Miscellaneous
 * [X] Verify that PreparedStatements are used instead of Statements
 * [X] Verify that Binding has been used wherever possible
 * [X] No exception occur due to bad User Input (i.e. inputting a number out of range, mismatch data type, etc.)
 * [X] No SQL Exceptions occur in the most common cases


#### Last Tested: 3/20/2017 - 02:20 AM