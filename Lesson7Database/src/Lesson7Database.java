import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class Lesson7Database {
    private static String dbURL = "jdbc:derby://localhost:1527/myDB;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
	public static void main(String[] args) throws IOException {
		String fileName = null;
		if (args.length < 1) {
			System.out.println("File name required");
			return;
		}
		fileName = args[0];
		//fileName = "C:\\Git\\ucsd-java4\\Lesson7Database\\bin\\Lessons.sql";
		createConnection();
		if (fileName!=null) {
			readStatements(fileName);
		}
		displayTable("Lessons");
		//drop the table
		executeStatement("DROP TABLE Lessons");
	}

	private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
            System.out.println("Database Connection info: " + dbURL);
            System.out.println("Connecting to the database: Done");
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
	
	private static void readStatements(String fileName) throws IOException {
	    //Read From Text File
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
            	executeStatement(line);
            }
            System.out.println("Populating the database: Done.");
        }
	}
	
	private static void executeStatement(String statement) {
		if (statement.trim().length() > 0)
			try
	        {
	            stmt = conn.createStatement();
	            stmt.execute(statement.replace(';', ' '));
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
	}
	
	private static void displayTable(String tableName)
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            System.out.println("Query of Lessons table results:");
            for (int i=1; i<=numberCols; i++)
            {
                //display Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
            	//display values
                int num = results.getInt(1);
                String title = results.getString(2);
                System.out.println(num + "\t\t" + title);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
}
