package com.fintellix.test.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dongliu.dbutils.Database;
import net.dongliu.dbutils.SQLResultSet;

public class DBUtils {
	private static Logger logger = LoggerFactory.getLogger(DBUtils.class);
	private static HashMap <String, Database> dbs = new HashMap <String, Database>();;
	private static Properties dbprops;
	
	private static Database loadDBConn(String connName) throws Exception{
		if(null == dbprops){
			InputStream ins = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
			dbprops = new Properties();
			dbprops.load(ins);
			ins.close();
		}
		
		try{
			String url = dbprops.getProperty(connName+".jdbc.url");
			String username = dbprops.getProperty(connName+".jdbc.username");
			String password = dbprops.getProperty(connName+".jdbc.password");
			
			if(null == url || null == username || null ==  password ){
				throw new Exception("url, username or password is null for db: " + connName);
			}
			Database db = Database.create(url,username,password);
			dbs.put(connName, db);
			return db;
			
		}catch (Exception e){
			logger.error("Error while trying to get db connection "+ connName, e);
		}
		return null;
	}
	
	private static SQLResultSet executeQuery(String query, String connName) throws Exception{
		//check if Database object already available for given conn.
		Database database = (Database)dbs.get(connName);
		if(null == database){
			database = loadDBConn(connName);
		}
		try{
			SQLResultSet result = database.query(query).execute();
			return result;
		}catch( Exception e){
			logger.error("Error while executing query. conn="+connName+" query="+ query, e);
			throw e;
		}
	
	}
	
	public static String getSingleRow(String query, String connName) throws Exception{
		
		Object[] result_obj = executeQuery(query, connName).toArray();
		if (result_obj == null) return null;
		String [] result_str = new String [result_obj.length];
		for (int index=0;index< result_obj.length;index++){
			result_str[index] = result_obj[index].toString();
		}
		return String.join(",", result_str);
		
	}

	public static List<Object[]> getMultipleRow(String query, String connName) throws Exception{
		return executeQuery(query, connName).toArrayList();
	}
	
	public static String deleteRecordsFromDB(String query,String connName) throws Exception {
		
		Object[] result_obj=executeQuery(query, connName).toArray();
		if (result_obj == null)
			return null;
		return connName;
	}
}
