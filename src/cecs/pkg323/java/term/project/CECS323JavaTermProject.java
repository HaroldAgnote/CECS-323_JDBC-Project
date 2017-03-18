package cecs.pkg323.java.term.project;

import java.sql.*;
import java.util.*;

/**
 *
 * Harold Agnote
 * Xinyi Chen
 *
 * Professor David Brown
 *
 * CECS 323 - Sec. 07
 *
 * 3/20/2017
 *
 * CECS 323 - JDBC roject
 *
 *
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
                    String table = "";
                    String data = "";
                    
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
                            table = "PUBLISHERS"; // TODO Add a Publisher/Update Books
                            System.out.println("Please enter the new publisher's name");
                            String publisherName = UserInput.getInputLine();
                            System.out.println("Please enter the new publisher's address");
                            String publisherAddress = UserInput.getInputLine();
                            System.out.println("Please enter the new publisher's Phone number");
                            String publisherPhone = UserInput.getInputLine();
                            System.out.println("Please enter the new publisher's email");
                            String publisherEmail = UserInput.getInputLine();
                            
                            
                            System.out.println("Please enter the old publisher's name that you wish to replace with");
                            String oldName = UserInput.getInputLine();
                            
                            String sql = "INSERT INTO "+table+ " (publisherName, publisherAddress,publisherPhone,publisherEmail) values(";
                            sql +=  singleQuoteString(publisherName) +","+singleQuoteString(publisherAddress)+","+singleQuoteString(publisherPhone)+","+singleQuoteString(publisherEmail);
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            
                            String sql2 = "Update Books set books.publishername = "+singleQuoteString(publisherName)+" where books.publisherName = "+singleQuoteString(oldName);
                            pstmt = conn.prepareStatement(sql2);
                            
                            String sql3 = "DELETE FROM "+table+" where publisherName = "+singleQuoteString(oldName);
                            pstmt = conn.prepareStatement(sql3);
                            break;
                        case 6:
                            removeBook( conn );
                            break;
                    }
                }
                else
                {
                    done = true;
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
            if (Integer.parseInt( input ) == -1)
            {
                return "N/A";
            }
        }
        catch ( NumberFormatException nfe )
        {
            
        }
        return input;
    }
    
    //Prompt the user for the database name, and the credentials.
    //If your database has no credentials, you can update this code to
    //remove that from the connection string.
    public static void login()
    {
        System.out.println( "Select Login Credentials: \n" );
        System.out.println( "1. Harold Agnote" );
        System.out.println( "2. Xinyi Chen\n" );
        System.out.println( "3. Manual Login");
        
        int choice = UserInput.getInt( 1, 3 );
        
        if ( choice == 1 )
        {
            USER = "admin";
            PASS = "test";
            DBNAME = "Term_Project";
        }
        else if (choice == 2)
        {
            USER = "ad";
            PASS = "password";
            DBNAME = "project1";
        }
        else
        {
            System.out.print("Name of the database: ");
            DBNAME = UserInput.getInputLine();
            System.out.print("Database user name: ");
            USER = UserInput.getInputLine();
            System.out.print("Database password: ");
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
        System.out.println( i + ". Go Back\n" );
        int choice = 0;
        do
        {
            System.out.print( "Select an entry from " + table + " to view more information about: " );
            choice = UserInput.getInt( 1, i );
            if ( choice < i )
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
        while ( choice < i );
        
    }
    
    /*
     * TODO: Need to account for nonexistent publishers/writing groups
     * Planning on just checking if a Publisher/Writing Group exists in the table
     * If so, proceed with add, otherwise prompt user to select a valid
     */
    public static void insertBook( Connection conn ) throws SQLException
    {
        HashSet <String> publisher_Book = getPublisherBookPair( conn );
        HashSet <String> group_Book = getGroupNameBookPair( conn );
        
        String bookTitle = setBookTitle();
        String groupName = setWritingGroup( conn );
        String publisherName = setPublisher(conn);
        int year = setYear();
        int pages = setPages();
        
        PreparedStatement pstmt = null;  // TODO: Change to Prepared Statement
        String table = "BOOKS";
        
        String publisher_Book_Pair = publisherName + "_" + bookTitle;
        String group_Book_Pair = groupName + "_" + bookTitle;
        
        while (publisher_Book.contains( publisher_Book_Pair ))
        {
            System.out.println("The database already contains an entry with: ");
            System.out.println("Book Title: " + bookTitle);
            System.out.println("Publisher: " + publisherName);
            System.out.println("Please change either attribute to proceed with adding this Book");
            bookTitle = setBookTitle();
            publisherName = setPublisher( conn );
            publisher_Book_Pair = publisherName + "_" + bookTitle;
        }
    
        while (group_Book.contains( group_Book_Pair))
        {
            System.out.println("The database already contains an entry with: ");
            System.out.println("Book Title: " + bookTitle);
            System.out.println("Writing Group: " + groupName);
            System.out.println("Please change either attribute to proceed with adding this Book");
            bookTitle = setBookTitle();
            publisherName = setPublisher( conn );
            publisher_Book_Pair = publisherName + "_" + bookTitle;
        }
        
        boolean confirm = false;
    
        String displayFormat = "%-20s: %-20s\n";
        System.out.println("Here's the information you want to put in: ");
        System.out.printf(displayFormat, "Group Name", groupName);
        System.out.printf(displayFormat, "Book Title", bookTitle);
        System.out.printf( displayFormat, "Publisher", publisherName );
        System.out.printf( displayFormat, "Year Published", dispNull( Integer.toString(year )) );
        System.out.printf( displayFormat, "Number of Pages", dispNull( Integer.toString( pages ) ) );
    
        System.out.println("");
    
        String sql = "INSERT INTO " + table;
        
        if (year == -1 && pages != -1)
        {
            sql += "(groupName, bookTitle,publisherName, numberOfPages) values(";
            sql += singleQuoteString( groupName ) + "," + singleQuoteString( bookTitle ) + "," + singleQuoteString( publisherName ) + ","  + pages + ")";
            pstmt = conn.prepareStatement(sql);
        }
        else if (year != -1 && pages == -1)
        {
            sql += "(groupName, bookTitle,publisherName,yearPublished) values(";
            sql += singleQuoteString( groupName ) + "," + singleQuoteString( bookTitle ) + "," + singleQuoteString( publisherName ) + "," + year + ")";
            pstmt = conn.prepareStatement(sql);
        }
        else if (year == -1 && pages == -1)
        {
            sql += "(groupName, bookTitle,publisherName) values(";
            sql += singleQuoteString( groupName ) + "," + singleQuoteString( bookTitle ) + "," + singleQuoteString( publisherName ) + ")";
            pstmt = conn.prepareStatement(sql);
        }
        else
        {
            sql += "(groupName, bookTitle,publisherName,yearPublished, numberOfPages) values(";
            sql += singleQuoteString( groupName ) + "," + singleQuoteString( bookTitle ) + "," + singleQuoteString( publisherName ) + "," + year + "," + pages + ")";
            pstmt = conn.prepareStatement(sql);
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
            if (bookTitle.trim().isEmpty())
            {
                System.out.println("Title cannot be Empty");
            }
            else if (bookTitle.length() > 50)
            {
                System.out.println("Title is too long");
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        return bookTitle;
    }
    
    public static String setWritingGroup(Connection conn) throws SQLException
    {
        HashSet <String> writingGroups = getWritingGroups( conn );
        boolean valid = false;
        String groupName;
        do
        {
            System.out.println( "Please enter the writer group's name of the book" );
            groupName = UserInput.getInputLine();
            if (groupName.trim().isEmpty())
            {
                System.out.println("Writing Group Name cannot be empty");
            }
            else if (!writingGroups.contains( groupName ))
            {
                System.out.println(groupName + " does not exist in WRITINGGROUPS");
                System.out.println("Please enter an existing Writing Group from the list:");
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
    
    public static String setPublisher(Connection conn) throws SQLException
    {
        HashSet <String> publishers = getPublishers( conn );
        boolean valid = false;
        String publisherName;
    
        do
        {
            System.out.println( "Please enter publisher name of the book" );
            publisherName = UserInput.getInputLine();
            if (publisherName.trim().isEmpty())
            {
                System.out.println("Publisher Name can't be empty");
            }
            else if (!publishers.contains( publisherName ))
            {
                System.out.println(publisherName + " does not exist in PUBLISHERS");
                System.out.println("Please enter an existing Publisher from the list");
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
        final int currentYear = (Calendar.getInstance()).get( Calendar.YEAR );
        boolean valid = false;
        int year = 0;
        
        do
        {
            System.out.println( "(Press Enter to Skip)" );
            System.out.print("Year Published: ");
            try
            {
                String input = UserInput.getInputLine();
                if (!(input.trim().isEmpty()))
                {
                    year = Integer.parseInt( input );
                }
                else
                {
                    year= -1;
                }
            }
            catch ( NumberFormatException ime )
            {
                System.out.println("Not an Integer");
            }
            if (year > currentYear)
            {
                System.out.println("This book can't be published in the future");
            }
            else if (year != -1 && year < 1900 )
            {
                System.out.println("Please enter a year after the 1900's");
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
            System.out.print("Number of Pages: ");
            try
            {
                String input = UserInput.getInputLine();
                if (!(input.trim().isEmpty()))
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
                System.out.println("Not an Integer");
            }
            if (pages == -1  || pages > 0)
            { 
                valid = true;
            }
            else
            {
                System.out.println("Invalid number of pages");
            }
        }
        while ( !valid );
        return pages;
    }
    
    public static void removeBook(Connection conn) throws SQLException
    {
        HashSet <String> books = getBooks(conn);
        String bookTitle;
        boolean valid = false;
        do
        {
            bookTitle = setBookTitle();
            if (!books.contains( bookTitle ))
            {
                System.out.println(bookTitle + " does not exist in Books");
                System.out.println("Please enter an existing books from the list:");
                displayInformation( "BOOKS", "BOOKTITLE", conn );
            }
            else
            {
                valid = true;
            }
        }
        while ( !valid );
        String sql = "DELETE FROM BOOKS WHERE BOOKS.BOOKTITLE = " + singleQuoteString( bookTitle );
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
    }
    
    public static HashSet <String> getPublishers(Connection conn) throws SQLException
    {
        HashSet <String> publishers = new HashSet<>(  );
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
    public static HashSet <String> getBooks(Connection conn) throws SQLException
    {
        HashSet <String> books = new HashSet<>();
        String sql = "SELECT BOOKTITLE FROM BOOKS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next())
        {
            String name = rs.getString("BOOKTITLE");
            books.add(name);
        }
        return books;
    }
    public static HashSet <String> getWritingGroups(Connection conn) throws SQLException
    {
        HashSet <String> writingGroups = new HashSet<>(  );
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
    
    public static HashSet <String> getPublisherBookPair(Connection conn) throws SQLException
    {
        HashSet <String> publisher_Book = new HashSet <>(  );
        String sql = "SELECT PUBLISHERNAME, BOOKTITLE FROM BOOKS";
        PreparedStatement stmt = conn.prepareStatement( sql );
        ResultSet rs = stmt.executeQuery(  );
        
        while (rs.next() )
        {
            String publisher = rs.getString( "PUBLISHERNAME" );
            String book = rs.getString("BOOKTITLE");
            publisher_Book.add( publisher + "_" + book );
        }
        
        return publisher_Book;
    }
    
    public static HashSet <String> getGroupNameBookPair(Connection conn) throws SQLException
    {
        HashSet <String> group_Book = new HashSet <>(  );
        String sql = "SELECT GROUPNAME, BOOKTITLE FROM BOOKS";
        PreparedStatement stmt = conn.prepareStatement( sql );
        ResultSet rs = stmt.executeQuery(  );
        
        while (rs.next() )
        {
            String publisher = rs.getString( "GROUPNAME" );
            String book = rs.getString("BOOKTITLE");
            group_Book.add( publisher + "_" + book );
        }
        
        return group_Book;
    }
    
    public static void displayFormattedArray(String [] display, int [] format)
    {
        for (int i = 0; i < display.length; i++)
        {
            System.out.print( String.format( "%-" + format[i] + "s", dispNull((display[i] ))));
        }
    }
    
    public static String singleQuoteString( String s )
    {
        return "'" + s + "'";
    }
}


