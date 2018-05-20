/*
 * DBConnectionManager.java
 *
 * Created on September 30, 2011, 1:01 PM
 */

package com.tcs.tools.web.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sybase.jdbc3.jdbc.SybDriver;

/**
 *
 * @author  369750
 */
public class DBConnectionManager {
    
    /** Creates a new instance of DBConnectionManager */
    public DBConnectionManager() {
    }
    private static Logger logger = Logger.getLogger("ToolLogger");
 /** Method to create a connection */   
public  static Connection getConnection(){
    //System.out.println("::::inside Get Connection method:::::"); 
    Connection con = null;
    /*String userName = "root";
    String password = "";
    String url = "jdbc:mysql://localhost/TOOLDB";*/
    //String userName = "ToolUser";
    //String password = "ToolUser";  
    //String url = "jdbc:mysql://172.20.0.156:3306/TOOLDB";
    //String url = "jdbc:mysql://172.20.0.156:3306/Environmentdb";
    
    String url =  ToolsUtil.readProperty("dbPropConnectionURL");
    String userName = ToolsUtil.readProperty("dbPropUserId");
    String password = ToolsUtil.readProperty("dbPropPassword");  
    
   
    //
   


        try{            
        	 //Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        	  //  con = DriverManager.getConnection (url, userName, password);
        	  //  System.out.println ("Database connection established");
        	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
        	con = DriverManager.getConnection(url,userName,password); 

             
                 }catch(Exception e){
            e.printStackTrace();
        }
    
    return con;

    }

/** Method to create a connection */   
/*public  static Connection getSybaseConnection(String pHostIp,String pHostPort,String pDbName,String pUserName,String pPassword){
  
   Connection con = null;
   
   //pUserName = "ToolUser";
   //pPassword = "ToolUser";
   //pHostIp = "dbhost.yourcompany.com";
   //pDbName = "someName";
   //pHostPort = 1234;  
   
   String sybaseURL = "jdbc:sybase:Tds:" + pHostIp + ":" + pHostPort + ":" + "?SERVICENAME=" + pDbName;

   try{            
       	//Class.forName("com.sybase.jdbc.SybDriver").newInstance(); 
	   Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
       	con = DriverManager.getConnection(sybaseURL,pUserName,pPassword); 

            
   }catch(Exception e){
        e.printStackTrace();
   }
   
   return con;

   }*/

public  static Connection getMySqlConnection (String user, 
		String passwd, 
		String host, 
		String port, 
		String database) {
    //System.out.println("::::inside Get Connection method:::::"); 
    Connection con = null;    
    String url = "jdbc:mysql://"+host+":"+port+"/"+database;
    
   
    
   
    //
   


        try{            
        	 //Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        	  //  con = DriverManager.getConnection (url, userName, password);
        	  //  System.out.println ("Database connection established");
        	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
        	con = DriverManager.getConnection(url,user,passwd); 

             
                 }catch(Exception e){
            e.printStackTrace();
        }
    
    return con;

    }


public static Connection getSybaseConnection (String user, 
		String passwd, 
		String host, 
		String port, 
		String database) {
	Connection lConnection = null;
	 SybDriver sybDriver;
		try {
			System.out.println(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database);
			logger.info("::::inside db connection mge getSybaseConnection:::::::::");
			logger.info(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database);
			sybDriver = 
				(SybDriver) Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance(); 
			//sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_5);
			//DriverManager.registerDriver(sybDriver);
			Properties props = new Properties();
			props.put("user", user);
			props.put("password", passwd);
			lConnection =
				DriverManager.getConnection("jdbc:sybase:Tds:" + host + ":" + port + "/" + database, 
					props); 
			logger.info(":::::lconnection from db conn mgr::::"+lConnection);
		} catch (SQLException e) {
			System.out.println("JDBC Connection error: " + e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			return lConnection;
		} catch (IllegalAccessException e) {
			System.out.println(
				"Illegal Access Exception thrown inside ConnectDBMS. Details: "
					+ e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (InstantiationException e) {
			System.out.println(
				"Exception thorwn inside ConnectDBMS. Details: " + e.getMessage()); 
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Generic Error:");
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			e.printStackTrace();
			return lConnection;
		}
		return lConnection;
	}


public static Connection getDb2Connection (String user, 
		String passwd, 
		String host, 
		String port, 
		String database,
		String schemaName) {
	Connection lConnection = null;
	 
		try {
			System.out.println(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database+":::schema name->"+schemaName);
			logger.info("::::inside db connection mge getDb2Connection:::::::::");
			logger.info(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database+":::schema name->"+schemaName);
			//Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance(); 
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
						   
			 

			//sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_5);
			//DriverManager.registerDriver(sybDriver);
			Properties props = new Properties();
			props.put("user", user);
			props.put("password", passwd);
			lConnection =
				DriverManager.getConnection("jdbc:db2://" + host + ":" + port + "/" + database+":currentSchema="+schemaName+";", 
					props); 
			lConnection.createStatement().executeUpdate("SET SCHEMA "+schemaName);
			logger.info(":::::lconnection from db conn mgr::::"+lConnection);
		} catch (SQLException e) {
			System.out.println("JDBC Connection error: " + e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			return lConnection;
		} catch (IllegalAccessException e) {
			System.out.println(
				"Illegal Access Exception thrown inside ConnectDBMS. Details: "
					+ e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (InstantiationException e) {
			System.out.println(
				"Exception thorwn inside ConnectDBMS. Details: " + e.getMessage()); 
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Generic Error:");
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			e.printStackTrace();
			return lConnection;
		}
		return lConnection;
	}

public static Connection getTestConnection (String user, 
		String passwd, 
		String host, 
		String port, 
		String database) {
	Connection lConnection = null;
	 SybDriver sybDriver;
		try {
			System.out.println(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database);
			logger.info("::::inside db connection mge getTestConnection:::::::::");
			logger.info(":::::::user->"+user+":::passwd->"+passwd+"::::host->"+host+":::port->"+port+":::::database->"+database);
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			//sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_5);
			//DriverManager.registerDriver(sybDriver);
			
			String url ="jdbc:mysql://"+host+":"+port+"/"+database;
			lConnection = DriverManager.getConnection(url,user,passwd); 
			logger.info(":::::lconnection from db conn mgr::::"+lConnection);
		} catch (SQLException e) {
			System.out.println("JDBC Connection error: " + e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			return lConnection;
		} catch (IllegalAccessException e) {
			System.out.println(
				"Illegal Access Exception thrown inside ConnectDBMS. Details: "
					+ e.getMessage());
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (InstantiationException e) {
			System.out.println(
				"Exception thorwn inside ConnectDBMS. Details: " + e.getMessage()); 
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Generic Error:");
			logger.info(":::::error lconnection from db conn mgr::::"+ e.getMessage());
			logger.error(":::::error lconnection from db conn mgr::::"+e.getStackTrace());
			e.printStackTrace();
			return lConnection;
		}
		return lConnection;
	}
public  static Connection getMsAccessConnection(){
    //System.out.println("::::inside Get Connection method:::::"); 
    Connection con = null;
        try{            
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
             
             // set this to a MS Access DB you have on your machine
            
            String filename = "C:\\arun\\code\\workspace_10102011\\sampledir\\config.files\\PATTERN_DB_TEST_11102011.mdb";
    //String filename = "C:\\Documents and Settings\\477780\\.netbeans\\3.6\\sampledir\\config.files\\PATTERN_DB_NEXT.mdb";
            //String filename =System.getProperty("user.dir").replaceAll("\\\\", "/")+"/config.files/PATTERN_DB.mdb";
    String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
   
// now we can get the connection from the DriverManager
    database+= filename.trim() + ";DriverID=22;READONLY=false}"; // add on to the end 
     con = DriverManager.getConnection( database ,"",""); 
    
        }catch(Exception e){
            e.printStackTrace();
        }
    
    return con;

    }

public  static void closeConnection(Statement lStatement,ResultSet lResultSet){
    try{
        if(lStatement != null) lStatement.close();
        if(lResultSet != null) lResultSet.close();
    }catch(Exception e){
        //e.printStackTrace();
    }
}
public  static void closeConnection(PreparedStatement lPreparedStatement,ResultSet lResultSet){
    try{
        if(lPreparedStatement != null) lPreparedStatement.close();
        if(lResultSet != null) lResultSet.close();
    }catch(Exception e){
        //e.printStackTrace();
    }
}
public static void closeConnection(Connection lConnection){
    try{
        if(lConnection != null) lConnection.close();       
    }catch(Exception e){
        //e.printStackTrace();
    }
}
 



/** Method to create a connection */   
public  static Connection getMySqlConnection(){
    //System.out.println("::::inside Get Connection method:::::"); 
    Connection con = null;
    String userName = "root";
    String password = "";
    String url = "jdbc:mysql://localhost/TOOLDB";
   


        try{            
        	 //Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        	  //  con = DriverManager.getConnection (url, userName, password);
        	  //  System.out.println ("Database connection established");
        	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
        	con = DriverManager.getConnection(url,userName,password); 

             
                 }catch(Exception e){
            e.printStackTrace();
        }
    
    return con;

    }
   


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("::::::::::::::"+getConnection());
        try {
			//System.out.println("::::::::::::::"+getSybaseConnection("temp", "temp","172.20.0.46", "3309", "TestSchema"));
        	//System.out.println("::::::::::::::"+ getDb2Connection ("temp", "temp","172.20.0.46", "3309", "TestSchema",null) );
 //       	System.out.println("::::::::::::::"+ getDb2Connection ("X174650", "rahim123","48.140.158.105", "50012", "gearsd1","gxprd01") );
        	Connection lc = getDb2Connection ("X174650", "rahim123","48.140.158.105", "50012", "gearsd1","gxprd01") ;
        	System.out.println("DB2 Connection::"+lc);
        	PreparedStatement pp;
        	
        	
        	System.out.println(":::after schema set");
        	
        	ResultSet rs = lc.createStatement().executeQuery("select * from GXT_Accrual_Reference");
        	while (rs.next()) {
				System.out.println(":::::rs1"+rs.getString(1));
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }
    
}
