package cecs.pkg323.java.term.project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class CECS323JavaTermProject {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    //            + "testdb;user=";
    /**
     * Takes the input string and outputs "N/A" if the string is empty or null.
     * @param input The string to be mapped.
     * @return  Either the input string or "N/A" as appropriate.
     */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static void main(String[] args) {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        System.out.print("Name of the database (not the user account): ");
        DBNAME = in.nextLine();
        System.out.print("Database user name: ");
        USER = in.nextLine();
        System.out.print("Database password: ");
        PASS = in.nextLine();
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        Connection conn = null; //initialize the connection
        Statement stmt = null;  //initialize the statement that we're using
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            boolean done = false;
            
            do
            {
                int menu = displayMainMenu();
                if (menu != 7)
                {
                    String table = "";
                    String data = "";
                    
                    switch ( menu )
                    {
                        case 1: displayInformation( "WRITINGGROUPS", "GroupName", conn );
                                break;
                        case 2: displayInformation( "PUBLISHERS", "PublisherName", conn );
                                break;
                        case 3: displayInformation( "BOOKS", "BookTitle", conn );
                                break;
                        case 4: table = "BOOKS"; // TODO Add a book
                                break;
                        case 5: table = "PUBLISHER"; // TODO Add a Publisher/Update Books
                                break;
                        case 6: table = "BOOKS"; // TODO Remove a book
                                break;
                    }
                }
                else
                {
                    done = true;
                }
            }
            while (!done);
            
            /*
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM TESTTABLE";
            ResultSet rs = stmt.executeQuery(sql);
            
            //STEP 5: Extract data from result set
            System.out.printf(displayFormat, "ID", "First Name", "Last Name", "Phone #");
            while (rs.next()) {
                //Retrieve by column name
                String id = rs.getString("au_id");
                String phone = rs.getString("phone");
                String first = rs.getString("au_fname");
                String last = rs.getString("au_lname");
                
                //Display values
                System.out.printf(displayFormat,
                        dispNull(id), dispNull(first), dispNull(last), dispNull(phone));
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
            */
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
    
    public static int displayMainMenu()
    {
        System.out.println("\nWhat would you like to do?\n");
        System.out.println("1. List Writing Groups");
        System.out.println("2. List Publishers");
        System.out.println("3. List Books\n");
        System.out.println("4. Add new Book");
        System.out.println("5. Insert a New Publisher\n");
        System.out.println("6. Remove a Book\n");
        System.out.println("7. Quit\n");
        return UserInput.getInt(1,7);
    }
    
    public static void displayInformation(String table, String mainCol, Connection conn) throws SQLException
    {
        ArrayList <String> information = new ArrayList <String> ();
        Statement stmt = null;
        stmt = conn.createStatement();
        String sql = "SELECT " + mainCol + " FROM " + table;
        sql += "\nORDER BY " + mainCol;
        ResultSet rs = stmt.executeQuery( sql );
        System.out.println(mainCol);
        int i = 1;
        while (rs.next())
        {
            String info = rs.getString(mainCol);
            information.add( info );
            System.out.println(i + ". " + info);
            i++;
            System.out.println();
        }
        System.out.println(i + 1 + ". Go Back\n");
        System.out.print("Select an entry to view more information about: " );
        int choice = UserInput.getInt( 1, i + 1 );
        if (choice < i + 1)
        {
            String info = information.get( choice - 1 );
            sql = "SELECT * FROM " + table;
            sql += "\nWHERE " + mainCol + " = '" + info + "'";
            rs = stmt.executeQuery( sql );
            ResultSetMetaData data = rs.getMetaData();
            String [] columns = new String [data.getColumnCount()];
            String [] rowData = new String[columns.length];
            rs.next();
            for (int j = 0; j < columns.length; j++)
            {
                columns[j] = data.getColumnName( j + 1 );
                rowData[j] = rs.getString(columns[j]);
            }
            for (int k = 0; k < columns.length; k++)
            {
                System.out.print(columns[k] + "\t");
            }
            System.out.println();
            for (int l = 0; l < columns.length; l++)
            {
                System.out.print(rowData[l] + "\t");
            }
        }
    }
}


