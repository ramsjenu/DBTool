package com.tcs.tools.web.idmt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import com.tcs.tools.web.constant.WebConstant;
import com.tcs.tools.web.util.DBConnectionManager;
import com.tcs.tools.web.util.ToolsUtil;

public class ReadFileAndStoreInDb {

	private PreparedStatement lPreparedStatementInsert = null;
	private PreparedStatement lPreparedStatement = null;
	private Connection lConnection = null;
	private ResultSet lResultSet = null;
	private int lInsertBatchCount=0;
	
	
	public String readFileFromDisk(String pFileName,String pFilePath,String pCreatedBy){
		try{
				lConnection = DBConnectionManager.getConnection();
				lConnection.setAutoCommit(false);
				
				lPreparedStatement = lConnection.prepareStatement(WebConstant.DELETE_IDMT_PROP_FILE_DETAILS);
				lPreparedStatement.setString(1, pFileName);
				lPreparedStatement.executeUpdate();
				
				lPreparedStatementInsert = lConnection.prepareStatement(WebConstant.INSERT_IDMT_PROP_FILE_DETAILS);
				int pLineNo =0;
				  // Open the file that is the first 
				  // command line parameter
				  FileInputStream fstream = new FileInputStream(pFilePath+pFileName);
				  // Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  //Read File Line By Line
				  while ((strLine = br.readLine()) != null)   {
					  // Print the content on the console
					  //System.out.println (strLine);
					  pLineNo ++;
					  insertIdmtPropFileDetails( pFileName, pFilePath,pLineNo+"",strLine,pCreatedBy);
				  }
				  //Close the input stream
				  in.close();
			  
				  lPreparedStatementInsert.executeBatch();
				  lConnection.commit();
				  lConnection.setAutoCommit(true);
			  
			    }catch (Exception e){//Catch exception if any
			    	System.err.println("Error: " + e.getMessage());
				}finally{
					 DBConnectionManager.closeConnection(lPreparedStatementInsert, null);
					 DBConnectionManager.closeConnection(lConnection);
				}
			  

		
		
		return "";		
	}
	
	public int insertIdmtPropFileDetails(String pFilename,String pFilePath,String pLineNo,String pText,String pCreatedBy){
		int lInsertCount = 0;
		try {
			
			
			
			lPreparedStatementInsert.setString(1, pFilename);
			lPreparedStatementInsert.setString(2, pFilePath);
			lPreparedStatementInsert.setString(3, pLineNo);
			lPreparedStatementInsert.setString(4, pText);
			lPreparedStatementInsert.setString(5, pCreatedBy);
			lPreparedStatementInsert.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			
			// Execute Query
			lPreparedStatementInsert.addBatch();
			lInsertBatchCount++;
			
			
			
			
			 if(lInsertBatchCount >= 1000){				 
				 lPreparedStatementInsert.executeBatch();
				 lInsertBatchCount = 0;
			 }
			 

			// lPreparedStatement.executeBatch();

		} catch (SQLException pSQLException) {
			pSQLException.printStackTrace();
		} catch (Exception pException) {
			pException.printStackTrace();
		} finally {
			// DBConnectionManager.closeConnection(lPreparedStatement, null);
		}
		
		return lInsertCount;
	}
	
	private String witeToFileFromDb(String pFileName,String pFilePath){
		 try {
			 
			 File lFile = new File(pFilePath);
			  lFile.mkdirs();
			BufferedWriter writer = new BufferedWriter(new FileWriter(pFilePath+pFileName));
			
			HashMap lDataMap = new HashMap();
			lDataMap.put("SERVER", "48.113.192.157");
			
			 
			 lConnection = DBConnectionManager.getConnection();
			  lPreparedStatement = lConnection.prepareStatement(WebConstant.SELECT_IDMT_PROP_FILE_DETAILS);
			  lPreparedStatement.setString(1,pFileName);
			  lResultSet = lPreparedStatement.executeQuery();
			 
			  //System.out.println(":::pFileName:::"+pFileName);
				if (lResultSet != null) {
	 				// this will step through our data row-by-row
	 				while (lResultSet.next()) {
	 					//System.out.println(":::res:::"+ToolsUtil.replaceNull(lResultSet.getString("FILE_TEXT")));
	 					//writer.write(ToolsUtil.replaceNull(lResultSet.getString("FILE_TEXT")));
	 					/*String[] lArrValue = ToolsUtil.replaceNull(lResultSet.getString("FILE_TEXT")).split("SERVER"+"=");
	 					if(lArrValue.length >1){
	 						System.out.println(":::matching string found in file"+lArrValue[1] );	 						
	 					}*/
	 					String lfrontext="SERVER";
	 					String lnewtext="1.2.3.6";
	 					String lWordCategory="";
	 					lWordCategory = "-D";
	 					
	 					String lDbText = ToolsUtil.replaceNull(lResultSet.getString("FILE_TEXT"));
	 					if(!lDbText.startsWith("#")){
		 					if("-D".equals(lWordCategory)){
		 						lDbText = lDbText.replaceAll("\\s+-D"+lfrontext+"=[\\d\\w\\W\\s\\n\\r]*\\s+-D", " -D"+lfrontext+"="+lnewtext+" -D");
		 					}else if("COLON".equals(lWordCategory)){
		 						lDbText = lDbText.replaceAll("\\s+-D"+lfrontext+"=[\\d\\w\\W\\s\\n\\r]*\\s+-D", " -D"+lfrontext+"="+lnewtext+" -D");
		 					}else{
		 						lDbText = lDbText.replaceAll("\\b"+lfrontext+"=[\\d\\w\\W\\s]*", lfrontext+"="+lnewtext);
		 					}
	 					}
	 					/*if(lDbText.startsWith("$")){
	 						
	 					}*/
	 					
	 					writer.write(lDbText);
	 					
	 					lResultSet.getInt("LINE_NO");
	 					writer.newLine();	 					
	 				}
	 			}
				writer.close();
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return "";
	}
	
	public List modifyFileContents(){
		List lModifiedFileList = new ArrayList();
		
		return lModifiedFileList;
	}
	public static void main(String args[]){
		System.out.println("::::::inside main::::::::");
		ReadFileAndStoreInDb lReadFileAndStoreInDb = new ReadFileAndStoreInDb();
		String pFileName="unload";
		String pFilePath="C:\\arun\\documents\\project\\POC\\idmt\\ForTool_IDMT\\ForTool_IDMT\\Configs\\";
		String pCreatedBy ="TestUser";
		lReadFileAndStoreInDb.readFileFromDisk(pFileName, pFilePath,pCreatedBy);
		
		lReadFileAndStoreInDb.witeToFileFromDb(pFileName,pFilePath+"\\temp\\");
	}
}
