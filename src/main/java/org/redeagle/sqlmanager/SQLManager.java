package org.redeagle.sqlmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.redeagle.BotConfiguration;
import org.redeagle.debugger.Log;

public class SQLManager {
	public Connection sql;
	
	public SQLManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			sql = DriverManager.getConnection("jdbc:sqlite:" + BotConfiguration.PATH +  "datamember.db");
			Log.i("DB Connected");
		} catch(Exception e) {
			Log.e("errrrrr");
		}
	}
	
	public ResultSet select(String queries) {
		try {
			Statement q = sql.createStatement();
			ResultSet rs = q.executeQuery(queries);
			return rs;
		} catch(Exception e) {
			Log.e("Select query error with table :" + queries);
		}
		return null;
	}
	
	public ArrayList<String> select(String tables, String fieldName) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Statement q = sql.createStatement();
			ResultSet queries = q.executeQuery("SELECT " + fieldName + " FROM " + tables);
			while(queries.next()) {
				result.add(queries.getString(fieldName));
			}

			q.close();
		} catch(Exception e) {
			Log.e("Select query error with table :" + tables + " field : " + fieldName);
		}
		return result;
	}
	
	public ArrayList<String> select(String tables, String fieldName, String additional) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Statement q = sql.createStatement();
			ResultSet queries = q.executeQuery("SELECT " + fieldName + " FROM " + tables + " " + additional + ";");
			while(queries.next()) {
				result.add(queries.getString(fieldName));
			}
			q.close();
			Log.d("Selected..  " + tables + " " + fieldName + " " + additional);
		} catch(SQLException e) {
			Log.e(e.getSQLState());
			Log.e(e.getMessage());
			Log.e("SELECT " + fieldName + " FROM " + tables + " " + additional + ";");
		}
		return result;
	}

	public void insert(String query) {
		try { 
			sql.setAutoCommit(false);
			Statement q = sql.createStatement();
			q.executeUpdate(query);
			sql.commit();
			
			q.close();
			Log.d("Inserted..  " + query);
		} catch(SQLException e) {
			Log.e(e.getSQLState());
			Log.e(e.getMessage());
			Log.e(query);
		}
	}
	
	public void createTable(String name, String keys) {
		try {
			Statement q = sql.createStatement();
			String query = "CREATE TABLE " + name + "(" + keys + ")";
			q.executeUpdate(query);
			q.close();
			Log.d("Created table with query -> " + query);
		} catch(SQLException e) {
			Log.e(e.getSQLState());
			Log.e(e.getMessage());
		} 
		
	}
	
	public void update(String query) {
		try {
			sql.setAutoCommit(false);
			Statement q = sql.createStatement();
			q.executeUpdate(query);
			sql.commit();
			
			q.close();
			
			Log.i("Updated.." + query);
		} catch(Exception e) {
			Log.e("Error updating... " + query);
		}
	}
	
	public void delete(String tables, String args) {
		try {
			sql.setAutoCommit(false);
			Statement q = sql.createStatement();
			q.executeUpdate("DELETE FROM " + tables + " " + args);
			sql.commit();
			
			q.close();
		} catch(Exception e) {
			Log.e(e.toString());
		}
	
	}
}
