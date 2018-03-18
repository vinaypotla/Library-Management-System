package com.library.management;

import java.sql.*;

public class Db_Connection {
	Connection conn = null;

	public ResultSet Connection(String query) {
		
		
		String url = "jdbc:mysql://localhost:3306/library_system"; // direct connect to database in url
		String user = "root";
		String password = "root";
		try {
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
		        return rs;
			} 

		catch(SQLException ex) {
			System.out.println("Error in connection: " + ex.getMessage());
			return null;
		}
	}
	public int Connection1(String query)
	{
		
		String url = "jdbc:mysql://localhost:3306/library_system"; 
		String user = "root";
		String password = "root";
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			int affectedrows = stmt.executeUpdate(query);
			
			return affectedrows;
			} 
		 
		catch(SQLException ex) {
			System.out.println("Error in connection: " + ex.getMessage());
			//ex.printStackTrace();
			return 0;
		}
	}
}
