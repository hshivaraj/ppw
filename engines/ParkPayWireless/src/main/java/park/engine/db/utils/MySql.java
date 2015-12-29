package park.engine.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.version.Version;

public class MySql {
	private String USERNAME = "root";
	private String PASSWORD = "mysore159";
	private String HOSTNAME = "localhost";
	private String DBNAME = null;
	private Integer PORT = 3306;
	
	private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    private String db_url = null;
	
    public MySql(String dbname) {
    	this.DBNAME = dbname;
    	this.db_url = "jdbc:mysql://" + this.HOSTNAME + ":" + 
    			this.PORT.toString() + "/" + dbname;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    	} catch(ClassNotFoundException ex) {
    		Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}
    	
    	try {
    		this.con = DriverManager.getConnection(this.db_url, this.USERNAME, 
    				this.PASSWORD);
    	} catch (SQLException ex) {
    		Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	}
    }
    
	public MySql(String hostname, Integer port, String dbname, 
			String username, String passwd) {

		this.USERNAME = username;
		this.PASSWORD = passwd;
		this.DBNAME = dbname;
		this.PORT = port;
		this.HOSTNAME = hostname;
	}
	
	public ResultSet executeQuery(String query) {
		try{
			this.st = this.con.createStatement();
			this.rs = this.st.executeQuery(query);
			return this.rs;
		} catch(SQLException ex) {
			Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
		}
	}
}