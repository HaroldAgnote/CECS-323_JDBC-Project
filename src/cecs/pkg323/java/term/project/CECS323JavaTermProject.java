package cecs.pkg323.java.term.project;

import java.sql.*;
import java.util.*;

/**
 * Harold Agnote
 * Xinyi Chen
 * <p>
 * Professor David Brown
 * <p>
 * CECS 323 - Sec. 07
 * <p>
 * 3/20/2017
 * <p>
 * CECS 323 - JDBC Project
 */
public class CECS323JavaTermProject
{
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    
    public static void main( String[] args )
    {
        login();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user=" + USER + ";password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try
        {
            //STEP 2: Register JDBC driver
            Class.forName( JDBC_DRIVER );
            
            //STEP 3: Open a connection
            System.out.println( "Connecting to database..." );
            conn = DriverManager.getConnection( DB_URL );
            
            boolean done = false;
            
            do
            {
                int menu = displayMainMenu();
                if ( menu != 7 )
                {
                    switch ( menu )
                    {
                        case 1:
                            displayWritingGroups( conn );
                            break;
                        case 2:
                            displayPublishers( conn );
                            break;
                        case 3:
                            displayBook( conn );
                            break;
                        case 4:
                            insertBook( conn );
                            break;
                        case 5:
                            insertPublisher( conn );
                            break;
                        case 6:
                            removeBook( conn );
                            break;
                        default:
                            done = true;
                            break;
                    }
                }
            }
            while ( !done );
        }
        catch ( SQLException se )
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch ( Exception e )
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        finally
        {
            //finally block used to close resources
            try
            {
                if ( stmt != null )
                {
                    stmt.close();
                }
            }
            catch ( SQLException se2 )
            {
            }// nothing we can do
            try
            {
                if ( conn != null )
                {
                    conn.close();
                }
            }
            catch ( SQLException se )
            {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println( "Goodbye!" );
    }//end main
    
    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     *
     * @param input
     *         The string to be mapped.
     *
     * @return Either the input string or "N/A" as appropriate.
     */
    public static String dispNull( String input )
    {
        //because of short circuiting, if it's null, it never checks the length.
        if ( input == null || input.length() == 0 )
        {
            return "N/A";
        }
        try
        {
            if ( Integer.parseInt( input ) == -1 )
            {
                return "N/A";
            }
        }
        catch ( NumberFormatException nfe )
        {
            
        }
        return input;
    }
    
    
    public static void login()
    {
        System.out.println( "Select Login Credentials: \n" );
        System.out.println( "1. Harold Agnote" );
        System.out.println( "2. Xinyi Chen\n" );
        System.out.println( "3. Manual Login" );
        
        int choice = UserInput.getInt( 1, 3 );
        
        if ( choice == 1 )
        {
            USER = "admin";
            PASS = "test";
            DBNAME = "Term_Project";
        }
        else if ( choice == 2 )
        {
            USER = "ad";
            PASS = "password";
            DBNAME = "project1";
        }
        else
        {
            /*
             * Prompt the user for the database name, and the credentials.
             * If your database has no credentials, you can update this code to
             * remove that from the connection string.
             */
    
            System.out.print( "Name of the database: " );
            DBNAME = UserInput.getInputLine();
            System.out.print( "Database user name: " );
            USER = UserInput.getInputLine();
            System.out.print( "Database password: " );
            PASS = UserInput.getInputLine();
        }
        
    }
    
    public static int displayMainMenu()
    {
        System.out.println( "\nWhat would you like to do?\n" );
        System.out.println( "1. List Writing Groups" );
        System.out.println( "2. List Publishers" );
        System.out.println( "3. List Books\n" );
        System.out.println( "4. Add new Book" );
        System.out.println( "5. Insert a New Publisher\n" );
        System.out.println( "6. Remove a Book\n" );
        System.out.println( "7. Quit\n" );
        return UserInput.getInt( 1, 7 );
    }
    
    public static void displayWritingGroups( Connection conn ) throws SQLException
    {
        String table = "WRITINGGROUPS";
        String mainCol = "GROUPNAME";
        ResultSet rs = displayInformation( table, mainCol, conn );
        displayMore( table, mainCol, rs, conn );
    }
    
    public static void displayPublishers( Connection conn ) throws SQLException
    {
        String table = "PUBLISHERS";
        String mainCol = "PUBLISHERNAME";
        ResultSet rs = displayInformation( table, mainCol, conn );
        displayMore( table, mainCol, rs, conn );
    }
    
    public static void displayBook( Connection conn ) throws SQLException
    {
        String table = "BOOKS";
        String mainCol = "BOOKTITLE";
        ResultSet rs = displayInformation( table, mainCol, conn );
        displayMore( table, mainCol, rs, conn );
    }
    
    public static ResultSet displayInformation( String table, String mainCol, Connection conn ) throws SQLException
    {
        String sql = "SELECT " + mainCol + " FROM " + table;
        sql += "\nORDER BY " + mainCol;
        PreparedStatement stmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE );
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int mainColLength = metaData.getColumnDisplaySize( rs.findColumn( mainCol ) );
        System.out.println( String.format( "%-" + mainColLength + "s\n", mainCol ) );
        int i = 1;
        while ( rs.next() )
        {
            String info = rs.getString( mainCol );
            System.out.println( i + ". " + info );
            i++;
            System.out.println();
        }
        rs.beforeFirst();
        return rs;
    }
    
    public static void displayMore( String table, String mainCol, ResultSet rs, Connection conn ) throws SQLException
    {
        ArrayList < String > information = new ArrayList <>();
        String sql = "SELECT * FROM " + table;
        sql += "\nWHERE " + mainCol + " = ?";
        PreparedStatement stmt = conn.prepareStatement( sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE );
        int i = 1;
        while ( rs.next() )
        {
            String info = rs.getString( mainCol );
            information.add( info );
            i++;
        }
        int choice = 0;
        do
        {
            System.out.println( "Input 0 to go back to main menu" );
            System.out.print( "Select an entry from " + table + " to view more information about: " );
            choice = UserInput.getInt( 0, i - 1 );
            if ( choice > 0 )
            {
                String info = information.get( choice - 1 );
                stmt.setString( 1, info );
                rs = stmt.executeQuery();
                ResultSetMetaData data = rs.getMetaData();
                int numColumns = data.getColumnCount();
                int[] colLength = new int[ numColumns ];
                String[] columns = new String[ numColumns ];
                String[] rowData = new String[ columns.length ];
                rs.next();
                System.out.println();
                for ( int j = 0; j < columns.length; j++ )
                {
                    columns[ j ] = data.getColumnName( j + 1 );
                    colLength[ j ] = data.getColumnDisplaySize( rs.findColumn( data.getColumnName( j + 1 ) ) ) + 5;
                    rowData[ j ] = rs.getString( columns[ j ] );
                }
                displayFormattedArray( columns, colLength );
                System.out.println( "\n" );
                displayFormattedArray( rowData, colLength );
                System.out.println( "\n" );
                rs.beforeFirst();
            }
        }
        while ( choice > 0 );
        
    }
    
    
    /*
     * Planning on just checking if a Publisher/Writing Group exists in the table
     * If so, proceed with add, otherwise prompt user to select a valid
     */
    public static void insertBook( Connection conn ) throws SQLException
    {
        PreparedStatement pstmt = null;
        String insertSql = "INSERT INTO BOOKS(groupName, bookTitle,publisherName,yearPublished, numberOfPages)\n";
        insertSql += "VALUES(?,?,?,?,?)";
        pstmt = conn.prepareStatement( insertSql );
        
        HashSet < String > publisher_Book = getPublisherBookPair( conn );
        HashSet < String > group_Book = getGroupNameBookPair( conn );
    
    
        String bookTitle;
        String groupName;
        String publisherName;
        int year;
        int pages;
    
        boolean edit = true;
    
        do
        {
            bookTitle = setBookTitle();
            groupName = setWritingGroup( conn );
            publisherName = setPublisher( conn );
            year = setYear();
            pages = setPages();
    
    
            String publisher_Book_Pair = publisherName + "_" + bookTitle;
            String group_Book_Pair = groupName + "_" + bookTitle;
    
            while ( publisher_Book.contains( publisher_Book_Pair ) )
            {
                System.out.println( "The database already contains an entry with: " );
                System.out.println( "Book Title: " + bookTitle );
                System.out.println( "Publisher: " + publisherName );
                System.out.println( "Please change either attribute to proceed with adding this Book" );
                bookTitle = setBookTitle();
                publisherName = setPublisher( conn );
                publisher_Book_Pair = publisherName + "_" + bookTitle;
            }
    
            while ( group_Book.contains( group_Book_Pair ) )
            {
                System.out.println( "The database already contains an entry with: " );
                System.out.println( "Book Title: " + bookTitle );
                System.out.println( "Writing Group: " + groupName );
                System.out.println( "Please change either attribute to proceed with adding this Book" );
                bookTitle = setBookTitle();
                groupName = setWritingGroup( conn );
                group_Book_Pair = groupName + "_" + bookTitle;
            }
    
            String displayFormat = "%-20s: %-20s\n";
            System.out.println( "Here's the information you want to put in: " );
            System.out.printf( displayFormat, "Group Name", groupName );
            System.out.printf( displayFormat, "Book Title", bookTitle );
            System.out.printf( displayFormat, "Publisher", publisherName );
            System.out.printf( displayFormat, "Year Published", dispNull( Integer.toString( year ) ) );
            System.out.printf( displayFormat, "Number of Pages", dispNull( Integer.toString( pages ) ) );
    
            System.out.println( "Does this look correct? (Y/N)" );
    
            boolean yesNo = UserInput.getYesNo();
    
            edit = !yesNo;
        }
        while ( edit );
    
        System.out.println( "Would you like to add " + bookTitle + " to the Database? (Y/N)" );
        boolean yesNo = UserInput.getYesNo();
    
        if ( yesNo )
        {
            pstmt.setString( 1, groupName );
            pstmt.setString( 2, bookTitle );
            pstmt.setString( 3, publisherName );
    
            if ( year == -1 )
            {
                pstmt.setString( 4, null );
            }
            else
            {
                pstmt.setString( 4, Integer.toString( year ) );
            }
    
            if ( pages == -1 )
            {
                pstmt.setString( 5, null );
            }
            else
            {
                pstmt.setString( 5, Integer.toString( pages ) );
            }
            pstmt.execute();
        }
    }
    
    public static void insertPublisher( Connection conn ) throws SQLException
    {
        String insertSql = "INSERT INTO PUBLISHERS (publisherName, publisherAddress,publisherPhone,publisherEmail)\n";
        insertSql += "VALUES(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement( insertSql );
        
        String viewSql = "SELECT BOOKTITLE FROM BOOKS\n";
        viewSql += "WHERE PUBLISHERNAME = ?";
        
        String updateSql = "UPDATE Books SET books.publishername = ? \n";
        updateSql += "WHERE BOOKS.PUBLISHERNAME = ?";
        
        ArrayList < String > matchingBooks = new ArrayList <>();
        HashSet < String > publishers = getPublishers( conn );
        
        String publisherName;
        String publisherAddress;
        String publisherPhone;
        String publisherEmail;
        String oldName;
        
        boolean edit = true;
        
        do
        {
            do
            {
                System.out.print( "Publisher Name: " );
                publisherName = UserInput.getInputLine();
                if ( publisherName.trim().isEmpty() )
                {
                    System.out.println( "Publisher Name cannot be empty" );
                }
                else if ( publisherName.length() > 50 )
                {
                    System.out.println( "Publisher Name is too long, please try again" );
                }
                else if ( publishers.contains( publisherName ) )
                {
                    System.out.println( "The database already contains an entry with: " );
                    System.out.println( "PublisherName: " + publisherName );
                    System.out.println( "Please enter new PublisherName" );
                }
                else
                {
                    break;
                }
            }
            while ( true );
            
            do
            {
                System.out.println( "(Press Enter to Skip)" );
                System.out.print( "Publisher Address: " );
                publisherAddress = UserInput.getInputLine();
                if ( publisherAddress.length() > 50 )
                {
                    System.out.println( "Publisher address is too long, try again" );
                }
                else
                {
                    break;
                }
                
            }
            while ( true );
            
            do
            {
                System.out.println( "(Press Enter to Skip)" );
                System.out.print( "Publisher's Phone number: " );
                publisherPhone = UserInput.getInputLine();
                if ( publisherPhone.length() > 20 )
                {
                    System.out.println( "Phone Number too long, try again" );
                }
                else
                {
                    break;
                }
                
            }
            while ( true );
            
            do
            {
                System.out.println( "(Press Enter to Skip)" );
                System.out.print( "Publisher Email: " );
                publisherEmail = UserInput.getInputLine();
                
                if ( publisherEmail.length() > 30 )
                {
                    System.out.println( "Publisher email is too long, try again" );
                }
                else
                {
                    break;
                }
            }
            while ( true );
            
            String displayFormat = "%-20s : %-50s\n";
            System.out.println( "Here's the information for the Publisher to be added: \n" );
            System.out.printf( displayFormat, "Publisher Name", publisherName );
            System.out.printf( displayFormat, "Publisher Address", dispNull( publisherAddress ) );
            System.out.printf( displayFormat, "Publisher Phone", dispNull( publisherPhone ) );
            System.out.printf( displayFormat, "Publisher Email", dispNull( publisherEmail ) );
            
            System.out.println( "Is this information correct? (Y/N)" );
            
            edit = !UserInput.getYesNo();
        }
        while ( edit );
        
        System.out.println( "Would you like to add this Publisher? (Y/N)" );
        
        boolean yesNo = UserInput.getYesNo();
        
        if ( yesNo )
        {
            if ( publisherAddress.trim().isEmpty() )
            {
                publisherAddress = null;
            }
            if ( publisherPhone.trim().isEmpty() )
            {
                publisherPhone = null;
            }
            if ( publisherEmail.trim().isEmpty() )
            {
                publisherEmail = null;
            }
            pstmt.setString( 1, publisherName );
            pstmt.setString( 2, publisherAddress );
            pstmt.setString( 3, publisherPhone );
            pstmt.setString( 4, publisherEmail );
            pstmt.executeUpdate();
            
            do
            {
                System.out.println( "Please enter the old publisher's name that you wish to update with" );
                oldName = UserInput.getInputLine();
                if ( !publishers.contains( oldName ) )
                {
                    System.out.println( "The database does not contains an entry with: " );
                    System.out.println( "PublisherName: " + oldName );
                    System.out.println( "Please enter existing publishers name" );
                    displayInformation( "PUBLISHERS", "PUBLISHERNAME", conn );
                }
                else if ( oldName.equals( publisherName ) )
                {
                    System.out.println( "You can't use the recently added Publisher" );
                }
                else
                {
                    break;
                }
            }
            while ( true );
            
            pstmt = conn.prepareStatement( viewSql );
            pstmt.setString( 1, oldName );
            ResultSet rs = pstmt.executeQuery();
            
            while ( rs.next() )
            {
                matchingBooks.add( rs.getString( "BOOKTITLE" ) );
            }
            
            System.out.println( "Here are the books that are published by: " + oldName );
            for ( int i = 0; i < matchingBooks.size(); i++ )
            {
                System.out.println( ( i + 1 ) + ". " + matchingBooks.get( i ) );
            }
            System.out.println( "\nThey will be updated such they are published by: " + publisherName );
            
            pstmt = conn.prepareStatement( updateSql );
            pstmt.setString( 1, publisherName );
            pstmt.setString( 2, oldName );
            pstmt.executeUpdate();
        }
    }
    
    public static void removeBook( Connection conn ) throws SQLException
    {
        PreparedStatement pstmt = null;
        String viewBookSql = "SELECT * FROM BOOKS WHERE BOOKS.BOOKTITLE = ?";
        String deleteBookSql = "DELETE FROM BOOKS WHERE BOOKS.BOOKTITLE = ?";
        pstmt = conn.prepareStatement( viewBookSql );
        HashSet < String > books = getBooks( conn );
    
        boolean valid = false;
    
        String bookTitle;
        String groupName = "";
        String publisherName = "";
        String yearPublished = "";
        String numberOfPages = "";
        
        do
        {
            System.out.println( "Enter Nothing to cancel Operation" );
            System.out.print( "Book Title: " );
            bookTitle = UserInput.getInputLine();
            if(bookTitle.length()>50)
            {
                System.out.println("Book Title is too long Try again");
            }
            else
            {
                if ( !books.contains( bookTitle ) && !bookTitle.trim().isEmpty() )
                {
                    System.out.println( bookTitle + " does not exist in Books" ); 
                    System.out.println( "Please enter an existing books from the list:\n" );
                    displayInformation( "BOOKS", "BOOKTITLE", conn );
                }
                else 
                {
                    valid = true;
                }
            }
            
        }
        while ( !valid );
        
        if ( !bookTitle.trim().isEmpty() )
        {
            pstmt.setString( 1, bookTitle );
            ResultSet rs = pstmt.executeQuery();
            
            while ( rs.next() )
            {
                groupName = rs.getString( "GROUPNAME" );
                publisherName = rs.getString( "PUBLISHERNAME" );
                yearPublished = rs.getString( "YEARPUBLISHED" );
                numberOfPages = rs.getString( "NUMBEROFPAGES" );
            }
            
            String displayFormat = "%-20s: %-20s\n";
            System.out.println( "This is the Book you want to remove:\n" );
            System.out.printf( displayFormat, "Group Name", groupName );
            System.out.printf( displayFormat, "Book Title", bookTitle );
            System.out.printf( displayFormat, "Publisher", publisherName );
            System.out.printf( displayFormat, "Year Published", dispNull( yearPublished ) );
            System.out.printf( displayFormat, "Number of Pages", dispNull( numberOfPages ) );
            
            System.out.println( "Do you want to remove this Book? (Y/N)" );
            
            boolean yesNo = UserInput.getYesNo();
            
            if ( yesNo )
            {
                pstmt = conn.prepareStatement( deleteBookSql );
                pstmt.setString( 1, bookTitle );
                pstmt.execute();
            }
        }
    }
    
    public static String setBookTitle()
    {
        String bookTitle;
        boolean valid = false;
        do
        {
            System.out.print( "Book Title: " );
            bookTitle = UserInput.getInputLine();
            if ( bookTitle.trim().isEmpty() )
            {
                System.out.println( "Title cannot be Empty" );
            }
            else if ( bookTitle.length() > 50 )
            {
                System.out.println( "Title is too long" );
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        return bookTitle;
    }
    
    public static String setWritingGroup( Connection conn ) throws SQLException
    {
        HashSet < String > writingGroups = getWritingGroups( conn );
        boolean valid = false;
        String groupName;
        do
        {
            System.out.print( "Writing Group Name: " );
            groupName = UserInput.getInputLine();
            if ( groupName.length() > 50 )
            {
                System.out.println( "Group Name is too long, please enter less than 50 characters" );
                groupName = UserInput.getInputLine();
            }
            if ( groupName.trim().isEmpty() )
            {
                System.out.println( "Writing Group Name cannot be empty" );
            }
            else if ( !writingGroups.contains( groupName ) )
            {
                System.out.println( groupName + " does not exist in WRITINGGROUPS" );
                System.out.println( "Please enter an existing Writing Group from the list:" );
                displayInformation( "WRITINGGROUPS", "GROUPNAME", conn );
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        return groupName;
    }
    
    public static String setPublisher( Connection conn ) throws SQLException
    {
        HashSet < String > publishers = getPublishers( conn );
        boolean valid = false;
        String publisherName;
    
        do
        {
            System.out.println( "Please enter publisher name of the book" );
            publisherName = UserInput.getInputLine();
            if ( publisherName.length() > 50 )
            {
                System.out.println( "Group Name is too long, please enter less than 50 characters" );
                publisherName = UserInput.getInputLine();
            }
            if ( publisherName.trim().isEmpty() )
            {
                System.out.println( "Publisher Name can't be empty" );
            }
            else if ( !publishers.contains( publisherName ) )
            {
                System.out.println( publisherName + " does not exist in PUBLISHERS" );
                System.out.println( "Please enter an existing Publisher from the list" );
                displayInformation( "PUBLISHERS", "PUBLISHERNAME", conn );
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        
        return publisherName;
    }
    
    public static int setYear()
    {
        final int currentYear = ( Calendar.getInstance() ).get( Calendar.YEAR );
        boolean valid = false;
        int year = 0;
        
        do
        {
            System.out.println( "(Press Enter to Skip)" );
            System.out.print( "Year Published: " );
            try
            {
                String input = UserInput.getInputLine();
                if ( !( input.trim().isEmpty() ) )
                {
                    year = Integer.parseInt( input );
                }
                else
                {
                    year = -1;
                }
            }
            catch ( NumberFormatException ime )
            {
                System.out.println( "Not an Integer" );
            }
            if ( year > currentYear )
            {
                System.out.println( "This book can't be published in the future" );
            }
            else if ( year != -1 && year < 1900 )
            {
                System.out.println( "Please enter a year after the 1900's" );
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        return year;
    }
    
    public static int setPages()
    {
        boolean valid = false;
        int pages = 0;
        do
        {
            System.out.println( "(Press Enter to Skip)" );
            System.out.print( "Number of Pages: " );
            try
            {
                String input = UserInput.getInputLine();
                if ( !( input.trim().isEmpty() ) )
                {
                    pages = Integer.parseInt( input );
                }
                else
                {
                    pages = -1;
                }
            }
            catch ( NumberFormatException ime )
            {
                System.out.println( "Not an Integer" );
            }
            if ( pages == -1 || pages > 0 )
            {
                valid = true;
            }
            else
            {
                System.out.println( "Invalid number of pages" );
            }
        }
        while ( !valid );
        return pages;
    }
    
    public static HashSet < String > getPublishers( Connection conn ) throws SQLException
    {
        HashSet < String > publishers = new HashSet <>();
        String sql = "SELECT PUBLISHERNAME FROM PUBLISHERS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( sql );
    
        while ( rs.next() )
        {
            String info = rs.getString( "PUBLISHERNAME" );
            publishers.add( info );
        }
        
        return publishers;
    }
    
    public static HashSet < String > getBooks( Connection conn ) throws SQLException
    {
        HashSet < String > books = new HashSet <>();
        String sql = "SELECT BOOKTITLE FROM BOOKS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( sql );
    
        while ( rs.next() )
        {
            String name = rs.getString( "BOOKTITLE" );
            books.add( name );
        }
        return books;
    }
    
    public static HashSet < String > getWritingGroups( Connection conn ) throws SQLException
    {
        HashSet < String > writingGroups = new HashSet <>();
        String sql = "SELECT GROUPNAME FROM WRITINGGROUPS";
        PreparedStatement stmt = conn.prepareStatement( sql );
        ResultSet rs = stmt.executeQuery();
    
        while ( rs.next() )
        {
            String info = rs.getString( "GROUPNAME" );
            writingGroups.add( info );
        }
    
        return writingGroups;
    }
    
    public static HashSet < String > getPublisherBookPair( Connection conn ) throws SQLException
    {
        HashSet < String > publisher_Book = new HashSet <>();
        String sql = "SELECT PUBLISHERNAME, BOOKTITLE FROM BOOKS";
        PreparedStatement stmt = conn.prepareStatement( sql );
        ResultSet rs = stmt.executeQuery();
    
        while ( rs.next() )
        {
            String publisher = rs.getString( "PUBLISHERNAME" );
            String book = rs.getString( "BOOKTITLE" );
            publisher_Book.add( publisher + "_" + book );
        }
        
        return publisher_Book;
    }
    
    public static HashSet < String > getGroupNameBookPair( Connection conn ) throws SQLException
    {
        HashSet < String > group_Book = new HashSet <>();
        String sql = "SELECT GROUPNAME, BOOKTITLE FROM BOOKS";
        PreparedStatement stmt = conn.prepareStatement( sql );
        ResultSet rs = stmt.executeQuery();
    
        while ( rs.next() )
        {
            String publisher = rs.getString( "GROUPNAME" );
            String book = rs.getString( "BOOKTITLE" );
            group_Book.add( publisher + "_" + book );
        }
        
        return group_Book;
    }
    
    public static void displayFormattedArray( String[] display, int[] format )
    {
        for ( int i = 0; i < display.length; i++ )
        {
            System.out.print( String.format( "%-" + format[ i ] + "s", dispNull( ( display[ i ] ) ) ) );
        }
    }
    
    public static String singleQuoteString( String s )
    {
        return "'" + s + "'";
    }
}


