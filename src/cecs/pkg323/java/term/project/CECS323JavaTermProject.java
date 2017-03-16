package cecs.pkg323.java.term.project;

import java.sql.*;
import java.util.ArrayList;

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
        
        int choice = UserInput.getInt( 1, 2 );
        
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
        ResultSetMetaData metaData = rs.getMetaData();
        
        int choice = 0;
        do
        {
            System.out.print( "Select an entry from " + metaData.getTableName( 1 ) + " to view more information about: " );
            choice = UserInput.getInt( 1, i );
            if ( choice < i )
            {
                String info = information.get( choice - 1 );
                stmt.setString( 1, info );
                rs = stmt.executeQuery();
                ResultSetMetaData data = rs.getMetaData();
                int[] colLength = new int[ data.getColumnCount() ];
                String[] columns = new String[ data.getColumnCount() ];
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
    
    // TODO: Need to account for nonexistant publishers/writing groups
    public static void insertBook( Connection conn ) throws SQLException
    {
        Statement stmt = null;  // TODO: Change to Prepared Statement
        String table = "BOOKS";
        System.out.println( "Please enter book title" );
        String title = UserInput.getInputLine();
        System.out.println( "Please enter book year published" );
        int year = UserInput.getInt();
        System.out.println( "Please enter total number of pages the book has" );
        int page = UserInput.getInt();
        System.out.println( "Please enter the writer group's name of the book" );
        String groupName = UserInput.getInputLine();
        System.out.println( "Please enter publisher name of the book" );
        String publisher = UserInput.getInputLine();
        String sql = "insert into " + table;
        sql += "(groupName, bookTitle,publisherName,yearPublished, numberOfPages) values(";
        sql += singleQuoteString( groupName ) + "," + singleQuoteString( title ) + "," + singleQuoteString( publisher ) + "," + year + "," + page + ")";
        stmt = conn.createStatement();
        stmt.executeUpdate( sql );
    }
    
    public static String singleQuoteString( String s )
    {
        return "'" + s + "'";
    }
}


