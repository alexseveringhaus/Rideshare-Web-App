package rideshare_ridehandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	protected Connection conn;
	public DBConnector(String db, String user, String pass) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?user=" + user + "&password=" + pass + "&useSSL=false");
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		catch (ClassNotFoundException cnfe) {
	        System.out.println("ClassNotFoundException: " + cnfe.getMessage());
	    }
	}
	
	//assumes rideshare database is the one we're connecting with
	public DBConnector() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost/rideshare?user=root&password=root&useSSL=false");
		}
		catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		catch (ClassNotFoundException cnfe) {
	        System.out.println("ClassNotFoundException: " + cnfe.getMessage());
	    }
	}
	
	//function to clean up resources like queries or a database connection if not null
	protected static<T extends AutoCloseable> void cleanUp(T obj) {
		try {
			if(obj != null){
				obj.close();
			}
		}
		catch(Exception e) {
			System.out.println("Error while closing resource");
		}
	}
	
	public void close() {
		cleanUp(this.conn);
	}
}

