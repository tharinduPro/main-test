package net.dgie.xmltask;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBHelper {
    private static Connection conn;

	public static Connection getConn(){
		return conn;
	}
    static {
        PropertyReader pr = new PropertyReader();
        String URL = pr.getProperty("jdbc.url");
        String driverClassName = pr.getProperty("jdbc.driverClassName");
        String userName = pr.getProperty("jdbc.username");
        String password = pr.getProperty("jdbc.password");
        try {
            Class.forName( driverClassName ); 
            conn = DriverManager.getConnection( URL, userName, password );
        } 
        catch( Exception ex ) {
            ex.printStackTrace();
        }
    }

}
