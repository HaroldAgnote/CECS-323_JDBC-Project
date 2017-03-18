# CECS 323 - JDBC Project
## Test Cases

Fill each [ ] with 'X' if test case was passed.

 * [X] Pass
 * [ ] Fail/Untested


### Listing Rows

 * [ ] All rows from a table are listed
 * [ ] Each row from the table can be selected/listed
 * [ ] All data from each row correctly matches data in Database
 * [ ] Can return to main menu after viewing data in rows
 

### Insert Book

 * [ ] Book's Writing Group cannot be blank (Test by just pressing Enter or putting spaces and then Enter)
 * [ ] Book's Writing Group cannot exceed 50 Characters
 * [ ] Book's Writing Group must exist in table WRITINGGROUP
 * [ ] Book's Title cannot be blank
 * [ ] Book's Title cannot exceed 50 characters
 * [ ] Book's Publisher cannot be blank
 * [ ] Book's Publisher cannot exceed 50 characters
 * [ ] Book's Publisher must exist in table PUBLISHERS
 * [ ] New Book must have UNIQUE Title-WritingGroup Pair
 * [ ] New Book must have UNIQUE Title-Publisher Pair
 * [ ] New Books must have been published at least after the 1900's
 * [ ] New Books cannot be published in the future
 * [ ] New Books must have at least 1 page
 * [ ] Make it optional to input 'year' and 'pages' attribute for new Books
 * [ ] If 'year' and 'pages' not inputted, verify that the attribute's value is 'NULL'
 * [ ] Verify that the new Book was added to database upon execution of SQL Statements
 
 
### Add Publisher

 * [ ] New Publisher's name cannot be blank
 * [ ] New Publisher's name cannot exceed 50 characters
 * [ ] New Publisher's name must not already exist in table PUBLISHERS
 * [ ] Make it optional to input address, phone, and email attributes
 * [ ] If either address, phone and email are not inputted, verify that they are 'NULL' in database
 * [ ] Address, phone and email cannot exceed 50, 20, 30 characters, respectively
 * [ ] Verify that the new Publisher was added to database upon execution of SQL Code
 * [ ] Publisher to be replaced must exist in table PUBLISHERS
 * [ ] Publisher to be replaced cannot be the same as the recently added Publisher
 * [ ] List all books that have been published by the Publisher to be replaced
 * [ ] Verify that the books have been updated such that they are published by the new Publisher
 
### Remove Book
 * [ ] Removed Book's title cannot be blank
 * [ ] Removed Book's title cannot exceed 50 characters
 * [ ] Removed Book must exist in table Books
 * [ ] Verify that the Book has been removed from the Database
 
 ### Miscellaneous
 * [ ] Verify that PreparedStatements are used instead of Statements
 * [ ] Verify that Binding has been used wherever possible
 * [ ] No exception occur due to bad User Input (i.e. inputting a number out of range, mismatch data type, etc.)
 * [ ] No SQL Exceptions occur in the most common cases