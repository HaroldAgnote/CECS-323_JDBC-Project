package cecs.pkg323.java.term.project;

import java.sql.*;
import java.time.Year;
import java.util.*;
import java.util.Date;

/**
 * @author Mimi Opkins with some tweaking from Dave Brown
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
                            table = "PUBLISHER"; // TODO Add a Publisher/Update Books
                            break;
                        case 6:
                            table = "BOOKS"; // TODO Remove a book; Put Below Code in method
                            displayInformation( "BOOKS", "BookTitle", conn );
                            String bookname = "a"; //modify
                            String sql = "delete from " + table + "where book.booktitle = " + singleQuoteString( bookname );
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
        else
        {
            return input;
        }
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
        System.out.println( i + ". Go Back\n" );
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
                for ( int k = 0; k < columns.length; k++ )
                {
                    System.out.print( String.format( "%-" + colLength[ k ] + "s", columns[ k ] ) );
                }
                System.out.println( "\n" );
                for ( int l = 0; l < columns.length; l++ )
                {
                    System.out.print( String.format( "%-" + colLength[ l ] + "s", rowData[ l ] ) );
                }
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
        final int currentYear = (Calendar.getInstance()).get( Calendar.YEAR );
        
        HashSet <String> publishers = getPublishers( conn );
        HashSet <String> writingGroups = getWritingGroups( conn );
    
        String groupName;
        String title;
        String publisherName;
        int year;
        int pages;
        
        boolean valid = false;
        Statement stmt = null;  // TODO: Change to Prepared Statement
        String table = "BOOKS";
        System.out.print( "Please Enter Book Title: " );
        do
        {
            title = UserInput.getInputLine();
            if (title.trim().isEmpty())
            {
                System.out.println("Title cannot be Empty");
            }
            else if (title.length() > 50)
            {
                System.out.println("Title is too long");
            }
            else 
            {
                break;
            }
        }
        while ( true );
        do
        {
            System.out.println( "Please enter book year published" );
            year = UserInput.getInt();
            if (year > currentYear)
            {
                System.out.println("This book can't be published in the future");
            }
            else if (year < 1900)
            {
                System.out.println("Please enter a year after the 1900's");
            }
            else
            {
                break;
            }
        }
        while ( true );
        System.out.println( "Please enter total number of pages the book has" );
        pages = UserInput.getInt();
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
                break;
            }
        }
        while ( true );
        
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
                break;
            }
        }
        while ( true );
        String sql = "insert into " + table;
        sql += "(groupName, bookTitle,publisherName,yearPublished, numberOfPages) values(";
        sql += singleQuoteString( groupName ) + "," + singleQuoteString( title ) + "," + singleQuoteString( publisherName ) + "," + year + "," + pages + ")";
        stmt = conn.createStatement();
        stmt.executeUpdate( sql );
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
    
    public static HashSet <String> getWritingGroups(Connection conn) throws SQLException
    {
        HashSet <String> writingGroups = new HashSet<>(  );
        String sql = "SELECT GROUPNAME FROM WRITINGGROUPS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( sql );
    
        while ( rs.next() )
        {
            String info = rs.getString( "GROUPNAME" );
            writingGroups.add( info );
        }
    
        return writingGroups;
    }
    
    
    public static String singleQuoteString( String s )
    {
        return "'" + s + "'";
    }
}


